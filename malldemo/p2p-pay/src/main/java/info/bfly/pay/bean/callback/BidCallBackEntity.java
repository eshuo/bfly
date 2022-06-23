package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.BaseCallBackEntity;
import info.bfly.pay.bean.enums.BID_STATUS;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 4.9 标的状态通知
 * Created by XXSun on 2017/2/17.
 */
public class BidCallBackEntity extends BaseCallBackEntity {
    /**
     * 商户标的号
     */
    @NotNull
    @Size(max = 64)
    private String     out_bid_no;
    /**
     * 新浪支付内部标的号
     */
    @NotNull
    @Size(max = 32)
    private String     inner_bid_no;
    /**
     * 标的状态
     */
    @NotNull
    private BID_STATUS bid_status;
    /**
     * 拒绝原因
     */
    private String     reject_reason;

    public String getOut_bid_no() {
        return out_bid_no;
    }

    public void setOut_bid_no(String out_bid_no) {
        this.out_bid_no = out_bid_no;
    }

    public String getInner_bid_no() {
        return inner_bid_no;
    }

    public void setInner_bid_no(String inner_bid_no) {
        this.inner_bid_no = inner_bid_no;
    }

    public BID_STATUS getBid_status() {
        return bid_status;
    }

    public void setBid_status(BID_STATUS bid_status) {
        this.bid_status = bid_status;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    @Override
    public String toString() {
        return "BidCallBackEntity{" +
                "out_bid_no='" + out_bid_no + '\'' +
                ", inner_bid_no='" + inner_bid_no + '\'' +
                ", bid_status=" + bid_status +
                ", reject_reason='" + reject_reason + '\'' +
                '}' + super.toString();
    }
}
