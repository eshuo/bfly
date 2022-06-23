package info.bfly.p2p.trusteeship.exception;

import org.apache.logging.log4j.message.ParameterizedMessageFactory;

/**
 * Description: 第三方资金托管时候,需要推进
 */
public class TrusteeshipTicketException extends Exception {
    private static final long serialVersionUID = 6156071238261755639L;


    public TrusteeshipTicketException(String msg) {
        super(msg);
    }

    public TrusteeshipTicketException(String message, Object... parameters) {
        super(ParameterizedMessageFactory.INSTANCE.newMessage(message, parameters).getFormattedMessage());

    }

    public TrusteeshipTicketException(Throwable e, String msg) {
        super(msg, e);
    }
}
