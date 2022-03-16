package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.Size;

public class QueryMiddleAccountResponseEntity extends SinaInEntity {
    /**
     * 账户列表
     */
    @Size(max = 4000)
    public String account_list;
    /**
     * 扩展信息
     * @return
     */
    @Size(max = 200)
    public String extend_param;

    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }

    public String getAccount_list() {
        return account_list;
    }

    public void setAccount_list(String account_list) {
        this.account_list = account_list;
    }

}
