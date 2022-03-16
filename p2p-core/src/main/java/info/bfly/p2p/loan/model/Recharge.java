package info.bfly.p2p.loan.model;

// default package

import info.bfly.archer.user.model.User;
import info.bfly.p2p.coupon.model.UserCoupon;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Recharge entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "recharge")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Recharge implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 7174502738247733922L;
    private String     id;
    private User       user;
    // 优惠券
    private UserCoupon coupon;
    /**
     * 请求时间
     */
    private Date       time;
    // 到账金额
    private Double     actualMoney;
    // 实际支付金额
    private Double     realMoney;

    /**
     * 充值到哪个账户上
     */
    private String  accountType;
    /**
     * 充值方式(不可为空)
     */
    private String     rechargeWay;
    // 手续费
    private Double     fee;
    /**
     * 充值成功或者失败时间
     */
    private Date       callbackTime;
    private String     status;
    // 是否为管理员充值
    private boolean    isRechargedByAdmin;
    private String     remark;

    /**
     * 订单编号
     */
    private String operationOrderNo;

    // Constructors

    /**
     * default constructor
     */
    public Recharge() {
    }

    /**
     * minimal constructor
     */
    public Recharge(String id, User user, Timestamp time, Double actualMoney, String rechargeWay, Double fee, boolean isRechargedByAdmin) {
        this.id = id;
        this.user = user;
        this.time = time;
        this.actualMoney = actualMoney;
        this.rechargeWay = rechargeWay;
        this.fee = fee;
        this.isRechargedByAdmin = isRechargedByAdmin;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    @Column(name = "actual_money", nullable = false, precision = 22, scale = 0)
    public Double getActualMoney() {
        return actualMoney;
    }

    @Column(name = "real_money", nullable = false, precision = 22, scale = 0)
    public Double getRealMoney() {
        return realMoney;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon")
    public UserCoupon getCoupon() {
        return coupon;
    }

    @Column(name = "fee", nullable = false, precision = 22, scale = 0)
    public Double getFee() {
        return fee;
    }


    @Column(name = "is_recharged_by_admin", nullable = false, columnDefinition = "BOOLEAN")
    public boolean getIsRechargedByAdmin() {
        return isRechargedByAdmin;
    }

    @Column(name = "account_type", nullable = false,  length = 19)
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name = "recharge_way", length = 32)
    public String getRechargeWay() {
        return rechargeWay;
    }


    @Column(name = "remark", length = 500)
    public String getRemark() {
        return remark;
    }

    @Column(name = "status", nullable = false, length = 100)
    public String getStatus() {
        return status;
    }

    @Column(name = "callback_time", length = 19)
    public Date getCallbackTime() {
        return callbackTime;
    }

    @Column(name = "time", nullable = false, length = 19)
    public Date getTime() {
        return time;
    }

    @Column(name = "order_no", unique = true, length = 64)
    public String getOperationOrderNo() {
        return operationOrderNo;
    }

    public void setActualMoney(Double actualMoney) {
        this.actualMoney = actualMoney;
    }

    public void setRealMoney(Double realMoney) {
        this.realMoney = realMoney;
    }

    public void setCoupon(UserCoupon coupon) {
        this.coupon = coupon;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsRechargedByAdmin(boolean isRechargedByAdmin) {
        this.isRechargedByAdmin = isRechargedByAdmin;
    }

    public void setRechargeWay(String rechargeWay) {
        this.rechargeWay = rechargeWay;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCallbackTime(Date successTime) {
        this.callbackTime = successTime;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOperationOrderNo(String operationOrderNo) {
        this.operationOrderNo = operationOrderNo;
    }

    public String getAccountType() {
        return accountType;
    }
}
