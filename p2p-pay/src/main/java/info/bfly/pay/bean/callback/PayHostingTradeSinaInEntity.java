package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;

/**
 * 3.4    托管交易支付响应 on 2017/2/17 0017.
 */
public class PayHostingTradeSinaInEntity extends SinaInEntity {

    /**
     * 支付订单号
     */
    @NotNull
    private String out_pay_no;
    /**
     * 支付状态
     */
    private String pay_status;
    /**
     * 后续推进需要的参数
     */
    private String ticket;
    /**
     * 收银台重定向地址
     */
    private String redirect_url;

    public String getOut_pay_no() {
        return out_pay_no;
    }

    public void setOut_pay_no(String out_pay_no) {
        this.out_pay_no = out_pay_no;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }
}
