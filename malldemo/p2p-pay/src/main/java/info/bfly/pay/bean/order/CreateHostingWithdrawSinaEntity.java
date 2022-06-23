package info.bfly.pay.bean.order;

import info.bfly.pay.bean.enums.IDENTITY_TYPE;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 托管提现 on 2017/2/17 0017.
 */

public class CreateHostingWithdrawSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 4567322857235867792L;
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
     * 银行卡ID
     */
    private String        card_id;
    /**
     * 提现方式
     */
    private String withdraw_mode = "CASHDESK";
    /**
     * 到账类型
     */
    private String payto_type;
    /**
     * 提现关闭时间
     */
    private String withdraw_close_time = "10m";
    /**
     * 用户IP地址
     */
    @NotNull
    private String user_ip;

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

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getWithdraw_mode() {
        return withdraw_mode;
    }

    public void setWithdraw_mode(String withdraw_mode) {
        this.withdraw_mode = withdraw_mode;
    }

    public String getPayto_type() {
        return payto_type;
    }

    public void setPayto_type(String payto_type) {
        this.payto_type = payto_type;
    }

    public String getWithdraw_close_time() {
        return withdraw_close_time;
    }

    public void setWithdraw_close_time(String withdraw_close_time) {
        this.withdraw_close_time = withdraw_close_time;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    @Override
    public String toString() {
        return "CreateHostingWithdrawSinaEntity{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", summary='" + summary + '\'' +
                ", identity_id='" + identity_id + '\'' +
                ", identity_type='" + identity_type + '\'' +
                ", account_type='" + account_type + '\'' +
                ", amount=" + amount +
                ", user_fee=" + user_fee +
                ", card_id='" + card_id + '\'' +
                ", withdraw_mode='" + withdraw_mode + '\'' +
                ", payto_type='" + payto_type + '\'' +
                ", withdraw_close_time='" + withdraw_close_time + '\'' +
                ", user_ip='" + user_ip + '\'' +
                "} " + super.toString();
    }
}
