package info.bfly.pay.bean.user;

import info.bfly.pay.bean.enums.ACCOUNT_TYPE;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;

/**
 * 会员开户接口 on 2017/4/20 0020.
 */

public class OpenAccountEntity extends SinaUserBaseEntity {


    private static final long serialVersionUID = 781889511722410230L;
    @NotNull
    @Value("#{refProperties['sinapay_client_ip']}")
    private String client_ip;


    private ACCOUNT_TYPE account_type = ACCOUNT_TYPE.BASIC;


    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public ACCOUNT_TYPE getAccount_type() {
        return account_type;
    }

    public void setAccount_type(ACCOUNT_TYPE account_type) {
        this.account_type = account_type;
    }
}
