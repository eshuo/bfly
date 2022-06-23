package info.bfly.pay.crowd.orders.controller;

import java.io.IOException;

import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.crowd.mall.controller.MallStageHome;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.crowd.orders.model.Order;
import info.bfly.crowd.orders.service.OrderService;
import info.bfly.crowd.user.model.UserAddress;
import info.bfly.crowd.user.service.UserAddressService;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.invest.exception.ExceedMaxAcceptableRate;
import info.bfly.p2p.invest.exception.ExceedMoneyNeedRaised;
import info.bfly.p2p.invest.exception.IllegalLoanStatusException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.exception.UserWealthOperationException;
import info.bfly.pay.controller.SinaOrderController;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static info.bfly.core.jsf.util.FacesUtil.sendRedirect;

/**
 * Created by Administrator on 2017/5/25 0025.
 */
@Component
@Scope(ScopeType.VIEW)
public class SinaOrdersHome extends MallStageHome {


    @Resource
    SinaOrderController sinaOrderController;

    @Autowired
    InvestService investService;

    @Resource
    private LoginUserInfo loginUserInfo;
    
    @Resource
    private MallStageService mallStageService;
    
    @Resource
    private UserAddressService userAddressService;
    
    @Resource
    private OrderService orderService;
    
    
    private String chooseAddressId;


    @Override
    public Class<MallStage> getEntityClass() {
        return MallStage.class;
    }
    
    
    /**
     * 提交订单
     * 
     * @param mallStageCacheId
     * @param userAddressId
     * @param jsCode
     * @return
     */
    public String commit(String mallStageCacheId, String mallStageId) {

        MallStage mallStage = this.getBaseService().get(MallStage.class,
                mallStageId);

        String addressId = "";
        if (null == loginUserInfo.getLoginUserId())
            return "pretty:memberLogin";

        // 自己不能投自己的项目
        if (mallStage.getLoan().getUser().getId()
                .equals(loginUserInfo.getLoginUserId())) {
            FacesUtil.addInfoMessage("你不能投自己的项目！");
            return "";
        }

        // 如果选中地址为空则查找默认地址，如果默认地址为空则提示
        if (StringUtils.isEmpty(chooseAddressId)) {
            // 查询默认收货地址
            UserAddress defaultUserAddress = userAddressService
                    .searchUserDefaultAddress(loginUserInfo.getLoginUserId());

            if (null == defaultUserAddress) {
                FacesUtil.addErrorMessage("请选中一个收货地址！");
                return "";
            }
            addressId = defaultUserAddress.getId();
        } else {
            addressId = chooseAddressId;
        }

        // 判断是否可以送达
        boolean flag = userAddressService.judgeIsReach(addressId, mallStage
                .getLoan().getId());
        if (!flag) {
            RequestContext.getCurrentInstance().execute(
                    "userAddressPromptOpen()");
            return "";
        }

        String orderId = "";
        // 初始化 mallStageCacheId值
        if (StringUtils.isNotEmpty(mallStageCacheId)
                && "0".equals(mallStageCacheId))
            mallStageCacheId = "";

        try {
            orderId = orderService.createOrder(mallStageCacheId, mallStageId,
                    loginUserInfo.getLoginUserId(), addressId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // FacesUtil.addInfoMessage("订单提交成功！订单号：" + orderId);

        this.saveSinaPay(mallStage.getLoan(), orderId, mallStageService
                .countMallTotalPrice(mallStageCacheId, mallStageId));
        return "";
    }
    
   

    /**
     * 跳转新浪支付
     *
     * @param loanId
     * @param orderId
     * @param money
     * @return
     */
    public String saveSinaPay(Loan loan, String orderId, Double money) {

        Invest invest = new Invest();
        invest.setUser(new User(loginUserInfo.getLoginUserId()));
        invest.setMoney(money);
        invest.setLoan(new Loan(loan.getId()));
        invest.setOrder(getBaseService().get(Order.class, orderId));
        invest.setIsAutoInvest(false);
        try {
            invest = investService.create(invest);
            String url = sinaOrderController.createHostingCollectTradeSinaPay(
                    invest);
            if (StringUtils.isNotEmpty(url)) {
                sendRedirect(sinaOrderController.getMainUrl() + url);
            }
        } catch (InsufficientBalance e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("账户余额不足，请充值！");
        } catch (ExceedMoneyNeedRaised e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("投资金额不能大于尚未募集的金额！");
        } catch (ExceedMaxAcceptableRate e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("竞标利率不能大于借款者可接受的最高利率！");
        } catch (ExceedDeadlineException e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("优惠券已过期！");
        } catch (UnreachedMoneyLimitException e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("投资金额未到达优惠券使用条件！");
        } catch (IllegalLoanStatusException e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("当前借款不可投资！");
        } catch (TrusteeshipReturnException e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage(e.getMessage());
        }

        return "";
    }


    public String getChooseAddressId() {
        return chooseAddressId;
    }


    public void setChooseAddressId(String chooseAddressId) {
        this.chooseAddressId = chooseAddressId;
    }
    

}
