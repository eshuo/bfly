package info.bfly.archer.items.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.items.model.SelectItemGroup;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class SelectItemGroupList extends EntityQuery<SelectItemGroup> implements Serializable {
    private static final long     serialVersionUID = 4820589213048001216L;
    private static final String[] RESTRICTIONS     = {"selectItemGroup.id like #{selectItemGroupList.example.id}", "selectItemGroup.name like #{selectItemGroupList.example.name}"};

    public SelectItemGroupList() {
        setRestrictionExpressionStrings(Arrays.asList(SelectItemGroupList.RESTRICTIONS));
    }
}
