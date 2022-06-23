package info.bfly.archer.node.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.node.model.WordFilter;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(ScopeType.REQUEST)
public class WordFilterList extends EntityQuery<WordFilter> implements Serializable {
    @Log
    static  Logger log;
    private List<WordFilter>               wordFilters;

    public WordFilterList() {
        final String[] RESTRICTIONS = { "id like #{wordFilterList.example.id}", "word like #{wordFilterList.example.word}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public List<WordFilter> getLanguages() {
        if (wordFilters == null) {
            wordFilters = (List<WordFilter>) getHt().findByNamedQuery("WordFilter.findAllWordFilter");
        }
        return wordFilters;
    }
}
