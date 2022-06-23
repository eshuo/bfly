package info.bfly.pay.bean.user;

import info.bfly.pay.bean.BaseSinaEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by XXSun on 2017/1/12.
 */

public class UserSinaEntity extends BaseSinaEntity {
    private static final long serialVersionUID = 8000424943214971315L;
    @NotNull
    @Size(max = 50)
    private String identity_id;
    @NotNull
    @Size(max = 16)
    private String identity_type="UID";

    @Size(max = 1)
    private String member_type;

    @NotNull
    @Value("#{refProperties['sinapay_client_ip']}")
    private String client_ip;


    private String extend_param= StringUtils.EMPTY;


    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public String getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(String identity_type) {
        this.identity_type = identity_type;
    }

    public String getMember_type() {
        return member_type;
    }

    public void setMember_type(String member_type) {
        this.member_type = member_type;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }
    public void addExtend_param(String extend_param_key,String extend_param_value) {
        this.extend_param += (this.extend_param.length()>0?"|":"")+extend_param_key+"^"+extend_param_value;
    }
}
