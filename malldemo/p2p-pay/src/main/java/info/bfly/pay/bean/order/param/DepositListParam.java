package info.bfly.pay.bean.order.param;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 充值明细列表  on 2017/2/17 0017.
 */
public class DepositListParam {

    /**
     * 充值订单号
     */
    @NotNull
    private String out_trade_no;

    /**
     * 交易金额
     */
    @NotNull
    private BigDecimal amount;
    /**
     * 状态
     */
    @NotNull
    private String out_trade_type;
    /**
     * 开始时间
     */
    @NotNull
    private String start_time;
    /**
     * 最后修改时间
     */
    @NotNull
    private String end_time;


    public DepositListParam(String out_trade_no, BigDecimal amount, String out_trade_type, String start_time, String end_time) {
        this.out_trade_no = out_trade_no;
        this.amount = amount;
        this.out_trade_type = out_trade_type;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return out_trade_no + "^" + amount +
                "^" + out_trade_type + "^" + start_time + "^" + end_time;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOut_trade_type() {
        return out_trade_type;
    }

    public void setOut_trade_type(String out_trade_type) {
        this.out_trade_type = out_trade_type;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

}
