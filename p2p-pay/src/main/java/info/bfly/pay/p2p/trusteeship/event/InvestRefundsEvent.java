package info.bfly.pay.p2p.trusteeship.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
public class InvestRefundsEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public InvestRefundsEvent(Object source) {
        super(source);
    }
}
