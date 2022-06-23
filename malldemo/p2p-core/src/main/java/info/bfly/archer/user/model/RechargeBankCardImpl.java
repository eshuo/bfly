package info.bfly.archer.user.model;


/**
 *
 *
 * Description: 银行直连，银行bean
 */
public class RechargeBankCardImpl implements RechargeBankCard {
    private String no;
    private String bankName;
    private String cardAlias;

    public RechargeBankCardImpl(String no, String bankName) {
        super();
        this.no = no;
        this.bankName = bankName;
    }

    @Override
    public String getBankName() {
        return bankName;
    }

    @Override
    public String getCardAlias() {
        return cardAlias;
    }

    @Override
    public String getNo() {
        return no;
    }

    @Override
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public void setCardAlias(String cardAlias) {
        this.cardAlias = cardAlias;
    }

    @Override
    public void setNo(String no) {
        this.no = no;
    }
}
