package info.bfly.pay.bean.order.param;

import info.bfly.pay.bean.order.OrderSinaEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 3.16    代收完成交易参数列表 on 2017/2/20 0020.
 */
public class FinishPreAuthTradeListParam extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 8534341799990836981L;

    /**
     * 代收完成单笔请求号
     */
    @NotNull
    private String out_trade_no;
    /**
     * 代收冻结订单号
     */
    @NotNull
    private String freezes_trade_no;
    /**
     * 代收完成金额
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

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getFreezes_trade_no() {
        return freezes_trade_no;
    }

    public void setFreezes_trade_no(String freezes_trade_no) {
        this.freezes_trade_no = freezes_trade_no;
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

    @Override
    public String toString() {
        return "FinishPreAuthTradeListParam{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", freezes_trade_no='" + freezes_trade_no + '\'' +
                ", amount=" + amount +
                ", summary='" + summary + '\'' +
                ", extend_param='" + extend_param + '\'' +
                "} ";
    }
}
