package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.BaseCallBackEntity;
import info.bfly.pay.bean.enums.WITHDRAW_STATUS;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 4.7 提现结果通知
 * Created by XXSun on 2017/2/17.
 */
public class WithdrawCallBackEntity extends BaseCallBackEntity {
    private static final long serialVersionUID = -4374430636474230491L;
    /**
     * 商户网站唯一订单号或者交易原始凭证号
     */
    @NotNull
    @Size(max = 32)
    private String          outer_trade_no;
    /**
     * 内部交易凭证号
     */
    @NotNull
    @Size(max = 32)
    private String          inner_trade_no;
    /**
     * 提现状态
     */
    @NotNull
    private WITHDRAW_STATUS withdraw_status;
    /**
     * 提现金额
     */
    @NotNull
    private BigDecimal      withdraw_amount;
    /**
     * 银行卡ID
     */
    private String          card_id;

    public String getOuter_trade_no() {
        return outer_trade_no;
    }

    public void setOuter_trade_no(String outer_trade_no) {
        this.outer_trade_no = outer_trade_no;
    }

    public String getInner_trade_no() {
        return inner_trade_no;
    }

    public void setInner_trade_no(String inner_trade_no) {
        this.inner_trade_no = inner_trade_no;
    }

    public WITHDRAW_STATUS getWithdraw_status() {
        return withdraw_status;
    }

    public void setWithdraw_status(WITHDRAW_STATUS withdraw_status) {
        this.withdraw_status = withdraw_status;
    }

    public BigDecimal getWithdraw_amount() {
        return withdraw_amount;
    }

    public void setWithdraw_amount(BigDecimal withdraw_amount) {
        this.withdraw_amount = withdraw_amount;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    @Override
    public String toString() {
        return "WithdrawCallBackEntity{" +
                "outer_trade_no='" + outer_trade_no + '\'' +
                ", inner_trade_no='" + inner_trade_no + '\'' +
                ", withdraw_status=" + withdraw_status +
                ", withdraw_amount=" + withdraw_amount +
                ", card_id='" + card_id + '\'' +
                "} " + super.toString();
    }
}
