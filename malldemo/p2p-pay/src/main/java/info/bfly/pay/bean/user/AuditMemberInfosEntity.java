package info.bfly.pay.bean.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


public class AuditMemberInfosEntity extends UserSinaEntity {
    private static final long serialVersionUID = -4268821565228810306L;

    /**
     * 请求审核订单号
     * 商户网站交易订单号，商户内部保证唯一
     */
    @NotNull
    @Size(max = 32)
    private String audit_order_no;


    @NotNull
    @Size(max = 90)
    private     String company_name;
    /**
     * 企业网址
     */
    @Size(max = 90)
    private     String website;
    /**
     * 企业地址
     */
    @NotNull
    @Size(max = 256)
    private     String address;
    /**
     * 执照号
     */
    @NotNull
    @Size(max = 50)
    private     String license_no;
    /**
     * 营业执照所在地
     */
    @NotNull
    @Size(max = 256)
    private     String license_address;
    /**
     * 执照过期日（营业期限）
     */
    @NotNull
    @JsonFormat(pattern = "YYYYMMdd" ,timezone = "GMT+8")
    private     Date   license_expire_date;
    /**
     * 营业范围
     */
    @NotNull
    @Size(max = 256 )
    private     String business_scope;
    /**
     * 联系电话
     */
    @NotNull
    @Size(max = 20 )
    private     String telephone;
    /**
     * 联系Email
     */
    @Size(max = 50 )
    private     String email;
    /**
     * 组织机构代码
     */
    @Size(max = 32)
    @NotNull
    private     String organization_no;
    /**
     * 企业简介
     */
    @NotNull
    @Size(max = 512)
    private     String summary;
    /**
     * 企业法人
     */
    @NotNull
    @Size(max = 50)
    private String legal_person;
    /**
     * 证件号码
     */
    @NotNull
    @Size(max = 18)
    private String cert_no;
    /**
     * 证件类型
     */
    @NotNull
    @Size(max = 18)
    private String cert_type;
    /**
     * 法人手机号码
     */
    @NotNull
    @Size(max = 20)
    private String legal_person_phone;
    /**
     * 银行编号
     */
    @NotNull
    @Size(max = 10)
    private String bank_code;
    /**
     * 银行卡号 密文，使用新浪支付RSA公钥加密。明文长度：30
     */
    @NotNull
    private String bank_account_no;
    /**
     * 卡类型
     */
    @NotNull
    @Size(max = 10)
    private String card_type;
    /**
     * 卡属性
     */
    @NotNull
    @Size(max = 10)
    private String card_attribute;
    /**
     * 省份
     */
    @NotNull
    @Size(max = 128)
    private String province;
    /**
     * 城市
     */
    @NotNull
    @Size(max = 128)
    private String city;
    /**
     * 银行支行名称
     */
    @Size(max = 225)
    private String bank_branch;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件摘要
     */
    private String digest;
    /**
     * 文件摘要算法
     */
    private String digestType;


    public String getAudit_order_no() {
        return audit_order_no;
    }

    public void setAudit_order_no(String audit_order_no) {
        this.audit_order_no = audit_order_no;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicense_no() {
        return license_no;
    }

    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }

    public String getLicense_address() {
        return license_address;
    }

    public void setLicense_address(String license_address) {
        this.license_address = license_address;
    }

    public Date getLicense_expire_date() {
        return license_expire_date;
    }

    public void setLicense_expire_date(Date license_expire_date) {
        this.license_expire_date = license_expire_date;
    }

    public String getBusiness_scope() {
        return business_scope;
    }

    public void setBusiness_scope(String business_scope) {
        this.business_scope = business_scope;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization_no() {
        return organization_no;
    }

    public void setOrganization_no(String organization_no) {
        this.organization_no = organization_no;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLegal_person() {
        return legal_person;
    }

    public void setLegal_person(String legal_person) {
        this.legal_person = legal_person;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getCert_type() {
        return cert_type;
    }

    public void setCert_type(String cert_type) {
        this.cert_type = cert_type;
    }

    public String getLegal_person_phone() {
        return legal_person_phone;
    }

    public void setLegal_person_phone(String legal_person_phone) {
        this.legal_person_phone = legal_person_phone;
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

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_attribute() {
        return card_attribute;
    }

    public void setCard_attribute(String card_attribute) {
        this.card_attribute = card_attribute;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDigestType() {
        return digestType;
    }

    public void setDigestType(String digestType) {
        this.digestType = digestType;
    }
}
