package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 3.17    代收撤销 on 2017/2/17 0017.
 */

public class CancelPreAuthTradeSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 8757378801813871919L;

    /**
     * 商户代收撤销请求号
     */
    @NotNull
    private String                      out_request_no;
    /**
     * 代收撤销交易列表
     */
    @NotNull
    private String trade_list;

    public String getOut_request_no() {
        return out_request_no;
    }

    public void setOut_request_no(String out_request_no) {
        this.out_request_no = out_request_no;
    }

    public String getTrade_list() {
        return trade_list;
    }

    public void setTrade_list(String trade_list) {
        this.trade_list = trade_list;
    }

    @Override
    public String toString() {
        return "CancelPreAuthTradeSinaEntity{" +
                "out_request_no='" + out_request_no + '\'' +
                ", trade_list='" + trade_list + '\'' +
                "} " + super.toString();
    }
}
