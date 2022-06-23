package info.bfly.p2p.borrower.model;

// default package

import info.bfly.archer.user.model.Area;
import info.bfly.archer.user.model.User;
import info.bfly.p2p.borrower.BorrowerConstant;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 工作及财务信息
 */
@Entity
@Table(name = "borrower_additional_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BorrowerAdditionalInfo implements Serializable {
    // Fields
    private static final long serialVersionUID = -808576969031597389L;
    private String       userId;
    private BorrowerInfo borrowerInfo;

    // 职业情况
    private String       occupation;
    // 单位名称
    private String       unit;
    // 工作地点
    private Area         jobArea;
    // 单位类型
    private String       unitType;
    // 公司行业
    private String       profession;
    // 公司规模
    private String       companySize;
    // 工作年限
    private String       workingLife;
    // 现单位工作年限
    private String       unitNow;
    // 工作职位
    private String       job;
    // 月收入
    private String       monthlyIncome;
    // 单位地址
    private String       unitArea;
    // 单位电话
    private String       unitPhone;
    // 工作邮箱
    private String       jobEmail;
    // 网店地址
    private String       shopUrl;
    // 网店联系方式
    private String       shopContact;
    // 就读院校
    private String       schoolingName;
    // 就读年级
    private String       schoolingGrade;
    // 就读专业
    private String       schoolingMajor;
    // 学校职务
    private String       schoolingJob;
    // 宿舍
    private String       hostel;
    // 班级
    private String       schoolingClass;
    // 紧急联系人
    private String       contactPerson;
    // 紧急联系人电话
    private String       contactPersonPhone;
    // 实习经历
    private String       internshipExperience;
    // 就业状态
    private String       workExperience;
    // 审核人
    private User         verifiedUser;
    // 审核是否通过
    private String       verified;
    // 审核信息
    private String       verifiedMessage;
    // 审核时间
    private Date         verifiedTime;

    /**
     * default constructor
     */
    public BorrowerAdditionalInfo() {
    }

    /**
     * minimal constructor
     */
    public BorrowerAdditionalInfo(String userId, BorrowerInfo borrowerInfo) {
        this.userId = userId;
        this.borrowerInfo = borrowerInfo;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public BorrowerInfo getBorrowerInfo() {
        return borrowerInfo;
    }

    @Column(name = "company_size", length = 100)
    public String getCompanySize() {
        return companySize;
    }

    @Column(name = "contactPerson", length = 10)
    public String getContactPerson() {
        return contactPerson;
    }

    @Column(name = "contactPersonPhone", length = 15)
    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    @Column(name = "hostel", length = 50)
    public String getHostel() {
        return hostel;
    }

    @Column(name = "internship_experience", length = 1000)
    public String getInternshipExperience() {
        return internshipExperience;
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

    @Column(name = "job", length = 100)
    public String getJob() {
        return job;
    }

    // Constructors
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_area")
    public Area getJobArea() {
        return jobArea;
    }

    @Column(name = "job_email", length = 100)
    public String getJobEmail() {
        return jobEmail;
    }

    @Column(name = "monthly_income", length = 100)
    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    @Column(name = "occupation", length = 100)
    public String getOccupation() {
        return occupation;
    }

    @Column(name = "profession", length = 100)
    public String getProfession() {
        return profession;
    }

    @Column(name = "schoolingClass", length = 20)
    public String getSchoolingClass() {
        return schoolingClass;
    }

    @Column(name = "schooling_grade", length = 100)
    public String getSchoolingGrade() {
        return schoolingGrade;
    }

    @Column(name = "schoolingJob", length = 50)
    public String getSchoolingJob() {
        return schoolingJob;
    }

    @Column(name = "schooling_major", length = 200)
    public String getSchoolingMajor() {
        return schoolingMajor;
    }

    @Column(name = "schooling_name", length = 200)
    public String getSchoolingName() {
        return schoolingName;
    }

    @Column(name = "shop_contact", length = 100)
    public String getShopContact() {
        return shopContact;
    }

    @Column(name = "shop_url", length = 500)
    public String getShopUrl() {
        return shopUrl;
    }

    @Column(name = "unit", length = 100)
    public String getUnit() {
        return unit;
    }

    @Column(name = "unit_area", length = 200)
    public String getUnitArea() {
        return unitArea;
    }

    @Column(name = "unit_now", length = 100)
    public String getUnitNow() {
        return unitNow;
    }

    @Column(name = "unit_phone", length = 100)
    public String getUnitPhone() {
        return unitPhone;
    }

    @Column(name = "unit_type", length = 100)
    public String getUnitType() {
        return unitType;
    }

    // Property accessors
    @Id
    @Column(name = "user_id", unique = true, nullable = false, length = 32)
    public String getUserId() {
        return userId;
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

    @Column(name = "work_experience", length = 1000)
    public String getWorkExperience() {
        return workExperience;
    }

    @Column(name = "working_life", length = 100)
    public String getWorkingLife() {
        return workingLife;
    }

    public void setBorrowerInfo(BorrowerInfo borrowerInfo) {
        this.borrowerInfo = borrowerInfo;
        if (borrowerInfo != null) {
            userId = borrowerInfo.getUserId();
        }
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public void setInternshipExperience(String internshipExperience) {
        this.internshipExperience = internshipExperience;
    }

    public void setIsPassedVerify(boolean isPassed) {
        if (isPassed) {
            setVerified(BorrowerConstant.Verify.passed);
        } else {
            setVerified(BorrowerConstant.Verify.refuse);
        }
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setJobArea(Area jobArea) {
        this.jobArea = jobArea;
    }

    public void setJobEmail(String jobEmail) {
        this.jobEmail = jobEmail;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setSchoolingClass(String schoolingClass) {
        this.schoolingClass = schoolingClass;
    }

    public void setSchoolingGrade(String schoolingGrade) {
        this.schoolingGrade = schoolingGrade;
    }

    public void setSchoolingJob(String schoolingJob) {
        this.schoolingJob = schoolingJob;
    }

    public void setSchoolingMajor(String schoolingMajor) {
        this.schoolingMajor = schoolingMajor;
    }

    public void setSchoolingName(String schoolingName) {
        this.schoolingName = schoolingName;
    }

    public void setShopContact(String shopContact) {
        this.shopContact = shopContact;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnitArea(String unitArea) {
        this.unitArea = unitArea;
    }

    public void setUnitNow(String unitNow) {
        this.unitNow = unitNow;
    }

    public void setUnitPhone(String unitPhone) {
        this.unitPhone = unitPhone;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public void setVerifiedUser(User user) {
        verifiedUser = user;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public void setWorkingLife(String workingLife) {
        this.workingLife = workingLife;
    }
}
