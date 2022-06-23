package info.bfly.p2p.invest.exception;

/**
 * Description: 投资金额大于尚未募集的金额
 */
public class ExceedMoneyNeedRaised extends Exception {
    private static final long serialVersionUID = 6113547190433162128L;

    public ExceedMoneyNeedRaised() {
    }

    public ExceedMoneyNeedRaised(String msg) {
        super(msg);
    }
}
