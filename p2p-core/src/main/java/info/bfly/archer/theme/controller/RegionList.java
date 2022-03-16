package info.bfly.archer.theme.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.theme.model.Region;
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
public class RegionList extends EntityQuery<Region> implements java.io.Serializable {
    private static final long          serialVersionUID = 3069029265084507807L;
    static               StringManager sm               = StringManager.getManager(CommonConstants.Package);
    @Log
    static Logger log;

    public RegionList() {
        final String[] RESTRICTIONS = {"id like #{regionList.example.id}", "title like #{regionList.example.title}",
                // "description like #{regionList.example.description}",
                // "templates like #{regionList.example.templates}",
                // "components like #{regionList.example.components}",
        };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
