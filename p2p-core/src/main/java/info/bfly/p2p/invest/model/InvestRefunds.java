package info.bfly.p2p.invest.model;

import info.bfly.archer.user.model.User;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 投资退款
 * Created by Administrator on 2017/6/5 0005.
 */
@Entity
@Table(name = "invest_refund")
public class InvestRefunds {


    private String id;

    //退款金额
    private BigDecimal refundMoney;

    //退款状态
    private String status;

    //退款订单号
    private String operationOrderNo;

    //退款投资
    private Invest invest;

    //退款人
    private User user;

    // 审核人
    private User verifyUser;


    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "refund_money")
    public BigDecimal getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    @Column(name = "status", length = 50)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "operation_order_no")
    public String getOperationOrderNo() {
        return operationOrderNo;
    }

    public void setOperationOrderNo(String operationOrderNo) {
        this.operationOrderNo = operationOrderNo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invest_id")
    public Invest getInvest() {
        return invest;
    }

    public void setInvest(Invest invest) {
        this.invest = invest;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verify_user_id")
    public User getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(User verifyUser) {
        this.verifyUser = verifyUser;
    }
}
