package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserBillService;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.WithdrawCash;
import info.bfly.p2p.user.service.WithdrawCashService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Scope(ScopeType.VIEW)
public class WithdrawHome extends EntityHome<WithdrawCash> implements Serializable {
    @Log
    static Logger log;
    private static StringManager userSM = StringManager.getManager(UserConstants.Package);
    @Resource
    WithdrawCashService wcs;
    @Resource
    private UserService userService;
    @Resource
    LoginUserInfo   loginUserInfo;
    @Resource
    UserBillService userBillService;
    /**
     * 交易密码
     */
    private String cashPassword;

    public WithdrawHome() {
        setUpdateView(FacesUtil.redirect("/admin/withdraw/withdrawList"));
    }

    /**
     * 计算手续费和罚金
     */
    public boolean calculateFee() {
        double fee = wcs.calculateFee(this.getInstance().getMoney());
        if (userBillService.getBalance(loginUserInfo.getLoginUserId()) < fee + this.getInstance().getMoney()) {
            FacesUtil.addErrorMessage("余额不足！");
            FacesUtil.getCurrentInstance().validationFailed();
            this.getInstance().setMoney(0D);
            return false;
        } else {
            this.getInstance().setFee(wcs.calculateFee(this.getInstance().getMoney()));
            return true;
        }
    }

    public Boolean checkInvest() {
        String userId = loginUserInfo.getLoginUserId();
        Boolean isRight = userService.hasRole(userId, "INVEST");
        return isRight;
    }

    @Override
    protected WithdrawCash createInstance() {
        WithdrawCash withdraw = new WithdrawCash();
        withdraw.setAccountType("借款账户");
        withdraw.setFee(0D);
        withdraw.setCashFine(0D);
        withdraw.setUser(new User(loginUserInfo.getLoginUserId()));
        return withdraw;
    }

    public String getCashPassword() {
        return cashPassword;
    }

    /**
     * 提现审核复核不通过
     */
    public String recheckFail() {
        getInstance().setVerifyUser(new User(loginUserInfo.getLoginUserId()));
        wcs.refuseWithdrawCashApply(this.getInstance());
        FacesUtil.addInfoMessage("复核未通过，用户账户的资金会自动解冻");
        return getUpdateView();
    }

    /**
     * 提现审核复核通过
     */
    public String recheckPass() {
        getInstance().setRecheckUser(new User(loginUserInfo.getLoginUserId()));
        wcs.passWithdrawCashRecheck(this.getInstance());
        FacesUtil.addInfoMessage("复核通过，用户账户资金会自动解冻并扣除");
        return getUpdateView();
    }

    public void setCashPassword(String cashPassword) {
        this.cashPassword = cashPassword;
    }

    /**
     * 提现审核初审不通过
     */
    public String verifyFail() {
        getInstance().setVerifyUser(new User(loginUserInfo.getLoginUserId()));
        wcs.refuseWithdrawCashApply(this.getInstance());
        FacesUtil.addInfoMessage("初审未通过，用户账户的资金会自动解冻");
        return getUpdateView();
    }

    /**
     * 提现审核初审通过
     */
    public String verifyPass() {
        getInstance().setVerifyUser(new User(loginUserInfo.getLoginUserId()));
        wcs.passWithdrawCashApply(this.getInstance());
        FacesUtil.addInfoMessage("审核通过，请等待系统复核");
        return getUpdateView();
    }

    /**
     * 提现
     */
    public String withdraw() {

        if (this.getInstance().getMoney() == null || this.getInstance().getMoney() < 0) {
            FacesUtil.addErrorMessage("提现金额有误！");
            return "";
        }
        this.getInstance().setUser(new User(loginUserInfo.getLoginUserId()));
        try {
            wcs.applyWithdrawCash(this.getInstance());
            FacesUtil.addInfoMessage("您的提现申请已经提交成功，请等待审核！");
            return "pretty:myCashFlow";
        } catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage("余额不足！");
            return null;
        }
    }
}
