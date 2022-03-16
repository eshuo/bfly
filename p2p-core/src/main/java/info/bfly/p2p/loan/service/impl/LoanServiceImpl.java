package info.bfly.p2p.loan.service.impl;

import info.bfly.archer.banner.model.BannerPicture;
import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.user.UserBillConstants.OperatorInfo;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.impl.UserBillBO;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.InvestConstants.InvestStatus;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.exception.BorrowedMoneyTooLittle;
import info.bfly.p2p.loan.exception.ExistWaitAffirmInvests;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.exception.InvalidExpectTimeException;
import info.bfly.p2p.loan.model.ApplyEnterpriseLoan;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.service.LoanCalculator;
import info.bfly.p2p.loan.service.LoanService;
import info.bfly.p2p.repay.model.LoanRepay;
import info.bfly.p2p.repay.service.RepayService;
import info.bfly.p2p.risk.service.SystemBillService;
import info.bfly.p2p.schedule.ScheduleConstants;
import info.bfly.p2p.schedule.job.CheckLoanOverExpectTime;
import org.hibernate.LockMode;
import org.hibernate.collection.internal.AbstractPersistentCollection;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description:
 */
@Service("loanService")
public class LoanServiceImpl implements LoanService {
    @Log
    private static Logger log;
    @Resource
    LoanBO loanBO;
    @Resource
    RepayService repayService;
    @Resource
    UserBillBO ubs;
    @Resource
    SystemBillService sbs;
    @Resource
    HibernateTemplate ht;
    @Resource
    StdScheduler scheduler;
    @Resource
    ConfigService configService;
    @Resource
    LoanCalculator loanCalculator;

