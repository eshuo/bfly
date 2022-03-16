package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 3.19    标的信息查询 on 2017/2/17 0017.
 */

public class QueryBidInfoSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = -8589370452699305557L;

    /**
     * 商户标的号
     */
    @NotNull
    private String out_bid_no;

    public String getOut_bid_no() {
        return out_bid_no;
    }

    public void setOut_bid_no(String out_bid_no) {
        this.out_bid_no = out_bid_no;
    }

    @Override
    public String toString() {
        return "QueryBidInfoSinaEntity{" +
                "out_bid_no='" + out_bid_no + '\'' +
                "} " + super.toString();
    }
}
