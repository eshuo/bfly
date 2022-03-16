package info.bfly.p2p.risk.model;

// default package
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统收益账户。
 */
@Entity
@Table(name = "system_bill")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class SystemBill implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -6833703399937745176L;
    private String id;
    private Date   time;
    private String type;
    private String reason;
    private Double money;
    private String detail;
    private Double balance;
    private Long   seqNum;

    // Constructors

    /**
     * default constructor
     */
    public SystemBill() {
    }

    @Column(name = "balance", precision = 22, scale = 0)
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

    @Column(name = "money", precision = 22, scale = 0)
    public Double getMoney() {
        return money;
    }

    @Column(name = "reason", nullable = false, length = 200)
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

    @Column(name = "type", nullable = false, length = 200)
    public String getType() {
        return type;
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

    public void setReason(String reason) {
        this.reason = reason;
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
}
