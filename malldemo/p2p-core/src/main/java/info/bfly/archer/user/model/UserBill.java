package info.bfly.archer.user.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * 借款者账户
 */
@Entity
@Table(name = "user_bill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserBill implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 6173240772492404563L;
    private String id;
    private Long   seqNum;
    private User   user;
    private Date   time;
    private String type;
    private String typeInfo;
    private String accountType ="BASIC";
    private Double money;
    private String detail;
    private Double balance;
    /**
     * 冻结金额
     */
    private Double frozenMoney;

    public UserBill() {
    }

    @Column(name = "balance", precision = 22, scale = 0)
    public Double getBalance() {
        return balance;
    }

    @Column(name = "detail", length = 200)
    public String getDetail() {
        return detail;
    }

    @Column(name = "frozen_money")
    public Double getFrozenMoney() {
        return frozenMoney;
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
    public Long getSeqNum() {
        return seqNum;
    }

    @Column(name = "time", nullable = false, length = 19)
    public Date getTime() {
        return time;
    }

    @Column(name = "type", nullable = false, length = 100)
    public String getType() {
        return type;
    }

    @Column(name = "type_info", nullable = false, length = 200)
    public String getTypeInfo() {
        return typeInfo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    @Column(name = "account_type", nullable = false, length = 19)
    public String getAccountType() {
        return accountType;
    }


    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setFrozenMoney(Double frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setSeqNum(Long seqNum) {
        this.seqNum = seqNum;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
