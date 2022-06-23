package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 支付结果查询 on 2017/2/17 0017.
 */

public class QueryPayRequestSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = -497017758256992651L;

    /**
     * 支付请求号
     */
    @NotNull
    private String out_pay_no;

    public String getOut_pay_no() {
        return out_pay_no;
    }

    public void setOut_pay_no(String out_pay_no) {
        this.out_pay_no = out_pay_no;
    }

    @Override
    public String toString() {
        return "QueryPayRequestSinaEntity{" +
                "out_pay_no='" + out_pay_no + '\'' +
                "} " + super.toString();
    }
}
