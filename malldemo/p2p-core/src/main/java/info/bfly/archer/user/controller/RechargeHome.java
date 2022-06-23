package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.pay.service.PayService;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.SpringBeanUtil;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.coupon.model.UserCoupon;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.user.service.RechargeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Scope(ScopeType.VIEW)
public class RechargeHome extends EntityHome<Recharge> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5722365474753119615L;
    @Log
    static Logger log;

    /**
     * 获取充值银行号码
     *
     * @param recharge
     * @return
     */
    private static String getBankNo(Recharge recharge) {
        String[] strs = recharge.getRechargeWay().split("_");
        if (strs.length > 1) {
            return strs[1];
        }
        return null;
    }

    /**
     * 获取支付方式
     *
     * @param recharge
     * @return
     */
    private static String getPayWay(Recharge recharge) {
        return recharge.getRechargeWay().split("_")[0];
    }

    @Resource
    private LoginUserInfo   loginUserInfo;
    @Resource
    private RechargeService rechargeService;

    public void calculateFee() {
        double fee = rechargeService.calculateFee(this.getInstance().getActualMoney());
        this.getInstance().setFee(fee);
    }

    @Override
    protected Recharge createInstance() {
        Recharge recharge = new Recharge();
        recharge.setFee(0D);
        recharge.setUser(new User(loginUserInfo.getLoginUserId()));
        recharge.setCoupon(new UserCoupon());
        return recharge;
    }

    public String offlineRecharge() {
        rechargeService.createOfflineRechargeOrder(getInstance());
        FacesUtil.addInfoMessage("您的线下充值记录已经提交，请等待管理员审核。请勿重复提交！");
        return "pretty:userCenter";
    }


    /**
     * 充值
     */
    public void recharge() {
        if (StringUtils.isEmpty(this.getInstance().getRechargeWay())) {
            FacesUtil.addErrorMessage("请选择充值方式！");
            return;
        }
        // 前台表单_blank提交，判空使用js验证，用于防止跳转到支付页面被拦截
        try {
            Recharge recharge= rechargeService.createRechargeOrder(getInstance());
        } catch (ExceedDeadlineException e) {
            e.printStackTrace();
        } catch (UnreachedMoneyLimitException e) {
            e.printStackTrace();
        }
        //TODO 生成连接
        FacesUtil.sendRedirect("");
    }

    /**
     * 管理员充值
     *
     * @param rechargeId
     */
    public void rechargeByAdmin(String rechargeId) {
        rechargeService.rechargeByAdmin(rechargeId);
        FacesUtil.addInfoMessage("充值成功，相应款项已经充入到用户账户中");
    }

    /**
     * 去往充值支付方
     */
    public void toRecharge() {
        Recharge recharge = getInstance();
        if (recharge == null) {
            return;
        }
        if (RechargeHome.log.isDebugEnabled()) {
            RechargeHome.log.debug("payWay:" + RechargeHome.getPayWay(recharge) + ", bankNo:" + RechargeHome.getBankNo(recharge));
        }
        PayService payService = (PayService) SpringBeanUtil.getBeanByName(RechargeHome.getPayWay(recharge) + "PayService");
        payService.recharge(FacesUtil.getCurrentInstance(), recharge, RechargeHome.getBankNo(recharge));
    }
}
