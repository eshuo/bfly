package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.enums.PAY_STATUS;
import info.bfly.pay.bean.enums.TRADE_STATUS;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建托管代收交易响应 on 2017/2/17 0017.
 */
public class CreateHostingCollectTradeSinaInEntity extends SinaInEntity implements Serializable {
    private static final long serialVersionUID = -7431989200870566683L;

    /**
     * 交易订单号
     */
    @NotNull
    private String       out_trade_no;
    /**
     * 交易状态
     */
    private TRADE_STATUS trade_status;
    /**
     * 支付状态
     */
    private PAY_STATUS   pay_status;
    /**
     * 后续推进需要的参数
     */
    private String       ticket;
    /**
     * 收银台重定向地址
     */
    private String       redirect_url;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public TRADE_STATUS getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(TRADE_STATUS trade_status) {
        this.trade_status = trade_status;
    }

    public PAY_STATUS getPay_status() {
        return pay_status;
    }

    public void setPay_status(PAY_STATUS pay_status) {
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

    @Override
    public String toString() {
        return "CreateHostingCollectTradeSinaInEntity{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", trade_status='" + trade_status + '\'' +
                ", pay_status='" + pay_status + '\'' +
                ", ticket='" + ticket + '\'' +
                ", redirect_url='" + redirect_url + '\'' +
                "} " + super.toString();
    }
}
