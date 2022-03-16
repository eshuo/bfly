package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.Role;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class RoleList extends EntityQuery<Role> implements Serializable {
    private static final long serialVersionUID = 7098372098005574338L;
    private List<Role> rolesExceptInvestorLoaner;

    public RoleList() {
        final String[] RESTRICTIONS = {"id like #{roleList.example.id}", "name like #{roleList.example.name}",};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public List<Role> getRolesExceptInvestorLoaner() {
        if (rolesExceptInvestorLoaner == null) {
            rolesExceptInvestorLoaner = (List<Role>) getHt().find("from Role role where role.id!='INVESTOR' and role.id!='LOANER'");
        }
        return rolesExceptInvestorLoaner;
    }
}
