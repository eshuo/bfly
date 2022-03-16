package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;

/**
 * 3.12    托管提现响应 on 2017/2/20 0020.
 */
public class CreateHostingWithdrawSinaInEntity extends SinaInEntity {


    /**
     * 提现订单号
     */
    @NotNull
    private String out_trade_no;

    /**
     * 提现状态
     */
    private String withdraw_status;

    /**
     * 收银台重定向地址
     */
    private String redirect_url;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getWithdraw_status() {
        return withdraw_status;
    }

    public void setWithdraw_status(String withdraw_status) {
        this.withdraw_status = withdraw_status;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }
}
