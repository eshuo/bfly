package info.bfly.pay.p2p.loan.controller;

import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.ArithUtil;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.service.InvestRefundsService;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.controller.LoanHome;
import info.bfly.p2p.loan.exception.BorrowedMoneyTooLittle;
import info.bfly.p2p.loan.exception.ExistWaitAffirmInvests;
import info.bfly.p2p.loan.exception.InvalidExpectTimeException;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.service.LoanCalculator;
import info.bfly.p2p.loan.service.LoanService;
import info.bfly.p2p.loan.service.impl.LoanBO;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.pay.bean.enums.ACCOUNT_TYPE;
import info.bfly.pay.controller.SinaOrderController;
import info.bfly.pay.controller.SinaUserController;
import info.bfly.pay.p2p.trusteeship.event.InvestRefundsEvent;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Component
@Scope(ScopeType.VIEW)
public class SinaLoanHome extends LoanHome {

    @Resource
    StdScheduler scheduler;

    @Resource
    ConfigService configService;

    @Log
    private Logger log;

    @Resource
    SinaOrderController sinaOrderController;

    @Autowired
    UserService userService;
    @Autowired
    SinaUserController sinaUserController;

    @Resource
    private LoanService loanService;

    @Resource
    private LoginUserInfo loginUser;

    @Resource
    private LoanBO loanBO;

    @Resource
    private HibernateTemplate ht;

    @Resource
    private LoanCalculator loanCalculator;

    @Resource
    private InvestRefundsService investRefundsService;

    @Autowired
    private ApplicationEventPublisher applicationContext;


    public SinaLoanHome() {
        setUpdateView(FacesUtil.redirect(LoanConstants.View.LOAN_LIST));
        setDeleteView(FacesUtil.redirect(LoanConstants.View.LOAN_LIST));
    }

    @Override
    public Class<Loan> getEntityClass() {
        return Loan.class;
    }

    /**
     * 调度，自动投标--
     *
     * @param loan
     */
    private void addAutoInvestJob(Loan loan) {
        /*JobDetail jobDetail2 = JobBuilder.newJob(AutoInvestAfterLoanPassed.class).withIdentity(loan.getId(), ScheduleConstants.JobGroup.AUTO_INVEST_AFTER_LOAN_PASSED).build();
        jobDetail2.getJobDataMap().put(AutoInvestAfterLoanPassed.LOAN_ID, loan.getId());
        // FIXME:需判断DELAY_TIME是否大于0
        Date startDate = DateUtil.addMinute(new Date(), Integer.parseInt(configService.getConfigValue(ConfigConstants.AutoInvest.DELAY_TIME)));
        SimpleTrigger trigger2 = TriggerBuilder.newTrigger().withIdentity(loan.getId(), ScheduleConstants.TriggerGroup.AUTO_INVEST_AFTER_LOAN_PASSED).forJob(jobDetail2)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()).startAt(startDate).build();
        try {
            scheduler.scheduleJob(jobDetail2, trigger2);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        if (log.isDebugEnabled())
            log.debug("添加[自动投标]调度成功，项目编号[" + loan.getId() + "]，时间：" + DateUtil.DateToString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS_CN));*/
    }


    /**
     * 管理员手动添加借款。
     *
     * @return
     */
    @Override
    @PreAuthorize("hasRole('LOAN_PUBLISH')")
    public String createAdminLoan() {
        Loan loan = this.getInstance();
        if (userService.isSetCashPassword(loan.getUser().getId()) || sinaUserController.queryIsSetPayPasswordSinaPay(loan.getUser().getId())) {
            return super.createAdminLoan();

        } else {
            FacesUtil.addErrorMessage("用户：" + loan.getUser().getId() + "未设置支付密码，不能发起借款！");
            return "";
        }

    }

    /**
     * 借款审核
     */
    @Transactional
    @PreAuthorize("hasRole('CHUSHEN')")
    public String verifySuccess() {
        Loan loan = this.getInstance();
        if (loan.getExpectTime() == null) {
            FacesUtil.addErrorMessage("请填写预计执行时间。");
            return null;
        }

        if (!loan.getExpectTime().after(new Date())) {
            FacesUtil.addErrorMessage("预计执行时间必须在当前时间之后");
            return null;
        }
        loan.setVerifyUser(new User(loginUser.getLoginUserId()));
        loan.setInitMoney(loan.getLoanMoney());
        loan = ht.merge(loan);
        try {
            String bidInfoSinaPay = sinaOrderController.createBidInfoSinaPay(loan);
            if (bidInfoSinaPay.equals("SUCCESS")) {
                loanService.passApply(loan);
//                addAutoInvestJob(loan);
                FacesUtil.addInfoMessage("审核通过");
                return FacesUtil.redirect(getUpdateView());
            } else if (bidInfoSinaPay.equals("SINA_VERIFY")) {
                FacesUtil.addInfoMessage("新浪审核中...");

                return FacesUtil.redirect(getUpdateView());
            } else {
                //未通过借款初审
                FacesUtil.addErrorMessage("新浪审核错误，" + bidInfoSinaPay);
                return "";
            }

        } catch (InvalidExpectTimeException e) {
            FacesUtil.addErrorMessage("预计执行时间必须在当前时间之后");
            return "";
        }

    }

