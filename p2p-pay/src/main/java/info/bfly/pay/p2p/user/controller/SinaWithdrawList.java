package info.bfly.pay.p2p.user.controller;

import info.bfly.archer.user.controller.WithdrawList;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.WithdrawCash;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/4 0004.
 */
@Component
@Scope(ScopeType.VIEW)
public class SinaWithdrawList extends WithdrawList {

    @Override
    public Class<WithdrawCash> getEntityClass() {
        return WithdrawCash.class;
    }


    public SinaWithdrawList() {
        final String[] RESTRICTIONS = {"id = #{sinaWithdrawList.example.id}", "user.username = #{sinaWithdrawList.example.user.username}", "status = #{sinaWithdrawList.example.status}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }


}
