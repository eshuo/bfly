package info.bfly.p2p.borrower.model;

// default package

import info.bfly.archer.user.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * CreditRatingLog entity.
 */
@Entity
@Table(name = "credit_rating_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class CreditRatingLog implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 9190232843085140849L;
    private String id;
    private User   user;
    private Date   time;
    private String operator;
    private String reason;
    private String details;

    // Constructors

    /**
     * default constructor
     */
    public CreditRatingLog() {
    }

    /**
     * minimal constructor
     */
    public CreditRatingLog(String id, User user, Date time, String operator, String reason) {
        this.id = id;
        this.user = user;
        this.time = time;
        this.operator = operator;
        this.reason = reason;
    }

    /**
     * full constructor
     */
    public CreditRatingLog(String id, User user, Date time, String operator, String reason, String details) {
        this.id = id;
        this.user = user;
        this.time = time;
        this.operator = operator;
        this.reason = reason;
        this.details = details;
    }

    @Column(name = "details", length = 200)
    public String getDetails() {
        return details;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "operator", nullable = false, length = 32)
    public String getOperator() {
        return operator;
    }

    @Column(name = "reason", nullable = false, length = 200)
    public String getReason() {
        return reason;
    }

    @Column(name = "time", nullable = false, length = 19)
    public Date getTime() {
        return time;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
