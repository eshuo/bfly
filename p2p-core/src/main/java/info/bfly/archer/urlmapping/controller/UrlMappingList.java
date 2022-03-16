package info.bfly.archer.urlmapping.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.urlmapping.model.UrlMapping;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

@Component
@Scope(ScopeType.REQUEST)
public class UrlMappingList extends EntityQuery<UrlMapping> implements Serializable {
    @Log
    static Logger log;

    public UrlMappingList() {
        final String[] RESTRICTIONS = {"id like #{urlMappingList.example.id}", "pattern like #{urlMappingList.example.pattern}", "viewId like #{urlMappingList.example.viewId}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
