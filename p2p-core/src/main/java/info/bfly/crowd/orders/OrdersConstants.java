package info.bfly.crowd.orders;

/**
 * Created by XXSun on 3/21/2017.
 */
public class OrdersConstants {

    public static final class View {
        public static final String ORDER_LIST = "/admin/order/orderList";
    }

    public static final class OrderStatus {

        public static final String WAITING_PAY = "waiting_pay";

        public static final String PAYED = "payed";

        public static final String FINISHED = "finished";
        
        public static final String PAY_FAILURE = "pay_failure";

    }

    public static final String Package = "info.bfly.crowd";
}
