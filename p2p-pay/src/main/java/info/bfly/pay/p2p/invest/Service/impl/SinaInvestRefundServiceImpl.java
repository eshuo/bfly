package info.bfly.pay.p2p.invest.Service.impl;

import info.bfly.core.annotations.Log;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.model.InvestRefunds;
import info.bfly.p2p.invest.service.InvestRefundsService;
import info.bfly.pay.controller.SinaOrderController;
import info.bfly.pay.p2p.invest.Service.SinaInvestRefundService;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
@Service("sinaInvestRefundService")
public class SinaInvestRefundServiceImpl implements SinaInvestRefundService {


    @Resource
    private InvestRefundsService investRefundsService;


    @Resource
    private SinaOrderController sinaOrderController;

    @Resource
    private HibernateTemplate ht;

    @Log
    Logger log;


    @Override
    public void addRefund(String investId) {

        Invest invest = ht.get(Invest.class, investId);

        if (invest == null) {
            log.error(investId + "退款处理为空!");
            return;
        }

        InvestRefunds init = investRefundsService.init(invest);

        String hostingRefundSinaPay = sinaOrderController.createHostingRefundSinaPay(init);

        if (hostingRefundSinaPay.equals("SUCCESS")) {
            init.setStatus(InvestConstants.InvestRefundsStatus.REFUND_HANDLE);
            investRefundsService.updateOrSaveInvestRefunds(init);
            log.info(investId + "Invest退款申请成功");
        } else {
            log.info(investId + "Invest退款申请退款失败！" + hostingRefundSinaPay);
        }


    }
}
