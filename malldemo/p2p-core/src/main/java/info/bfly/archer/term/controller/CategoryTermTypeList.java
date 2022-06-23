package info.bfly.archer.term.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.term.TermConstants;
import info.bfly.archer.term.model.CategoryTermType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 菜单类型查询
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class CategoryTermTypeList extends EntityQuery<CategoryTermType> implements Serializable {
    /**
     *
     */
    private static final long          serialVersionUID = 7451068707464166652L;
    static               StringManager sm               = StringManager.getManager(TermConstants.Package);
    @Log
    static Logger log;

    public CategoryTermTypeList() {
        final String[] RESTRICTIONS = {"id like #{categoryTermTypeList.example.id}", "name like #{categoryTermTypeList.example.name}", "description like #{categoryTermTypeList.example.description}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
