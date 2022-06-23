package info.bfly.pay.bean.user;

import info.bfly.pay.bean.BaseSinaEntity;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Administrator on 2017/4/20 0020.
 */
public class SinaUserBaseEntity extends BaseSinaEntity {


    private static final long serialVersionUID = 5317758143258267913L;
    @NotNull
    @Size(max = 50)
    private String identity_id;
    @NotNull
    @Size(max = 16)
    private String identity_type = "UID";

    private String extend_param = StringUtils.EMPTY;


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

    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }
}
