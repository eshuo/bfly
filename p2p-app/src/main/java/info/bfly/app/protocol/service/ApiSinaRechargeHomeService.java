package info.bfly.app.protocol.service;

import info.bfly.api.exception.ParameterExection;
import info.bfly.archer.key.ResponseMsg;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.trusteeship.exception.TrusteeshipFormException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipTicketException;
import info.bfly.p2p.user.service.RechargeService;
import info.bfly.pay.controller.SinaOrderController;
import info.bfly.pay.p2p.user.controller.SinaRechargeHome;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/4/10 0010.
 */
@Service
public class ApiSinaRechargeHomeService extends SinaRechargeHome {

    @Resource
    private RechargeService rechargeService;


    @Resource
    SinaOrderController sinaOrderController;


    @Override
    public Class<Recharge> getEntityClass() {
        return Recharge.class;
    }


    public String createHostingDeposit(Recharge recharge, String rechargeId) throws ExceedDeadlineException, UnreachedMoneyLimitException, TrusteeshipTicketException, TrusteeshipFormException, TrusteeshipReturnException {

        if (recharge == null)
            return "";

        recharge.setRechargeWay("sinaPay");
        Recharge rechargeOrder = null;
        if (StringUtils.isEmpty(rechargeId))
            rechargeOrder = rechargeService.createRechargeOrder(recharge);
        else
            rechargeOrder = rechargeService.get(rechargeId);

        if(rechargeOrder==null)
            throw new ParameterExection(ResponseMsg.INVEST_ERROR,"生成订单失败！");

        return sinaOrderController.createHostingDepositSinaPay(rechargeOrder);
    }

}
