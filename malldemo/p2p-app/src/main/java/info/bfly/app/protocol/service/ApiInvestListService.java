package info.bfly.app.protocol.service;

import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.invest.controller.InvestList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
@Scope(ScopeType.REQUEST)
@Service
public class ApiInvestListService extends InvestList {

    public ApiInvestListService() {
        final String[] RESTRICTIONS = { "invest.id like #{apiInvestListService.example.id}", "invest.status like #{apiInvestListService.example.status}", "invest.loan.user.id like #{apiInvestListService.example.loan.user.id}",
                "invest.loan.id like #{apiInvestListService.example.loan.id}", "invest.loan.name like #{apiInvestListService.example.loan.name}", "invest.loan.type like #{apiInvestListService.example.loan.type}",
                "invest.user.id = #{apiInvestListService.example.user.id}", "invest.user.username = #{apiInvestListService.example.user.username}", "invest.time >= #{apiInvestListService.searchcommitMinTime}",
                "invest.status like #{apiInvestListService.example.status}", "invest.time <= #{apiInvestListService.searchcommitMaxTime}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

}
