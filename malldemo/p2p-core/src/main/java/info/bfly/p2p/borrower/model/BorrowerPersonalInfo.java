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
 * 借款人一般信息
 */
@Entity
@Table(name = "borrower_personal_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BorrowerPersonalInfo implements Serializable {
    // Fields
    private static final long serialVersionUID = -4313077274847664096L;
    private String       userId;
    private BorrowerInfo borrowerInfo;
    // 审核人
    private User         verifiedUser;
    // 学历
    private String       degree;
    // 毕业院校
    private String       school;
    // 入学年份
    private String       schoolYear;
    // 婚姻状况
    private String       marriageInfo;
    // 是否有孩子
    private String       childrenInfo;
    // 是否有房子
    private String       hasHouse;
    // 是否房贷
    private String       hasHousingLoan;
    // 是否有车
    private String       hasCar;
    // 是否车贷
    private String       hasCarLoan;
    /**
     * 直属亲属
     */
    private String       direct;
    // 关系
    private String       directRelative;
    // 电话
    private String       directRelativePhone;
    /**
     * 其他联系人
     */
    private String       other;
    private String       otherContact;
    private String       otherContactPhone;
    // 审核是否通过
    private String       verified;
    // 审核信息
    private String       verifiedMessage;
    // 审核时间
    private Date         verifiedTime;

    // Constructors

    /**
     * default constructor
     */
    public BorrowerPersonalInfo() {
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public BorrowerInfo getBorrowerInfo() {
        return borrowerInfo;
    }

    @Column(name = "children_info", length = 200)
    public String getChildrenInfo() {
        return childrenInfo;
    }

    @Column(name = "degree", length = 200)
    public String getDegree() {
        return degree;
    }

    @Column(name = "direct", length = 10)
    public String getDirect() {
        return direct;
    }

    @Column(name = "direct_relative", length = 100)
    public String getDirectRelative() {
        return directRelative;
    }

    @Column(name = "direct_relative_phone", length = 100)
    public String getDirectRelativePhone() {
        return directRelativePhone;
    }

    @Column(name = "has_car", length = 200)
    public String getHasCar() {
        return hasCar;
    }

    @Column(name = "has_car_loan", length = 200)
    public String getHasCarLoan() {
        return hasCarLoan;
    }

    @Column(name = "has_house", length = 200)
    public String getHasHouse() {
        return hasHouse;
    }

    @Column(name = "has_housing_loan", length = 200)
    public String getHasHousingLoan() {
        return hasHousingLoan;
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

    @Column(name = "marriage_info", length = 200)
    public String getMarriageInfo() {
        return marriageInfo;
    }

    @Column(name = "other", length = 10)
    public String getOther() {
        return other;
    }

    @Column(name = "other_contact", length = 100)
    public String getOtherContact() {
        return otherContact;
    }

    @Column(name = "other_contact_phone", length = 100)
    public String getOtherContactPhone() {
        return otherContactPhone;
    }

    @Column(name = "school", length = 200)
    public String getSchool() {
        return school;
    }

    @Column(name = "school_year", length = 200)
    public String getSchoolYear() {
        return schoolYear;
    }

    // Property accessors
    @Id
    @Column(name = "user_id", unique = true, nullable = false, length = 32)
    public String getUserId() {
        return userId;
    }

    @Column(name = "verified", length = 32, nullable = false)
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

    public void setBorrowerInfo(BorrowerInfo borrowerInfo) {
        this.borrowerInfo = borrowerInfo;
        if (borrowerInfo != null) {
            userId = borrowerInfo.getUserId();
        }
    }

    public void setChildrenInfo(String childrenInfo) {
        this.childrenInfo = childrenInfo;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public void setDirectRelative(String directRelative) {
        this.directRelative = directRelative;
    }

    public void setDirectRelativePhone(String directRelativePhone) {
        this.directRelativePhone = directRelativePhone;
    }

    public void setHasCar(String hasCar) {
        this.hasCar = hasCar;
    }

    public void setHasCarLoan(String hasCarLoan) {
        this.hasCarLoan = hasCarLoan;
    }

    public void setHasHouse(String hasHouse) {
        this.hasHouse = hasHouse;
    }

    public void setHasHousingLoan(String hasHousingLoan) {
        this.hasHousingLoan = hasHousingLoan;
    }

    public void setIsPassedVerify(boolean isPassed) {
        if (isPassed) {
            setVerified(BorrowerConstant.Verify.passed);
        } else {
            setVerified(BorrowerConstant.Verify.refuse);
        }
    }

    public void setMarriageInfo(String marriageInfo) {
        this.marriageInfo = marriageInfo;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setOtherContact(String otherContact) {
        this.otherContact = otherContact;
    }

    public void setOtherContactPhone(String otherContactPhone) {
        this.otherContactPhone = otherContactPhone;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
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
}
