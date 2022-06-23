package info.bfly.pay.p2p.user.controller;

import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.controller.WithdrawHome;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.WithdrawCash;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.pay.controller.SinaOrderController;
import info.bfly.pay.controller.SinaUserController;
import info.bfly.pay.p2p.user.service.SinaWithdrawCashService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 提现 on 2017/3/4 0004.
 */
@Component
@Scope(ScopeType.REQUEST)
public class SinaWithdrawHome extends WithdrawHome {

    @Log
    Logger log;


    @Autowired
    SinaWithdrawCashService sinaWithdrawCashService;

    @Resource
    SinaOrderController sinaOrderController;
    @Resource
    SinaUserController  sinaUserController;

    @Resource
    LoginUserInfo loginUserInfo;

    @Autowired
    IdGenerator idGenerator;


    @Override
    public Class<WithdrawCash> getEntityClass() {
        return WithdrawCash.class;
    }

    /**
     * 第一步。申请提现
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String applyWithdraw() throws InsufficientBalance {
        if (this.getInstance().getMoney() == null || this.getInstance().getMoney() < 0) {
            FacesUtil.addErrorMessage("提现金额有误！");
            return "";
        }
        setInstance(sinaWithdrawCashService.generateWithdraw(getInstance().getMoney(), new User(loginUserInfo.getLoginUserId())));
        sinaWithdrawCashService.doLog(this.getInstance());
        try {
            sinaWithdrawCashService.applyWithdrawCash(this.getInstance());
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }


        return "";

    }

    /**
     * 第三步。确认提现
     */
    public void withdrawPay(String id)  {
        try {
            String withdrawLink = sinaWithdrawCashService.getWithdrawLink(id);
            FacesUtil.sendRedirect(sinaUserController.getMainUrl() +withdrawLink);
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
    }

    /**
     * 提现审核初审通过
     */
    public String verifyPass() {

        try {
            sinaWithdrawCashService.passWithdrawCashApply(this.getInstance());
        } catch (TrusteeshipReturnException e) {
           FacesUtil.addErrorMessage(e.getMessage());
        }
        return getUpdateView();
    }

    /**
     * 提现审核初审不通过
     */
    public String verifyFail() {
        try {
            sinaWithdrawCashService.refuseWithdrawCashApply(this.getInstance());
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
        return getUpdateView();
    }


    /**
     * 提现审核复核通过
     */
    public String recheckPass() {
        try {
            sinaWithdrawCashService.passWithdrawCashRecheck(this.getInstance());
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
        return getUpdateView();
    }

    /**
     * 提现审核复核不通过
     */
    public String recheckFail() {
        try {
            sinaWithdrawCashService.refuseWithdrawCashRecheck(this.getInstance());
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
        return getUpdateView();
    }

}
