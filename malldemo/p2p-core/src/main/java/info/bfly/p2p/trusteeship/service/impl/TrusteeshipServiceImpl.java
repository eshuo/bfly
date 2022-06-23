package info.bfly.p2p.trusteeship.service.impl;

import info.bfly.core.annotations.Log;
import info.bfly.p2p.trusteeship.service.TrusteeshipService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("trusteeshipService")
public class TrusteeshipServiceImpl implements TrusteeshipService {
    @Log
    Logger log;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleSendedOperations() {
    }
}
