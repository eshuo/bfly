package info.bfly.archer.items.controller;

// default package

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.items.model.SelectItem;
import info.bfly.archer.items.model.SelectItemGroup;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.REQUEST)
public class SelectItemList extends EntityQuery<SelectItem> implements Serializable {
    private static final String[] RESTRICTIONS = {"selectItem.id like #{selectItemList.example.id}", "selectItem.name like #{selectItemList.example.name}",
            "selectItem.selectItemGroup.id like #{selectItemList.example.selectItemGroup.id}"};

    public SelectItemList() {
        setRestrictionExpressionStrings(Arrays.asList(SelectItemList.RESTRICTIONS));
    }

    @Override
    protected void initExample() {
        SelectItem selectItem = new SelectItem();
        selectItem.setSelectItemGroup(new SelectItemGroup());
        setExample(selectItem);
    }
}
