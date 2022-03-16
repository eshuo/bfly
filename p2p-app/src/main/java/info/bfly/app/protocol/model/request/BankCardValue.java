package info.bfly.app.protocol.model.request;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class BankCardValue {


    /**
     * 解绑卡生成ID
     */
    private String cardId;

    /**
     * 开户行
     */
    private String bankNo;
    /**
     * 省份
     */
    private String bankProvince;

    /**
     * 城市
     */
    private String bankCity;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 手机号
     */
    private String bindPhone;

    /**
     * 短信验证码
     */
    private String code;


    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
