package info.bfly.p2p.loan.controller;

import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Description:
 */
@Component
@Scope(ScopeType.VIEW)
public class LoanList2 extends LoanList implements Serializable {
    private static final long serialVersionUID = 2923484560269695636L;

    public LoanList2() {
        final String[] RESTRICTIONS = {"loan.id like #{loanList2.example.id}", "loan.repayType like #{loanList2.example.repayType}", "loan.status like #{loanList2.example.status}",
                "loan.name like #{loanList2.example.name}", "loan.rate >=#{loanList2.minRate}", "loan.rate <=#{loanList2.maxRate}", "loan.status like #{loanList2.example.status}",
                "loan.riskLevel like #{loanList2.example.riskLevel}", "loan.type like #{loanList2.example.type}", "loan.user.id = #{loanList2.example.user.id}",
                "loan.user.username like #{loanList2.example.user.username}", "loan.businessType like #{loanList2.example.businessType}", "loan.projectDuration >= #{loanList2.minDeadline}",
                "loan.projectDuration <= #{loanList2.maxDeadline}", "loan.money >= #{loanList2.minMoney}", "loan.money <= #{loanList2.maxMoney}", "loan.loanMoney >= #{loanList2.minLoanMoney}",
                "loan.loanMoney <= #{loanList2.maxLoanMoney}", "loan.commitTime >= #{loanList2.searchCommitMinTime}", "loan.commitTime <= #{loanList2.searchCommitMaxTime}",
                "loan.loanPurpose like #{loanList2.example.loanPurpose}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
