package info.bfly.p2p.loan.controller;

import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Description:
 */
@Component
@Scope(ScopeType.VIEW)
public class LoanList3 extends LoanList {
    private static final long serialVersionUID = 1872875245507457728L;

    public LoanList3() {
        final String[] RESTRICTIONS = {"loan.id like #{loanList3.example.id}", "loan.repayType like #{loanList3.example.repayType}", "loan.status like #{loanList3.example.status}",
                "loan.name like #{loanList3.example.name}", "loan.rate >=#{loanList3.minRate}", "loan.rate <=#{loanList3.maxRate}", "loan.status like #{loanList3.example.status}",
                "loan.riskLevel like #{loanList3.example.riskLevel}", "loan.type like #{loanList3.example.type}", "loan.user.id = #{loanList3.example.user.id}",
                "loan.user.username like #{loanList3.example.user.username}", "loan.businessType like #{loanList3.example.businessType}", "loan.projectDuration >= #{loanList3.minDeadline}",
                "loan.projectDuration <= #{loanList3.maxDeadline}", "loan.money >= #{loanList3.minMoney}", "loan.money <= #{loanList3.maxMoney}", "loan.loanMoney >= #{loanList3.minLoanMoney}",
                "loan.loanMoney <= #{loanList3.maxLoanMoney}", "loan.commitTime >= #{loanList3.searchCommitMinTime}", "loan.commitTime <= #{loanList3.searchCommitMaxTime}",
                "loan.loanPurpose like #{loanList3.example.loanPurpose}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
