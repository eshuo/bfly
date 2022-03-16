package info.bfly.p2p.loan.exception;

/**
 * Description: 存在等待第三方资金托管确认的投资
 */
public class ExistWaitAffirmInvests extends Exception {
    private static final long serialVersionUID = -5140289093651745910L;

    public ExistWaitAffirmInvests(String msg) {
        super(msg);
    }

    public ExistWaitAffirmInvests(String msg, Throwable e) {
        super(msg, e);
    }
}
