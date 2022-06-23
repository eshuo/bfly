package info.bfly.p2p.loan.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.DateStyle;
import info.bfly.core.util.DateUtil;
import info.bfly.crowd.mall.MallConstants;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.exception.BorrowedMoneyTooLittle;
import info.bfly.p2p.loan.exception.ExistWaitAffirmInvests;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.exception.InvalidExpectTimeException;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.service.LoanCalculator;
import info.bfly.p2p.loan.service.LoanService;
import info.bfly.p2p.schedule.ScheduleConstants;
import info.bfly.p2p.schedule.job.AutoInvestAfterLoanPassed;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
@Component
@Scope(ScopeType.VIEW)
public class LoanHome extends EntityHome<Loan> implements Serializable {
    private static final long serialVersionUID = 3545283925810357900L;

    private boolean ispass = false;
    @Resource
    private LoanService loanService;
    @Resource
    private LoginUserInfo loginUser;
    @Resource
    private UserService userService;
    @Resource
    ConfigService configService;
    @Resource
    MallStageService mallStageService;
    @Log
    private Logger log;
    @Resource
    StdScheduler scheduler;

    @Resource
    LoanCalculator loanCalculator;

    public LoanHome() {
        setUpdateView(FacesUtil.redirect(LoanConstants.View.LOAN_LIST));
        setDeleteView(FacesUtil.redirect(LoanConstants.View.LOAN_LIST));
        setSaveView(FacesUtil.redirect(MallConstants.View.MALL_LIST));
    }

    /**
     * 调度，自动投标--
     *
     * @param loan
     */
    private void addAutoInvestJob(Loan loan) {
        JobDetail jobDetail2 = JobBuilder.newJob(AutoInvestAfterLoanPassed.class).withIdentity(loan.getId(), ScheduleConstants.JobGroup.AUTO_INVEST_AFTER_LOAN_PASSED).build();
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
            log.debug("添加[自动投标]调度成功，项目编号[" + loan.getId() + "]，时间：" + DateUtil.DateToString(startDate, DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
    }

    /**
     * 删除自动投标
     *
     * @param loan
     */
    private void delAutoInvestJob(Loan loan) {
        //scheduler.deleteJob(null);
        JobKey jobKey = JobKey.jobKey(loan.getId(), ScheduleConstants.TriggerGroup.AUTO_INVEST_AFTER_LOAN_PASSED);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.debug(e.getMessage());
        }

    }

    /**
     * 管理员手动添加借款。
     *
     * @return
     */
    @PreAuthorize("hasRole('LOAN_PUBLISH')")
    public String createAdminLoan() {
        Loan loan = this.getInstance();
        //增加项目期限天数
        if ("month".equals(loan.getType().getRepayTimeUnit())) {
            loan.setProjectDuration(loan.getDeadline() * 30);
        } else {
            loan.setProjectDuration(loan.getDeadline());
        }
        boolean checked = false;
        if (!userService.hasRole(loan.getUser().getId(), "INVESTOR")) {
            FacesUtil.addErrorMessage("用户：" + loan.getUser().getId() + "未实名认证，不能发起借款！");
            checked = true;
        }
        if (checked) {
            return null;
        }
        try {
            loanService.createLoanByAdmin(loan);
        } catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage("余额不足，无法支付借款保证金。");
            return null;
        } catch (InvalidExpectTimeException e) {
            FacesUtil.addErrorMessage("预计执行时间必须在当前时间之后");
            return null;
        }
        FacesUtil.addInfoMessage("发布借款成功");
        return FacesUtil.redirect(getUpdateView());
    }

    /**
     * 延期
     *
     * @return
     */
    @PreAuthorize("hasRole('FUSHEN')")
    public String delay() {
        try {

            Loan instance = getInstance();

            Double continueMoney = instance.getLoanMoney();

            Double loanMoney = getBaseService().get(Loan.class, instance.getId()).getLoanMoney();

            if (continueMoney < loanMoney) {
                FacesUtil.addErrorMessage("延期借款总金额不能小于延期前融资总金额！");
                return "";
            }


            loanService.update(instance);
            loanService.delayExpectTime(instance.getId(), instance.getExpectTime());
        } catch (InvalidExpectTimeException e) {
            FacesUtil.addErrorMessage("预计执行时间必须在当前时间之后");
            return null;
        }
        FacesUtil.addInfoMessage("项目延期成功，募集期延至" + DateFormatUtils.format(getInstance().getExpectTime(), "yyyy年MM月dd日 HH:mm:ss"));
        return FacesUtil.redirect(getUpdateView());
    }


