package info.bfly.pay.bean.user;

import javax.validation.constraints.Pattern;

/**
 * 查看用户是否委托扣款 on 2017/4/20 0020.
 */

public class QueryWithholdAuthorityEntity extends SinaUserBaseEntity {

    private static final long serialVersionUID = 6350492804091823293L;
    /**
     * 授权类型
     */
    private String auth_type;

    /**
     * 授权子类型
     */
    private String auth_sub_type;


    /**
     * 是否展示授权明细
     */
    @Pattern(regexp = "Y|N")
    private String is_detail_disp = "Y";


    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    public String getAuth_sub_type() {
        return auth_sub_type;
    }

    public void setAuth_sub_type(String auth_sub_type) {
        this.auth_sub_type = auth_sub_type;
    }

    public String getIs_detail_disp() {
        return is_detail_disp;
    }

    public void setIs_detail_disp(String is_detail_disp) {
        this.is_detail_disp = is_detail_disp;
    }
}
