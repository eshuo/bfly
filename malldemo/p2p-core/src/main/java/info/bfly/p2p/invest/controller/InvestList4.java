package info.bfly.p2p.invest.controller;

import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 */
@Component
@Scope(ScopeType.VIEW)
public class InvestList4 extends InvestList {
    public InvestList4() {
        final String[] RESTRICTIONS = {"invest.id like #{investList4.example.id}", "invest.status like #{investList4.example.status}", "invest.loan.user.id like #{investList4.example.loan.user.id}",
                "invest.loan.id like #{investList4.example.loan.id}", "invest.loan.name like #{investList4.example.loan.name}", "invest.loan.type like #{investList4.example.loan.type}",
                "invest.user.id = #{investList4.example.user.id}", "invest.user.username = #{investList4.example.user.username}", "invest.time >= #{investList4.searchcommitMinTime}",
                "invest.status like #{investList4.example.status}", "invest.time <= #{investList4.searchcommitMaxTime}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
