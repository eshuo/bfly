package info.bfly.archer.menu.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.menu.MenuConstants;
import info.bfly.archer.menu.model.Menu;
import info.bfly.archer.menu.model.MenuType;
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
@Scope(ScopeType.VIEW)
public class MenuTypeHome extends EntityHome<MenuType> implements Serializable {
    private static final long serialVersionUID = 6122879462532135419L;
    @Log
    static Logger log;
    private static final StringManager sm = StringManager.getManager(MenuConstants.Package);

    public MenuTypeHome() {
        setUpdateView(FacesUtil.redirect(MenuConstants.View.MENU_TYPE_LIST));
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (MenuTypeHome.log.isInfoEnabled()) {
            MenuTypeHome.log.info(MenuTypeHome.sm.getString("log.info.deleteMenuType", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        Set<Menu> menuSets = getInstance().getMenus();
        if (menuSets != null && menuSets.size() > 0) {
            FacesUtil.addWarnMessage(MenuTypeHome.sm.getString("canNotDeleteMenuType"));
            if (MenuTypeHome.log.isInfoEnabled()) {
                MenuTypeHome.log.info(MenuTypeHome.sm.getString("log.info.deleteMenuUnsuccessful", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
            }
            return null;
        } else {
            return super.delete();
        }
    }
}
