package info.bfly.pay.bean.order.param;

import info.bfly.pay.bean.enums.WITHDRAW_STATUS;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by XXSun on 5/17/2017.
 * 提现数据
 */
public class WithdrawCashParam implements Serializable {
    private static final long serialVersionUID = -7651268065912786177L;

    /**
     * 交易订单号
     */
    private String        out_trade_no;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 到账类型
     */
    private WITHDRAW_STATUS status;

    /**
     * 开始时间
     */
    private String start_time;
    /**
     * 最后修改时间
     */
    private String end_time;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public WITHDRAW_STATUS getStatus() {
        return status;
    }

    public void setStatus(WITHDRAW_STATUS status) {
        this.status = status;
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
