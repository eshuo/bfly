package info.bfly.p2p.borrower.model;

import info.bfly.archer.user.model.User;
import info.bfly.p2p.borrower.BorrowerConstant;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * 实名认证一般信息
 */
@Entity
@Table(name = "investor_personal_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class InvestorPersonalInfo implements java.io.Serializable {
    private static final long serialVersionUID = -3554851583794219102L;
    // Fields;
    private String       userId;
    private BorrowerInfo borrowerInfo;

    //TODO 审核记录
    // 审核人
    private User         verifiedUser;
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
    public InvestorPersonalInfo() {
    }

    /**
     * minimal constructor
     */
    public InvestorPersonalInfo(String userId, BorrowerInfo borrowerInfo) {
        this.userId = userId;
        this.borrowerInfo = borrowerInfo;
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

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public BorrowerInfo getBorrowerInfo() {
        return borrowerInfo;
    }

    public void setIsPassedVerify(boolean isPassed) {
        if (isPassed) {
            setVerified(BorrowerConstant.Verify.passed);
        } else {
            setVerified(BorrowerConstant.Verify.refuse);
        }
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


    public void setBorrowerInfo(BorrowerInfo borrowerInfo) {
        this.borrowerInfo = borrowerInfo;
    }
}
