package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QueryIsSetPayPasswordEntity extends SinaInEntity {
    /**
     * 是否已经设置支付密码：Y或N
     */
    @NotNull
    @Size(max=1)
    private String is_set_paypass;

    public String getIs_set_paypass() {
        return is_set_paypass;
    }

    public void setIs_set_paypass(String is_set_paypass) {
        this.is_set_paypass = is_set_paypass;
    }
}
