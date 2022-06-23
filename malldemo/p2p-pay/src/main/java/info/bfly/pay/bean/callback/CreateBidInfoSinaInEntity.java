package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.enums.BID_STATUS;

import javax.validation.constraints.NotNull;

/**
 * 3.18    标的录入响应 on 2017/2/20 0020.
 */
public class CreateBidInfoSinaInEntity extends SinaInEntity {

    /**
     * 商户标的号
     */
    @NotNull
    private String out_bid_no;
    /**
     * 标的凭证号
     */
    @NotNull
    private String inner_bid_no;
    /**
     * 标的状态
     */
    @NotNull
    private BID_STATUS bid_status;
    /**
     * 拒绝原因
     */
    private String reject_reason;
    /**
     * 创建时间
     */
    @NotNull
    private String gmt_create;
    /**
     * 修改时间
     */
    @NotNull
    private String gmt_modify;

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

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getGmt_modify() {
        return gmt_modify;
    }

    public void setGmt_modify(String gmt_modify) {
        this.gmt_modify = gmt_modify;
    }

    @Override
    public String toString() {
        return "CreateBidInfoSinaInEntity{" +
                "out_bid_no='" + out_bid_no + '\'' +
                ", inner_bid_no='" + inner_bid_no + '\'' +
                ", bid_status=" + bid_status +
                ", reject_reason='" + reject_reason + '\'' +
                ", gmt_create='" + gmt_create + '\'' +
                ", gmt_modify='" + gmt_modify + '\'' +
                "} " + super.toString();
    }
}
