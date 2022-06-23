package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 3.8托管退款 on 2017/2/17 0017.
 */

public class CreateHostingRefundSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = -6626580230855790461L;
    /**
     * 交易订单号
     */
    @NotNull
    private String out_trade_no;
    /**
     * 需要退款的商户订单号
     */
    @NotNull
    private String orig_outer_trade_no;
    /**
     * 退款金额
     */
    @NotNull
    private BigDecimal refund_amount;
    /**
     * 摘要
     */
    @NotNull
    private String summary;
    /**
     * 分账信息列表（目前代付不支持退款，因此退款时分账列表都为空）
     */
    private String split_list;
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

    public String getOrig_outer_trade_no() {
        return orig_outer_trade_no;
    }

    public void setOrig_outer_trade_no(String orig_outer_trade_no) {
        this.orig_outer_trade_no = orig_outer_trade_no;
    }

    public BigDecimal getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(BigDecimal refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSplit_list() {
        return split_list;
    }

    public void setSplit_list(String split_list) {
        this.split_list = split_list;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    @Override
    public String toString() {
        return "CreateHostingRefundSinaEntity{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", orig_outer_trade_no='" + orig_outer_trade_no + '\'' +
                ", refund_amount=" + refund_amount +
                ", summary='" + summary + '\'' +
                ", split_list='" + split_list + '\'' +
                ", user_ip='" + user_ip + '\'' +
                "} " + super.toString();
    }
}
