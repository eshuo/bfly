package info.bfly.archer.theme.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.controller.EntityQuery;
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
public class ComponentList extends EntityQuery<info.bfly.archer.theme.model.Component> implements java.io.Serializable {
    private static final long          serialVersionUID = 6648384238107149229L;
    static               StringManager sm               = StringManager.getManager(CommonConstants.Package);
    @Log
    static Logger log;

    public ComponentList() {
        final String[] RESTRICTIONS = {"id like #{componentList.example.id}", "name like #{componentList.example.name}",
                // "scriptUrl like #{componentList.example.scriptUrl}",
                // "enable like #{componentList.example.enable}",
                // "description like #{componentList.example.description}",
                // "componentParameters like #{componentList.example.componentParameters}",
                // "regions like #{componentList.example.regions}",
        };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