    private void addDealLoanStatusJob(Loan loan) {
        // 调度，到期自动改项目状态
        JobDetail jobDetail = JobBuilder.newJob(CheckLoanOverExpectTime.class).withIdentity(loan.getId(), ScheduleConstants.JobGroup.CHECK_LOAN_OVER_EXPECT_TIME).build();// 任务名，任务组，任务执行类
        jobDetail.getJobDataMap().put(CheckLoanOverExpectTime.LOAN_ID, loan.getId());
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(loan.getId(), ScheduleConstants.TriggerGroup.CHECK_LOAN_OVER_EXPECT_TIME).forJob(jobDetail)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()).startAt(loan.getExpectTime()).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        if (LoanServiceImpl.log.isDebugEnabled())
            LoanServiceImpl.log.debug("添加[到期自动修改项目状态]调度成功，项目编号[" + loan.getId() + "]");
    }

    @Override
    public Loan get(String id) {

        return ht.get(Loan.class, id);
    }

    @Override
    @Transactional
    public void sinaPassApply(String loanId, String Sina_bid_no, String Inner_bid_no) {
        Loan loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
        loan.setSina_bid_no(Sina_bid_no);
        loan.setInner_bid_no(Inner_bid_no);
        ht.update(loan);
        if (loan.getStatus().equals(LoanConstants.LoanStatus.WAITING_VERIFY_AFFIRM))
            try {
                passApply(loan);
            } catch (InvalidExpectTimeException e) {
                log.error(e.getMessage());
            }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void applyEnterpriseLoan(ApplyEnterpriseLoan ael) {
        ael.setId(IdGenerator.randomUUID());
        ael.setStatus(LoanConstants.ApplyEnterpriseLoanStatus.WAITING_VERIFY);
        ael.setApplyTime(new Date());
        ht.save(ael);
    }

    /**
     * 申请借款
     *
     * @param loan
     * @throws InsufficientBalance 余额不足以支付保证金
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void applyLoan(Loan loan) throws InsufficientBalance {
        // 借款保证金费率
        double cashDepositMoney = loanCalculator.calculateCashDeposit(loan.getLoanMoney());
        // 如果保证金不够，需要先进行充值
        if (cashDepositMoney > ubs.getBalance(loan.getUser().getId(),loan.getAccountType())) {
            throw new InsufficientBalance("用户余额不足以支付保证金。");
        }
        loan.setDeposit(cashDepositMoney);
        loan.setCommitTime(new Date());
        // 设置借款状态
        loan.setStatus(LoanConstants.LoanStatus.WAITING_VERIFY);
        loan.setId(loanBO.generateId());
        ht.save(loan);
        // 冻结保证金
        ubs.freezeMoney(loan.getUser().getId(), cashDepositMoney, OperatorInfo.APPLY_LOAN, "借款ID:" + loan.getId() + "申请，冻结保证金",loan.getAccountType());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createLoanByAdmin(Loan loan) throws InsufficientBalance, InvalidExpectTimeException {
        // FIXME:开始计息时间，必须在（当前时间往前一个还款阶段）之后。
        // 预计执行时间是否在当前时间之前，如果是，抛异常
        if (!loan.getExpectTime().after(new Date())) {
            throw new InvalidExpectTimeException();
        }
        loan.setCommitTime(new Date());
        loan.setMoney(0D);
        // 设置借款状态
        loan.setStatus(LoanConstants.LoanStatus.WAITING_VERIFY);
        loan.setId(loanBO.generateId());
        setPics(loan);
        ht.save(loan);
        // 冻结保证金
        if (loan.getDeposit() != null && loan.getDeposit() > 0) {
            ubs.freezeMoney(loan.getUser().getId(), loan.getDeposit(), OperatorInfo.APPLY_LOAN, "发起借款，冻结保证金，借款ID:" + loan.getId(),loan.getAccountType());
        }
        if (LoanServiceImpl.log.isDebugEnabled())
            LoanServiceImpl.log.debug("添加项目成功，编号[" + loan.getId() + "],名称：[" + loan.getName() + "]");
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void dealComplete(String loanId) {
        // 所有还款都完成了，则借款状态为“完成”
        if (isCompleted(loanId)) {
            Loan loan = ht.get(Loan.class, loanId);
            ht.evict(loan);
            loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
            loan.setCompleteTime(new Date());
            loan.setStatus(LoanConstants.LoanStatus.COMPLETE);
            ht.merge(loan);
            List<Invest> is = (List<Invest>) ht.find("from Invest invest where invest.loan.id=? and invest.status in (?,?,?)", loanId, InvestStatus.REPAYING, InvestStatus.OVERDUE,
                    InvestStatus.BAD_DEBT);
            for (Invest invest : is) {
                invest.setStatus(InvestConstants.InvestStatus.COMPLETE);
                ht.update(invest);
            }
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void dealOverExpectTime(String loanId) {
        // FIXME loan需要验证
        Loan loan = ht.get(Loan.class, loanId);
        ht.evict(loan);
        loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
        // 只有筹款中的借款，才能通过调度改成等待复核
        if (isOverExpectTime(loanId) && LoanConstants.LoanStatus.RAISING.equals(loan.getStatus())) {
            loan.setStatus(LoanConstants.LoanStatus.RECHECK);
            try {
                ht.merge(loan);
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
    }

    @Override
    public void dealRaiseComplete(String loanId) throws NoMatchingObjectsException {
        if (loanCalculator.calculateMoneyNeedRaised(loanId) == 0) {
            // 项目募集完成
            Loan loan = ht.get(Loan.class, loanId);
            loan.setStatus(LoanConstants.LoanStatus.RECHECK);
            ht.update(loan);
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void delayExpectTime(String loanId, Date newExpectTime) throws InvalidExpectTimeException {
        // 预计执行时间是否在当前时间之前，如果是，抛异常
        if (!newExpectTime.after(new Date())) {
            throw new InvalidExpectTimeException();
        } else {
            Loan loan = ht.get(Loan.class, loanId);
            ht.evict(loan);
            loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
            // FIXME:loan不存在
            // 添加项目到期调度任务
            loan.setExpectTime(newExpectTime);
            loan.setStatus(LoanConstants.LoanStatus.RAISING);
            ht.merge(loan);
            try {
                SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(TriggerKey.triggerKey(loanId, ScheduleConstants.TriggerGroup.CHECK_LOAN_OVER_EXPECT_TIME));
                if (trigger != null) {
                    // 修改时间
                    Trigger newTrigger = trigger.getTriggerBuilder().withSchedule(SimpleScheduleBuilder.simpleSchedule()).startAt(newExpectTime).build();
                    // 重启触发器
                    scheduler.rescheduleJob(trigger.getKey(), newTrigger);
                } else {
                    JobDetail jobDetail = JobBuilder.newJob(CheckLoanOverExpectTime.class).withIdentity(loanId, ScheduleConstants.JobGroup.CHECK_LOAN_OVER_EXPECT_TIME).build();// 任务名，任务组，任务执行类
                    jobDetail.getJobDataMap().put(CheckLoanOverExpectTime.LOAN_ID, loanId);
                    trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startAt(newExpectTime).withSchedule(SimpleScheduleBuilder.simpleSchedule())
                            .withIdentity(loanId, ScheduleConstants.TriggerGroup.CHECK_LOAN_OVER_EXPECT_TIME).build();
                    scheduler.scheduleJob(jobDetail, trigger);
                }
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void continueInvest(String loanId, Double money) throws ExistWaitAffirmInvests {
        Loan loan = ht.get(Loan.class, loanId);
        ht.evict(loan);
        loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);

        if (money == loan.getLoanMoney()) {
            throw new ExistWaitAffirmInvests("");
        }

        loan.setLoanMoney(money);
        loan.setStatus(LoanConstants.LoanStatus.RAISING);
        ht.merge(loan);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void fail(String loanId, String operatorId) throws ExistWaitAffirmInvests {
        // 流标
        Loan loan = ht.get(Loan.class, loanId);
        ht.evict(loan);
        loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
        List<Invest> invests = loan.getInvests();
        try {
            for (Invest investment : invests) {
                if (investment.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM)) {
                    throw new ExistWaitAffirmInvests("investID:" + investment.getId());
                }
                if (investment.getStatus().equals(InvestConstants.InvestStatus.BID_SUCCESS)) {
                    // FIXME：investMoney-代金券金额，优惠券变为可用
                    ubs.unfreezeMoney(investment.getUser().getId(), investment.getMoney(), OperatorInfo.CANCEL_LOAN, "借款" + loan.getId() + "流标，解冻投资金额",loan.getAccountType());
                }
                // 更改投资状态
                investment.setStatus(InvestConstants.InvestStatus.CANCEL);
                ht.update(investment);
            }
            if (loan.getDeposit() != null && loan.getDeposit().doubleValue() > 0) {
                ubs.unfreezeMoney(loan.getUser().getId(), loan.getDeposit(), OperatorInfo.CANCEL_LOAN, "借款" + loan.getId() + "流标，解冻保证金",loan.getAccountType());
            }

            loan.setCancelTime(new Date());
            loan.setStatus(LoanConstants.LoanStatus.CANCEL);
            ht.merge(loan);
        } catch (InsufficientBalance ib) {
            throw new RuntimeException(ib);
        }
    }

    @Override
    public List<Invest> getSuccessfulInvests(String loanId) {
        return (List<Invest>) ht.find("select im from Invest im where im.loan.id=? and im.status in (?,?,?,?,?)", loanId, InvestConstants.InvestStatus.BID_SUCCESS,
                InvestConstants.InvestStatus.OVERDUE, InvestConstants.InvestStatus.COMPLETE, InvestConstants.InvestStatus.BAD_DEBT, InvestConstants.InvestStatus.REPAYING);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void giveMoneyToBorrower(String loanId) throws ExistWaitAffirmInvests, BorrowedMoneyTooLittle {
        Loan loan = ht.get(Loan.class, loanId);
        ht.evict(loan);
        loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
        // FIXME:loan不存在
        // 有两种放款，一种是项目募集完成了，放款；一种是项目未募集满额，得根据项目的实际募集金额，修改项目借款金额，然后进行放款。
        // 更改项目状态，放款。
        loan.setStatus(LoanConstants.LoanStatus.REPAYING);
        // 获取当前日期
        Date dateNow = new Date();
        // 设置放款日期
        loan.setGiveMoneyTime(dateNow);
        if (loan.getInterestBeginTime() == null) {
            loan.setInterestBeginTime(dateNow);
        }
        // 实际到借款账户的金额
        double actualMoney = 0D;
        List<Invest> invests = loan.getInvests();
        double allMoney = 0D;
        for (Invest invest : invests) {

            //TODO 单个放款是否影响？？？
            if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM)) {
                // 放款时候，需要检查是否要等待确认的投资，如果有，则不让放款。
                throw new ExistWaitAffirmInvests("investID:" + invest.getId());
            }
            if (invest.getStatus().equals(InvestStatus.REPAYING) || invest.getStatus().equals(InvestStatus.BID_SUCCESS) || invest.getStatus().equals(InvestStatus.OVERDUE) || invest.getStatus().equals(InvestStatus.COMPLETE) || invest.getStatus().equals(InvestStatus.BAD_DEBT)) {
                allMoney = ArithUtil.add(allMoney, invest.getInvestMoney());
            }
            if (invest.getStatus().equals(InvestStatus.BID_SUCCESS)) {
                actualMoney = ArithUtil.add(actualMoney, invest.getInvestMoney());
                // 更改投资状态
                invest.setStatus(InvestConstants.InvestStatus.REPAYING);
                ht.update(invest);
            }
        }

        if (loan.getMoney() + actualMoney != allMoney) {
            throw new RuntimeException("融资金额异常不一致！");
        }
        // 设置借款实际借到的金额
        loan.setMoney(allMoney);
        // 根据借款期限产生还款信息
        repayService.generateRepay(loan.getId());
        // 借款手续费-借款保证金
        double subR = ArithUtil.sub(loan.getLoanGuranteeFee(), loan.getDeposit());
        double tooLittle = ArithUtil.sub(actualMoney, subR);
        // 借到的钱，可能不足以支付借款手续费
        if (tooLittle <= 0) {
            throw new BorrowedMoneyTooLittle("actualMoney：" + tooLittle);
        }
        // 把借款转给借款人账户
        ubs.transferIntoBalance(loan.getUser().getId(), actualMoney, OperatorInfo.GIVE_MONEY_TO_BORROWER, "借款到账, 借款ID：" + loan.getId(),loan.getAccountType());
        try {
            ubs.unfreezeMoney(loan.getUser().getId(), loan.getDeposit(), OperatorInfo.GIVE_MONEY_TO_BORROWER, "借款成功，解冻借款保证金, 借款ID：" + loan.getId(),loan.getAccountType());
            ubs.transferOutFromBalance(loan.getUser().getId(), loan.getLoanGuranteeFee(), OperatorInfo.GIVE_MONEY_TO_BORROWER, "取出借款管理费, 借款ID：" + loan.getId(),loan.getAccountType());
            sbs.transferOut(actualMoney, OperatorInfo.GIVE_MONEY_TO_BORROWER, "借款到账, 借款ID：" + loan.getId());
            sbs.transferInto(loan.getLoanGuranteeFee(), OperatorInfo.GIVE_MONEY_TO_BORROWER, "借款管理费, 借款ID：" + loan.getId());
        } catch (InsufficientBalance e) {
            throw new RuntimeException(e);
        }
        ht.merge(loan);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void giveMoneyToOneBorrower(String investId) throws ExistWaitAffirmInvests, BorrowedMoneyTooLittle {
        Invest invest = ht.get(Invest.class, investId);
        ht.evict(invest);
        invest = ht.get(Invest.class, investId, LockMode.PESSIMISTIC_WRITE);
        Loan loan = invest.getLoan();
        if (invest.getStatus().equals(InvestStatus.ONE_SINAPAY_WAIT)) {
            Double investMoney = invest.getInvestMoney();
            //设置总金额
            loan.setMoney(loan.getMoney() + investMoney);
            //生成单个投资还款记录
            repayService.generateOneRepay(investId);
            // 把借款转给借款人账户
            ubs.transferIntoBalance(loan.getUser().getId(), investMoney, OperatorInfo.GIVE_MONEY_TO_BORROWER, "借款单个到账, 借款ID：" + loan.getId(),loan.getAccountType());
            try {
                //FIXME 借款保证金与管理费从总放款中操作
                sbs.transferOut(investMoney, OperatorInfo.GIVE_MONEY_TO_BORROWER, "单个借款到账, 借款ID：" + loan.getId() + "，投资ID:" + investId);
            } catch (InsufficientBalance e) {
                throw new RuntimeException(e);
            }
            invest.setStatus(InvestStatus.REPAYING);
            ht.merge(invest);
            ht.merge(loan);
        }
    }

    @Override
    public boolean isCompleted(String loanId) {
        List<LoanRepay> repays = (List<LoanRepay>) ht.find("from LoanRepay repay where repay.loan.id=?", loanId);
        for (LoanRepay repay : repays) {
            // 如果有一笔还款状态不是“完成”，则返回false
            if (!repay.getStatus().equals(LoanConstants.RepayStatus.COMPLETE)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isOverExpectTime(String loanId) {
        Loan loan = ht.get(Loan.class, loanId);
        // FIXME:loan为空验证
        return !new Date().before(loan.getExpectTime());
    }

    @Override
    public boolean isRaiseCompleted(String loanId) throws NoMatchingObjectsException {
        return loanCalculator.calculateMoneyNeedRaised(loanId) == 0;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void passApply(Loan loan) throws InvalidExpectTimeException {
        // FIXME:验证
        // 预计执行时间是否在当前时间之前，如果是，抛异常
        if (!loan.getExpectTime().after(new Date())) {
            throw new InvalidExpectTimeException();
        }
        setPics(loan);
        addDealLoanStatusJob(loan);
        // 审核通过
        loan.setVerified(LoanConstants.LoanVerifyStatus.PASSED);
        loan.setStatus(LoanConstants.LoanStatus.RAISING);
        loan.setVerifyTime(new Date());
        //增加项目期限天数
        if ("month".equals(loan.getType().getRepayTimeUnit())) {
            loan.setProjectDuration(loan.getDeadline() * 30);
        } else {
            loan.setProjectDuration(loan.getDeadline());
        }
        ht.merge(loan);
        log.debug("借款[编号：{},名称：{}]审核通过!审核人：{}", loan.getId(), loan.getName(), loan.getVerifyUser().getId());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void refuseApply(String loanId, String refuseInfo, String verifyUserId) {
        Loan loan = ht.get(Loan.class, loanId);
        ht.evict(loan);
        loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
        //TODO 判断是否已经通过了
        User verifyUser = ht.get(User.class, verifyUserId);
        if (loan != null && verifyUser != null) {
            loan.setVerified(LoanConstants.LoanVerifyStatus.FAILED);
            loan.setStatus(LoanConstants.LoanStatus.VERIFY_FAIL);
            loan.setVerifyMessage(refuseInfo);
            loan.setExpectTime(null);
            // 审核人
            loan.setVerifyUser(verifyUser);
            loan.setVerifyTime(new Date());
            ht.merge(loan);
        }

    }

    /**
     * 赋值项目资料和抵押相关物资的图片
     *
     * @param loan
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private void setPics(Loan loan) {
        List<BannerPicture> lips = loan.getLoanInfoPics();
        List<BannerPicture> gips = loan.getGuaranteeInfoPics();
        if (lips != null && !(lips instanceof AbstractPersistentCollection)) {
            for (BannerPicture lip : lips) {
                ht.saveOrUpdate(lip);
            }
        }
        if (gips != null && !(gips instanceof AbstractPersistentCollection)) {
            for (BannerPicture gip : gips) {
                ht.saveOrUpdate(gip);
            }
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void update(Loan loan) {
        // 只能更改不影响流程的字段
        setPics(loan);
        ht.merge(loan);
        if (LoanServiceImpl.log.isDebugEnabled()) LoanServiceImpl.log.debug("修改项目成功，编号[" + loan.getId() + "]");
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void verifyEnterpriseLoan(ApplyEnterpriseLoan ael) {
        ael.setStatus(LoanConstants.ApplyEnterpriseLoanStatus.VERIFIED);
        ht.update(ael);
    }

    @Override
    public boolean isCompletedLoan(String loanId) throws NoMatchingObjectsException {
        return loanCalculator.calculateMoneyNeedRaised(loanId) == 0;
    }

    @Override
    public boolean isLoanFullInvest(String loanId) throws NoMatchingObjectsException {
        return loanCalculator.calculateMoneyToBidSuccess(loanId) == 0;
    }

    @Override
    public Double getLoanMoneyToBidSuccess(String loanId) {
        List<Object> investMoney = (List<Object>) ht.find("select sum(invest.money) from Invest invest where invest.loan.id=? and invest.status =? ", loanId,
                InvestConstants.InvestStatus.BID_SUCCESS);
        double sumMoney = investMoney.get(0) == null ? 0D : (Double) investMoney.get(0);
        return sumMoney;
    }


}
