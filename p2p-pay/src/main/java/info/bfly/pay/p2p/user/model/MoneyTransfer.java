package info.bfly.pay.p2p.user.model;

import info.bfly.archer.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by XXSun on 6/5/2017.
 * <p>
 * 转账请求
 */
@Entity
@Table(name = "money_transfer")
public class MoneyTransfer implements Serializable {
    private static final long serialVersionUID = 55285920043796797L;
    private String id;
    /**
     * 请求时间
     */
    private Date   date;

    /**
     * 转账用户
     */
    private User fromUser;

    /**
     * 转出账户类型
     */
    private String formAccountType;
    /**
     * 转入用户
     */
    private User   toUser;
    /**
     * 转入账户类型
     */
    private String toAccountType;
    /**
     * 金额
     */
    private Double money;
    /**
     * 手续费
     */
    private Double fee;
    /**
     * 状态
     */
    private String status;
    /**
     * 转账类型
     */
    private String type;
    /**
     * 转账发起源
     */
    private String target;
    /**
     * 处理完成时间
     */
    private Date   callbackTime;
    /**
     * 审核人
     */
    private User   verifyUser;
    /**
     * 审核信息
     */
    private String verifyMessage;
    /**
     * 审核时间
     */
    private Date   verifyTime;

    /**
     * 订单编号
     */
    private String operationOrderNo;


    public MoneyTransfer() {
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }


    @Column(name = "init_time", length = 19)
    public Date getDate() {
        return date;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_user_id", nullable = false)
    public User getFromUser() {
        return fromUser;
    }

    @Column(name = "form_account_type", nullable = false, length = 50)
    public String getFormAccountType() {
        return formAccountType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    public User getToUser() {
        return toUser;
    }

    @Column(name = "to_account_type", nullable = false, length = 50)
    public String getToAccountType() {
        return toAccountType;
    }

    @Column(name = "money", nullable = false, precision = 22, scale = 0)
    public Double getMoney() {
        return money;
    }

    @Column(name = "fee", nullable = false, precision = 22, scale = 0)
    public Double getFee() {
        return fee;
    }

    @Column(name = "status", length = 20)
    public String getStatus() {
        return status;
    }

    @Column(name = "type", length = 20)
    public String getType() {
        return type;
    }

    @Column(name = "target", length = 32)
    public String getTarget() {
        return target;
    }

    @Column(name = "callback_time", length = 19)
    public Date getCallbackTime() {
        return callbackTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verify_user_id")
    public User getVerifyUser() {
        return verifyUser;
    }

    @Column(name = "verify_message", length = 500)
    public String getVerifyMessage() {
        return verifyMessage;
    }

    @Column(name = "verify_time")
    public Date getVerifyTime() {
        return verifyTime;
    }

    @Column(name = "order_no", unique = true, length = 64)
    public String getOperationOrderNo() {
        return operationOrderNo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public void setFormAccountType(String formAccountType) {
        this.formAccountType = formAccountType;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
    }

    public void setVerifyUser(User verifyUser) {
        this.verifyUser = verifyUser;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public void setOperationOrderNo(String operationOrderNo) {
        this.operationOrderNo = operationOrderNo;
    }
}
