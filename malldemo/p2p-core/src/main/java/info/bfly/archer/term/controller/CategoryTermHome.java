package info.bfly.archer.term.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.menu.controller.MenuHome;
import info.bfly.archer.menu.model.Menu;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.term.TermConstants;
import info.bfly.archer.term.model.CategoryTerm;
import info.bfly.archer.theme.controller.TplVars;
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
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Component
@Scope(ScopeType.VIEW)
public class CategoryTermHome extends EntityHome<CategoryTerm> implements Serializable {
    private static final long serialVersionUID = -8640434280029291362L;
    @Log
    static Logger log;
    private static final StringManager sm = StringManager.getManager(TermConstants.Package);
    @Resource
    HibernateTemplate ht;
    private String id;
    private String Name1;
    private String keywords;
    private String description1;
    private String title;
    public StringBuffer navStringAll = new StringBuffer();
    @Resource
    private MenuHome menuHome;
    @Resource
    private TplVars  tplVars;
    private String       navTitle          = "";
    private String       navId             = "";
    private Menu         menu              = null;
    public  StringBuffer SEOStringAll      = new StringBuffer();
    public  StringBuffer SEOKeywordsAll    = new StringBuffer();
    public  StringBuffer SEODescpritionAll = new StringBuffer();

    public CategoryTermHome() {
        setUpdateView(FacesUtil.redirect(TermConstants.View.TERM_LIST));
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        Set<Node> nodeSets = getInstance().getNodes();
        List<CategoryTerm> ct = getInstance().getChildren();
        if ((nodeSets != null && nodeSets.size() > 0) || ct.size() > 0) {
            FacesUtil.addErrorMessage("删除失败，请先删除该分类下的所有分类或文章。");
            return null;
        } else {
            return super.delete();
        }
    }

    private void fillHref(StringBuffer navStringAll, CategoryTerm cat) {
        String url = "";
        if (null != cat && null != navStringAll) {
            Menu menu = menuHome.getMenuById(cat.getId());
            if (null != menu) {
                url = menu.getUrl();
                if (!StringUtils.isBlank(url)) {
                    navStringAll.insert(0, "<a href='" + getComponentsPath() + url + "'>" + cat.getName() + "</a>>>");
                }
            }
        }
    }

    private String getComponentsPath() {
        return tplVars.getContextPath();
    }

    public String getDescription1() {
        return description1;
    }


    @Override
    public String getId() {
        return id;
    }

    public String getMenuParentId() {
        String menuParent = "";
        CategoryTerm cat = getInstance();
        if (null != cat) {
            Menu menu = menuHome.getMenuById(cat.getId());
            if (null != menu) {
                Menu parent = menu.getParent();
                if (null != parent) {
                    menuParent = parent.getId();
                } else {
                    menuParent = menu.getId();
                }
            }
        }
        return menuParent;
    }

    public String getName1() {
        return Name1;
    }

    public String getNowTitle(CategoryTerm cat) {
        if (null != cat) {
            if (null != cat.getParent()) {
                fillHref(navStringAll, cat);
                getNowTitle(cat.getParent());
            } else {
                fillHref(navStringAll, cat);
            }
        }
        // System.out.println(navStringAll);
        return navStringAll.toString();
    }

    public String getParentTile() {
        String parentTile = "";
        CategoryTerm cat = getInstance();
        if (null != cat) {
            if (null != cat.getParent()) {
                parentTile = cat.getParent().getName();
            } else {
                parentTile = cat.getName();
            }
        }
        // System.out.println("parentTile:" + parentTile);
        return parentTile;
    }

    public String getRootTile() {
        String rootTile = "";
        CategoryTerm cat = getInstance();
        if (null != cat) {
            rootTile = getNowTitle(cat);
            // System.out.println(rootTile);
        }
        return rootTile;
    }

    public String getSEOdescriptionString(CategoryTerm cat) {
        if (null != cat) {
            if (null != cat.getParent()) {
                plusSEOString(SEODescpritionAll, cat, TermConstants.View.CONNECTOR_DESCRIPTION);
                getSEOdescriptionString(cat.getParent());
            } else {
                plusSEOString(SEODescpritionAll, cat, TermConstants.View.CONNECTOR_DESCRIPTION);
            }
        }
        return SEODescpritionAll.toString();
    }

    public String getSEOkeywordsString(CategoryTerm cat) {
        if (null != cat) {
            if (null != cat.getParent()) {
                plusSEOString(SEOKeywordsAll, cat, TermConstants.View.CONNECTOR_KEYWORDS);
                getSEOkeywordsString(cat.getParent());
            } else {
                plusSEOString(SEOKeywordsAll, cat, TermConstants.View.CONNECTOR_KEYWORDS);
            }
        }
        return SEOKeywordsAll.toString();
    }

    public String getSEOTitleString(CategoryTerm cat) {
        if (null != cat) {
            if (null != cat.getParent()) {
                plusSEOString(SEOStringAll, cat, TermConstants.View.CONNECTOR_TITLE);
                getSEOTitleString(cat.getParent());
                // System.out.println(cat.getDescription());
            } else {
                plusSEOString(SEOStringAll, cat, TermConstants.View.CONNECTOR_TITLE);
            }
        }
        return SEOStringAll.toString();
    }

    public CategoryTerm getTermById(String id) {
        CategoryTerm instance = getBaseService().get(getEntityClass(), id);
        return instance;
    }

    private void plusSEOString(StringBuffer navStringAll, CategoryTerm cat, String connector) {
        if (null != cat && null != navStringAll) {
            navStringAll.append(cat.getName() + connector);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        // 判断父菜单是否是自己本身
        boolean parentIsOneself = false;
        CategoryTerm term = getInstance();
        while (term.getParent() != null) {
            if (StringUtils.equals(getInstance().getId(), term.getParent().getId())) {
                parentIsOneself = true;
                break;
            }
            term = term.getParent();
        }
        if (parentIsOneself) {
            FacesUtil.addWarnMessage(CategoryTermHome.sm.getString("parentCanNotBeItself"));
            return null;
        }
        return super.save();
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setName1(String name1) {
        Name1 = name1;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
