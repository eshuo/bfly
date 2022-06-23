package info.bfly.archer.menu.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.common.service.BaseService;
import info.bfly.archer.menu.MenuConstants;
import info.bfly.archer.menu.model.Menu;
import info.bfly.archer.menu.model.MenuType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 菜单查询控制器
 */
@Component
@Scope(ScopeType.VIEW)
public class MenuList extends EntityQuery<Menu> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7949888911048946502L;
    @Log
    static Logger log;
    private static       StringManager sm           = StringManager.getManager(MenuConstants.Package);
    private static final String[]      RESTRICTIONS = {"id like #{menuList.example.id}", "label like #{menuList.example.label}", "url like #{menuList.example.url}",
            "menuType.id = #{menuList.example.menuType.id}"};
    @Resource
    private HibernateTemplate                         ht;
    private TreeNode                                  root;
    private String                                    menuTypeId;
    private List<info.bfly.archer.menu.model.TreeNode> menuTree;
    @Resource(name = "baseService")
    BaseService<MenuType> menuTypeService;

    public MenuList() {
        setRestrictionExpressionStrings(Arrays.asList(MenuList.RESTRICTIONS));
    }

    private void buildMenuTree(Menu parent, List<Menu> menuList) {
        for (int i = 0; i < menuList.size(); i++) {
            Menu menu = menuList.get(i);
            if (menu.getParent() != null && StringUtils.isNotEmpty(menu.getParent().getId()) && menu.getParent().getId().equals(parent.getId())
                    && !menu.getId().equals(((Menu) FacesUtil.getExpressionValue("#{menuHome.instance}")).getId())) {
                int level = 0;// 记录parent是第几级菜单
                Menu newMenu = parent.getParent();
                while (newMenu != null) {
                    level++;
                    newMenu = newMenu.getParent();
                }
                String prefixStr = "";
                int prod = (int) Math.pow(2, (level + 1));
                for (int j = 0; j < prod; j++) {
                    if (j == (prod - 1)) {
                        prefixStr = prefixStr + "&nbsp;-";
                    } else {
                        prefixStr = prefixStr + "&nbsp;";
                    }
                }
                info.bfly.archer.menu.model.TreeNode node = createNewNode(menu.getId(), prefixStr + menu.getLabel());
                menuTree.add(node);
                buildMenuTree(menu, menuList);
            }
        }
    }

    private TreeNode createNewNode(Menu menu, TreeNode parentNode) {
        TreeNode newNode = new DefaultTreeNode(menu, parentNode);
        newNode.setExpanded(true);
        return newNode;
    }

    private info.bfly.archer.menu.model.TreeNode createNewNode(String id, String label) {
        info.bfly.archer.menu.model.TreeNode node = new info.bfly.archer.menu.model.TreeNode();
        node.setId(id);
        node.setLabel(label);
        return node;
    }

    public List<Menu> getMainMenus() {
        return getMenusByType(MenuConstants.MenuType.MAIN_MENU);
    }

    public List<Menu> getManagementMenus() {
        return getMenusByType(MenuConstants.MenuType.MANAGEMENT);
    }

    /**
     * 通过菜单编号，查询该菜单下的所有子菜单（包含 enable为不可用的）
     *
     * @param parentId
     * @return
     */
    public List<Menu> getMenusByParentId(final String parentId) {
        getHt().setCacheQueries(true);
        return (List<Menu>) getHt().findByNamedQuery("Menu.findMenusByParentId", parentId);
    }

    @SuppressWarnings("unchecked")
    public List<Menu> getMenusByType(String typeId) {
        if (StringUtils.isEmpty(typeId)) {
            return null;
        }
        getHt().setCacheQueries(true);
        return (List<Menu>) getHt().findByNamedQuery("Menu.findMenusByType", typeId);
    }

    public List<info.bfly.archer.menu.model.TreeNode> getMenuTree() {
        menuTree = new ArrayList<info.bfly.archer.menu.model.TreeNode>();
        if (StringUtils.isNotEmpty(menuTypeId)) {
            FacesUtil.getViewMap().put("menuTypeId", menuTypeId);
        } else {
            menuTypeId = (String) FacesUtil.getViewMap().get("menuTypeId");
        }
        if (StringUtils.isEmpty(menuTypeId)) {
            menuTypeId = ((Menu) FacesUtil.getExpressionValue("#{menuHome.instance}")).getMenuType().getId();
            FacesUtil.getViewMap().put("menuTypeId", menuTypeId);
        }
        List<Menu> list = (List<Menu>) ht.findByNamedQuery("Menu.findMenusByTypeId", menuTypeId);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Menu menu = list.get(i);
                if (menu.getParent() == null && !menu.getId().equals(((Menu) FacesUtil.getExpressionValue("#{menuHome.instance}")).getId())) {
                    info.bfly.archer.menu.model.TreeNode node = createNewNode(menu.getId(), menu.getLabel());
                    menuTree.add(node);
                    buildMenuTree(menu, list);
                }
            }
        }
        return menuTree;
    }

    public String getMenuTypeId() {
        return menuTypeId;
    }

    public List<Menu> getNavigationMenus() {
        return getMenusByType(MenuConstants.MenuType.NAVIGATION);
    }

    /**
     * 通过url路径找到相匹配（多个相同URL同样的情况下，只取第一个结果）的菜单的根枝干菜单编号。<br/>
     * 原则： <li>1.比如 （NULL） - news(/term/news) - setup(/node/news/setup)，传入
     * “/node/news/setup” 得出 news</li> <li>
     * 2.如果是以“/node/{分类术语编号}/{文章编号}”的格式，如果为找到结果集，则试图去查找URL“/term/{分类术语编号}”</li>
     * <li>3.如果为找到URL“/term/{分类术语编号}”，则尝试查找URL“/term/{分类术语编号}%”</li>
     *
     * @param url
     * @return
     */
    public Menu getMenuByChildUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        Menu currentMenu = null;
        List<Menu> menus = (List<Menu>) getHt().findByNamedQuery("Menu.getNavMenuByUrl", url, MenuConstants.MenuType.NAVIGATION);
        if (menus.size() > 0) {
            currentMenu = menus.get(0);
        }
        // 如果符合 */*-list/page 模式，并且在菜单表中未找到该路径
        if (currentMenu == null && url.matches(".*?/([^/]+)-list/[^/]*$")) {
            String turl = url.replaceFirst("/[^/]*$", "");
            currentMenu = getMenuByChildUrl(turl);
        }
        if (currentMenu == null && url.matches(".*?/([^/]+)-page/[^/]*$")) {
            String turl = url.replaceFirst("/[^/]*$", "");
            currentMenu = getMenuByChildUrl(turl);
        }
        return currentMenu;
    }

    public List<Menu> getBrindByMenu(Menu menu) {
        if (menu == null)
            return null;

        List<Menu> brind = new ArrayList<Menu>();
        while (menu.getParent() != null) {
            brind.add(0, menu.getParent());
            menu = menu.getParent();
        }
        return brind;
    }

    public TreeNode getRoot() {
        root = new DefaultTreeNode("root", null);
        root.setExpanded(true);
        List<Menu> menuList = new ArrayList<Menu>();
        addOrder("seqNum", EntityQuery.DIR_ASC);
        menuList = (List<Menu>) ht.find(getRenderedHql(), getParameterValues());
        if (menuList != null && menuList.size() > 0) {
            for (Menu m : menuList) {
                if ((m.getParent() == null || StringUtils.isEmpty(m.getParent().getId())) || !menuList.contains(m.getParent())) {
                    TreeNode newChild = createNewNode(m, root);
                    initTreeNode(menuList, m, newChild);
                }
            }
        }
        return root;
    }

    public void handleMenuChange() {
        menuTypeId = ((Menu) FacesUtil.getExpressionValue("#{menuHome.instance}")).getMenuType().getId();
        menuTree = getMenuTree();
    }

    @Override
    public void initExample() {
        Menu menu = new Menu();
        MenuType menuType = new MenuType();
        // menuType.setId(MenuConstants.MenuType.MAIN_MENU);
        menu.setMenuType(menuType);
        setExample(menu);
    }

    private void initTreeNode(List<Menu> menuList, Menu parentMenu, TreeNode parentNode) {
        if (menuList != null && menuList.size() > 0) {
            for (Menu menu : menuList) {
                if (menu.getParent() != null && menu.getParent().getId().equals(parentMenu.getId())) {
                    TreeNode newNode = createNewNode(menu, parentNode);
                    initTreeNode(menuList, menu, newNode);
                }
            }
        }
    }

    public void setMenuTree(List<info.bfly.archer.menu.model.TreeNode> menuTree) {
        this.menuTree = menuTree;
    }

    public void setMenuTypeId(String menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
}
