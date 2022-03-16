package info.bfly.p2p.loan.exception;

/**
 * Description: 募集到的资金太少，为0、或者不足以支付借款保证金；
 *
 */
public class BorrowedMoneyTooLittle extends Exception {
    private static final long serialVersionUID = -1661921253737273870L;

    public BorrowedMoneyTooLittle() {
    }

    public BorrowedMoneyTooLittle(String msg) {
        super(msg);
    }

    public BorrowedMoneyTooLittle(String msg, Throwable e) {
        super(msg, e);
    }
}
