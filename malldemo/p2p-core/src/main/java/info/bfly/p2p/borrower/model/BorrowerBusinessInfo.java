package info.bfly.p2p.borrower.model;

import info.bfly.archer.user.model.User;
import info.bfly.p2p.borrower.BorrowerConstant;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by XXSun on 5/19/2017.
 * 企业用户信息
 */
@Entity
@Table(name = "borrower_business_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BorrowerBusinessInfo implements Serializable {

    private static final long serialVersionUID = -8568037431592840928L;
    // Fields;

    private String userId;

    private BorrowerInfo borrowerInfo;

    /**
     * 公司名称
     */
    private String company_name;
    /**
     * 企业网址
     */
    private String website;
    /**
     * 企业地址
     */
    private String address;
    /**
     * 执照号
     */
    private String license_no;
    /**
     * 营业执照所在地
     */
    private String license_address;
    /**
     * 执照过期日（营业期限）
     */
    private Date   license_expire_date;
    /**
     * 营业范围
     */
    private String business_scope;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 联系Email
     */
    private String email;
    /**
     * 组织机构代码
     */
    private String organization_no;
    /**
     * 企业简介
     */
    private String summary;
    /**
     * 企业法人
     */
    private String legal_person;
    /**
     * 证件号码
     */
    private String cert_no;
    /**
     * 证件类型
     */
    private String cert_type;
    /**
     * 法人手机号码
     */
    private String legal_person_phone;
    /**
     * 银行编号
     */
    private String bank_code;
    /**
     * 银行卡号 密文，使用新浪支付RSA公钥加密。明文长度：30
     */
    private String bank_account_no;
    /**
     * 卡类型
     */
    private String card_type;
    /**
     * 卡属性
     */
    private String card_attribute;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 银行支行名称
     */
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
    /**
     * 经办人Id
     */
    private String agent_id;
    /**
     * 经办人姓名
     */
    private String agent_name;
    /**
     * 经办人证件号
     */
    private String agent_license_no;
    /**
     * 经办人证件类型
     */
    private String license_type_code;
    /**
     * 经办人手机号
     */
    private String agent_mobile;
    /**
     * 经办人邮箱
     */
    private String agent_email;


    // 审核人
    private User   verifiedUser;
    // 审核是否通过
    private String verified;
    // 审核信息
    private String verifiedMessage;
    // 审核时间
    private Date   verifiedTime;
    //  经办人信息审核人
    private User   agentVerifiedUser;
    // 经办人信息审核是否通过
    private String agentVerified;
    // 经办人信息审核信息
    private String agentVerifiedMessage;
    // 经办人信息审核时间
    private Date   agentVerifiedTime;


    @Id
    @Column(name = "user_id", unique = true, nullable = false, length = 32)
    public String getUserId() {
        return userId;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public BorrowerInfo getBorrowerInfo() {
        return borrowerInfo;
    }

    @Column(name = "company_name", length = 90)
    public String getCompany_name() {
        return company_name;
    }

    @Column(name = "website", length = 90)
    public String getWebsite() {
        return website;
    }

    @Column(name = "address", length = 90)
    public String getAddress() {
        return address;
    }

    @Column(name = "license_no", length = 90)
    public String getLicense_no() {
        return license_no;
    }

    @Column(name = "license_address", length = 90)
    public String getLicense_address() {
        return license_address;
    }


    @Column(name = "license_expire_date")
    public Date getLicense_expire_date() {
        return license_expire_date;
    }


    @Column(name = "business_scope", length = 256)
    public String getBusiness_scope() {
        return business_scope;
    }


    @Column(name = "telephone", length = 20)
    public String getTelephone() {
        return telephone;
    }


    @Column(name = "email", length = 50)
    public String getEmail() {
        return email;
    }


    @Column(name = "organization_no", length = 32)
    public String getOrganization_no() {
        return organization_no;
    }


    @Column(name = "summary", length = 512)
    public String getSummary() {
        return summary;
    }


    @Column(name = "legal_person", length = 32)
    public String getLegal_person() {
        return legal_person;
    }


    @Column(name = "cert_no", length = 18)
    public String getCert_no() {
        return cert_no;
    }


    @Column(name = "cert_type", length = 18)
    public String getCert_type() {
        return cert_type;
    }


    @Column(name = "legal_person_phone", length = 20)
    public String getLegal_person_phone() {
        return legal_person_phone;
    }


    @Column(name = "bank_code", length = 10)
    public String getBank_code() {
        return bank_code;
    }

    @Column(name = "bank_account_no", length = 30)
    public String getBank_account_no() {
        return bank_account_no;
    }

    @Column(name = "card_type", length = 10)
    public String getCard_type() {
        return card_type;
    }

    @Column(name = "card_attribute", length = 10)
    public String getCard_attribute() {
        return card_attribute;
    }


    @Column(name = "province", length = 10)
    public String getProvince() {
        return province;
    }


    @Column(name = "city", length = 10)
    public String getCity() {
        return city;
    }


    @Column(name = "bank_branch", length = 256)
    public String getBank_branch() {
        return bank_branch;
    }


    @Column(name = "fileName", length = 32)
    public String getFileName() {
        return fileName;
    }


    @Column(name = "digest", length = 32)
    public String getDigest() {
        return digest;
    }


    @Column(name = "digestType", length = 32)
    public String getDigestType() {
        return digestType;
    }


    @Transient
    public Boolean getIsPassedVerify() {
        if (StringUtils.equals(getVerified(), BorrowerConstant.Verify.passed)) {
            return true;
        }
        if (StringUtils.equals(getVerified(), BorrowerConstant.Verify.refuse)) {
            return false;
        }
        return false;
    }

    public void setIsPassedVerify(boolean isPassed) {
        if (isPassed) {
            setVerified(BorrowerConstant.Verify.passed);
        } else {
            setVerified(BorrowerConstant.Verify.refuse);
        }
    }


    @Transient
    public Boolean getAgentIsPassedVerify() {
        if (StringUtils.equals(getAgentVerified(), BorrowerConstant.Verify.passed)) {
            return true;
        }
        if (StringUtils.equals(getAgentVerified(), BorrowerConstant.Verify.refuse)) {
            return false;
        }
        return false;
    }

    public void setAgentIsPassedVerify(boolean isPassed) {
        if (isPassed) {
            setAgentVerified(BorrowerConstant.Verify.passed);
        } else {
            setAgentVerified(BorrowerConstant.Verify.refuse);
        }
    }

    @Column(name = "verified", length = 20)
    public String getVerified() {
        return verified;
    }

    @Column(name = "verified_message", length = 500)
    public String getVerifiedMessage() {
        return verifiedMessage;
    }

    @Column(name = "verified_time")
    public Date getVerifiedTime() {
        return verifiedTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_user")
    public User getVerifiedUser() {
        return verifiedUser;
    }


    @Column(name = "agentId", length = 32)
    public String getAgent_id() {
        return agent_id;
    }

    @Column(name = "agentName", length = 32)
    public String getAgent_name() {
        return agent_name;
    }

    @Column(name = "agentLicenseNo", length = 32)
    public String getAgent_license_no() {
        return agent_license_no;
    }

    @Column(name = "licenseTypeCode", length = 32)
    public String getLicense_type_code() {
        return license_type_code;
    }

    @Column(name = "agentMobile", length = 32)
    public String getAgent_mobile() {
        return agent_mobile;
    }

    @Column(name = "agentEmail", length = 100)
    public String getAgent_email() {
        return agent_email;
    }

    @Column(name = "agentVerified", length = 20)
    public String getAgentVerified() {
        return agentVerified;
    }

    @Column(name = "agentVerifiedMessage", length = 500)
    public String getAgentVerifiedMessage() {
        return agentVerifiedMessage;
    }

    @Column(name = "agentVerified_time")
    public Date getAgentVerifiedTime() {
        return agentVerifiedTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agentVerified_user")
    public User getAgentVerifiedUser() {
        return agentVerifiedUser;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBorrowerInfo(BorrowerInfo borrowerInfo) {
        this.borrowerInfo = borrowerInfo;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }

    public void setLicense_address(String license_address) {
        this.license_address = license_address;
    }

    public void setLicense_expire_date(Date license_expire_date) {
        this.license_expire_date = license_expire_date;
    }

    public void setBusiness_scope(String business_scope) {
        this.business_scope = business_scope;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOrganization_no(String organization_no) {
        this.organization_no = organization_no;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setLegal_person(String legal_person) {
        this.legal_person = legal_person;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public void setCert_type(String cert_type) {
        this.cert_type = cert_type;
    }

    public void setLegal_person_phone(String legal_person_phone) {
        this.legal_person_phone = legal_person_phone;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public void setBank_account_no(String bank_account_no) {
        this.bank_account_no = bank_account_no;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public void setCard_attribute(String card_attribute) {
        this.card_attribute = card_attribute;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBank_branch(String bank_branch) {
        this.bank_branch = bank_branch;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public void setDigestType(String digestType) {
        this.digestType = digestType;
    }

    public void setVerifiedUser(User verifiedUser) {
        this.verifiedUser = verifiedUser;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public void setVerifiedMessage(String verifiedMessage) {
        this.verifiedMessage = verifiedMessage;
    }

    public void setVerifiedTime(Date verifiedTime) {
        this.verifiedTime = verifiedTime;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public void setAgent_license_no(String agent_license_no) {
        this.agent_license_no = agent_license_no;
    }

    public void setLicense_type_code(String license_type_code) {
        this.license_type_code = license_type_code;
    }

    public void setAgent_mobile(String agent_mobile) {
        this.agent_mobile = agent_mobile;
    }

    public void setAgent_email(String agent_email) {
        this.agent_email = agent_email;
    }

    public void setAgentVerifiedUser(User agentVerifiedUser) {
        this.agentVerifiedUser = agentVerifiedUser;
    }

    public void setAgentVerified(String agentVerified) {
        this.agentVerified = agentVerified;
    }

    public void setAgentVerifiedMessage(String agentVerifiedMessage) {
        this.agentVerifiedMessage = agentVerifiedMessage;
    }

    public void setAgentVerifiedTime(Date agentVerifiedTime) {
        this.agentVerifiedTime = agentVerifiedTime;
    }
}