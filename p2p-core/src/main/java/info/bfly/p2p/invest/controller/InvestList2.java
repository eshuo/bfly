package info.bfly.p2p.invest.controller;

import info.bfly.core.annotations.ScopeType;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 */
@Component
@Scope(ScopeType.VIEW)
public class InvestList2 extends InvestList {
    public InvestList2() {
        final String[] RESTRICTIONS = {"invest.id like #{investList2.example.id}", "invest.status like #{investList2.example.status}", "invest.loan.user.id like #{investList2.example.loan.user.id}",
                "invest.loan.id like #{investList2.example.loan.id}", "invest.loan.name like #{investList2.example.loan.name}", "invest.loan.type like #{investList2.example.loan.type}",
                "invest.user.id = #{investList2.example.user.id}", "invest.user.username = #{investList2.example.user.username}", "invest.time >= #{investList2.searchcommitMinTime}",
                "invest.status like #{investList2.example.status}", "invest.time <= #{investList2.searchcommitMaxTime}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
