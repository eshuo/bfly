package info.bfly.archer.menu.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.menu.MenuConstants;
import info.bfly.archer.menu.model.Menu;
import info.bfly.archer.menu.model.MenuType;
import info.bfly.archer.term.model.CategoryTerm;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(ScopeType.REQUEST)
public class MenuHome extends EntityHome<Menu> implements java.io.Serializable {
    /**
     *
     */
    private static final long          serialVersionUID = 1L;
    @Log
    static Logger log;
    private static final StringManager sm               = StringManager.getManager(MenuConstants.Package);
    // private Menu selectedMenu;
    @Resource
    HibernateTemplate                  ht;
    private Map<String, String> referenceUrls;

    public MenuHome() {
        setUpdateView(FacesUtil.redirect(MenuConstants.View.MENU_LIST));
    }

    @Override
    public Menu createInstance() {
        Menu menu = new Menu();
        // menu.setPermission(new Permission());
        // 设置父菜单
        String parentId = FacesUtil.getParameter("parentId");
        if (StringUtils.isNotEmpty(parentId)) {
            Menu parent = new Menu();
            parent.setId(parentId);
            menu.setParent(parent);
        }
        else {
            menu.setParent(new Menu());
        }
        String typeId = FacesUtil.getParameter("typeId");
        MenuType type = new MenuType();
        if (StringUtils.isNotEmpty(typeId)) {
            type.setId(typeId);
            menu.setMenuType(type);
        }
        else {// 默认菜单类型为“主菜单”
            type.setId(MenuConstants.MenuType.MAIN_MENU);
            menu.setMenuType(type);
        }
        menu.setSeqNum(0);
        return menu;
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (MenuHome.log.isInfoEnabled()) {
            MenuHome.log.info(MenuHome.sm.getString("log.info.deleteMenu", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        List<Menu> menus = getInstance().getChildren();
        if (menus != null && menus.size() > 0) {
            FacesUtil.addInfoMessage(MenuHome.sm.getString("canNotDeleteMenu"));
            if (MenuHome.log.isInfoEnabled()) {
                MenuHome.log.info(MenuHome.sm.getString("log.info.deleteMenuUnsucessful", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
            }
            return null;
        } else {
            return super.delete();
        }
    }

    /**
     * 通过菜单编号得到该菜单
     *
     * @param menuId
     * @return
     */
    public Menu getMenuById(final String menuId) {
        return getBaseService().get(Menu.class, menuId);
    }

    // FIXME:制定好url的规则以后，改进。
    public Map<String, String> getReferenceUrls() {
        if (referenceUrls == null) {
            referenceUrls = new LinkedHashMap<String, String>();
            referenceUrls.put(MenuHome.sm.getString("referenceUrlsLabel"), "");
            // terms
            List<CategoryTerm> terms = (List<CategoryTerm>) getBaseService().find("Select new CategoryTerm(id,name) from CategoryTerm order by name");
            for (CategoryTerm term : terms) {
                referenceUrls.put(term.getName() + ":/term/" + term.getId(), "/term/" + term.getId());
            }
        }
        return referenceUrls;
    }

    private boolean parentIsChild(String menuId, String parentId) {
        boolean result = false;
        List<Menu> menuList = (List<Menu>) ht.findByNamedQuery("Menu.findMenusByParentId", menuId);
        if (menuList != null && menuList.size() > 0) {
            for (int i = 0; i < menuList.size(); i++) {
                String childId = menuList.get(i).getId();
                if (childId.equals(parentId)) {
                    FacesUtil.addWarnMessage(MenuHome.sm.getString("childIdCanNotBeTheParent"));
                    result = true;
                    return result;
                }
                else {
                    result = parentIsChild(childId, parentId);
                    if (result) return result;
                }
            }
        }
        return result;
    }

    private boolean parentIsItself(String menuId, String parentId) {
        boolean result = false;
        if (menuId.equals(parentId)) {
            FacesUtil.addWarnMessage(MenuHome.sm.getString("parentCanNotBeItself"));
            result = true;
        }
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        // // 当前菜单的上级菜单不能是它本身
        if (parentIsItself(getInstance().getId(), getInstance().getParent().getId())) {
            return FacesUtil.redirect("/admin/menu/menuEdit?id=" + getInstance().getId() + "&typeId=" + getInstance().getMenuType().getId());
        }
        // 父菜单不能是它的子菜单
        if (parentIsChild(getInstance().getId(), getInstance().getParent().getId())) {
            return FacesUtil.redirect("/admin/menu/menuEdit?id=" + getInstance().getId() + "&typeId=" + getInstance().getMenuType().getId());
        }
        if (StringUtils.isEmpty(getInstance().getParent().getId())) {
            getInstance().setParent(null);
        }
        // if(StringUtils.isEmpty(getInstance().getPermission().getId())){
        // getInstance().setPermission(null);
        // }
        return super.save();
    }
}
