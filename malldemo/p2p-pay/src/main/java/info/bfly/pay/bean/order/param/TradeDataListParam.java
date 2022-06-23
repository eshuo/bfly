package info.bfly.pay.bean.order.param;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 交易明细列表  on 2017/2/17 0017.
 */
public class TradeDataListParam {

    /**
     * 交易订单号
     */
    @NotNull
    private String out_trade_no;
    /**
     * 摘要
     */
    @NotNull
    private String summary;
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
    /**
     * 代收完成金额
     */
    private BigDecimal collection_amount;

    public TradeDataListParam(String out_trade_no, String summary, BigDecimal amount, String out_trade_type, String start_time, String end_time, BigDecimal collection_amount) {
        this.out_trade_no = out_trade_no;
        this.summary = summary;
        this.amount = amount;
        this.out_trade_type = out_trade_type;
        this.start_time = start_time;
        this.end_time = end_time;
        this.collection_amount = collection_amount;
    }

    @Override
    public String toString() {
        return out_trade_no + "^" + summary + "^" + amount +
                "^" + out_trade_type + "^" + start_time + "^" + end_time + "^"+ collection_amount;
    }

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

    public BigDecimal getCollection_amount() {
        return collection_amount;
    }

    public void setCollection_amount(BigDecimal collection_amount) {
        this.collection_amount = collection_amount;
    }
}
