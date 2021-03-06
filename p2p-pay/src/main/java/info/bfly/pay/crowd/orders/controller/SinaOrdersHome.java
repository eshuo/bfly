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
     * ????????????
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

        // ??????????????????????????????
        if (mallStage.getLoan().getUser().getId()
                .equals(loginUserInfo.getLoginUserId())) {
            FacesUtil.addInfoMessage("??????????????????????????????");
            return "";
        }

        // ?????????????????????????????????????????????????????????????????????????????????
        if (StringUtils.isEmpty(chooseAddressId)) {
            // ????????????????????????
            UserAddress defaultUserAddress = userAddressService
                    .searchUserDefaultAddress(loginUserInfo.getLoginUserId());

            if (null == defaultUserAddress) {
                FacesUtil.addErrorMessage("??????????????????????????????");
                return "";
            }
            addressId = defaultUserAddress.getId();
        } else {
            addressId = chooseAddressId;
        }

        // ????????????????????????
        boolean flag = userAddressService.judgeIsReach(addressId, mallStage
                .getLoan().getId());
        if (!flag) {
            RequestContext.getCurrentInstance().execute(
                    "userAddressPromptOpen()");
            return "";
        }

        String orderId = "";
        // ????????? mallStageCacheId???
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

        // FacesUtil.addInfoMessage("?????????????????????????????????" + orderId);

        this.saveSinaPay(mallStage.getLoan(), orderId, mallStageService
                .countMallTotalPrice(mallStageCacheId, mallStageId));
        return "";
    }
    
   

    /**
     * ??????????????????
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
            FacesUtil.addErrorMessage("?????????????????????????????????");
        } catch (ExceedMoneyNeedRaised e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("????????????????????????????????????????????????");
        } catch (ExceedMaxAcceptableRate e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("????????????????????????????????????????????????????????????");
        } catch (ExceedDeadlineException e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("?????????????????????");
        } catch (UnreachedMoneyLimitException e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("?????????????????????????????????????????????");
        } catch (IllegalLoanStatusException e) {
            orderService.updateFailOrderStatus(orderId);
            FacesUtil.addErrorMessage("???????????????????????????");
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
