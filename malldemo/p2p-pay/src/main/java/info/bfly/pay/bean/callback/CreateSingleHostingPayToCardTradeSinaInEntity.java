package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2017/2/20 0020.
 */
public class CreateSingleHostingPayToCardTradeSinaInEntity extends SinaInEntity {

    /**
     * 交易订单号
     */
    @NotNull
    private String out_trade_no;
    /**
     * 交易状态
     */
    private String withdraw_status;

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
}
