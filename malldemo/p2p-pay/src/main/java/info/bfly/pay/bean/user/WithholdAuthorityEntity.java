package info.bfly.pay.bean.user;

import java.math.BigDecimal;

/**
 * 委托扣款 on 2017/4/20 0020.
 */

public class WithholdAuthorityEntity extends SinaUserBaseEntity {


    private static final long serialVersionUID = -2372086565269354198L;
    /**
     * 单笔额度
     */
    private BigDecimal quota;

    /**
     * 日累计额度
     */
    private BigDecimal day_quota;


    /**
     * 代扣授权类型白名单
     *
     * 以逗号分隔，期望的授权类型，目前可用的授权类型为
     * ALL:银行卡授权(该授权包含账户授权)
     * ACCOUNT,账户授权
     */
    private String auth_type_whitelist;


    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public BigDecimal getDay_quota() {
        return day_quota;
    }

    public void setDay_quota(BigDecimal day_quota) {
        this.day_quota = day_quota;
    }

    public String getAuth_type_whitelist() {
        return auth_type_whitelist;
    }

    public void setAuth_type_whitelist(String auth_type_whitelist) {
        this.auth_type_whitelist = auth_type_whitelist;
    }
}
