package info.bfly.pay.p2p.trusteeship.listener;

import info.bfly.core.annotations.Log;
import info.bfly.pay.p2p.trusteeship.event.DepositStatusSyncEvent;
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
public class DepositStatusSyncEventListener extends AbstractSystemUserSyncEventListener<DepositStatusSyncEvent> {

    @Log
    Logger logger;

    @Autowired
    SinaPayOperationService sinaPayOperationService;

    @Override
    void handleEvent(DepositStatusSyncEvent event) {
        logger.info("SinaPayListener : DepositStatusSyncEvent");
        sinaPayOperationService.DepositStatusSyncOperation(event.getTrusteeshipOperationId());
    }
}
