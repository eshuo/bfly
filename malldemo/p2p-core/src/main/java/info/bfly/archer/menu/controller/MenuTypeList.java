package info.bfly.archer.menu.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.menu.model.MenuType;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 菜单类型查询
 * 
 * @author wanghm
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class MenuTypeList extends EntityQuery<MenuType> implements java.io.Serializable {
    private static final long          serialVersionUID = 5435553236191593488L;
    static               StringManager sm               = StringManager.getManager(CommonConstants.Package);

    // private static
    public MenuTypeList() {
        final String[] RESTRICTIONS = {"id like #{menuTypeList.example.id}", "name like #{menuTypeList.example.name}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
