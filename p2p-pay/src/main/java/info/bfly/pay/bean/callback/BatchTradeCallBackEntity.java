package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.BaseCallBackEntity;
import info.bfly.pay.bean.enums.BATCH_STATUS;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 4.4 批次处理结果通知
 * Created by XXSun on 2017/2/17.
 */
public class BatchTradeCallBackEntity extends BaseCallBackEntity {
    /**
     * 商户网站唯一订单批次号或者交易原始批次凭证号
     */
    @NotNull
    @Size(max = 32)
    private String       outer_batch_no;
    /**
     * 内部交易订单批次号
     */
    @NotNull
    @Size(max = 32)
    private String       inner_batch_no;
    /**
     * 批次总交易笔数
     */
    @NotNull
    @Size(max = 32)
    private String       batch_quantity;
    /**
     * 批次总金额 单位元，可以含小数点
     */
    @NotNull
    private BigDecimal   batch_amount;
    /**
     * 批次状态
     */
    @NotNull
    private BATCH_STATUS batch_status;
    /**
     * 交易明细列表 参数间用“~”分隔，各条目之间用“$”分隔
     */
    @NotNull
    private String       trade_list;
    /**
     * 批次创建时间 yyyyMMddHHmmss
     */
    @NotNull
    @Size(min = 14,max = 14)
    private String       gmt_create;
    /**
     * 批次处理结束时间，格式yyyyMMddHHmmss
     */
    private String       gmt_finished;

    public String getOuter_batch_no() {
        return outer_batch_no;
    }

    public void setOuter_batch_no(String outer_batch_no) {
        this.outer_batch_no = outer_batch_no;
    }

    public String getInner_batch_no() {
        return inner_batch_no;
    }

    public void setInner_batch_no(String inner_batch_no) {
        this.inner_batch_no = inner_batch_no;
    }

    public String getBatch_quantity() {
        return batch_quantity;
    }

    public void setBatch_quantity(String batch_quantity) {
        this.batch_quantity = batch_quantity;
    }

    public BigDecimal getBatch_amount() {
        return batch_amount;
    }

    public void setBatch_amount(BigDecimal batch_amount) {
        this.batch_amount = batch_amount;
    }

    public BATCH_STATUS getBatch_status() {
        return batch_status;
    }

    public void setBatch_status(BATCH_STATUS batch_status) {
        this.batch_status = batch_status;
    }

    public String getTrade_list() {
        return trade_list;
    }

    public void setTrade_list(String trade_list) {
        this.trade_list = trade_list;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getGmt_finished() {
        return gmt_finished;
    }

    public void setGmt_finished(String gmt_finished) {
        this.gmt_finished = gmt_finished;
    }

    @Override
    public String toString() {
        return "BatchTradeCallBackEntity{" +
                "outer_batch_no='" + outer_batch_no + '\'' +
                ", inner_batch_no='" + inner_batch_no + '\'' +
                ", batch_quantity='" + batch_quantity + '\'' +
                ", batch_amount=" + batch_amount +
                ", batch_status=" + batch_status +
                ", trade_list='" + trade_list + '\'' +
                ", gmt_create='" + gmt_create + '\'' +
                ", gmt_finished='" + gmt_finished + '\'' +
                "} " + super.toString();
    }
}
