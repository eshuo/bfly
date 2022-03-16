package info.bfly.archer.system.controller;

import info.bfly.archer.menu.model.Menu;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.el.SpringSecurityELLibrary;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.PermissionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

import static info.bfly.core.jsf.el.SpringSecurityELLibrary.isAuthenticated;

@Component
@Scope(ScopeType.SESSION)
public class LoginUserInfo implements Serializable {
    private static final long serialVersionUID = 2359599087254308891L;
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;
    @Autowired
    SessionRegistry sessionRegistry;
    private String loginUserId;
    private String selectMenuId;
    private String remoteAddr;
    private String theme = "bootstrap";         // redmond// ;bluesky;aristo

    /**
     * 递归找到menus中第一个当前用户可以访问的menu
     */
    private Menu getFirstGrantedMenu(List<Menu> menus) {
        for (Menu menu : menus) {
            if (menu.getChildren().size() > 0) {
                return getFirstGrantedMenu(menu.getChildren());
            } else if (SpringSecurityELLibrary.ifAllGranted(PermissionUtil.getPermissionStr(menu.getPermissions())) || menu.getPermissions().size() == 0) {
                return menu;
            }
        }
        return null;
    }

    public String getLoginUserId() {
        if (isAuthenticated())
            loginUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        else
            loginUserId = null;
        return loginUserId;
    }


    public String getRemoteAddr() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();

        if (details instanceof WebAuthenticationDetails)
            remoteAddr = ((WebAuthenticationDetails) details).getRemoteAddress();

        if (details instanceof OAuth2AuthenticationDetails)
            remoteAddr = ((OAuth2AuthenticationDetails) details).getRemoteAddress();

        if (StringUtils.isEmpty(remoteAddr))
            remoteAddr = "";

        return remoteAddr;
    }

    /*
     * 查询平台在线总人数
     */
    public int getNumberOfUsers() {
        return sessionRegistry.getAllPrincipals().size();
    }

    public String getSelectMenuId() {
        return selectMenuId;
    }

    public String getTheme() {
        return theme;
    }

    public void selectMenu(String selectMenuId) {
        setSelectMenuId(selectMenuId);
        Menu menu = getFirstGrantedMenu(ht.get(Menu.class, selectMenuId).getChildren());
        if (menu != null) {
            FacesUtil.sendRedirect(FacesUtil.getHttpServletRequest().getContextPath() + menu.getUrl());
        }
    }

    public void setSelectMenuId(String selectMenuId) {
        this.selectMenuId = selectMenuId;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
