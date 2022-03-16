package info.bfly.p2p.trusteeship.exception;

import org.apache.logging.log4j.message.ParameterizedMessageFactory;

/**
 * Description: 第三方资金托管时候,返回的错误信息
 */
public class TrusteeshipReturnException extends Exception {
    private static final long serialVersionUID = 6156071238261755639L;


    public TrusteeshipReturnException(String msg) {
        super(msg);
    }

    public TrusteeshipReturnException(String message, Object... parameters) {
        super(ParameterizedMessageFactory.INSTANCE.newMessage(message, parameters).getFormattedMessage());

    }

    public TrusteeshipReturnException(Throwable e, String msg) {
        super(msg, e);
    }
}
