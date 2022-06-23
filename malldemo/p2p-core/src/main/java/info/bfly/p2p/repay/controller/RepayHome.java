package info.bfly.p2p.repay.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.user.service.impl.UserBillBO;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.repay.exception.AdvancedRepayException;
import info.bfly.p2p.repay.exception.NormalRepayException;
import info.bfly.p2p.repay.exception.OverdueRepayException;
import info.bfly.p2p.repay.model.Repay;
import info.bfly.p2p.repay.service.RepayService;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 还款
 *
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class RepayHome extends EntityHome<Repay> implements Serializable {
    @Resource
    RepayService repayService;
    @Resource
    UserBillBO   userBillBO;

    /**
     * 提前还款
     */
    public void advanceRepay(String loanId) {
        try {
            repayService.advanceRepay(loanId);
            FacesUtil.addInfoMessage("提前还款成功！");
        } catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage("余额不足！");
        } catch (AdvancedRepayException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
    }

    /**
     * 正常还款
     *
     * @return
     */
    public void normalRepay(String repayId) {
        try {
            repayService.normalRepay(repayId);
            FacesUtil.addInfoMessage("还款成功！");
        } catch (InsufficientBalance e) {
            // 余额不足
            FacesUtil.addErrorMessage("您的账户余额不足，无法完成还款，请充值。");
        } catch (NormalRepayException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
    }

    /**
     * 逾期还款
     */
    public void overdueRepay(String repayId) {
        try {
            repayService.overdueRepay(repayId);
        } catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage("余额不足！");
        } catch (OverdueRepayException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
    }
}
