package info.bfly.p2p.message.exception;

/**
 * Description: 短信发送出错
 */
public class SmsSendErrorException extends RuntimeException {
    private static final long serialVersionUID = -403779076788050086L;

    public SmsSendErrorException(String msg) {
        super(msg);
    }

    public SmsSendErrorException(String msg, Throwable e) {
        super(msg, e);
    }
}
