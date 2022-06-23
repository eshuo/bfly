package info.bfly.p2p.repay.service.impl;

import info.bfly.archer.config.ConfigConstants.RepayAlert;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.user.UserBillConstants.OperatorInfo;
import info.bfly.archer.user.service.impl.UserBillBO;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.DateStyle;
import info.bfly.core.util.DateUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.invest.InvestConstants.InvestStatus;
import info.bfly.p2p.invest.InvestConstants.TransferStatus;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.model.TransferApply;
import info.bfly.p2p.invest.service.TransferService;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.LoanConstants.RepayStatus;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.model.LoanType;
import info.bfly.p2p.loan.service.LoanService;
import info.bfly.p2p.message.MessageConstants;
import info.bfly.p2p.message.model.UserMessageTemplate;
import info.bfly.p2p.message.service.impl.MessageBO;
import info.bfly.p2p.repay.RepayConstants.RepayType;
import info.bfly.p2p.repay.RepayConstants.RepayUnit;
import info.bfly.p2p.repay.exception.AdvancedRepayException;
import info.bfly.p2p.repay.exception.IllegalLoanTypeException;
import info.bfly.p2p.repay.exception.NormalRepayException;
import info.bfly.p2p.repay.exception.OverdueRepayException;
import info.bfly.p2p.repay.model.InvestRepay;
import info.bfly.p2p.repay.model.LoanRepay;
import info.bfly.p2p.repay.model.Repay;
import info.bfly.p2p.repay.service.NormalRepayCalculator;
import info.bfly.p2p.repay.service.RepayService;
import info.bfly.p2p.risk.FeeConfigConstants.FeePoint;
import info.bfly.p2p.risk.FeeConfigConstants.FeeType;
import info.bfly.p2p.risk.service.SystemBillService;
import info.bfly.p2p.risk.service.impl.FeeConfigBO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Description:
 */
@Service("repayService")
public class RepayServiceImpl implements RepayService {
    @Log
    private Logger                    log;
    @Resource
    private HibernateTemplate         ht;
    @Resource
    private UserBillBO                userBillBO;
    @Resource
    private SystemBillService         systemBillService;
    @Resource
    private FeeConfigBO               feeConfigBO;
    @Resource
    private LoanService               loanService;
    @Resource
    private TransferService           transferService;
    @Resource
    private NormalRepayCalculator     normalRepayRFCLCalculator;
    @Resource
    private NormalRepayCalculator     normalRepayCPMCalculator;
    @Resource
    private NormalRepayRLIOCalculator normalRepayRLIOCalculator;
    @Resource
    private MessageBO                 messageBO;
    @Resource
    private ConfigService             configService;

    @Autowired
    private IdGenerator idGenerator;


    @Override
    public LoanRepay get(String id) {
        return ht.get(LoanRepay.class, id);
    }

