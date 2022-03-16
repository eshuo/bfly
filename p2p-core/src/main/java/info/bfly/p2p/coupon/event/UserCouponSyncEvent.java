package info.bfly.p2p.coupon.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by XXSun on 6/6/2017.
 */
public class UserCouponSyncEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public UserCouponSyncEvent(Object source) {
        super(source);
    }
}
