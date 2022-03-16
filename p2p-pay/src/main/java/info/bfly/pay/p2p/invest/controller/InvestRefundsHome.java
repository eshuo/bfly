package info.bfly.pay.p2p.invest.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.model.InvestRefunds;
import info.bfly.p2p.invest.service.InvestRefundsService;
import info.bfly.pay.controller.SinaOrderController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

@Component
@Scope(ScopeType.VIEW)
public class InvestRefundsHome extends EntityHome<InvestRefunds> implements Serializable {
    private static final long serialVersionUID = -5772437188954167624L;


    @Resource
    InvestRefundsService investRefundsService;


    @Resource
    SinaOrderController sinaOrderController;


    /**
     * 申请退款
     *
     * @param invest
     * @return
     */
    public String applyRefund(Invest invest) {

        if (invest == null) {
            FacesUtil.addErrorMessage("退款订单不能为空！");
        }

        //TODO 项目状态  非退款项目
        InvestRefunds init = investRefundsService.init(invest);

        String hostingRefundSinaPay = sinaOrderController.createHostingRefundSinaPay(init);

        if (hostingRefundSinaPay.equals("SUCCESS")) {

            init.setStatus(InvestConstants.InvestRefundsStatus.REFUND_HANDLE);
            investRefundsService.updateOrSaveInvestRefunds(init);
            ///admin/loan/investmentInfoList.htm
            FacesUtil.addInfoMessage("申请成功");
            return "";
        } else {
            FacesUtil.addErrorMessage("申请退款失败！" + hostingRefundSinaPay);
            return "";
        }


    }


}