    @Override
    @Transactional
    public void update(LoanRepay lr) {
        ht.update(lr);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void advanceRepay(String loanId) throws InsufficientBalance, AdvancedRepayException {
        Loan loan = ht.get(Loan.class, loanId);
        ht.evict(loan);
        loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
        // 查询当期还款是否已还清
        String repaysHql = "select lr from LoanRepay lr where lr.loan.id = ?";
        List<LoanRepay> repays = (List<LoanRepay>) ht.find(repaysHql, loanId);
        // 剩余所有本金
        double sumCorpus = 0D;
        // 手续费总额
        double feeAll = 0D;
        for (LoanRepay repay : repays) {
            if (repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
                // 在还款期，而且没还款
                if (isInRepayPeriod(repay.getRepayDay())) {
                    // 有未完成的当期还款。
                    throw new AdvancedRepayException("当期还款未完成");
                } else {
                    sumCorpus = ArithUtil.add(sumCorpus, repay.getCorpus());
                    feeAll = ArithUtil.add(feeAll, repay.getFee());
                    repay.setTime(new Date());
                    repay.setStatus(LoanConstants.RepayStatus.COMPLETE);
                }
            } else if (repay.getStatus().equals(LoanConstants.RepayStatus.BAD_DEBT) || repay.getStatus().equals(LoanConstants.RepayStatus.OVERDUE)) {
                // 还款中存在逾期或者坏账
                throw new AdvancedRepayException("还款中存在逾期或者坏账");
            }
        }
        // 给投资人的罚金
        double feeToInvestor = feeConfigBO.getFee(FeePoint.ADVANCE_REPAY_INVESTOR, FeeType.PENALTY, null, null, sumCorpus);
        // 给系统的罚金
        double feeToSystem = feeConfigBO.getFee(FeePoint.ADVANCE_REPAY_SYSTEM, FeeType.PENALTY, null, null, sumCorpus);
        double sumPay = ArithUtil.add(feeToInvestor, feeToSystem, sumCorpus, feeAll);
        // 扣除还款金额+罚金
        userBillBO.transferOutFromBalance(loan.getUser().getId(), sumPay, OperatorInfo.ADVANCE_REPAY, "借款：" + loan.getId() + "提前还款，本金：" + sumCorpus + "，用户罚金：" + feeToInvestor + "，系统罚金：" + feeToSystem
                + "，借款手续费：" + feeAll);
        // 余额不足，抛异常
        // 按比例分给投资人和系统（默认优先给投资人，剩下的给系统，以防止提转差额的出现）
        List<InvestRepay> irs = (List<InvestRepay>) ht.find("from InvestRepay ir where ir.invest.loan.id=? and ir.status=?", loan.getId(), RepayStatus.REPAYING);
        double feeToInvestorTemp = feeToInvestor;
        // 更改投资的还款信息
        for (int i = 0; i < irs.size(); i++) {
            InvestRepay ir = irs.get(i);
            // FIXME: 记录repayWay信息
            ir.setStatus(LoanConstants.RepayStatus.COMPLETE);
            ir.setTime(new Date());
            // 罚金
            double cashFine;
            if (i == irs.size() - 1) {
                cashFine = feeToInvestorTemp;
            } else {
                cashFine = ArithUtil.round(ir.getCorpus() / sumCorpus * feeToInvestor, 2);
                feeToInvestorTemp = ArithUtil.sub(feeToInvestorTemp, cashFine);
            }
            userBillBO.transferIntoBalance(ir.getInvest().getUser().getId(), ArithUtil.add(ir.getCorpus(), cashFine), OperatorInfo.ADVANCE_REPAY, "投资：" + ir.getInvest().getId() + "收到还款" + "  本金："
                    + ir.getCorpus() + "  罚息：" + cashFine);
        }
        List<TransferApply> tas = (List<TransferApply>) ht.find("from TransferApply ta where ta.invest.loan.id=? and ta.status=?", loan.getId(), TransferStatus.TRANSFERING);
        for (TransferApply ta : tas) {
            // 该投资下面的债权转让，取消。
            transferService.cancel(ta.getId());
        }
        systemBillService.transferInto(ArithUtil.add(feeToSystem, feeAll), OperatorInfo.ADVANCE_REPAY, "提前还款罚金及借款手续费到账，借款ID:" + loan.getId());
        // 改项目状态。
        loan.setStatus(LoanConstants.LoanStatus.COMPLETE);
        ht.merge(loan);
        loanService.dealComplete(loan.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoRepay() {
        log.debug("autoRepay start");
        List<LoanRepay> lrs = (List<LoanRepay>) ht.find("from LoanRepay repay where repay.status !='" + LoanConstants.RepayStatus.COMPLETE + "'");
        for (LoanRepay lr : lrs) {
            ht.lock(lr, LockMode.PESSIMISTIC_WRITE);
            Loan loan = lr.getLoan();
            ht.lock(loan, LockMode.PESSIMISTIC_WRITE);
            List<InvestRepay> irs = (List<InvestRepay>) ht.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?", lr.getLoan().getId(), lr.getPeriod());
            Date repayDay = DateUtil.addDay(DateUtil.StringToDate(DateUtil.DateToString(lr.getRepayDay(), DateStyle.YYYY_MM_DD_CN)), 1);
            if (lr.getStatus().equals(LoanConstants.RepayStatus.REPAYING) && repayDay.before(new Date())) {
                // 到还款日了，自动扣款
                try {
                    normalRepay(lr);
                } catch (InsufficientBalance e) {
                    // 账户余额不足，则逾期
                    log.debug("autoRepay InsufficientBalance overdue repayId:" + lr.getId());
                    lr.setStatus(LoanConstants.RepayStatus.OVERDUE);
                    // FIXME:冻结用户，只允许还钱，其他都不能干。
                    loan.setStatus(LoanConstants.LoanStatus.OVERDUE);
                    for (InvestRepay ir : irs) {
                        ir.setStatus(RepayStatus.OVERDUE);
                        ir.getInvest().setStatus(InvestStatus.OVERDUE);
                        ht.update(ir.getInvest());
                        ht.update(ir);
                    }
                } catch (NormalRepayException e) {
                    throw new RuntimeException(e);
                }
            } else if (lr.getStatus().equals(LoanConstants.RepayStatus.OVERDUE)) {

                log.debug("autoRepay overdue repayId:" + lr.getId());

                // 计算逾期罚息, 用户罚息+网站罚息
                double defalutInterestAll = 0D;
                for (InvestRepay ir : irs) {
                    // 单笔投资罚息
                    double overdueAllMoney = ArithUtil.mul(ArithUtil.add(ir.getCorpus(), ir.getInterest()), DateUtil.getIntervalDays(new Date(), ir.getRepayDay()));
                    ir.setDefaultInterest(feeConfigBO.getFee(FeePoint.OVERDUE_REPAY_INVESTOR, FeeType.PENALTY, null, null, overdueAllMoney));
                    defalutInterestAll = ArithUtil.add(defalutInterestAll, ir.getDefaultInterest());
                }
                ht.update(lr);
                // 网站罚息
                double overdueLRAllMoney = ArithUtil.mul(ArithUtil.add(lr.getCorpus(), lr.getInterest()), DateUtil.getIntervalDays(new Date(), lr.getRepayDay()));
                // 用户罚息+网站罚息
                lr.setDefaultInterest(ArithUtil.add(defalutInterestAll, feeConfigBO.getFee(FeePoint.OVERDUE_REPAY_SYSTEM, FeeType.PENALTY, null, null, overdueLRAllMoney)));
                if (DateUtil.addYear(lr.getRepayDay(), 1).before(new Date())) {
                    // 逾期一年以后，项目改为还账状态
                    log.debug("autoRepay badDebt repayId:" + lr.getId());

                    lr.setStatus(LoanConstants.RepayStatus.BAD_DEBT);
                    loan.setStatus(LoanConstants.LoanStatus.BAD_DEBT);
                    for (InvestRepay ir : irs) {
                        ir.setStatus(RepayStatus.BAD_DEBT);
                        ir.getInvest().setStatus(InvestStatus.BAD_DEBT);
                        ht.update(ir.getInvest());
                        ht.update(ir);
                    }
                }
            }
            ht.merge(lr);
            ht.merge(loan);
        }
    }

    /**
     * 生成借款的还款数据
     *
     * @param loan
     * @param loanType
     * @param allInvestRepays
     */
    private void generateLoanRepays(Loan loan, LoanType loanType, List<List<InvestRepay>> allInvestRepays) {
        // 创建loanRepays以便保存
        List<LoanRepay> loanRepays = new ArrayList<LoanRepay>();
        // 借款手续费，平均到每笔还款中收取
        Double fee = ArithUtil.div(loan.getFeeOnRepay(), loan.getDeadline(), 2);
        for (int i = 1; i <= loan.getDeadline(); i++) {
            // 初始化loanRepay信息
            LoanRepay loanRepay = new LoanRepay();
            loanRepay.setCorpus(0D);
            loanRepay.setDefaultInterest(0D);
            loanRepay.setId(loan.getId() + StringUtils.leftPad(String.valueOf(i), 4, "0"));
            loanRepay.setInterest(0D);
            loanRepay.setLength(loanType.getRepayTimePeriod());
            loanRepay.setLoan(loan);
            // 借款者手续费
            loanRepay.setFee(fee);
            loanRepay.setPeriod(i);
            if (loanType.getRepayTimeUnit().equals(RepayUnit.DAY)) {
                // 按天s还款
                loanRepay.setRepayDay(DateUtil.addDay(loan.getInterestBeginTime(), i * loanType.getRepayTimePeriod()));
            } else if (loanType.getRepayTimeUnit().equals(RepayUnit.MONTH)) {
                // 按月s还款
                loanRepay.setRepayDay(DateUtil.addMonth(loan.getInterestBeginTime(), i * loanType.getRepayTimePeriod()));
            }
            loanRepay.setStatus(LoanConstants.RepayStatus.REPAYING);
            loanRepays.add(loanRepay);
            // 到期还本付息，这里有bug
            if (loanType.getRepayType().equals(RepayType.RLIO)) {
                if (loanType.getRepayTimeUnit().equals(RepayUnit.DAY)) {
                    // 按天s还款
                    loanRepay.setLength(loan.getDeadline());
                    // 计息日+第几期*还款周期单位=还款日
                    loanRepay.setRepayDay(DateUtil.addDay(loan.getInterestBeginTime(), loan.getDeadline()));
                } else if (loanType.getRepayTimeUnit().equals(RepayUnit.MONTH)) {
                    // 按月s还款
                    loanRepay.setLength(loan.getDeadline());
                    // 计息日+第几期*还款周期=还款日
                    loanRepay.setRepayDay(DateUtil.addMonth(loan.getInterestBeginTime(), loan.getDeadline()));
                }
                break;
            }
        }
        // 根据每笔投资的还款信息，更新借款的还款信息。
        for (List<InvestRepay> irs : allInvestRepays) {
            for (InvestRepay ir : irs) {
                loanRepays.get(ir.getPeriod() - 1).setCorpus(ArithUtil.add(loanRepays.get(ir.getPeriod() - 1).getCorpus(), ir.getCorpus()));
                loanRepays.get(ir.getPeriod() - 1).setInterest(ArithUtil.add(loanRepays.get(ir.getPeriod() - 1).getInterest(), ir.getInterest()));
            }
        }
        // 保存借款的还款信息
        for (LoanRepay loanRepay : loanRepays) {
            ht.save(loanRepay);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateRepay(String loanId) {
        Loan loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
        ht.evict(loan);
        loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
        LoanType loanType = ht.get(LoanType.class, loan.getType().getId());
        if (loan.getInterestBeginTime() == null) {
            // 发起借款的时候，如果不制定计息开始时间，则默认为放款日
            loan.setInterestBeginTime(new Date());
        }
        // 先付利息后还本金
        if (loanType.getRepayType().equals(RepayType.RFCL)) {
            gRepays(loan, normalRepayRFCLCalculator);
        } else if (loanType.getRepayType().equals(RepayType.CPM)) {
            gRepays(loan, normalRepayCPMCalculator);
        } else if (loanType.getRepayType().equals(RepayType.RLIO)) {
            gRepays(loan, normalRepayRLIOCalculator);
        } else {
            throw new IllegalLoanTypeException("RepayType: " + loan.getType().getRepayType() + ". 不支持该还款类型。");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateOneRepay(String investId) {
        Invest invest = ht.get(Invest.class, investId, LockMode.PESSIMISTIC_WRITE);

        ht.evict(invest);
        invest = ht.get(Invest.class, investId, LockMode.PESSIMISTIC_WRITE);
        Loan loan = invest.getLoan();
        LoanType loanType = ht.get(LoanType.class, loan.getType().getId());
        if (loan.getInterestBeginTime() == null) {
            // 发起借款的时候，如果不制定计息开始时间，则默认为放款日
            loan.setInterestBeginTime(new Date());
        }
        // 先付利息后还本金
        if (loanType.getRepayType().equals(RepayType.RFCL)) {
            gOneRepays(loan, normalRepayRFCLCalculator, invest);
        } else if (loanType.getRepayType().equals(RepayType.CPM)) {
            gOneRepays(loan, normalRepayCPMCalculator, invest);
        } else if (loanType.getRepayType().equals(RepayType.RLIO)) {
            gOneRepays(loan, normalRepayRLIOCalculator, invest);
        } else {
            throw new IllegalLoanTypeException("RepayType: " + loan.getType().getRepayType() + ". 不支持该还款类型。");
        }
    }

    /**
     * 生成单个投资还款数据
     *
     * @param loan
     * @param normalRepayCalculator
     * @param invest
     */
    private void gOneRepays(Loan loan, NormalRepayCalculator normalRepayCalculator, Invest invest) {

        LoanType loanType = loan.getType();
        List<List<InvestRepay>> allInvestRepays = new ArrayList<List<InvestRepay>>();
        if (invest.getStatus().equals(InvestStatus.ONE_SINAPAY_WAIT)) {
            List<Repay> repays = normalRepayCalculator.generateRepays(invest.getInvestMoney(), invest.getTime(), loan.getRate(), loan.getDeadline(), loanType.getRepayTimeUnit(), loanType.getRepayTimePeriod(), loan
                    .getInterestBeginTime(), loanType.getInterestType(), loanType.getInterestPoint(), null);
            // 保存投资的还款信息
            Double investInterest = 0D;
            List<InvestRepay> irs = new ArrayList<InvestRepay>();
            for (Repay repay : repays) {
                InvestRepay investRepay = new InvestRepay();
                investRepay.setDefaultInterest(repay.getDefaultInterest());
                investRepay.setCorpus(repay.getCorpus());
                investRepay.setId(idGenerator.nextId(Invest.class));
                investRepay.setInterest(repay.getInterest());
                investRepay.setInvest(invest);
                investRepay.setLength(repay.getLength());
                investRepay.setPeriod(repay.getPeriod());
                investRepay.setRepayDay(repay.getRepayDay());
                investRepay.setStatus(LoanConstants.RepayStatus.REPAYING);
                // 投资者手续费=所得利息*借款中存储的投资者手续费比例
                investRepay.setFee(ArithUtil.round(ArithUtil.mul(repay.getInterest(), loan.getInvestorFeeRate()), 2));
                investInterest = ArithUtil.add(investInterest, investRepay.getInterest());
                ht.save(investRepay);
                irs.add(investRepay);
            }
            ht.update(invest);
            allInvestRepays.add(irs);
        }
        generateLoanRepays(loan, loanType, allInvestRepays);
    }

    /**
     * 生成还款数据
     *
     * @param loan
     * @param normalRepayCalculator
     */
    private void gRepays(Loan loan, NormalRepayCalculator normalRepayCalculator) {
        LoanType loanType = loan.getType();
        List<List<InvestRepay>> allInvestRepays = new ArrayList<List<InvestRepay>>();
        List<Invest> invests = loanService.getSuccessfulInvests(loan.getId());
        for (Invest im : invests) {
            if (im.getStatus().equals(InvestStatus.BID_SUCCESS)) {
                List<Repay> repays = normalRepayCalculator.generateRepays(im.getMoney(), im.getTime(), loan.getRate(), loan.getDeadline(), loanType.getRepayTimeUnit(), loanType.getRepayTimePeriod(), loan
                        .getInterestBeginTime(), loanType.getInterestType(), loanType.getInterestPoint(), null);
                // 保存投资的还款信息
                Double investInterest = 0D;
                List<InvestRepay> irs = new ArrayList<InvestRepay>();
                for (Repay repay : repays) {
                    InvestRepay investRepay = new InvestRepay();
                    investRepay.setCorpus(repay.getCorpus());
                    investRepay.setDefaultInterest(repay.getDefaultInterest());
                    // 投资编号+还款第几期（四位，左侧补0）
                    investRepay.setId(idGenerator.nextId(Invest.class));
                    investRepay.setInterest(repay.getInterest());
                    investRepay.setInvest(im);
                    investRepay.setLength(repay.getLength());
                    investRepay.setPeriod(repay.getPeriod());
                    investRepay.setRepayDay(repay.getRepayDay());
                    investRepay.setStatus(LoanConstants.RepayStatus.REPAYING);
                    // 投资者手续费=所得利息*借款中存储的投资者手续费比例
                    investRepay.setFee(ArithUtil.round(ArithUtil.mul(repay.getInterest(), loan.getInvestorFeeRate()), 2));
                    investInterest = ArithUtil.add(investInterest, investRepay.getInterest());
                    ht.save(investRepay);
                    irs.add(investRepay);
                }
                ht.update(im);
                allInvestRepays.add(irs);
            }

        }

        generateLoanRepays(loan, loanType, allInvestRepays);
    }

    @Override
    public boolean isInRepayPeriod(Date repayDate) {
        repayDate = DateUtil.StringToDate(DateUtil.DateToString(repayDate, DateStyle.YYYY_MM_DD_CN));
        Date now = new Date();
        Date upperLimit = DateUtil.addMonth(repayDate, -4);
        repayDate = DateUtil.addMinute(repayDate, 1439);
        // 还款日上推一个月，算是还款期。
        return (now.before(repayDate)) && (!now.before(upperLimit));
    }

    /**
     * 判断是否是个人资金代投
     *
     * @param inverIdString
     * @return
     */
    public boolean isWealth(String inverIdString) {
        List<InvestRepay> irs = (List<InvestRepay>) ht.find("from Invest inv where inv.investId = ?", inverIdString);
        return null != irs && irs.size() > 0;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void normalRepay(LoanRepay repay) throws InsufficientBalance, NormalRepayException {
        ht.evict(repay);
        repay = ht.get(LoanRepay.class, repay.getId(), LockMode.PESSIMISTIC_WRITE);
        // 正常还款
        if (!(repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING) || repay.getStatus().equals(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY))) {
            // 该还款不处于正常还款状态。
            throw new NormalRepayException("还款：" + repay.getId() + "不处于正常还款状态。");
        }
        List<InvestRepay> irs = (List<InvestRepay>) ht.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?", repay.getLoan().getId(), repay.getPeriod());
        // TODO:投资的所有还款信息加和，判断是否等于借款的还款信息，如果不相等，抛异常
        // 更改投资的还款信息
        for (InvestRepay ir : irs) {
            // FIXME: 记录repayWay信息
            ir.setStatus(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY);
            ir.setTime(new Date());
            ht.update(ir);
            String userId = null;
            userId = ir.getInvest().getUser().getId();
            if (isWealth(ir.getInvest().getId())) {
                if (!wealthRepay(ir, repay)) {
                    throw new NormalRepayException("还款：" + repay.getId() + " 正常还款状态，代投收益转账失败 。");
                }
                continue;
            }
            userBillBO.transferIntoBalance(userId, ArithUtil.add(ir.getCorpus(), ir.getInterest()), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款, 还款ID:" + repay.getId()
                    + "  借款ID:" + repay.getLoan().getId() + "  本金：" + ir.getCorpus() + "  利息：" + ir.getInterest());
            // 投资者手续费
            userBillBO.transferOutFromBalance(userId, ir.getFee(), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:" + repay.getId());
            systemBillService.transferInto(ir.getFee(), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:" + repay.getId());
        }
        // 更改借款的还款信息
        double payMoney = ArithUtil.add(ArithUtil.add(repay.getCorpus(), repay.getInterest()), repay.getFee());
        repay.setTime(new Date());
        repay.setStatus(LoanConstants.RepayStatus.COMPLETE);
        ht.merge(repay);
        // 判断是否所有还款结束，更改等待还款的投资状态和还款状态，还有项目状态。
        loanService.dealComplete(repay.getLoan().getId());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void normalRepay(String repayId) throws InsufficientBalance, NormalRepayException {
        // 正常还款
        LoanRepay repay = ht.get(LoanRepay.class, repayId, LockMode.PESSIMISTIC_WRITE);
        normalRepay(repay);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void overdueRepay(String repayId) throws InsufficientBalance, OverdueRepayException {
        LoanRepay lr = ht.get(LoanRepay.class, repayId);
        ht.evict(lr);
        lr = ht.get(LoanRepay.class, repayId, LockMode.PESSIMISTIC_WRITE);
        if (lr.getStatus().equals(LoanConstants.RepayStatus.OVERDUE) || lr.getStatus().equals(LoanConstants.RepayStatus.BAD_DEBT)) {
            List<InvestRepay> irs = (List<InvestRepay>) ht.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?", lr.getLoan().getId(), lr.getPeriod());
            double defaultInterest = lr.getDefaultInterest();
            // 更改投资的还款信息
            for (InvestRepay ir : irs) {
                ir.setStatus(LoanConstants.RepayStatus.COMPLETE);
                ir.setTime(new Date());
                ht.update(ir);
                userBillBO.transferIntoBalance(ir.getInvest().getUser().getId(), ArithUtil.add(ir.getCorpus(), ir.getInterest(), ir.getDefaultInterest()), OperatorInfo.OVERDUE_REPAY, "投资："
                        + ir.getInvest().getId() + "收到还款, 还款ID:" + lr.getId() + "  借款ID:" + lr.getLoan().getId() + "  本金：" + ir.getCorpus() + "  利息：" + ir.getInterest() + "  罚息："
                        + ir.getDefaultInterest());
                defaultInterest = ArithUtil.sub(defaultInterest, ir.getDefaultInterest());
                // 投资者手续费
                userBillBO.transferOutFromBalance(ir.getInvest().getUser().getId(), ir.getFee(), OperatorInfo.OVERDUE_REPAY, "投资：" + ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:" + lr.getId());
                systemBillService.transferInto(ir.getFee(), OperatorInfo.OVERDUE_REPAY, "投资：" + ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:" + lr.getId());
            }
            // 更改借款的还款信息
            double payMoney = ArithUtil.add(ArithUtil.add(lr.getCorpus(), lr.getInterest()), lr.getFee(), lr.getDefaultInterest());
            lr.setTime(new Date());
            lr.setStatus(LoanConstants.RepayStatus.COMPLETE);
            // 投资者的借款账户，扣除还款。
            userBillBO.transferOutFromBalance(lr.getLoan().getUser().getId(), payMoney, OperatorInfo.OVERDUE_REPAY, "借款：" + lr.getLoan().getId() + "逾期还款, 还款ID：" + lr.getId() + " 本金：" + lr.getCorpus()
                    + "  利息：" + lr.getInterest() + "  手续费：" + lr.getFee() + "  罚息：" + lr.getDefaultInterest());
            // 借款者手续费
            systemBillService.transferInto(lr.getFee(), OperatorInfo.OVERDUE_REPAY, "借款：" + lr.getLoan().getId() + "逾期还款，扣除手续费， 还款ID：" + lr.getId());
            // 罚息转入网站账户
            systemBillService.transferInto(defaultInterest, OperatorInfo.OVERDUE_REPAY, "借款：" + lr.getLoan().getId() + "逾期还款，扣除罚金， 还款ID：" + lr.getId());
            ht.merge(lr);
            // 判断是否所有还款结束，更改等待还款的投资状态和还款状态，还有项目状态。
            loanService.dealComplete(lr.getLoan().getId());
        } else {
            throw new OverdueRepayException("还款不处于逾期还款状态");
        }
    }

    @Override
    public void overdueRepayByAdmin(String repayId, String adminUserId) {
        // TODO Auto-generated method stub
    }

    @Override
    @Transactional
    public void finishRepay(String loanRepayId) throws InsufficientBalance {
        LoanRepay repay = ht.get(LoanRepay.class, loanRepayId, LockMode.PESSIMISTIC_WRITE);
        if (repay.getStatus().equals(RepayStatus.WAIT_FINISH_FUNDS)) {
            repay.setStatus(RepayStatus.FINISH_FUNDS);
            Double allRepayMoney = ArithUtil.add(repay.getCorpus(),
                    repay.getDefaultInterest(), repay.getFee(), repay.getInterest());
            Double repayMoney = ArithUtil.add(repay.getCorpus(),
                    repay.getDefaultInterest(), repay.getInterest());
            // 借款者的账户，扣除还款。
            userBillBO.transferOutFromFrozen(repay.getLoan().getUser().getId(), allRepayMoney, OperatorInfo.NORMAL_REPAY, "借款：" + repay.getLoan().getId() + "正常还款, 还款ID：" + repay.getId() + " 本金："
                    + repay.getCorpus() + "  利息：" + repay.getInterest() + "  手续费：" + repay.getFee());
            // 借款者手续费
            systemBillService.transferInto(repay.getFee(), OperatorInfo.NORMAL_REPAY, "借款：" + repay.getLoan().getId() + "正常还款，扣除手续费， 还款ID：" + repay.getId());
            // 转入系统账户
            systemBillService.transferInto(repayMoney, OperatorInfo.NORMAL_REPAY, "借款：" + repay.getLoan().getId() + "正常还款，转入资金池， 还款ID：" + repay.getId() + " 本金："
                    + repay.getCorpus() + "  利息：" + repay.getInterest());

            ht.update(repay);
        }
    }

    @Override
    @Transactional
    public void finishInvestRepay(String investRepayId) throws NormalRepayException, InsufficientBalance {
        InvestRepay ir = ht.get(InvestRepay.class, investRepayId, LockMode.PESSIMISTIC_WRITE);
        List<LoanRepay> lrs = (List<LoanRepay>) ht.find("from LoanRepay lr where lr.loan.id=? and lr.period=?", ir.getInvest().getLoan().getId(), ir.getPeriod());
        if (lrs.size() != 1) {
            throw new NormalRepayException("发现多条还款记录");
        }
        //TODO 校验LoanRePay?
        LoanRepay repay = lrs.get(0);
        // FIXME: 记录repayWay信息
        ir.setStatus(RepayStatus.COMPLETE);
        ir.setTime(new Date());
        ht.update(ir);
        String userId = ir.getInvest().getUser().getId();
        userBillBO.transferIntoBalance(userId, ArithUtil.add(ir.getCorpus(), ir.getInterest()), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款, 还款ID:" + repay.getId()
                + "  借款ID:" + ir.getInvest().getLoan().getId() + "  本金：" + ir.getCorpus() + "  利息：" + ir.getInterest());
        // 投资者手续费
        userBillBO.transferOutFromBalance(userId, ir.getFee(), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:" + repay.getId());
        systemBillService.transferInto(ir.getFee(), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:" + repay.getId());
        systemBillService.transferOut(ArithUtil.add(ir.getCorpus(), ir.getInterest()), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款, 还款ID:" + repay.getId()
                + "  借款ID:" + ir.getInvest().getLoan().getId() + "  本金：" + ir.getCorpus() + "  利息：" + ir.getInterest());
        dealComplete(repay.getId());
    }

    @Override
    @Transactional
    public void dealComplete(String loanRepayId) {
        if (isComplete(loanRepayId)) {
            LoanRepay repay = ht.get(LoanRepay.class, loanRepayId);
            ht.evict(repay);
            repay = ht.get(LoanRepay.class, loanRepayId, LockMode.PESSIMISTIC_WRITE);
            repay.setStatus(RepayStatus.COMPLETE);
            ht.merge(repay);
            loanService.dealComplete(repay.getLoan().getId());
        }
    }

    @Override
    public boolean isComplete(String loanRepayId) {
        LoanRepay repay = ht.get(LoanRepay.class, loanRepayId);
        List<InvestRepay> irs = (List<InvestRepay>) ht.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?", repay.getLoan().getId(), repay.getPeriod());
        for (InvestRepay ir : irs) {
            if (!ir.getStatus().equals(RepayStatus.COMPLETE)) {
                return false;
            }
        }
        return true;

    }

    @Override
    public void repayAlert() {
        int daysBefore = Integer.parseInt(configService.getConfigValue(RepayAlert.DAYS_BEFORE));
        List<LoanRepay> lrs = (List<LoanRepay>) ht.find("from LoanRepay lr where lr.status =? and lr.repayDay<=?", LoanConstants.RepayStatus.REPAYING, DateUtil.DateToString(DateUtil.addDay(new Date(), daysBefore), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        log.info("repay alert start, size:" + lrs.size());

        // 还款提醒。。。
        for (LoanRepay lr : lrs) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", lr.getLoan().getUser().getUsername());
            params.put("loanName", lr.getLoan().getName());
            params.put("days", String.valueOf(DateUtil.getIntervalDays(new Date(), lr.getRepayDay())));
            messageBO.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.REPAY_ALERT + "_sms"), params, lr.getLoan().getUser().getMobileNumber());
        }
    }

    /**
     * 根据InvestRepay转钱到user_wealth
     *
     * @param ir
     * @param repay
     * @return
     * @throws InsufficientBalance
     */
    private boolean wealthRepay(InvestRepay ir, LoanRepay repay) throws InsufficientBalance {
        // 如果是公司代投，则需要把钱转入个人用户
        String userId = null;
        Invest invest = ir.getInvest();
        if (null != invest) {
            userId = invest.getUser().getId();
        } else {
            return false;
        }
        userBillBO.transferIntoBalance(userId, ArithUtil.add(ir.getCorpus(), ir.getInterest()), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款, 还款ID:" + repay.getId() + "  借款ID:"
                + repay.getLoan().getId() + "  本金：" + ir.getCorpus() + "  利息：" + ir.getInterest());
        // 投资者手续费
        userBillBO.transferOutFromBalance(userId, ir.getFee(), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:" + repay.getId());
        systemBillService.transferInto(ir.getFee(), OperatorInfo.NORMAL_REPAY, "投资：" + ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:" + repay.getId());
        return true;
    }


}
