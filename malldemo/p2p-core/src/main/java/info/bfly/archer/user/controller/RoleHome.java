package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class RoleHome extends EntityHome<Role> implements Serializable {
    private static final long          serialVersionUID = 7138360013508124310L;
    private static       StringManager sm               = StringManager.getManager(UserConstants.Package);
    private final static String        UPDATE_VIEW      = FacesUtil.redirect("/admin/user/roleList");
    @Log
    Logger log;

    public RoleHome() {
        // FIXME：保存角色的时候会执行一条更新User的语句
        setUpdateView(RoleHome.UPDATE_VIEW);
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (log.isInfoEnabled()) {
            log.info(RoleHome.sm.getString("log.info.deleteRole", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        List<User> userLists = getInstance().getUsers();
        if (userLists != null && userLists.size() > 0) {
            FacesUtil.addWarnMessage(RoleHome.sm.getString("canNotDeleteRole"));
            if (log.isInfoEnabled()) {
                log.info(RoleHome.sm.getString("log.info.deleteRoleUnsuccessful", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
            }
            return null;
        } else {
            return super.delete();
        }
    }
}
