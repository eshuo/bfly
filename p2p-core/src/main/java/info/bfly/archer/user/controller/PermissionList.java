package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.Permission;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class PermissionList extends EntityQuery<Permission> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2062362439316134859L;

    public PermissionList() {
        final String[] RESTRICTIONS = {"id like #{permissionList.example.id}", "name like #{permissionList.example.name}",};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
