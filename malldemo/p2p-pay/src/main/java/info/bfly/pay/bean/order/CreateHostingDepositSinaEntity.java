package info.bfly.pay.bean.order;

import info.bfly.pay.bean.enums.IDENTITY_TYPE;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 3.10托管充值 on 2017/2/17 0017.
 */
public class CreateHostingDepositSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 3213867730843734147L;
    /**
     * 交易订单号
     */
    @NotNull
    private String        out_trade_no;
    /**
     * 摘要
     */
    private String        summary;
    /**
     * 用户标识信息
     */
    @NotNull
    private String        identity_id;
    /**
     * 用户标识类型
     */
    @NotNull
    private IDENTITY_TYPE identity_type;
    /**
     * 账户类型
     */
    private String        account_type;
    /**
     * 金额
     */
    @NotNull
    private BigDecimal    amount;
    /**
     * 用户手续费
     */
    private BigDecimal    user_fee;
    /**
     * 付款用户IP地址
     */
    @NotNull
    private String        payer_ip;
    /**
     * 充值关闭时间
     */
    private String deposit_close_time = "30m";
    /**
     * 支付方式
     */
    @NotNull
    private String pay_method;


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public IDENTITY_TYPE getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(IDENTITY_TYPE identity_type) {
        this.identity_type = identity_type;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getUser_fee() {
        return user_fee;
    }

    public void setUser_fee(BigDecimal user_fee) {
        this.user_fee = user_fee;
    }

    public String getPayer_ip() {
        return payer_ip;
    }

    public void setPayer_ip(String payer_ip) {
        this.payer_ip = payer_ip;
    }

    public String getDeposit_close_time() {
        return deposit_close_time;
    }

    public void setDeposit_close_time(String deposit_close_time) {
        this.deposit_close_time = deposit_close_time;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    @Override
    public String toString() {
        return "CreateHostingDepositSinaEntity{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", summary='" + summary + '\'' +
                ", identity_id='" + identity_id + '\'' +
                ", identity_type='" + identity_type + '\'' +
                ", account_type='" + account_type + '\'' +
                ", amount=" + amount +
                ", user_fee=" + user_fee +
                ", payer_ip='" + payer_ip + '\'' +
                ", deposit_close_time='" + deposit_close_time + '\'' +
                ", pay_method='" + pay_method + '\'' +
                "} " + super.toString();
    }
}
