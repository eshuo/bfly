package info.bfly.p2p.invest.exception;

/**
 * Description: 投资竞标利率大于借款可接受的最大利率
 */
public class ExceedMaxAcceptableRate extends Exception {
    private static final long serialVersionUID = 3415891769821030249L;

    public ExceedMaxAcceptableRate() {
    }

    public ExceedMaxAcceptableRate(String msg) {
        super(msg);
    }

    public ExceedMaxAcceptableRate(String msg, Throwable e) {
        super(msg, e);
    }
}
