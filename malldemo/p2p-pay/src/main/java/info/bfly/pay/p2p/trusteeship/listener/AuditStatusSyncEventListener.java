package info.bfly.pay.p2p.trusteeship.listener;

import info.bfly.core.annotations.Log;
import info.bfly.pay.p2p.trusteeship.event.AuditStatusSyncEvent;
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
public class AuditStatusSyncEventListener extends AbstractSystemUserSyncEventListener<AuditStatusSyncEvent> {

    @Log
    Logger logger;

    @Autowired
    SinaPayOperationService sinaPayOperationService;


    @Override
    void handleEvent(AuditStatusSyncEvent event) {
        logger.info("SinaPayListener : AuditStatusSyncEvent");
        sinaPayOperationService.AuditStatusSyncOperation((String) event.getSource());
    }


}
