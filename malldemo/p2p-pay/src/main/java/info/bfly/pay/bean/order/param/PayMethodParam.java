package info.bfly.pay.bean.order.param;

import info.bfly.pay.bean.enums.PAYMETHOD_TYPE;

import java.math.BigDecimal;

/**
 * Created by XXSun on 3/1/2017.
 */
public class PayMethodParam {
    private String     payType;
    private BigDecimal payMoney;
    private String     extendParam;

    public final static PayMethodParam ONLINE_BANK = new PayMethodParam(PAYMETHOD_TYPE.ONLINE_BANK.getType_name(),"SINAPAY,DEBIT,C");
    public final static PayMethodParam BALANCE     = new PayMethodParam(PAYMETHOD_TYPE.BALANCE.getType_name());
    public final static PayMethodParam BINDING_PAY = new PayMethodParam(PAYMETHOD_TYPE.BINDING_PAY.getType_name());

    public PayMethodParam() {

    }

    public PayMethodParam(String payType) {
        this.payType = payType;
    }

    public PayMethodParam(String payType,String extendParam) {
        this.payType = payType;
        this.extendParam = extendParam;
    }
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }
}