    /**
     * 增加失败
     */
    public String verifyFail() {
        loanService.refuseApply(this.getInstance().getId(), this.getInstance().getVerifyMessage(), loginUser.getLoginUserId());
        FacesUtil.addInfoMessage("拒绝借款申请");

        return FacesUtil.redirect(getUpdateView());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasRole('FUSHEN')")
    public String recheck() throws TrusteeshipReturnException {
        Loan loan = getInstance();
        loan = ht.merge(loan);
        sinaOrderController.createSingleHostingPayTradeSinaPay(loan, loanService.getLoanMoneyToBidSuccess(loan.getId()), ACCOUNT_TYPE.BASIC);
        return getUpdateView();
    }


    private Double getLoanActualMoney(Loan loan) throws ExistWaitAffirmInvests,
            BorrowedMoneyTooLittle {
        // 实际到借款账户的金额
        double actualMoney = 0D;
        loan = getBaseService().get(Loan.class, loan.getId());
        for (Invest invest : loan.getInvests()) {
            /*if (invest.getStatus().equals(
                    InvestConstants.InvestStatus.WAIT_AFFIRM)) {
                // 放款时候，需要检查是否要等待确认的投资，如果有，则不让放款。
                throw new ExistWaitAffirmInvests("investID:" + invest.getId());
            }*/
            if (invest.getStatus().equals(
                    InvestConstants.InvestStatus.BID_SUCCESS)) {
                // 放款时候，需要只更改BID_SUCCESS 的借款。
                actualMoney = ArithUtil.add(actualMoney,
                        invest.getInvestMoney());
            }
        }
        // 借款手续费-借款保证金
        double subR = ArithUtil.sub(loan.getLoanGuranteeFee(),
                loan.getDeposit());

        double tooLittle = ArithUtil.sub(actualMoney, subR);
        // 借到的钱，可能不足以支付借款手续费
        if (tooLittle <= 0) {
            throw new BorrowedMoneyTooLittle("actualMoney：" + tooLittle);
        }
        return actualMoney;
    }


    //流标
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasRole('FUSHEN')")
    public String failByManager() {
        Loan loan = getInstance();
        loan = ht.merge(loan);

        try {

            if (loanService.isRaiseCompleted(loan.getId())) {
                FacesUtil.addErrorMessage("募集完成不能流标！");
                return "";
            }
            sinaOrderController.cancelPreAuthTradeSinaPay(loan);

            //处理支付成功的
            List<Invest> invests = loan.getInvests();
            for (Invest invest : invests) {
                if (invest.getStatus().equals(InvestConstants.InvestStatus.BID_SUCCESS)) {
                    applicationContext.publishEvent(new InvestRefundsEvent(invest.getId()));
                }
            }
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
        return getUpdateView();

    }


    /**
     * 代收完成
     *
     * @return
     */

    @PreAuthorize("hasRole('FUSHEN')")
    public String finishPreAuthTrade(Loan loan) {

        try {
            Double aDouble = loanCalculator.calculateMoneyNeedRaised(loan.getId());

            if (aDouble < loan.getLoanMoney()) {

//                //改变项目金额
//                loan.setLoanMoney(loan.getLoanMoney()-aDouble);
//                loan.setStatus(LoanConstants.LoanStatus.WAITING_RECHECK_VERIFY);
//                loanService.update(loan);
                String s = sinaOrderController.finishPreAuthTradeSinaPay(loan);
                FacesUtil.addInfoMessage(s);
                loan.setStatus(LoanConstants.LoanStatus.RECHECK);
                loanService.update(loan);
                FacesUtil.addInfoMessage("代收放款完成，请稍后进行复审放款!");
            } else {
                FacesUtil.addErrorMessage("投资对象没有融资");
            }

        } catch (TrusteeshipReturnException e) {
//            e.printStackTrace();
            FacesUtil.addErrorMessage(e.getMessage());
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage("投资对象不存在");
        }

        return "";
    }


}