    /**
     * 流标
     *
     * @return
     */
    @PreAuthorize("hasRole('FUSHEN')")
    public String failByManager() {
        try {
            loanService.fail(this.getInstance().getId(), loginUser.getLoginUserId());
        } catch (ExistWaitAffirmInvests e) {
            FacesUtil.addInfoMessage("流标失败，存在等待第三方资金托管确认的投资。");
            return null;
        }
        FacesUtil.addInfoMessage("流标成功");
        return FacesUtil.redirect(getUpdateView());
    }

    public boolean getIspass() {
        return ispass;
    }

    /**
     * 判断一个loan是否有某个属性
     *
     * @param loanId
     * @param attrId 属性id
     * @return
     */
    public boolean hasAttr(String loanId, String attrId) {
        String hql = "select loan from Loan loan left join loan.loanAttrs attr where loan.id=? and attr.id = ?";
        return getBaseService().find(hql, loanId, attrId).size() > 0;
    }

    public void initVerify(Loan loan) {
        setInstance(loan);
        if (LoanConstants.LoanVerifyStatus.PASSED.equals(this.getInstance().getVerified())) {
            ispass = true;
        }
    }

    /**
     * 放款
     */
    @PreAuthorize("hasRole('FUSHEN')")
    public String recheck() throws TrusteeshipReturnException {
        try {
            loanService.giveMoneyToBorrower(this.getInstance().getId());
        } catch (ExistWaitAffirmInvests e) {
            FacesUtil.addInfoMessage("放款失败，存在等待第三方资金托管确认的投资。");
            return null;
        } catch (BorrowedMoneyTooLittle e) {
            FacesUtil.addInfoMessage("放款失败，募集到的资金太少。");
            return null;
        }
        FacesUtil.addInfoMessage("放款成功");
        return FacesUtil.redirect(getUpdateView());
    }

    /**
     * 申请借款
     */
    @Override
    public String save() {
        try {
            User user = userService.getUserById(loginUser.getLoginUserId());
            Loan loan = getInstance();
            loan.setUser(user);
            //增加项目期限天数
            if ("month".equals(loan.getType().getRepayTimeUnit())) {
                loan.setProjectDuration(loan.getDeadline() * 30);
            } else {
                loan.setProjectDuration(loan.getDeadline());
            }
            loanService.applyLoan(loan);
            FacesUtil.addInfoMessage("发布借款成功，请填写个人基本信息。");
            return "pretty:loanerPersonInfo";
        } catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage(e.getMessage());
            log.debug(e.getMessage());
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录！");
            log.debug(e.getMessage());
        }
        return null;
    }

    public void setIspass(boolean ispass) {
        this.ispass = ispass;
    }

    /**
     * 更新借款，只能更新不影响流程的字段
     */
    @Override
    public String update() {
        loanService.update(getInstance());
        FacesUtil.addInfoMessage("项目修改成功！");
        return FacesUtil.redirect(getUpdateView());
    }

    /**
     * 借款审核
     */
    @PreAuthorize("hasRole('CHUSHEN')")
    public String verify() {
        if (ispass) {
            FacesUtil.addInfoMessage("通过借款申请");
        } else {
            loanService.refuseApply(this.getInstance().getId(), this.getInstance().getVerifyMessage(), loginUser.getLoginUserId());
            FacesUtil.addInfoMessage("拒绝借款申请");
        }
        return FacesUtil.redirect(getUpdateView());
    }

    @Override
    @PreAuthorize("hasRole('LOAN_PUBLISH')")
    public String delete() {
        Loan loan = this.getInstance();
        return delete(loan);
    }

    @Override
    public String delete(String id) {
        //do nothing
        return FacesUtil.redirect(getUpdateView());
    }

    @PreAuthorize("hasRole('LOAN_PUBLISH')")
    public String delete(Loan loan) {
        if (LoanConstants.LoanStatus.WAITING_VERIFY.equals(loan.getStatus()))
            loan.setStatus(LoanConstants.LoanStatus.DEL);
        loanService.update(loan);
        delAutoInvestJob(loan);
        FacesUtil.addInfoMessage("项目删除成功！");
        return FacesUtil.redirect(getUpdateView());
    }

    @Transactional(readOnly = false)
    public String saveStage() {
        List<MallStage> mallstages = new ArrayList<MallStage>();
        mallstages = getInstance().getMallstages();
        for (int i = 0; i < mallstages.size(); i++) {
            MallStage mallstage = new MallStage();
            mallstage = mallstages.get(i);
            mallstage.setLoan(getInstance());
            mallStageService.save(mallstage);
        }
        return FacesUtil.redirect(getSaveView());
    }


}
