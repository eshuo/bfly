package info.bfly.pay.p2p.trusteeship.event;

import org.springframework.context.ApplicationEvent;

/**
 * 企业会员审核通知Event
 * Created by Administrator on 2017/5/25 0025.
 */
public class AuditStatusSyncEvent extends ApplicationEvent {



    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AuditStatusSyncEvent(Object source) {
        super(source);
    }
}
