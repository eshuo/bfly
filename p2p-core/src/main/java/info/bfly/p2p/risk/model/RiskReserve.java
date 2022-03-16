package info.bfly.p2p.risk.model;

// default package

import info.bfly.archer.user.model.User;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 风险准备金账户记录
 */
@Entity
@Table(name = "risk_reserve")
public class RiskReserve implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -1009407861527910019L;
    private String  id;
    private User    user;
    private Date    time;
    private Integer seqNum;
    private String  type;
    private Double  money;
    private String  detail;
    private Double  balance;

    // Constructors

    /**
     * default constructor
     */
    public RiskReserve() {
    }

    @Column(name = "balance", nullable = false, precision = 22, scale = 0)
    public Double getBalance() {
        return balance;
    }

    @Column(name = "detail", length = 200)
    public String getDetail() {
        return detail;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "money", nullable = false, precision = 22, scale = 0)
    public Double getMoney() {
        return money;
    }

    @Column(name = "seq_num", nullable = false)
    public Integer getSeqNum() {
        return seqNum;
    }

    @Column(name = "time", nullable = false, length = 19)
    public Date getTime() {
        return time;
    }

    @Column(name = "type", length = 32)
    public String getType() {
        return type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator")
    public User getUser() {
        return user;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
