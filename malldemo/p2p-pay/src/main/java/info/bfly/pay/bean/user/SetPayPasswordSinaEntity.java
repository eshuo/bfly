package info.bfly.pay.bean.user;

import info.bfly.pay.bean.BaseSinaEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by hdy on 2017.2.16.
 */

public class SetPayPasswordSinaEntity extends BaseSinaEntity {

    private static final long serialVersionUID = -1246226113067294500L;
    /**
     * 用户Id
     */
    @NotNull
    @Size(max = 50)
    private String identity_id;

    @NotNull
    @Size(max = 16)
    private String identity_type = "UID";

//    /**
//     * 委托扣款展示方式:参数名1^参数值1|参数名2^参数值2|……
//     */
//    @Size(max=300)
//    private String withhold_param;
//
//
//    /**
//     * 展示委托扣款类型:  ALL:银行卡委托扣款 ,ACCOUNT:余额委托扣款, NONE:无
//     */
//    @Size(max=300)
//    private String withhold_auth_type;
//    /**
//     * 委托扣款是否必选: Y或N
//     */
//    @Size(max=300)
//    private String is_check;


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


}
