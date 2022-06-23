package info.bfly.pay.p2p.user.controller;

import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.controller.RechargeHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.trusteeship.exception.TrusteeshipFormException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipTicketException;
import info.bfly.p2p.trusteeship.exception.UserWealthOperationException;
import info.bfly.p2p.user.service.RechargeService;
import info.bfly.pay.controller.SinaOrderController;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static info.bfly.core.jsf.util.FacesUtil.sendRedirect;

/**
 * Created by Administrator on 2017/3/3 0003.
 */
@Component
@Scope(ScopeType.VIEW)
public class SinaRechargeHome extends RechargeHome {


    @Resource
    SinaOrderController sinaOrderController;


    @Resource
    LoginUserInfo loginUserInfo;

    @Resource
    private RechargeService rechargeService;


    /**
     * 账户充值
     */
    public void recharge() {

        if (getInstance() == null || getInstance().getActualMoney() <= 0.0) {
            FacesUtil.addInfoMessage("充值金额有误！");
        }
        getInstance().getUser().setId(loginUserInfo.getLoginUserId());
        getInstance().setRechargeWay("online_bank");
        Recharge initRecharge = null;
        try {
            initRecharge = rechargeService.createRechargeOrder(getInstance());
        } catch (ExceedDeadlineException e) {
            throw new UserWealthOperationException("优惠券已过期");
        } catch (UnreachedMoneyLimitException e) {
            throw new UserWealthOperationException("投资金额未到达优惠券使用条件");
        }
        String reUrl = null;
        try {
            reUrl = sinaOrderController.createHostingDepositSinaPay(initRecharge);
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        } catch (TrusteeshipFormException e) {

            FacesUtil.sendHtmlValue(e.getForm());

        } catch (TrusteeshipTicketException e) {
           //需要验证码
        }

        if (StringUtils.isNotEmpty(reUrl))
            sendRedirect(sinaOrderController.getMainUrl() + reUrl);

    }

    public void repay(String rechargeId) {
        Recharge recharge = rechargeService.get(rechargeId);
        if (recharge.getStatus().equals(UserConstants.RechargeStatus.WAIT_PAY)) {
            String reUrl = null;
            try {
                reUrl = sinaOrderController.createHostingDepositSinaPay(recharge);
            } catch (TrusteeshipReturnException e) {
                FacesUtil.addErrorMessage(e.getMessage());
            } catch (TrusteeshipFormException e) {

                FacesUtil.sendHtmlValue(e.getForm());

            } catch (TrusteeshipTicketException e) {
                //需要验证码
            }
            if (StringUtils.isNotEmpty(reUrl))
                sendRedirect(sinaOrderController.getMainUrl() + reUrl);
        }
    }
}
