package info.bfly.pay.p2p.repay.controller;

import info.bfly.archer.user.service.UserBillService;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.ArithUtil;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.repay.controller.RepayHome;
import info.bfly.p2p.repay.exception.NormalRepayException;
import info.bfly.p2p.repay.model.LoanRepay;
import info.bfly.p2p.repay.model.Repay;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.pay.bean.enums.ACCOUNT_TYPE;
import info.bfly.pay.controller.SinaOrderController;
import info.bfly.pay.controller.SinaSystemController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static info.bfly.core.jsf.util.FacesUtil.sendRedirect;

/**
 * Created by XXSun on 3/12/2017.
 */
@Component
@Scope(ScopeType.VIEW)
public class SinaRepayHome extends RepayHome {
    @Autowired
    UserBillService      userBillService;
    @Autowired
    SinaOrderController  sinaOrderController;
    @Autowired
    SinaSystemController sinaSystemController;


    @Override
    public Class<Repay> getEntityClass() {
        return Repay.class;
    }

    public void reRepay(String repayId) {
        try {
            LoanRepay lr = getBaseService().get(LoanRepay.class, repayId);
            // 判断是否可还款
            checkReRepay(lr);
            sinaOrderController.createHostingCollectTradeSinaPay(lr);
        } catch (InsufficientBalance e) {
            // 余额不足
            FacesUtil.addErrorMessage("您的账户余额不足，无法完成还款，请充值。");
        } catch (NormalRepayException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
    }

    @Override
    public void normalRepay(String repayId) {
        try {
            LoanRepay lr = getBaseService().get(LoanRepay.class, repayId);
            // 判断是否可还款
            checkNormalRepay(lr);
            String url = sinaOrderController.createHostingCollectTradeSinaPay(lr);

            if (StringUtils.isNotEmpty(url))
                sendRedirect(sinaOrderController.getMainUrl() + url);

        } catch (InsufficientBalance e) {
            // 余额不足
            FacesUtil.addErrorMessage("您的账户余额不足，无法完成还款，请充值。");
        } catch (NormalRepayException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        } catch (TrusteeshipReturnException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
    }

    private void checkNormalRepay(LoanRepay repay) throws InsufficientBalance,
            NormalRepayException {
        if (!repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
            // 该还款不处于正常还款状态。
            throw new NormalRepayException("还款：" + repay.getId() + "不处于正常还款状态。");
        }

        double repayAllMoney = ArithUtil
                .add(repay.getCorpus(), repay.getDefaultInterest(),
                        repay.getFee(), repay.getInterest());
        double balance = userBillService.getBalance(repay.getLoan().getUser()
                .getId());
        if (balance < repayAllMoney) {
            throw new InsufficientBalance("balance:" + balance
                    + "  repayAllMoney:" + repayAllMoney);
        }
    }

    private void checkReRepay(LoanRepay repay) throws InsufficientBalance,
            NormalRepayException {
        if (!repay.getStatus().equals(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY)) {
            // 该还款不处于正常还款状态。
            throw new NormalRepayException("还款：" + repay.getId() + "不处于正常还款状态。");
        }
    }

    /**
     * 完成还款
     *
     * @param repayId
     */
    public void compliteRepay(String repayId) {
        try {
            LoanRepay lr = getBaseService().get(LoanRepay.class, repayId);
            sinaSystemController.createBatchHostingPayTradeSinaPay(lr, ACCOUNT_TYPE.BASIC);
        } catch (TrusteeshipReturnException e) {

        } catch (NormalRepayException e) {
        }
    }
}
