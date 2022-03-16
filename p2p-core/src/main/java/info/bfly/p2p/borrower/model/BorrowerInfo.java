package info.bfly.p2p.borrower.model;

// default package

import info.bfly.archer.user.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * BorrowerInfo entity.
 */
@Entity
@Table(name = "borrower_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BorrowerInfo implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 4588517679671467286L;
    private String                 userId;
    private User                   user;
    private String                 creditRating;
    private Double                 creditLimit;
    private String                 riskLevel;
    private Double                 riskFactor;
    /**
     * 财务信息
     */
    private BorrowerAdditionalInfo borrowerAdditionalInfo;
    /**
     * 认证资料
     */
    private BorrowerAuthentication borrowerAuthentication;
    /**
     * 个人信息
     */
    private BorrowerPersonalInfo   borrowerPersonalInfo;
    /**
     * 实名认证
     */
    private InvestorPersonalInfo   investorPersonalInfo;

    /**
     * 企业认证信息
     */
    private BorrowerBusinessInfo borrowerBusinessInfo;

    // Constructors

    /**
     * default constructor
     */
    public BorrowerInfo() {
    }

    /**
     * minimal constructor
     */
    public BorrowerInfo(User user) {
        userId = user.getId();
        this.user = user;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "borrowerInfo")
    public BorrowerAdditionalInfo getBorrowerAdditionalInfo() {
        return borrowerAdditionalInfo;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "borrowerInfo")
    public BorrowerAuthentication getBorrowerAuthentication() {
        return borrowerAuthentication;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "borrowerInfo")
    public BorrowerPersonalInfo getBorrowerPersonalInfo() {
        return borrowerPersonalInfo;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "borrowerInfo")
    public InvestorPersonalInfo getInvestorPersonalInfo() {
        return investorPersonalInfo;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "borrowerInfo")
    public BorrowerBusinessInfo getBorrowerBusinessInfo() {
        return borrowerBusinessInfo;
    }

    @Column(name = "credit_limit", precision = 22, scale = 0)
    public Double getCreditLimit() {
        return creditLimit;
    }

    @Column(name = "credit_rating", length = 100)
    public String getCreditRating() {
        return creditRating;
    }

    @Column(name = "risk_factor", precision = 22, scale = 0)
    public Double getRiskFactor() {
        return riskFactor;
    }

    @Column(name = "risk_level", length = 100)
    public String getRiskLevel() {
        return riskLevel;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }


    // Property accessors
    @Id
    @Column(name = "user_id", unique = true, nullable = false, length = 32)
    public String getUserId() {
        return userId;
    }

    public void setBorrowerAdditionalInfo(BorrowerAdditionalInfo borrowerAdditionalInfo) {
        this.borrowerAdditionalInfo = borrowerAdditionalInfo;
    }

    public void setBorrowerAuthentication(BorrowerAuthentication borrowerAuthentication) {
        this.borrowerAuthentication = borrowerAuthentication;
    }

    public void setBorrowerPersonalInfo(BorrowerPersonalInfo borrowerPersonalInfo) {
        this.borrowerPersonalInfo = borrowerPersonalInfo;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public void setRiskFactor(Double riskFactor) {
        this.riskFactor = riskFactor;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            userId = user.getId();
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setInvestorPersonalInfo(InvestorPersonalInfo investorPersonalInfo) {
        this.investorPersonalInfo = investorPersonalInfo;
    }

    public void setBorrowerBusinessInfo(BorrowerBusinessInfo borrowerBusinessInfo) {
        this.borrowerBusinessInfo = borrowerBusinessInfo;
    }
}
