package info.bfly.pay.bean.order;

import info.bfly.pay.bean.enums.OUT_TRADE_CODE;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 批量托管代付交易 on 2017/2/17 0017.
 */

public class CreateBatchHostingPayTradeSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = -1535001092058893534L;
    /**
     * 支付请求号
     */
    @NotNull
    private String         out_pay_no;
    /**
     * 外部业务码
     */
    @NotNull
    private OUT_TRADE_CODE out_trade_code;
    /**
     * 交易列表
     */
    @NotNull
    private String         trade_list;
    /**
     * 通知方式
     */
    @NotNull
    private String         notify_method;
    /**
     * 用户IP地址
     */
    @NotNull
    @Value("#{refProperties['sinapay_partner_id']}")
    private String         user_ip;


    public String getOut_pay_no() {
        return out_pay_no;
    }

    public void setOut_pay_no(String out_pay_no) {
        this.out_pay_no = out_pay_no;
    }

    public OUT_TRADE_CODE getOut_trade_code() {
        return out_trade_code;
    }

    public void setOut_trade_code(OUT_TRADE_CODE out_trade_code) {
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

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    @Override
    public String toString() {
        return "CreateBatchHostingPayTradeSinaEntity{" +
                "out_pay_no='" + out_pay_no + '\'' +
                ", out_trade_code='" + out_trade_code + '\'' +
                ", trade_list=" + trade_list +
                ", notify_method='" + notify_method + '\'' +
                ", user_ip='" + user_ip + '\'' +
                "} " + super.toString();
    }
}
