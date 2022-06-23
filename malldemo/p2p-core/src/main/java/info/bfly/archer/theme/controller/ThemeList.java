package info.bfly.archer.theme.controller;

import info.bfly.archer.theme.ThemeConstants;
import info.bfly.archer.theme.model.Theme;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单类型查询
 *
 * @author wanghm
 *
 */
@Component
@Scope(ScopeType.REQUEST)
public class ThemeList {
    // private static final long serialVersionUID = 1697137990909862041L;
    static StringManager sm = StringManager.getManager(ThemeConstants.Package);
    @Log
    static Logger log;
    @Resource
    HibernateTemplate    ht;
    private List<Theme> allThemes;

    public ThemeList() {
        /*
         * final String[] RESTRICTIONS = {"id like #{themeList.example.id}",
         * "name like #{themeList.example.name}",
         * "screenshotUri like #{themeList.example.screenshotUri}",
         * "description like #{themeList.example.description}",
         * "templates like #{themeList.example.templates}", };
         *
         * setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
         */
    }

    @SuppressWarnings("unchecked")
    public List<Theme> getAllThemes() {
        if (allThemes == null) {
            allThemes = (List<Theme>) ht.findByNamedQuery("Theme.findAllOrderByStatus");
        }
        return allThemes;
    }
}
