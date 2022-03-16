package info.bfly.archer.config.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.model.ConfigType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Scope(ScopeType.VIEW)
public class ConfigTypeList extends EntityQuery<ConfigType> implements java.io.Serializable {
    private static final long          serialVersionUID = 2809025189706426377L;
    static               StringManager sm               = StringManager.getManager(ConfigConstants.Package);
    @Log
    static Logger log;

    public ConfigTypeList() {
        final String[] RESTRICTIONS = {"id like #{configTypeList.example.id}", "name like #{configTypeList.example.name}", "description like #{configTypeList.example.description}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
