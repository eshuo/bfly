package info.bfly.app.protocol.service;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.WithdrawCash;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
@Service
@Scope(ScopeType.REQUEST)
public class ApiSinaWithdrawList extends EntityQuery<WithdrawCash> {

    public ApiSinaWithdrawList() {
        final String[] RESTRICTIONS = {"id = #{apiSinaWithdrawList.example.id}", "user.username = #{apiSinaWithdrawList.example.user.username}", "status = #{apiSinaWithdrawList.example.status}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

}
