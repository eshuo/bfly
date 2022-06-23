package info.bfly.pay.p2p.trusteeship.event;

import org.springframework.context.ApplicationEvent;

/**
 * 充值结果通知Event
 * Created by Administrator on 2017/5/25 0025.
 */
public class DepositStatusSyncEvent extends ApplicationEvent {


    private String trusteeshipOperationId;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DepositStatusSyncEvent(Object source, String trusteeshipOperationId) {
        super(source);
        this.trusteeshipOperationId = trusteeshipOperationId;
    }

    public String getTrusteeshipOperationId() {
        return trusteeshipOperationId;
    }

    public void setTrusteeshipOperationId(String trusteeshipOperationId) {
        this.trusteeshipOperationId = trusteeshipOperationId;
    }
}
