package info.bfly.archer.openauth.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.openauth.model.OpenAuth;
import info.bfly.archer.openauth.service.OpenAuthService;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 */
@Component
@Scope(ScopeType.REQUEST)
public class OpenAuthHome extends EntityHome<OpenAuth> implements Serializable {
    @Resource(name = "qqOpenAuthService")
    private OpenAuthService qqOAS;
    @Resource(name = "sinaWeiboOpenAuthService")
    private OpenAuthService sinaWeiboOAS;

    public String getQQAuthUrl() {
        return qqOAS.getAuthUrl();
    }

    public String getSinaWeiboAuthUrl() {
        return sinaWeiboOAS.getAuthUrl();
    }
    /**
     * 第三方授权后，绑定已有账号
     *
     * @return
     */
    // public String bindingExistUser(){
    // }
}
