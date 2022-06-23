package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/20 0020.
 */
public class CreateHostingRefundSinaInEntity extends SinaInEntity implements Serializable {
    private static final long serialVersionUID = -7959615073075577382L;

    /**
     * 交易订单号
     */
    @NotNull
    private String out_trade_no;
    /**
     * 退款状态
     */
    private String refund_status;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }
}
