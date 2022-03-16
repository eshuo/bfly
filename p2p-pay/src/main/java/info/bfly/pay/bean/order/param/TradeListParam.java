package info.bfly.pay.bean.order.param;

import info.bfly.pay.bean.order.OrderSinaEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 3.3.2    交易参数 on 2017/2/17 0017.
 */
public class TradeListParam extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 3056703022426490024L;
    /**
     * 交易订单号
     */
    @NotNull
    private String out_trade_no;
    /**
     * 收款人标识
     */
    @NotNull
    private String payee_identity;
    /**
     * 标识类型
     */
    @NotNull
    private String payee_identity_type;

    /**
     * 账户类型
     */
    private String     account_type;
    /**
     * 金额
     */
    @NotNull
    private BigDecimal amount;
    /**
     * 分账信息列表
     */
    private String split_list;
    /**
     * 摘要
     */
    @NotNull
    private String     summary;
    /**
     * 扩展信息
     */
    private String     extend_param;
    /**
     * 用户手续费
     */
    private String     payer_fee;
    /**
     * 商户标的号
     */
    private String     goods_id;
    /**
     * 债权变动明细列表
     */
    private String     creditor_info_list;

    public TradeListParam(){

    }

    public TradeListParam(String out_trade_no, String payee_identity, String payee_identity_type, String account_type, BigDecimal amount, String split_list, String summary, String extend_param, String payer_fee, String goods_id, String creditor_info_list) {
        this.out_trade_no = out_trade_no;
        this.payee_identity = payee_identity;
        this.payee_identity_type = payee_identity_type;
        this.account_type = account_type;
        this.amount = amount;
        this.split_list = split_list;
        this.summary = summary;
        this.extend_param = extend_param;
        this.payer_fee = payer_fee;
        this.goods_id = goods_id;
        this.creditor_info_list = creditor_info_list;
    }


    @Override
    public String toString() {
        return out_trade_no + "~" + payee_identity + "~" + payee_identity_type + "~" + account_type + "~" + amount + "~" + split_list + "~" + summary + "~" + extend_param + "~" + payer_fee + "~" + goods_id + "~" + creditor_info_list;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPayee_identity() {
        return payee_identity;
    }

    public void setPayee_identity(String payee_identity) {
        this.payee_identity = payee_identity;
    }

    public String getPayee_identity_type() {
        return payee_identity_type;
    }

    public void setPayee_identity_type(String payee_identity_type) {
        this.payee_identity_type = payee_identity_type;
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

    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }

    public String getPayer_fee() {
        return payer_fee;
    }

    public void setPayer_fee(String payer_fee) {
        this.payer_fee = payer_fee;
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
}
