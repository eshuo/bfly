package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.menu.model.Menu;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.Permission;
import info.bfly.archer.user.model.Role;
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
import java.util.Set;

@Component
@Scope(ScopeType.REQUEST)
public class PermissionHome extends EntityHome<Permission> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8035454856873913607L;
    @Log
    static Logger log;
    private static       StringManager userSM      = StringManager.getManager(UserConstants.Package);
    private final static String        UPDATE_VIEW = FacesUtil.redirect("/admin/user/permissionList");

    public PermissionHome() {
        setUpdateView(PermissionHome.UPDATE_VIEW);
    }

    @Transactional(readOnly = false)
    public void ajaxDelete() {
        super.delete();
        FacesUtil.addInfoMessage(EntityHome.commonSM.getString("deleteLabel") + EntityHome.commonSM.getString("success"));
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (PermissionHome.log.isInfoEnabled()) {
            PermissionHome.log.info(PermissionHome.userSM.getString("log.info.deletePermission", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        Set<Role> roleSets = getInstance().getRoles();
        Set<Menu> menuSets = getInstance().getMenus();
        if (roleSets != null && roleSets.size() > 0) {
            FacesUtil.addWarnMessage(PermissionHome.userSM.getString("canNotDeletePermissionByRole"));
            if (PermissionHome.log.isInfoEnabled()) {
                PermissionHome.log
                        .info(PermissionHome.userSM.getString("log.info.deletePermissionByRoleUnsuccessful", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
            }
            return null;
        } else if (menuSets != null && menuSets.size() > 0) {
            FacesUtil.addWarnMessage(PermissionHome.userSM.getString("canNotDeletePermissionByMenu"));
            if (PermissionHome.log.isInfoEnabled()) {
                PermissionHome.log
                        .info(PermissionHome.userSM.getString("log.info.deletePermissionByMenuUnsuccessful", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
            }
            return null;
        } else {
            return super.delete();
        }
    }
}
