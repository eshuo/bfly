package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.LevelForUser;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class LevelList extends EntityQuery<LevelForUser> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6423642197988808883L;

    public LevelList() {
        final String[] RESTRICTIONS = {"level like #{levelList.example.level}", "name like #{levelList.example.name}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
