package info.bfly.pay.bean.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by XXSun on 2017/1/13.
 */

public class RealUserSinaEntity extends UserSinaEntity{
    private static final long serialVersionUID = -2779718113223355174L;
    @NotNull
    @Size(max=50)
    private String real_name;
    @NotNull
    @Pattern(regexp = "IC")
    private String cert_type="IC";
    @NotNull
    private String cert_no;
    @Pattern(regexp = "Y|N")
    private String need_confirm = "Y";

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getCert_type() {
        return cert_type;
    }

    public void setCert_type(String cert_type) {
        this.cert_type = cert_type;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getNeed_confirm() {
        return need_confirm;
    }

    public void setNeed_confirm(String need_confirm) {
        this.need_confirm = need_confirm;
    }
}
