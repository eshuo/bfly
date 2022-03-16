package info.bfly.archer.theme.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.theme.model.ComponentParameter;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
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
public class ComponentParameterList extends EntityQuery<ComponentParameter> implements java.io.Serializable {
    private static final long          serialVersionUID = 7972343757104858835L;
    static               StringManager sm               = StringManager.getManager(CommonConstants.Package);
    @Log
    static Logger log;

    public ComponentParameterList() {
        /*
         * final String[] RESTRICTIONS =
         * {"id like #{componentParameterList.example.id}",
         * "component like #{componentParameterList.example.component}",
         * "name like #{componentParameterList.example.name}",
         * "value like #{componentParameterList.example.value}",
         * "description like #{componentParameterList.example.description}", };
         * 
         * setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
         */
    }
}
