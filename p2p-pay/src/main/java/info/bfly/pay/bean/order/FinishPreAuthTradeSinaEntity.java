package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 3.16    代收完成 Administrator on 2017/2/17 0017.
 */

public class FinishPreAuthTradeSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = -6373000052041499440L;

    /**
     * 代收完成请求号
     */
    @NotNull
    private String out_request_no;
    /**
     * 交易列表
     * 。参数间用“~”分隔，各条目之间用“$”分隔，备注信息不要包含特殊分隔符
     */
    @NotNull
    private String trade_list;
    /**
     * 用户IP地址
     */
    @NotNull
    private String user_ip;

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

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    @Override
    public String toString() {
        return "FinishPreAuthTradeSinaEntity{" +
                "out_request_no='" + out_request_no + '\'' +
                ", trade_list='" + trade_list + '\'' +
                ", user_ip='" + user_ip + '\'' +
                "} " + super.toString();
    }
}
