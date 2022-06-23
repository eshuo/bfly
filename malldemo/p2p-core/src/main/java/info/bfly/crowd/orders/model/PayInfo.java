package info.bfly.crowd.orders.model;

import info.bfly.archer.user.model.UserPointHistory;
import info.bfly.p2p.coupon.model.UserCoupon;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created by XXSun on 3/22/2017.
 * 支付信息
 */
@Entity
@Table(name="pay_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class PayInfo implements Serializable {
    private static final long serialVersionUID = 6629358504836245936L;
    private String           id;
    /**
     * 订单
     */
    private Order            order;
    /**
     * 订单金额
     */
    private double           fee;
    /**
     * 运费
     */
    private double           freight;
    /**
     * 使用的积分
     */
    private UserPointHistory usePoint;
    /**
     * 使用的优惠券
     */
    private UserCoupon       userCoupon;

    /**
     * 订单超时时间
     */
    private Date expireTime;

    /**
     * 状态
     */
    private String status;
    /**
     * 最终支付金额
     */
    private double payMoney;

    /**
     * 交易订单号
     */
    private String operationOrderNo;

    /**
     * 增加的积分
     */
    private UserPointHistory addPoint;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "order_id")
    public Order getMallOrder() {
        return order;
    }

    public void setMallOrder(Order order) {
        this.order = order;
    }

    @Column(name = "fee")
    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @Column(name = "freight")
    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    @OneToOne
    @JoinColumn(name = "user_point_id", referencedColumnName = "id", unique = true)
    public UserPointHistory getUsePoint() {
        return usePoint;
    }

    public void setUsePoint(UserPointHistory usePoint) {
        this.usePoint = usePoint;
    }

    @OneToOne
    @JoinColumn(name = "user_coupon_id", referencedColumnName = "id", unique = true)
    public UserCoupon getUserCoupon() {
        return userCoupon;
    }

    public void setUserCoupon(UserCoupon userCoupon) {
        this.userCoupon = userCoupon;
    }

    @Column(name = "expire_time")
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Column(name = "status", length = 50)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "pay_money")
    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    @Column(name = "operation_order_no", length = 30)
    public String getOperationOrderNo() {
        return operationOrderNo;
    }

    public void setOperationOrderNo(String operationOrderNo) {
        this.operationOrderNo = operationOrderNo;
    }

    @OneToOne
    @JoinColumn(name = "add_point", referencedColumnName = "id", unique = true)
    public UserPointHistory getAddPoint() {
        return addPoint;
    }

    public void setAddPoint(UserPointHistory addPoint) {
        this.addPoint = addPoint;
    }
}
