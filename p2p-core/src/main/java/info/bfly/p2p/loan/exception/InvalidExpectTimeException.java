package info.bfly.p2p.loan.exception;

/**
 *
 *
 * Description: 项目预计执行时间不合法
 *
 */
public class InvalidExpectTimeException extends Exception {
    private static final long serialVersionUID = 406514312644359498L;

    public InvalidExpectTimeException() {
    }

    public InvalidExpectTimeException(String msg) {
        super(msg);
    }
}
