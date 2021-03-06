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
     * ?????????????????????--
     *
     * @param loan
     */
    private void addAutoInvestJob(Loan loan) {
        /*JobDetail jobDetail2 = JobBuilder.newJob(AutoInvestAfterLoanPassed.class).withIdentity(loan.getId(), ScheduleConstants.JobGroup.AUTO_INVEST_AFTER_LOAN_PASSED).build();
        jobDetail2.getJobDataMap().put(AutoInvestAfterLoanPassed.LOAN_ID, loan.getId());
        // FIXME:?????????DELAY_TIME????????????0
        Date startDate = DateUtil.addMinute(new Date(), Integer.parseInt(configService.getConfigValue(ConfigConstants.AutoInvest.DELAY_TIME)));
        SimpleTrigger trigger2 = TriggerBuilder.newTrigger().withIdentity(loan.getId(), ScheduleConstants.TriggerGroup.AUTO_INVEST_AFTER_LOAN_PASSED).forJob(jobDetail2)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()).startAt(startDate).build();
        try {
            scheduler.scheduleJob(jobDetail2, trigger2);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        if (log.isDebugEnabled())
            log.debug("??????[????????????]???????????????????????????[" + loan.getId() + "]????????????" + DateUtil.DateToString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS_CN));*/
    }


    /**
     * ??????????????????????????????
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
            FacesUtil.addErrorMessage("?????????" + loan.getUser().getId() + "?????????????????????????????????????????????");
            return "";
        }

    }

    /**
     * ????????????
     */
    @Transactional
    @PreAuthorize("hasRole('CHUSHEN')")
    public String verifySuccess() {
        Loan loan = this.getInstance();
        if (loan.getExpectTime() == null) {
            FacesUtil.addErrorMessage("??????????????????????????????");
            return null;
        }

        if (!loan.getExpectTime().after(new Date())) {
            FacesUtil.addErrorMessage("?????????????????????????????????????????????");
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
                FacesUtil.addInfoMessage("????????????");
                return FacesUtil.redirect(getUpdateView());
            } else if (bidInfoSinaPay.equals("SINA_VERIFY")) {
                FacesUtil.addInfoMessage("???????????????...");

                return FacesUtil.redirect(getUpdateView());
            } else {
                //?????????????????????
                FacesUtil.addErrorMessage("?????????????????????" + bidInfoSinaPay);
                return "";
            }

        } catch (InvalidExpectTimeException e) {
            FacesUtil.addErrorMessage("?????????????????????????????????????????????");
            return "";
        }

    }

    /**
     * ????????????
     */
    public String verifyFail() {
        loanService.refuseApply(this.getInstance().getId(), this.getInstance().getVerifyMessage(), loginUser.getLoginUserId());
        FacesUtil.addInfoMessage("??????????????????");

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
        // ??????????????????????????????
        double actualMoney = 0D;
        loan = getBaseService().get(Loan.class, loan.getId());
        for (Invest invest : loan.getInvests()) {
            /*if (invest.getStatus().equals(
                    InvestConstants.InvestStatus.WAIT_AFFIRM)) {
                // ??????????????????????????????????????????????????????????????????????????????????????????
                throw new ExistWaitAffirmInvests("investID:" + invest.getId());
            }*/
            if (invest.getStatus().equals(
                    InvestConstants.InvestStatus.BID_SUCCESS)) {
                // ??????????????????????????????BID_SUCCESS ????????????
                actualMoney = ArithUtil.add(actualMoney,
                        invest.getInvestMoney());
            }
        }
        // ???????????????-???????????????
        double subR = ArithUtil.sub(loan.getLoanGuranteeFee(),
                loan.getDeposit());

        double tooLittle = ArithUtil.sub(actualMoney, subR);
        // ???????????????????????????????????????????????????
        if (tooLittle <= 0) {
            throw new BorrowedMoneyTooLittle("actualMoney???" + tooLittle);
        }
        return actualMoney;
    }


    //??????
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasRole('FUSHEN')")
    public String failByManager() {
        Loan loan = getInstance();
        loan = ht.merge(loan);

        try {

            if (loanService.isRaiseCompleted(loan.getId())) {
                FacesUtil.addErrorMessage("???????????????????????????");
                return "";
            }
            sinaOrderController.cancelPreAuthTradeSinaPay(loan);

            //?????????????????????
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
     * ????????????
     *
     * @return
     */

    @PreAuthorize("hasRole('FUSHEN')")
    public String finishPreAuthTrade(Loan loan) {

        try {
            Double aDouble = loanCalculator.calculateMoneyNeedRaised(loan.getId());

            if (aDouble < loan.getLoanMoney()) {

//                //??????????????????
//                loan.setLoanMoney(loan.getLoanMoney()-aDouble);
//                loan.setStatus(LoanConstants.LoanStatus.WAITING_RECHECK_VERIFY);
//                loanService.update(loan);
                String s = sinaOrderController.finishPreAuthTradeSinaPay(loan);
                FacesUtil.addInfoMessage(s);
                loan.setStatus(LoanConstants.LoanStatus.RECHECK);
                loanService.update(loan);
                FacesUtil.addInfoMessage("????????????????????????????????????????????????!");
            } else {
                FacesUtil.addErrorMessage("????????????????????????");
            }

        } catch (TrusteeshipReturnException e) {
//            e.printStackTrace();
            FacesUtil.addErrorMessage(e.getMessage());
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage("?????????????????????");
        }

        return "";
    }


}
