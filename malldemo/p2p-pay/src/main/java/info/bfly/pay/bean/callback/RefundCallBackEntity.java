package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.BaseCallBackEntity;
import info.bfly.pay.bean.enums.REFUND_STATUS;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 4.5 退款结果通知
 * Created by XXSun on 2017/2/17.
 */
public class RefundCallBackEntity extends BaseCallBackEntity {
    private static final long serialVersionUID = 6674442609579222698L;
    /**
     * 原交易商户网站唯一订单号 或者 退款原交易原始凭证号
     */
    @NotNull
    @Size(max = 32)
    private String        orig_outer_trade_no;
    /**
     * 商户网站唯一订单号或者交易原始凭证号
     */
    @NotNull
    @Size(max = 32)
    private String        outer_trade_no;
    /**
     * 退款交易凭证号
     */
    @NotNull
    @Size(max = 32)
    private String        inner_trade_no;
    /**
     * 退款金额
     */
    @NotNull
    private BigDecimal    refund_amount;
    /**
     * 退款金额
     */
    @NotNull
    private REFUND_STATUS refund_status;
    /**
     * 交易退款时间
     */
    @NotNull
    @Size(min = 14, max = 14)
    private String        gmt_refund;

    public String getOrig_outer_trade_no() {
        return orig_outer_trade_no;
    }

    public void setOrig_outer_trade_no(String orig_outer_trade_no) {
        this.orig_outer_trade_no = orig_outer_trade_no;
    }

    public String getOuter_trade_no() {
        return outer_trade_no;
    }

    public void setOuter_trade_no(String outer_trade_no) {
        this.outer_trade_no = outer_trade_no;
    }

    public String getInner_trade_no() {
        return inner_trade_no;
    }

    public void setInner_trade_no(String inner_trade_no) {
        this.inner_trade_no = inner_trade_no;
    }

    public BigDecimal getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(BigDecimal refund_amount) {
        this.refund_amount = refund_amount;
    }

    public REFUND_STATUS getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(REFUND_STATUS refund_status) {
        this.refund_status = refund_status;
    }

    public String getGmt_refund() {
        return gmt_refund;
    }

    public void setGmt_refund(String gmt_refund) {
        this.gmt_refund = gmt_refund;
    }

    @Override
    public String toString() {
        return "RefundCallBackEntity{" +
                "orig_outer_trade_no='" + orig_outer_trade_no + '\'' +
                ", outer_trade_no='" + outer_trade_no + '\'' +
                ", inner_trade_no='" + inner_trade_no + '\'' +
                ", refund_amount=" + refund_amount +
                ", refund_status=" + refund_status +
                ", gmt_refund='" + gmt_refund + '\'' +
                "} " + super.toString();
    }
}
