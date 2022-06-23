package info.bfly.p2p.risk.model;

import info.bfly.archer.user.model.User;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 系统金额记录 主要是记录充值提现记录，查看系统账户的余额。
 */
@Entity
@Table(name = "system_money_log")
public class SystemMoneyLog implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 4553230921034091318L;
    private String id;
    private User   user;
    private Date   time;
    private String type;
    private String reason;
    private String detail;
    private Double money;
    // private Double balance;
    private String fromAccount;
    private Long   seqNum;
    private String toAccount;
    private String description;

    // Constructors

    /**
     * default constructor
     */
    public SystemMoneyLog() {
    }

    /**
     * full constructor
     */
    public SystemMoneyLog(String id, User user, Timestamp time, String reason, Double money, String fromAccount, String toAccount) {
        this.id = id;
        this.user = user;
        this.time = time;
        this.reason = reason;
        this.money = money;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return description;
    }

    @Column(name = "detail", length = 200)
    public String getDetail() {
        return detail;
    }

    @Column(name = "from_account", length = 32)
    public String getFromAccount() {
        return fromAccount;
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

    @Column(name = "reason", nullable = false, length = 100)
    public String getReason() {
        return reason;
    }

    @Column(name = "seq_num", nullable = false)
    public Long getSeqNum() {
        return seqNum;
    }

    @Column(name = "time", nullable = false, length = 19)
    public Date getTime() {
        return time;
    }

    // @Column(name = "balance", nullable = false, precision = 22, scale = 0)
    // public Double getBalance() {
    // return this.balance;
    // }
    //
    // public void setBalance(Double balance) {
    // this.balance = balance;
    // }
    @Column(name = "to_account", length = 32)
    public String getToAccount() {
        return toAccount;
    }

    @Column(name = "type", nullable = false, length = 200)
    public String getType() {
        return type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator")
    public User getUser() {
        return user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSeqNum(Long seqNum) {
        this.seqNum = seqNum;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
