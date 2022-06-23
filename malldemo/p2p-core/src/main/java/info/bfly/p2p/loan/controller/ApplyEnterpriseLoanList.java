package info.bfly.p2p.loan.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.ApplyEnterpriseLoan;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Description: 企业借款申请List
 */
@Component
@Scope(ScopeType.VIEW)
public class ApplyEnterpriseLoanList extends EntityQuery<ApplyEnterpriseLoan> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -133753226540920852L;
    private Date searchcommitMinTime;                     // 最小申请时间
    private Date searchcommitMaxTime;                     // 最大申请时间

    public ApplyEnterpriseLoanList() {
        final String[] RESTRICTIONS = {"user.id = #{applyEnterpriseLoanList.example.user.id}", "applyEnterpriseLoan.type like #{applyEnterpriseLoanList.example.type}",
                "applyEnterpriseLoan.applyTime >= #{applyEnterpriseLoanList.searchcommitMinTime}", "applyEnterpriseLoan.applyTime <= #{applyEnterpriseLoanList.searchcommitMaxTime}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public Date getSearchcommitMaxTime() {
        return searchcommitMaxTime;
    }

    public Date getSearchcommitMinTime() {
        return searchcommitMinTime;
    }

    @Override
    protected void initExample() {
        ApplyEnterpriseLoan example = new ApplyEnterpriseLoan();
        example.setUser(new User());
        setExample(example);
    }

    public void setSearchcommitMaxTime(Date searchcommitMaxTime) {
        this.searchcommitMaxTime = searchcommitMaxTime;
    }

    public void setSearchcommitMinTime(Date searchcommitMinTime) {
        this.searchcommitMinTime = searchcommitMinTime;
    }

    /**
     * 设置查询的起始和结束时间 查询条件：申请时间
     */
    public void setSearchStartEndTime(Date startTime, Date endTime) {
        searchcommitMinTime = startTime;
        searchcommitMaxTime = endTime;
    }
}
