package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;

/**
 * 3.2    创建托管代付交易响应 on 2017/2/17 0017.
 */
public class CreateSingleHostingPayTradeSinaInEntity extends SinaInEntity {


    /**
     * 交易订单号
     */
    @NotNull
    private String out_trade_no;
    /**
     * 交易状态
     */
    private String trade_status;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }
}
