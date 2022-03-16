package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.BaseCallBackEntity;
import info.bfly.pay.bean.enums.DEPOSIT_STATUS;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 4.6 充值结果通知
 * Created by XXSun on 2017/2/17.
 */
public class DepositCallBackEntity extends BaseCallBackEntity {
    /**
     * 商户网站唯一订单号或者交易原始凭证号
     */
    @NotNull
    @Size(max = 32)
    private String         outer_trade_no;
    /**
     * 内部交易凭证号
     */
    @NotNull
    @Size(max = 32)
    private String         inner_trade_no;
    /**
     * 充值状态
     */
    @NotNull
    private DEPOSIT_STATUS deposit_status;
    /**
     * 充值金额
     */
    @NotNull
    private BigDecimal     deposit_amount;
    /**
     * 支付方式
     */
    private String         pay_method;

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

    public DEPOSIT_STATUS getDeposit_status() {
        return deposit_status;
    }

    public void setDeposit_status(DEPOSIT_STATUS deposit_status) {
        this.deposit_status = deposit_status;
    }

    public BigDecimal getDeposit_amount() {
        return deposit_amount;
    }

    public void setDeposit_amount(BigDecimal deposit_amount) {
        this.deposit_amount = deposit_amount;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    @Override
    public String toString() {
        return "DepositCallBackEntity{" +
                "outer_trade_no='" + outer_trade_no + '\'' +
                ", inner_trade_no='" + inner_trade_no + '\'' +
                ", deposit_status=" + deposit_status +
                ", deposit_amount=" + deposit_amount +
                ", pay_method='" + pay_method + '\'' +
                "} " + super.toString();
    }
}
