package info.bfly.pay.p2p.trusteeship.listener;

import info.bfly.core.annotations.Log;
import info.bfly.pay.p2p.trusteeship.event.TradeStatusSyncEvent;
import info.bfly.pay.p2p.trusteeship.service.SinaPayOperationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/5/26 0026.
 */
@Async
@Component
public class TradeStatusSyncEventListener extends AbstractSystemUserSyncEventListener<TradeStatusSyncEvent> {

    @Log
    Logger logger;

    @Autowired
    SinaPayOperationService sinaPayOperationService;

    @Override
    void handleEvent(TradeStatusSyncEvent event) {
        logger.info("SinaPayListener : TradeStatusSyncEvent");

        sinaPayOperationService.TradeStatusSyncOperation(event.getTrusteeshipOperationId());
    }
}
