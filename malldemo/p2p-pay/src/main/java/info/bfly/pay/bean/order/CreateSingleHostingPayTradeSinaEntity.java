package info.bfly.pay.bean.order;

import info.bfly.pay.bean.enums.ACCOUNT_TYPE;
import info.bfly.pay.bean.enums.OUT_TRADE_CODE;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 3.2    创建托管代付交易 on 2017/2/17 0017.
 */

public class CreateSingleHostingPayTradeSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 2964314013853947495L;
    /**
     * 交易订单号
     */
    @NotNull
    private String         out_trade_no;
    /**
     * 外部业务码
     */
    @NotNull
    private OUT_TRADE_CODE out_trade_code;
    /**
     * 收款人标识
     */
    @NotNull
    private String         payee_identity_id;
    /**
     * 收款人标识类型
     */
    @NotNull
    private String payee_identity_type = "UID";
    /**
     * 收款人账户类型
     */
    private ACCOUNT_TYPE account_type;
    /**
     * 金额
     */
    @NotNull
    private BigDecimal   amount;
    /**
     * 分账信息列表
     */
    private String       split_list;
    /**
     * 摘要
     */
    @NotNull
    private String       summary;
    /**
     * 用户IP地址
     */
    @NotNull
    private String       user_ip;
    /**
     * 商户标的号
     */
    private String       goods_id;
    /**
     * 债权变动明细列表
     */
    private String       creditor_info_list;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public OUT_TRADE_CODE getOut_trade_code() {
        return out_trade_code;
    }

    public void setOut_trade_code(OUT_TRADE_CODE out_trade_code) {
        this.out_trade_code = out_trade_code;
    }

    public String getPayee_identity_id() {
        return payee_identity_id;
    }

    public void setPayee_identity_id(String payee_identity_id) {
        this.payee_identity_id = payee_identity_id;
    }

    public String getPayee_identity_type() {
        return payee_identity_type;
    }

    public void setPayee_identity_type(String payee_identity_type) {
        this.payee_identity_type = payee_identity_type;
    }

    public ACCOUNT_TYPE getAccount_type() {
        return account_type;
    }

    public void setAccount_type(ACCOUNT_TYPE account_type) {
        this.account_type = account_type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSplit_list() {
        return split_list;
    }


    public void setSplit_list(String split_list) {
        this.split_list = split_list;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getCreditor_info_list() {
        return creditor_info_list;
    }

    public void setCreditor_info_list(String creditor_info_list) {
        this.creditor_info_list = creditor_info_list;
    }

    @Override
    public String toString() {
        return "CreateSingleHostingPayTradeSinaEntity{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", out_trade_code='" + out_trade_code + '\'' +
                ", payee_identity_id='" + payee_identity_id + '\'' +
                ", payee_identity_type='" + payee_identity_type + '\'' +
                ", account_type='" + account_type + '\'' +
                ", amount=" + amount +
                ", split_list=" + split_list +
                ", summary='" + summary + '\'' +
                ", user_ip='" + user_ip + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", creditor_info_list='" + creditor_info_list + '\'' +
                "} " + super.toString();
    }
}
