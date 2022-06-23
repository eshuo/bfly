package info.bfly.pay.p2p.user.controller;

import info.bfly.archer.user.controller.UserHome;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.pay.controller.SinaUserController;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


/**
 * Created by XXSun on 3/9/2017.
 */
@Component
public class SinaUserHome extends UserHome{
    @Log
    Logger             log;
    @Autowired
    SinaUserController sinaUserController;

    @PreAuthorize("hasRole('ROLE_ADMIN') and not hasRole('INACTIVE')")
    public void redirectSinaUserInfo(String userId){
        try {
            String link = sinaUserController.showMemberInfosSinaSinaPay(userId, "1", "DEFAULT", "", "", "",true);
            FacesUtil.sendRedirect(sinaUserController.getMainUrl()+link);
        } catch (TrusteeshipReturnException e) {
            log.error("获取用户连接信息错误 {}",e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER') and not hasRole('INACTIVE')")
    public void redirectSinaUserInfo(){
        try {
            String link = sinaUserController.showMemberInfosSinaSinaPay(getId(), "1", "DEFAULT", "", "", "",false);
            FacesUtil.sendRedirect(sinaUserController.getMainUrl()+link);
        } catch (TrusteeshipReturnException e) {
            log.error("获取用户连接信息错误 {}",e.getMessage());
        }
    }
}
