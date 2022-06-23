package info.bfly.archer.user.model;

/**
 * Description: 银行直连，银行bean
 */
public interface RechargeBankCard {
    String getBankName();

    String getCardAlias();

    String getNo();

    void setBankName(String bankName);

    void setCardAlias(String cardAlias);

    void setNo(String no);
}
