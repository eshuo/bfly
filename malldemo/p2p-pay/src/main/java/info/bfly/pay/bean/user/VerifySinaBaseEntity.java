package info.bfly.pay.bean.user;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户认证参数on 2017/4/20 0020.
 */

public class VerifySinaBaseEntity extends SinaUserBaseEntity {

    private static final long serialVersionUID = 5201916103039767452L;
    @NotNull
    @Size(max = 100)
    private String verify_type = "MOBILE";

    @NotNull
    @Value("#{refProperties['sinapay_client_ip']}")
    private String client_ip;


    public String getVerify_type() {
        return verify_type;
    }

    public void setVerify_type(String verify_type) {
        this.verify_type = verify_type;
    }


    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }
}
