package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;

/**
 * 3.5    支付结果查询响应 on 2017/2/17 0017.
 */
public class QueryPayRequestSinaInEntity extends SinaInEntity {

    private static final long serialVersionUID = -5904550345086816502L;
    /**
     * 支付订单号
     */
    @NotNull
    private String out_pay_no;
    /**
     * 支付状态
     */
    private String pay_status;

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
}
