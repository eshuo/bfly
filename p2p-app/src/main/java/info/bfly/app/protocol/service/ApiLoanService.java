package info.bfly.app.protocol.service;

import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.controller.LoanList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by XXSun on 2016/12/26.
 */
@Scope(ScopeType.REQUEST)
@Service
public class ApiLoanService extends LoanList implements Serializable {
    private static final long serialVersionUID = -974404397035429685L;
    public ApiLoanService() {
        final String[] RESTRICTIONS = {"loan.id like #{apiLoanService.example.id}", "loan.repayType like #{apiLoanService.example.repayType}", "loan.status like #{apiLoanService.example.status}",
                "loan.name like #{apiLoanService.example.name}", "loan.rate >=#{apiLoanService.minRate}", "loan.rate <=#{apiLoanService.maxRate}", "loan.status like #{apiLoanService.example.status}",
                "loan.riskLevel like #{apiLoanService.example.riskLevel}", "loan.type like #{apiLoanService.example.type}", "loan.user.id = #{apiLoanService.example.user.id}",
                "loan.user.username like #{apiLoanService.example.user.username}", "loan.businessType like #{apiLoanService.example.businessType}", "loan.projectDuration >= #{apiLoanService.minDeadline}",
                "loan.projectDuration <= #{apiLoanService.maxDeadline}", "loan.money >= #{apiLoanService.minMoney}", "loan.money <= #{apiLoanService.maxMoney}", "loan.loanMoney >= #{apiLoanService.minLoanMoney}",
                "loan.loanMoney <= #{apiLoanService.maxLoanMoney}", "loan.commitTime >= #{apiLoanService.searchCommitMinTime}", "loan.commitTime <= #{apiLoanService.searchCommitMaxTime}",
                "loan.loanPurpose like #{apiLoanService.example.loanPurpose}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
