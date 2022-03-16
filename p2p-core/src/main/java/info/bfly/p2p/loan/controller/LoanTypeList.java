package info.bfly.p2p.loan.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.LoanType;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Description: 借款类型查询相关
 */
@Component
@Scope(ScopeType.VIEW)
public class LoanTypeList extends EntityQuery<LoanType> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5724270450215898416L;

    public LoanTypeList() {
        final String[] RESTRICTIONS = {"id like #{loanTypeList.example.id}", "name like #{loanTypeList.example.name}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
