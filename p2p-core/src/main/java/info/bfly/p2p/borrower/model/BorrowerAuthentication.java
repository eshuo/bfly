package info.bfly.p2p.borrower.model;

// default package

import info.bfly.archer.picture.model.AuthenticationMaterials;
import info.bfly.archer.user.model.User;
import info.bfly.p2p.borrower.BorrowerConstant;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * 借款人认证资料
 */
@Entity
@Table(name = "borrower_authentication")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BorrowerAuthentication implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -1868036273046249657L;
    private String                  userId;
    private BorrowerInfo            borrowerInfo;
    /**
     * 审核人
     */
    private User                    verifiedUser;
    /**
     * 身份证扫描件
     */
    private AuthenticationMaterials idCardScan;
    /**
     * 手持身份证照片
     */
    private AuthenticationMaterials idCardPhoto;
    /**
     * 银行征信报告
     */
    private AuthenticationMaterials bankCreditReport;
    /**
     * 户口卡
     */
    private AuthenticationMaterials huKouScan;
    /**
     * 手持户口卡照片
     */
    private AuthenticationMaterials huKouPhoto;
    /**
     * 学历证书扫描件
     */
    private AuthenticationMaterials diplomaScan;
    /**
     * 收入证明
     */
    private AuthenticationMaterials proofEarnings;
    /**
     * 账户流水扫描件
     */
    private AuthenticationMaterials accountFlow;
    /**
     * 工作证件扫描件
     */
    private AuthenticationMaterials workCertificate;
    /**
     * 学生证
     */
    private AuthenticationMaterials studentId;
    /**
     * 职称证书
     */
    private AuthenticationMaterials positionalTitles;
    /**
     * 房产证明
     */
    private AuthenticationMaterials houseInfo;
    /**
     * 车辆证明
     */
    private AuthenticationMaterials carInfo;
    /**
     * 结婚证
     */
    private AuthenticationMaterials marriageCertificate;
    /**
     * 其他财产证明
     */
    private AuthenticationMaterials otherEstate;
    /**
     * 其他居住地证明
     */
    private AuthenticationMaterials otherDomicile;
    /**
     * 其他可确认身份的证件
     */
    private AuthenticationMaterials otherIdCertificate;
    /**
     * 其他能证明稳定收入的材料
     */
    private AuthenticationMaterials otherIncomeInfo;
    /**
     * 微博认证
     */
    private AuthenticationMaterials microblogInfo;


    //企业认证信息
    /**
     * 企业营业执照
     */
    private AuthenticationMaterials businessLicense;
    /**
     * 组织机构代码证
     */
    private AuthenticationMaterials organizationLicense;
    /**
     * 税务登记证
     */
    private AuthenticationMaterials taxRegistrationLicense;
    /**
     * 单位银行结算账户开户许可证
     */
    private AuthenticationMaterials bankAccountOpenLicense;

    /**
     * 机构信用代码证
     */
    private AuthenticationMaterials creditAgenciesLicense;

    /**
     * ICP备案许可
     */
    private AuthenticationMaterials ICPLicense;

    /**
     * 行业许可证
     */
    private AuthenticationMaterials vocationalPermissionLicense;

    /**
     * 企业法人证件正面
     */
    private AuthenticationMaterials legalIdCardFront;

    /**
     * 企业法人证件反面
     */
    private AuthenticationMaterials legalIdCardBack;

    /**
     * 企业流水账户信息
     */
    private AuthenticationMaterials businessAccountFlow;

    /**
     * 是否审核通过
     */
    private String verified;
    /**
     * 审核意见
     */
    private String verifiedMessage;
    // 审核时间
    private Date   verifiedTime;

    // Constructors

    /**
     * default constructor
     */
    public BorrowerAuthentication() {
    }

    /**
     * minimal constructor
     */
    public BorrowerAuthentication(String userId, BorrowerInfo borrowerInfo) {
        this.userId = userId;
        this.borrowerInfo = borrowerInfo;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_flow")
    public AuthenticationMaterials getAccountFlow() {
        return accountFlow;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_credit_report")
    public AuthenticationMaterials getBankCreditReport() {
        return bankCreditReport;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public BorrowerInfo getBorrowerInfo() {
        return borrowerInfo;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_account_flow")
    public AuthenticationMaterials getBusinessAccountFlow() {
        return businessAccountFlow;
    }



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_info")
    public AuthenticationMaterials getCarInfo() {
        return carInfo;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diploma_scan")
    public AuthenticationMaterials getDiplomaScan() {
        return diplomaScan;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_info")
    public AuthenticationMaterials getHouseInfo() {
        return houseInfo;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hu_kou_photo")
    public AuthenticationMaterials getHuKouPhoto() {
        return huKouPhoto;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hu_kou_scan")
    public AuthenticationMaterials getHuKouScan() {
        return huKouScan;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_card_photo")
    public AuthenticationMaterials getIdCardPhoto() {
        return idCardPhoto;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_card_scan")
    public AuthenticationMaterials getIdCardScan() {
        return idCardScan;
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

    @Column(name = "verified", length = 32)
    public String getVerified() {
        return verified;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marriage_certificate")
    public AuthenticationMaterials getMarriageCertificate() {
        return marriageCertificate;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "microblog_info")
    public AuthenticationMaterials getMicroblogInfo() {
        return microblogInfo;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_domicile")
    public AuthenticationMaterials getOtherDomicile() {
        return otherDomicile;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_estate")
    public AuthenticationMaterials getOtherEstate() {
        return otherEstate;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_id_certificate")
    public AuthenticationMaterials getOtherIdCertificate() {
        return otherIdCertificate;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_income_info")
    public AuthenticationMaterials getOtherIncomeInfo() {
        return otherIncomeInfo;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positional_titles")
    public AuthenticationMaterials getPositionalTitles() {
        return positionalTitles;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proof_earnings")
    public AuthenticationMaterials getProofEarnings() {
        return proofEarnings;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    public AuthenticationMaterials getStudentId() {
        return studentId;
    }

    // Property accessors
    @Id
    @Column(name = "user_id", unique = true, nullable = false, length = 32)
    public String getUserId() {
        return userId;
    }

    @Column(name = "verified_message", length = 1000)
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_certificate")
    public AuthenticationMaterials getWorkCertificate() {
        return workCertificate;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_license")
    public AuthenticationMaterials getBusinessLicense() {
        return businessLicense;
    }
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_license")
    public AuthenticationMaterials getOrganizationLicense() {
        return organizationLicense;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_registration_license")
    public AuthenticationMaterials getTaxRegistrationLicense() {
        return taxRegistrationLicense;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_open_license")
    public AuthenticationMaterials getBankAccountOpenLicense() {
        return bankAccountOpenLicense;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_agencies_license")
    public AuthenticationMaterials getCreditAgenciesLicense() {
        return creditAgenciesLicense;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icp_license")
    public AuthenticationMaterials getICPLicense() {
        return ICPLicense;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocational_permission_license")
    public AuthenticationMaterials getVocationalPermissionLicense() {
        return vocationalPermissionLicense;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legalId_card_front")
    public AuthenticationMaterials getLegalIdCardFront() {
        return legalIdCardFront;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legalId_card_back")
    public AuthenticationMaterials getLegalIdCardBack() {
        return legalIdCardBack;
    }

    public void setAccountFlow(AuthenticationMaterials accountFlow) {
        this.accountFlow = accountFlow;
    }

    public void setBankCreditReport(AuthenticationMaterials bankCreditReport) {
        this.bankCreditReport = bankCreditReport;
    }

    public void setBorrowerInfo(BorrowerInfo borrowerInfo) {
        this.borrowerInfo = borrowerInfo;
        if (borrowerInfo != null) {
            userId = borrowerInfo.getUserId();
        }
    }

    public void setBusinessAccountFlow(AuthenticationMaterials businessAccountFlow) {
        this.businessAccountFlow = businessAccountFlow;
    }

    public void setBusinessLicense(AuthenticationMaterials businessLicense) {
        this.businessLicense = businessLicense;
    }

    public void setCarInfo(AuthenticationMaterials carInfo) {
        this.carInfo = carInfo;
    }

    public void setDiplomaScan(AuthenticationMaterials diplomaScan) {
        this.diplomaScan = diplomaScan;
    }

    public void setHouseInfo(AuthenticationMaterials houseInfo) {
        this.houseInfo = houseInfo;
    }

    public void setHuKouPhoto(AuthenticationMaterials huKouPhoto) {
        this.huKouPhoto = huKouPhoto;
    }

    public void setHuKouScan(AuthenticationMaterials huKouScan) {
        this.huKouScan = huKouScan;
    }

    public void setIdCardPhoto(AuthenticationMaterials idCardPhoto) {
        this.idCardPhoto = idCardPhoto;
    }

    public void setIdCardScan(AuthenticationMaterials idCardScan) {
        this.idCardScan = idCardScan;
    }

    public void setIsPassedVerify(boolean isPassed) {
        if (isPassed) {
            setVerified(BorrowerConstant.Verify.passed);
        } else {
            setVerified(BorrowerConstant.Verify.refuse);
        }
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public void setMarriageCertificate(AuthenticationMaterials marriageCertificate) {
        this.marriageCertificate = marriageCertificate;
    }

    public void setMicroblogInfo(AuthenticationMaterials microblogInfo) {
        this.microblogInfo = microblogInfo;
    }

    public void setOtherDomicile(AuthenticationMaterials otherDomicile) {
        this.otherDomicile = otherDomicile;
    }

    public void setOtherEstate(AuthenticationMaterials otherEstate) {
        this.otherEstate = otherEstate;
    }

    public void setOtherIdCertificate(AuthenticationMaterials otherIdCertificate) {
        this.otherIdCertificate = otherIdCertificate;
    }

    public void setOtherIncomeInfo(AuthenticationMaterials otherIncomeInfo) {
        this.otherIncomeInfo = otherIncomeInfo;
    }

    public void setPositionalTitles(AuthenticationMaterials positionalTitles) {
        this.positionalTitles = positionalTitles;
    }

    public void setProofEarnings(AuthenticationMaterials proofEarnings) {
        this.proofEarnings = proofEarnings;
    }

    public void setStudentId(AuthenticationMaterials studentId) {
        this.studentId = studentId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVerifiedMessage(String verifiedMessage) {
        this.verifiedMessage = verifiedMessage;
    }

    public void setVerifiedTime(Date verifiedTime) {
        this.verifiedTime = verifiedTime;
    }

    public void setVerifiedUser(User user) {
        verifiedUser = user;
    }

    public void setWorkCertificate(AuthenticationMaterials workCertificate) {
        this.workCertificate = workCertificate;
    }

    public void setOrganizationLicense(AuthenticationMaterials organizationLicense) {
        this.organizationLicense = organizationLicense;
    }

    public void setTaxRegistrationLicense(AuthenticationMaterials taxRegistrationLicense) {
        this.taxRegistrationLicense = taxRegistrationLicense;
    }

    public void setBankAccountOpenLicense(AuthenticationMaterials bankAccountOpenLicense) {
        this.bankAccountOpenLicense = bankAccountOpenLicense;
    }

    public void setCreditAgenciesLicense(AuthenticationMaterials creditAgenciesLicense) {
        this.creditAgenciesLicense = creditAgenciesLicense;
    }

    public void setICPLicense(AuthenticationMaterials ICPLicense) {
        this.ICPLicense = ICPLicense;
    }

    public void setVocationalPermissionLicense(AuthenticationMaterials vocationalPermissionLicense) {
        this.vocationalPermissionLicense = vocationalPermissionLicense;
    }

    public void setLegalIdCardFront(AuthenticationMaterials legalIdCardFront) {
        this.legalIdCardFront = legalIdCardFront;
    }

    public void setLegalIdCardBack(AuthenticationMaterials legalIdCardBack) {
        this.legalIdCardBack = legalIdCardBack;
    }
}
