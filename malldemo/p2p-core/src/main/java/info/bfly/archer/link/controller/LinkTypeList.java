package info.bfly.archer.link.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.link.model.LinkType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

@Component
@Scope(ScopeType.VIEW)
public class LinkTypeList extends EntityQuery<LinkType> implements Serializable {
    @Log
    static Logger log;

    public LinkTypeList() {
        final String[] RESTRICTIONS = {"id like #{linkTypeList.example.id}", "name like #{linkTypeList.example.name}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
