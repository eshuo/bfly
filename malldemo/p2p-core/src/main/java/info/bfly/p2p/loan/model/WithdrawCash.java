package info.bfly.p2p.loan.model;

// default package

import info.bfly.archer.user.model.User;
import info.bfly.p2p.bankcard.model.BankCard;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * WithdrawCash entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "withdraw_cash")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class WithdrawCash implements Serializable {
    // Fields
    private static final long serialVersionUID = 1687115323381260791L;
    private String   id;
    private User     user;
    private Date     time;
    private BankCard bankCard;
    private String   type;
    /**
     * 提现金额 提现金额-手续费=实际到账金额
     */
    private Double   money;
    /**
     * 手续费
     */
    private Double   fee;

    private String  accountType;
    /**
     * 提现罚金
     */
    private Double  cashFine;
    private String  status;
    /**
     * 提现处理完成时间
     */
    private Date    callbackTime;
    /**
     * 审核人
     */
    private User    verifyUser;
    /**
     * 审核信息
     */
    private String  verifyMessage;
    private Date    verifyTime;
    private User    recheckUser;
    private String  recheckMessage;
    private Date    recheckTime;
    /**
     * 是否为管理员提现
     */
    private Boolean isWithdrawByAdmin;

    /**
     * 冻结订单编号
     */
    private String freezeOperationOrderNo;
    /**
     * 订单编号
     */
    private String operationOrderNo;

    // Constructors

    /**
     * default constructor
     */
    public WithdrawCash() {
    }

    @Column(name = "account_type", length = 32)
    public String getAccountType() {
        return accountType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_card_id")
    public BankCard getBankCard() {
        return bankCard;
    }

    @Column(name = "cash_fine", nullable = false, precision = 22, scale = 0)
    public Double getCashFine() {
        return cashFine;
    }

    @Column(name = "fee", nullable = false, precision = 22, scale = 0)
    public Double getFee() {
        return fee;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "is_withdraw_by_admin", columnDefinition = "BOOLEAN")
    public Boolean getIsWithdrawByAdmin() {
        return isWithdrawByAdmin;
    }

    @Column(name = "money", nullable = false, precision = 22, scale = 0)
    public Double getMoney() {
        return money;
    }

    @Column(name = "recheck_message", length = 500)
    public String getRecheckMessage() {
        return recheckMessage;
    }

    @Column(name = "recheck_time")
    public Date getRecheckTime() {
        return recheckTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recheck_user_id")
    public User getRecheckUser() {
        return recheckUser;
    }

    @Column(name = "status", nullable = false, length = 50)
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

    @Column(name = "type")
    public String getType() {
        return type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    @Column(name = "verify_message", length = 500)
    public String getVerifyMessage() {
        return verifyMessage;
    }

    @Column(name = "verify_time")
    public Date getVerifyTime() {
        return verifyTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verify_user_id")
    public User getVerifyUser() {
        return verifyUser;
    }


    @Column(name = "freeze_order_no", unique = true, length = 64)
    public String getFreezeOperationOrderNo() {
        return freezeOperationOrderNo;
    }

    @Column(name = "order_no", unique = true, length = 64)
    public String getOperationOrderNo() {
        return operationOrderNo;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }

    public void setCashFine(Double cashFine) {
        this.cashFine = cashFine;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsWithdrawByAdmin(Boolean isWithdrawByAdmin) {
        this.isWithdrawByAdmin = isWithdrawByAdmin;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setRecheckMessage(String recheckMessage) {
        this.recheckMessage = recheckMessage;
    }

    public void setRecheckTime(Date recheckTime) {
        this.recheckTime = recheckTime;
    }

    public void setRecheckUser(User recheckUser) {
        this.recheckUser = recheckUser;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
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

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public void setVerifyUser(User verifyUser) {
        this.verifyUser = verifyUser;
    }

    public void setFreezeOperationOrderNo(String freezeOperationOrderNo) {
        this.freezeOperationOrderNo = freezeOperationOrderNo;
    }

    public void setOperationOrderNo(String operationOrderNo) {
        this.operationOrderNo = operationOrderNo;
    }
}
