package info.bfly.archer.system.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.system.model.Dict;
import info.bfly.core.annotations.ScopeType;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class DictList extends EntityQuery<Dict> implements java.io.Serializable {
    private static final long serialVersionUID = -7398785598777852149L;

    public DictList() {
        final String[] RESTRICTIONS = {"parent.key like #{dictList.example.parent.key}", "key like #{dictList.example.key}", "value like #{dictList.example.value}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @Override
    protected void initExample() {
        Dict example = new Dict();
        example.setParent(new Dict());
        setExample(example);
    }
}
