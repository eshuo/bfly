package info.bfly.pay.bean.user;

import info.bfly.pay.bean.enums.CARD_ATTRIBUTE;
import info.bfly.pay.bean.enums.CARD_TYPE;
import info.bfly.pay.bean.enums.VERIFY_MODE;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class BindBankCardEntity extends UserSinaEntity {
    /**
     * 绑卡请求号:商户网站交易订单号，商户内部保证唯一
     */
    @NotNull
    @Size(max = 32)
    private String         request_no;
    /**
     * 银行编号
     */
    @NotNull
    @Size(max = 10)
    private String         bank_code;
    /**
     * 银行卡号 密文，使用新浪支付RSA公钥加密。明文长度：30
     */
    @NotNull
    private String         bank_account_no;
    /**
     * 户名 密文，使用新浪支付RSA公钥加密。明文长度：50。空则使用实名认证时实名信息
     */
    private String         account_name;
    /**
     * 卡类型
     */
    @NotNull
    private CARD_TYPE      card_type;
    /**
     * 卡属性
     */
    @NotNull
    private CARD_ATTRIBUTE card_attribute;
    /**
     * 证件类型 身份证，为空则使用实名认证中的证件信息
     */
    private String         cert_type;
    /**
     * 证件号码 密文，使用新浪支付RSA公钥加密。明文长度：18。空则使用实名认证时实名信息
     */
    private String         cert_no;
    /**
     * 银行预留手机号 密文，使用新浪支付RSA公钥加密。明文长度：16。如认证方式不为空，则要求此信息也不能为空。
     */
    @Size(max = 16)
    private String         phone_no;
    /**
     * 有效期 密文，使用新浪支付RSA公钥加密。明文长度：10；信用卡专用，有效期(10/13)，（月份/年份）
     */
    private String         validity_period;
    /**
     * CVV2 密文，使用新浪支付RSA公钥加密。明文长度：10；信用卡专用
     */
    private String         verification_value;
    /**
     * 省份
     */
    @NotNull
    @Size(max = 128)
    private     String      province;
    /**
     * 城市
     */
    @NotNull
    @Size(max = 128)
    private     String      city;
    /**
     * 银行支行名称
     */
    @Size(max = 225)
    private     String      bank_branch;
    /**
     * 认证方式:银行卡真实性认证方式，见附录“卡认证方式”，空则表示不认证
     */
    private     VERIFY_MODE verify_mode;

    public String getRequest_no() {
        return request_no;
    }

    public void setRequest_no(String request_no) {
        this.request_no = request_no;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getBank_account_no() {
        return bank_account_no;
    }

    public void setBank_account_no(String bank_account_no) {
        this.bank_account_no = bank_account_no;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public CARD_TYPE getCard_type() {
        return card_type;
    }

    public void setCard_type(CARD_TYPE card_type) {
        this.card_type = card_type;
    }

    public CARD_ATTRIBUTE getCard_attribute() {
        return card_attribute;
    }

    public void setCard_attribute(CARD_ATTRIBUTE card_attribute) {
        this.card_attribute = card_attribute;
    }

    public String getCert_type() {
        return cert_type;
    }

    public void setCert_type(String cert_type) {
        this.cert_type = cert_type;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getValidity_period() {
        return validity_period;
    }

    public void setValidity_period(String validity_period) {
        this.validity_period = validity_period;
    }

    public String getVerification_value() {
        return verification_value;
    }

    public void setVerification_value(String verification_value) {
        this.verification_value = verification_value;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(String bank_branch) {
        this.bank_branch = bank_branch;
    }

    public VERIFY_MODE getVerify_mode() {
        return verify_mode;
    }

    public void setVerify_mode(VERIFY_MODE verify_mode) {
        this.verify_mode = verify_mode;
    }

    @Override
    public String toString() {
        return "BindBankCardEntity{" +
                "request_no='" + request_no + '\'' +
                ", bank_code='" + bank_code + '\'' +
                ", bank_account_no='" + bank_account_no + '\'' +
                ", account_name='" + account_name + '\'' +
                ", card_type=" + card_type +
                ", card_attribute='" + card_attribute + '\'' +
                ", cert_type='" + cert_type + '\'' +
                ", cert_no='" + cert_no + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", validity_period='" + validity_period + '\'' +
                ", verification_value='" + verification_value + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", bank_branch='" + bank_branch + '\'' +
                ", verify_mode=" + verify_mode +
                "} " + super.toString();
    }
}
