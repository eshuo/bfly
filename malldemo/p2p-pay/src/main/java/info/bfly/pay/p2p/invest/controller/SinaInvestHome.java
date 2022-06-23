package info.bfly.pay.p2p.invest.controller;

import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.controller.InvestHome;
import info.bfly.p2p.invest.exception.ExceedMaxAcceptableRate;
import info.bfly.p2p.invest.exception.ExceedMoneyNeedRaised;
import info.bfly.p2p.invest.exception.IllegalLoanStatusException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.pay.bean.enums.ACCOUNT_TYPE;
import info.bfly.pay.controller.SinaOrderController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static info.bfly.core.jsf.util.FacesUtil.sendRedirect;

/**
 * Created by XXSun on 3/2/2017.
 */
@Component
@Scope(ScopeType.VIEW)
public class SinaInvestHome extends InvestHome {
    @Resource
    SinaOrderController sinaOrderController;

    @Autowired
    InvestService investService;

    @Resource
    LoginUserInfo loginUserInfo;


    private String form;


    @Override
    public String save() {
        Loan loan = getBaseService().get(Loan.class,
                getInstance().getLoan().getId());
        if (loan.getUser().getId()
                .equals(loginUserInfo.getLoginUserId())) {
            FacesUtil.addInfoMessage("你不能投自己的项目！");
            return null;
        } else {
            this.getInstance().setUser(
                    new User(loginUserInfo.getLoginUserId()));
            this.getInstance().setIsAutoInvest(false);

            try {
                Invest invest = investService.create(getInstance());
                createSinaPay(invest);
            } catch (InsufficientBalance e) {
                FacesUtil.addErrorMessage("账户余额不足，请充值！");
//                throw new UserWealthOperationException("账户余额不足，请充值！");
            } catch (ExceedMoneyNeedRaised e) {
                FacesUtil.addErrorMessage("投资金额不能大于尚未募集的金额！");
                //               throw new UserWealthOperationException("投资金额不能大于尚未募集的金额！");
            } catch (ExceedMaxAcceptableRate e) {
                FacesUtil.addErrorMessage("竞标利率不能大于借款者可接受的最高利率！");
                //            throw new UserWealthOperationException("竞标利率不能大于借款者可接受的最高利率！");
            } catch (ExceedDeadlineException e) {
                FacesUtil.addErrorMessage("优惠券已过期！");
                //         throw new UserWealthOperationException("优惠券已过期");
            } catch (UnreachedMoneyLimitException e) {
                FacesUtil.addErrorMessage("投资金额未到达优惠券使用条件！");
                //        throw new UserWealthOperationException("投资金额未到达优惠券使用条件");
            } catch (IllegalLoanStatusException e) {
                FacesUtil.addErrorMessage("当前借款不可投资！");
                //          throw new UserWealthOperationException("当前借款不可投资");
            }
        }
        return "";
    }

    /**
     * 创建新浪支付操作
     *
     * @param invest 需要支付订单
     */
    private void createSinaPay(Invest invest) {
        try {
            String form = sinaOrderController.createHostingCollectTradeSinaPay(invest);
            if (StringUtils.isNotEmpty(form)) {
//                    FacesUtil.sendRedirect(form);
                sendRedirect(sinaOrderController.getMainUrl() + form);
            }
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
            //   throw new UserWealthOperationException(e.getMessage());
        }
    }

    public void repay(String investId) {
        Invest invest = investService.get(investId);
        try {

            String form = sinaOrderController.createHostingCollectTradeSinaPay(invest);
            if (StringUtils.isNotEmpty(form)) {
                sendRedirect(sinaOrderController.getMainUrl() + form);
            }
        } catch (TrusteeshipReturnException e) {
            e.printStackTrace();
        }
    }


    /**
     * 撤销  loan为RAISING状态   Invest为BID_FROZEN状态
     *
     * @param investId
     */
    public void refund(String investId) {
        Invest invest = investService.get(investId);
        if (invest.getLoan().getStatus().equals(LoanConstants.LoanStatus.RAISING)) {
            try {
                String sinaData = sinaOrderController.oneCancelPreAuthTradeSinaPay(invest);
                if (sinaData.equals("SUCCESS")) {
                    invest.setStatus(InvestConstants.InvestStatus.WAIT_CANCEL_AFFIRM);
                    investService.update(invest);
                    //TODO 刷新当前页
                    FacesUtil.addInfoMessage(" 申请代收撤销成功 ！");
                } else {
                    FacesUtil.addInfoMessage(sinaData);
                }
            } catch (TrusteeshipReturnException e) {
                FacesUtil.addErrorMessage(e.getMessage());
            }
        } else
            FacesUtil.addErrorMessage(" 非筹款中的投资不能退款 ！");
    }


    /**
     * 单笔放款
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasRole('FUSHEN')")
    public String recheckOne(String investId) {

        Invest instance = investService.get(investId);

        Invest merge = getBaseService().merge(instance);

        try {
            sinaOrderController.createOneSingleHostingPayTradeSinaPay(merge, ACCOUNT_TYPE.BASIC);
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }

        //TODO 返回刷新页面

        return "";
    }

    @Override
    public Class<Invest> getEntityClass() {
        return Invest.class;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
