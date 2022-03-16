package info.bfly.p2p.coupon.model;

// default package

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 优惠券 Coupon entity.
 */
@Entity
@Table(name = "coupon")
@NamedQueries({@NamedQuery(name="Coupon.findById",query = "from Coupon coupon where coupon.id = ?")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Coupon implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 8494557972014876927L;
    /**
     * 规则：使用节点+类型+金额+使用下限
     */
    private String id;
    /**
     * 优惠券名称
     */
    private String name;
    /**
     * 代金券类型
     */
    private String type;
    /**
     * 状态（是否可用等等），为此优惠券整体的状态
     */
    private String status;
    /**
     * 金额
     */
    private Double money;
    /**
     * 使用下限（多少钱以上才可以使用该优惠券）
     */
    private Double lowerLimitMoney;
    /**
     * 有效期
     */
    private Integer periodOfValidity;


    /**
     * 失效时间
     */
    private Date deadline;

    /**
     * 生成时间
     */
    private Date generateTime;

    // Constructors

    /**
     * default constructor
     */
    public Coupon() {
    }

    public Coupon(String id) {
        this.id = id;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "lower_limit_money", precision = 22, scale = 0)
    public Double getLowerLimitMoney() {
        return lowerLimitMoney;
    }

    @Column(name = "money", precision = 22, scale = 0)
    public Double getMoney() {
        return money;
    }

    @Column(name = "name", length = 200)
    public String getName() {
        return name;
    }

    @Column(name = "periodOfValidity")
    public Integer getPeriodOfValidity() {
        return periodOfValidity;
    }

    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return status;
    }

    @Column(name = "type", nullable = false, length = 200)
    public String getType() {
        return type;
    }

    @Column(name = "deadline")
    public Date getDeadline() {
        return deadline;
    }

    @Column(name = "generate_time")
    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLowerLimitMoney(Double lowerLimitMoney) {
        this.lowerLimitMoney = lowerLimitMoney;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPeriodOfValidity(Integer periodOfValidity) {
        this.periodOfValidity = periodOfValidity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }
}
