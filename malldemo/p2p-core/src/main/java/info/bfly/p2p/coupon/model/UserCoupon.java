package info.bfly.p2p.coupon.model;

// default package

import info.bfly.archer.user.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户持有优惠券 Coupon entity.
 */
@Entity
@Table(name = "user_coupon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserCoupon implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 95808524201445370L;
    private String id;
    /**
     * 持有用户
     */
    private User   user;
    /**
     * 描述
     */
    private String description;
    /**
     * 生成时间
     */
    private Date   generateTime;
    /**
     * 使用时间
     */
    private Date   usedTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 优惠券
     */
    private Coupon coupon = new Coupon();
    /**
     * 使用到哪个订单
     */
    private String target;
    /**
     * 有效期
     */
    private Date   deadline;

    /**
     * 订单编号
     */
    private String operationOrderNo;
    // Constructors

    /**
     * default constructor
     */
    public UserCoupon() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon", nullable = false)
    public Coupon getCoupon() {
        return coupon;
    }

    @Column(name = "deadline")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDeadline() {
        return deadline;
    }

    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    public String getDescription() {
        return description;
    }

    @Column(name = "generate_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getGenerateTime() {
        return generateTime;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return status;
    }

    @Column(name = "target", length = 64)
    public String getTarget() {
        return target;
    }

    @Column(name = "order_no", unique = true, length = 64)
    public String getOperationOrderNo() {
        return operationOrderNo;
    }

    @Column(name = "used_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUsedTime() {
        return usedTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOperationOrderNo(String operationOrderNo) {
        this.operationOrderNo = operationOrderNo;
    }

}
