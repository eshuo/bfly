package info.bfly.archer.theme.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.theme.ThemeConstants;
import info.bfly.archer.theme.model.Template;
import info.bfly.archer.theme.model.Theme;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 菜单类型查询
 * 
 * @author wanghm
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class TemplateList extends EntityQuery<Template> implements java.io.Serializable {
    private static final long          serialVersionUID = -3158336515691959685L;
    static               StringManager sm               = StringManager.getManager(ThemeConstants.Package);
    @Log
    static Logger log;

    public TemplateList() {
        final String[] RESTRICTIONS = {"id like #{templateList.example.id}", "name like #{templateList.example.name}", "template.id = #{templateList.example.theme.id}",};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @Override
    protected void initExample() {
        Template template = new Template();
        template.setTheme(new Theme());
        setExample(template);
    }
}
