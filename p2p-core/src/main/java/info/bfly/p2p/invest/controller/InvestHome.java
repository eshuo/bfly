package info.bfly.p2p.invest.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.exception.ExceedInvestTransferMoney;
import info.bfly.p2p.invest.exception.ExceedMaxAcceptableRate;
import info.bfly.p2p.invest.exception.ExceedMoneyNeedRaised;
import info.bfly.p2p.invest.exception.IllegalLoanStatusException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.model.TransferApply;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.invest.service.TransferService;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.repay.service.RepayService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 */
@Component
@Scope(ScopeType.VIEW)
public class InvestHome extends EntityHome<Invest> implements Serializable {
    @Resource
    private InvestService   investService;
    @Resource
    private LoginUserInfo   loginUserInfo;
    @Resource
    private RepayService    repayService;
    @Resource
    private TransferService transferService;

    @Override
    protected Invest createInstance() {
        Invest invest = new Invest();
        TransferApply ta = new TransferApply();
        Loan loan = new Loan();
        loan.setUser(new User());
        invest.setLoan(loan);
        invest.setTransferApply(ta);
        return invest;
    }

    /**
     * 某个用户投资的数量
     *
     * @return
     */
    public long getInvestAmount(String userId) {
        String hql = "select count(*) from Invest invest where invest.user.id=?";
        return (Long) getBaseService().iterate(hql, userId).next();
    }

    /**
     * 获取某个用户总的投资额
     *
     * @return
     */
    public double getSumInvest(String userId) {
        Object o = getBaseService().find("select sum(im.money) from Invest im where im.user.id=?", userId).get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 获取某个用户获得的总的投资额(投资成功的) 取状态投资已完成和债权转让成功 yinxunzhi
     *
     * @return
     */
    public double getSumInvestMoney(String userId) {
        Object o = getBaseService().find("select sum(im.money) from Invest im where im.user.id=? and im.status in (?,?)", userId, InvestConstants.InvestStatus.COMPLETE).get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 投资
     */
    @Override
    public String save() {
        try {
            Loan loan = getBaseService().get(Loan.class, getInstance().getLoan().getId());
            if (loan.getUser().getId().equals(loginUserInfo.getLoginUserId())) {
                FacesUtil.addInfoMessage("你不能投自己的项目！");
                return null;
            } else {
                this.getInstance().setUser(new User(loginUserInfo.getLoginUserId()));
                this.getInstance().setIsAutoInvest(false);
                Invest invest = investService.create(getInstance());
                investService.success(invest);

            }
        } catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage("账户余额不足，请充值！");
            return null;
        } catch (ExceedMoneyNeedRaised e) {
            FacesUtil.addErrorMessage("投资金额不能大于尚未募集的金额！");
            return null;
        } catch (ExceedMaxAcceptableRate e) {
            FacesUtil.addErrorMessage("竞标利率不能大于借款者可接受的最高利率！");
            return null;
        } catch (ExceedDeadlineException e) {
            FacesUtil.addErrorMessage("优惠券已过期");
            return null;
        } catch (UnreachedMoneyLimitException e) {
            FacesUtil.addErrorMessage("投资金额未到达优惠券使用条件");
            return null;
        } catch (IllegalLoanStatusException e) {
            FacesUtil.addErrorMessage("当前借款不可投资");
            return null;
        }
        FacesUtil.addInfoMessage("投资成功！");
        if (FacesUtil.isMobileRequest()) {
            return "pretty:mobile_user_invests";
        }
        return "pretty:user_invest_bidding";
    }

    /**
     * 购买债权转让
     */
    public String transfer() {
        try {
            if (loginUserInfo.getLoginUserId().equals(getInstance().getTransferApply().getInvest().getUser().getId())) {
                FacesUtil.addErrorMessage("您不能购买自己的债权");
                return null;
            }
            transferService.transfer(getInstance().getTransferApply().getId(), loginUserInfo.getLoginUserId(), getInstance().getInvestMoney());
            return "pretty:user-transfer-purchased";
        } catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage("余额不足");
        } catch (ExceedInvestTransferMoney e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
        return null;
    }
}
