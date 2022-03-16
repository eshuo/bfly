package info.bfly.p2p.invest.controller;

import info.bfly.core.annotations.ScopeType;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 */
@Component
@Scope(ScopeType.VIEW)
public class InvestList3 extends InvestList {
    public InvestList3() {
        final String[] RESTRICTIONS = {"invest.id like #{investList3.example.id}", "invest.status like #{investList3.example.status}", "invest.loan.user.id like #{investList3.example.loan.user.id}",
                "invest.loan.id like #{investList3.example.loan.id}", "invest.loan.name like #{investList3.example.loan.name}", "invest.loan.type like #{investList3.example.loan.type}",
                "invest.user.id = #{investList3.example.user.id}", "invest.user.username = #{investList3.example.user.username}", "invest.time >= #{investList3.searchcommitMinTime}",
                "invest.status like #{investList3.example.status}", "invest.time <= #{investList3.searchcommitMaxTime}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
