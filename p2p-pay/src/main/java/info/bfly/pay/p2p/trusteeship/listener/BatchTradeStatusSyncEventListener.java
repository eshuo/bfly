package info.bfly.pay.p2p.trusteeship.listener;

import info.bfly.core.annotations.Log;
import info.bfly.pay.p2p.trusteeship.event.BatchTradeStatusSyncEvent;
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
public class BatchTradeStatusSyncEventListener extends AbstractSystemUserSyncEventListener<BatchTradeStatusSyncEvent> {

    @Log
    Logger logger;

    @Autowired
    SinaPayOperationService sinaPayOperationService;

    @Override
    void handleEvent(BatchTradeStatusSyncEvent event) {
        logger.info("SinaPayListener : BatchTradeStatusSyncEvent");
        sinaPayOperationService.BatchTradeStatusSyncOperation(event.getTrusteeshipOperationId());
    }
}
