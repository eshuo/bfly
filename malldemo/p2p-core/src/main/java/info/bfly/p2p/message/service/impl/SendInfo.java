package info.bfly.p2p.message.service.impl;

public class SendInfo extends ApiResultBase {
    private static final long serialVersionUID = 3176679383556154165L;
    private Integer           count;
    private Integer           fee;
    private Long              sid;

    public Integer getCount() {
        return count;
    }

    public Integer getFee() {
        return fee;
    }

    public Long getSid() {
        return sid;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }
}
