package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 3.15创建批量代付到提现卡交易 on 2017/2/17 0017.
 */
public class CreateBatchHostingPayToCardTradeSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 1579613108970386312L;
    /**
     * 支付请求号
     */
    @NotNull
    private String out_pay_no;
    /**
     * 交易码
     */
    @NotNull
    private String out_trade_code;
    /**
     * 交易列表
     */
    @NotNull
    private String trade_list;
    /**
     * 通知方式
     */
    @NotNull
    private String notify_method;
    /**
     * 到账类型
     */
    private String payto_type;
    /**
     * 用户IP地址
     */
    @NotNull
    private String user_ip;

    public String getOut_pay_no() {
        return out_pay_no;
    }

    public void setOut_pay_no(String out_pay_no) {
        this.out_pay_no = out_pay_no;
    }

    public String getOut_trade_code() {
        return out_trade_code;
    }

    public void setOut_trade_code(String out_trade_code) {
        this.out_trade_code = out_trade_code;
    }

    public String getTrade_list() {
        return trade_list;
    }

    public void setTrade_list(String trade_list) {
        this.trade_list = trade_list;
    }

    public String getNotify_method() {
        return notify_method;
    }

    public void setNotify_method(String notify_method) {
        this.notify_method = notify_method;
    }

    public String getPayto_type() {
        return payto_type;
    }

    public void setPayto_type(String payto_type) {
        this.payto_type = payto_type;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    @Override
    public String toString() {
        return "CreateBatchHostingPayToCardTradeSinaEntity{" +
                "out_pay_no='" + out_pay_no + '\'' +
                ", out_trade_code='" + out_trade_code + '\'' +
                ", trade_list=" + trade_list +
                ", notify_method='" + notify_method + '\'' +
                ", payto_type='" + payto_type + '\'' +
                ", user_ip='" + user_ip + '\'' +
                "} " + super.toString();
    }
}
