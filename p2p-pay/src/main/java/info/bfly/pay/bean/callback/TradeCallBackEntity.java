package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.BaseCallBackEntity;
import info.bfly.pay.bean.enums.TRADE_STATUS;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 4.3 交易结果通知
 * Created by XXSun on 2017/2/17.
 */
public class TradeCallBackEntity extends BaseCallBackEntity {
    private static final long serialVersionUID = 6831909026643425034L;
    /**
     * 商户网站唯一订单号或者交易原始凭证号
     */
    @NotNull
    @Size(max = 32)
    private String       outer_trade_no;
    /**
     * 内部交易凭证号
     */
    @NotNull
    @Size(max = 32)
    private String       inner_trade_no;
    /**
     * 交易状态
     */
    @NotNull
    private TRADE_STATUS trade_status;
    /**
     * 交易金额 单位元，可以含小数点
     */
    private BigDecimal   trade_amount;
    /**
     * 交易创建时间 yyyyMMddHHmmss
     */
    @NotNull
    @Size(min = 14,max = 14)
    private String       gmt_create;
    /**
     * 交易支付时间 yyyyMMddHHmmss
     */
    private String       gmt_payment;
    /**
     * 交易关闭时间 yyyyMMddHHmmss
     */
    private String       gmt_close;
    /**
     * 支付方式 <br>
     * 用户实际支付使用的支付方式。<br>
     * 格式：支付方式^金额^扩展|支付方式^金额^扩展。扩展信息内容以“，”分隔
     */
    private String       pay_method;
    private BigDecimal   auth_finish_amount;

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

    public TRADE_STATUS getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(TRADE_STATUS trade_status) {
        this.trade_status = trade_status;
    }

    public BigDecimal getTrade_amount() {
        return trade_amount;
    }

    public void setTrade_amount(BigDecimal trade_amount) {
        this.trade_amount = trade_amount;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getGmt_payment() {
        return gmt_payment;
    }

    public void setGmt_payment(String gmt_payment) {
        this.gmt_payment = gmt_payment;
    }

    public String getGmt_close() {
        return gmt_close;
    }

    public void setGmt_close(String gmt_close) {
        this.gmt_close = gmt_close;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public BigDecimal getAuth_finish_amount() {
        return auth_finish_amount;
    }

    public void setAuth_finish_amount(BigDecimal auth_finish_amount) {
        this.auth_finish_amount = auth_finish_amount;
    }

    @Override
    public String toString() {
        return "TradeCallBackEntity{" +
                "outer_trade_no='" + outer_trade_no + '\'' +
                ", inner_trade_no='" + inner_trade_no + '\'' +
                ", trade_status=" + trade_status +
                ", trade_amount=" + trade_amount +
                ", gmt_create='" + gmt_create + '\'' +
                ", gmt_payment='" + gmt_payment + '\'' +
                ", gmt_close='" + gmt_close + '\'' +
                ", pay_method='" + pay_method + '\'' +
                ", auth_finish_amount=" + auth_finish_amount +
                "} " + super.toString();
    }
}
