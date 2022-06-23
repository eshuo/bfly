package info.bfly.pay.bean.order.param;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 3.15.2代付到提现交易参数 on 2017/2/20 0020.
 */
public class HostingPayToCardTradeListParam implements Serializable {


    private static final long serialVersionUID = -3250490738946748463L;


    /**
     * 交易订单号
     */
    @NotNull
    private String out_trade_no;

    /**
     * 收款方式
     */
    @NotNull
    private String payment_method;
    /**
     * 金额
     */
    @NotNull
    private BigDecimal amount;

    /**
     * 摘要
     */
    @NotNull
    private String summary;
    /**
     * 扩展信息
     */
    private String extend_param;

    /**
     * 商户标的号
     */
    @NotNull
    private String goods_id;
    /**
     * 债权变动明细列表
     */
    private String creditor_info_list;

    public HostingPayToCardTradeListParam(String out_trade_no, String payment_method, BigDecimal amount, String summary, String extend_param, String goods_id, String creditor_info_list) {
        this.out_trade_no = out_trade_no;
        this.payment_method = payment_method;
        this.amount = amount;
        this.summary = summary;
        this.extend_param = extend_param;
        this.goods_id = goods_id;
        this.creditor_info_list = creditor_info_list;
    }

    @Override
    public String toString() {
        return out_trade_no + "~" + payment_method + "~" + amount +
                "~" + summary + "~" + extend_param + "~" + goods_id + "~" + creditor_info_list;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
