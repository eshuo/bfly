package info.bfly.pay.bean.order;

import info.bfly.pay.bean.BaseSinaEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class OrderSinaEntity extends BaseSinaEntity implements Serializable {
    private static final long serialVersionUID = -1588937831103980199L;


    /**
     * 拓展信息
     */
    private String extend_param = "";


    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }

    public void addExtend_param(String extend_param_key, String extend_param_value) {
        this.extend_param += (this.extend_param.length() > 0 ? "|" : "") + extend_param_key + "^" + extend_param_value;

    }

    @Override
    public String toString() {
        return "OrderSinaEntity{" +
                "extend_param='" + extend_param + '\'' +
                "} " + super.toString();
    }
}
