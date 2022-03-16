package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 3.4    托管交易支付 on 2017/2/17 0017.
 */

public class PayHostingTradeSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 3456234445252106416L;
    /**
     * 支付请求号
     */
    @NotNull
    private String out_pay_no;
    /**
     * 商户网站唯一交易订单号集合
     */
    @NotNull
    private String outer_trade_no_list;
    /**
     * 付款用户IP地址
     */
    @NotNull
    private String payer_ip;
    /**
     * 支付方式
     */
    @NotNull
    private String pay_method;

    public String getOut_pay_no() {
        return out_pay_no;
    }

    public void setOut_pay_no(String out_pay_no) {
        this.out_pay_no = out_pay_no;
    }

    public String getOuter_trade_no_list() {
        return outer_trade_no_list;
    }

    public void setOuter_trade_no_list(String outer_trade_no_list) {
        this.outer_trade_no_list = outer_trade_no_list;
    }

    public String getPayer_ip() {
        return payer_ip;
    }

    public void setPayer_ip(String payer_ip) {
        this.payer_ip = payer_ip;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    @Override
    public String toString() {
        return "PayHostingTradeSinaEntity{" +
                "out_pay_no='" + out_pay_no + '\'' +
                ", outer_trade_no_list='" + outer_trade_no_list + '\'' +
                ", payer_ip='" + payer_ip + '\'' +
                ", pay_method='" + pay_method + '\'' +
                "} " + super.toString();
    }
}
