package info.bfly.pay.p2p.trusteeship.listener;

import info.bfly.core.annotations.Log;
import info.bfly.pay.p2p.invest.Service.SinaInvestRefundService;
import info.bfly.pay.p2p.trusteeship.event.InvestRefundsEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
@Async
@Component
public class InvestRefundsEventListener implements ApplicationListener<InvestRefundsEvent> {

    @Autowired
    private SinaInvestRefundService sinaInvestRefundService;

    @Log
    Logger log;

    @Override
    public void onApplicationEvent(InvestRefundsEvent event) {
        String source = (String) event.getSource();
        log.info("InvestRefundsEventListener增加退款申请发布:" + source);
        sinaInvestRefundService.addRefund(source);
    }
}
