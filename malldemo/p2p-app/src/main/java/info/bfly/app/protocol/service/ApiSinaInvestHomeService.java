package info.bfly.app.protocol.service;

import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.exception.ExceedMaxAcceptableRate;
import info.bfly.p2p.invest.exception.ExceedMoneyNeedRaised;
import info.bfly.p2p.invest.exception.IllegalLoanStatusException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.pay.controller.SinaOrderController;
import info.bfly.pay.p2p.invest.controller.SinaInvestHome;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/4/7 0007.
 */
@Service
@Scope(ScopeType.REQUEST)
public class ApiSinaInvestHomeService extends SinaInvestHome {

    @Resource
    SinaOrderController sinaOrderController;

    @Autowired
    InvestService investService;

    /**
     * 创建托管代收交易
     * 投资
     *
     * @return
     */
    public String appCreateTransactionInvest(Invest investInstance) throws IllegalLoanStatusException, ExceedDeadlineException, ExceedMoneyNeedRaised, ExceedMaxAcceptableRate, InsufficientBalance, UnreachedMoneyLimitException, TrusteeshipReturnException {

        Loan loan = getBaseService().get(Loan.class,
                investInstance.getLoan().getId());

        if (!loan.getUser().getId().equals(investInstance.getUser().getId())) {
            investInstance.setIsAutoInvest(false);

            Invest invest = investService.create(investInstance);
            //TODO 使用优惠券

            String form = sinaOrderController.createHostingCollectTradeSinaPay(invest);

            if (StringUtils.isNotEmpty(form)) {
                return form;
            }
            throw new TrusteeshipReturnException("创建订单失败");

        } else {
            throw new TrusteeshipReturnException("你不能投自己的项目！");
        }

    }


    public String addRepay(String investId) throws TrusteeshipReturnException {
        Invest invest = investService.get(investId);

        if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM)) {
            return sinaOrderController.createHostingCollectTradeSinaPay(invest);
        } else {
            throw new TrusteeshipReturnException("该订单不可继续投资");
        }


    }


    @Override
    public Class<Invest> getEntityClass() {
        return Invest.class;
    }
}
