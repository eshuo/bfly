package info.bfly.pay.bean.user;

import javax.validation.constraints.Size;

/**
 * 银行卡展示参数on 2017/4/20 0020.
 */

public class WebBankEntity extends SinaUserBaseEntity {

    private static final long serialVersionUID = -8340611254156941007L;
    /**
     * 是否允许用户绑定银行卡
     */
    @Size(max = 1)
    private String could_bind = "Y";

    /**
     * 是否允许用户删除银行卡
     */
    @Size(max = 1)
    private String could_unbind = "Y";


    public String getCould_bind() {
        return could_bind;
    }

    public void setCould_bind(String could_bind) {
        this.could_bind = could_bind;
    }

    public String getCould_unbind() {
        return could_unbind;
    }

    public void setCould_unbind(String could_unbind) {
        this.could_unbind = could_unbind;
    }
}
