package info.bfly.p2p.repay.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.repay.model.LoanRepay;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Component
@Scope(ScopeType.VIEW)
public class LoanRepayList extends EntityQuery<LoanRepay> implements Serializable {
    @Log
    static  Logger log;
    private Date                           searchMinTime;
    private Date                           searchMaxTime;

    public LoanRepayList() {
        final String[] RESTRICTIONS = {"id like #{loanRepayList.example.id}", "loan.id like #{loanRepayList.example.loan.id}", "loan.user.id = #{loanRepayList.example.loan.user.id}",
                "repayDay >= #{loanRepayList.searchMinTime}", "repayDay <= #{loanRepayList.searchMaxTime}", "status like #{loanRepayList.example.status}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public Date getSearchMaxTime() {
        return searchMaxTime;
    }

    public Date getSearchMinTime() {
        return searchMinTime;
    }

    @Override
    protected void initExample() {
        LoanRepay example = new LoanRepay();
        Loan loan = new Loan();
        loan.setUser(new User());
        example.setLoan(loan);
        setExample(example);
    }

    public void setSearchMaxTime(Date searchMaxTime) {
        this.searchMaxTime = searchMaxTime;
    }

    public void setSearchMinTime(Date searchMinTime) {
        this.searchMinTime = searchMinTime;
    }
}
