package info.bfly.p2p.invest.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.loan.model.Loan;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class InvestList extends EntityQuery<Invest> implements Serializable {
    private static final String lazyModelCountHql  = "select count(distinct invest) from Invest invest";
    private static final String lazyModelHql       = "select distinct invest from Invest invest";
    // private static final String lazyModelCountHql =
    // "select count(distinct invest) from Invest invest left join MotionTracking mk on invest.user.id=mk.who";
    // private static final String lazyModelHql =
    // "select distinct invest from Invest invest left join MotionTracking mk on invest.user.id=mk.who";
    // start
    // 主要为了当有推荐人的时候执行关联查询
    private static final String lazyModelCountHql1 = "select count(distinct invest) from Invest invest,MotionTracking mk where invest.user.id=mk.who";
    private static final String lazyModelHql1      = "select distinct invest from Invest invest,MotionTracking mk where invest.user.id=mk.who";
    private static final long serialVersionUID = 4349844019462223959L;

    @Log
    private static Logger log;

    private Date   searchcommitMinTime;
    private Date   searchcommitMaxTime;
    // end
    private String referee;                                                                                                              // 投资人

    public InvestList() {
        setCountHql(InvestList.lazyModelCountHql);
        setHql(InvestList.lazyModelHql);
        final String[] RESTRICTIONS = { "invest.id like #{investList.example.id}", "invest.status like #{investList.example.status}", "invest.loan.user.id like #{investList.example.loan.user.id}",
                "invest.loan.id like #{investList.example.loan.id}", "invest.loan.name like #{investList.example.loan.name}", "invest.loan.type like #{investList.example.loan.type}",
                "invest.user.id = #{investList.example.user.id}", "invest.user.username = #{investList.example.user.username}", "invest.time >= #{investList.searchcommitMinTime}",
                "invest.status like #{investList.example.status}", "invest.time <= #{investList.searchcommitMaxTime}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    /**
     * @param userId
     *            (用户id)
     * @return 获取推荐人名称
     */
    public String getFromWhere(String userId) {
        String hql = "select fromWhere from MotionTracking where who=?";
        List<String> list = (List<String>) getHt().find(hql, new Object[] { userId });
        String value = "";
        if (list != null && list.size() > 0) {
            value = list.get(0);
        }
        return value;
    }

    @Override
    public LazyDataModel<Invest> getLazyModel() {
        if (StringUtils.isNotEmpty(referee)) {
            setCountHql(InvestList.lazyModelCountHql1);
            setHql(InvestList.lazyModelHql1);
            addRestriction("mk.fromWhere=#{investList.referee}");
            log.info(InvestList.lazyModelCountHql1.toString());
        }
        return super.getLazyModel();
    }

    public String getReferee() {
        return referee;
    }

    /**
     * hch
     *
     * @param referee
     * @return 获取推荐人总投资金额
     */
    public double getRefereeInvestSum(String referee) {
        String hql = "select sum(money) from Invest where user.id in(select who from MotionTracking where fromWhere=?)";
        Double value = (Double) getHt().find(hql, new Object[] { referee }).get(0);
        if (value == null) {
            value = (double) 0;
        }
        return value;
    }

    public Date getSearchcommitMaxTime() {
        return searchcommitMaxTime;
    }

    public Date getSearchcommitMinTime() {
        return searchcommitMinTime;
    }

    @Override
    protected void initExample() {
        Invest example = new Invest();
        Loan loan = new Loan();
        loan.setUser(new User());
        example.setUser(new User());
        example.setLoan(loan);
        setExample(example);
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public void setSearchcommitMaxTime(Date searchcommitMaxTime) {
        this.searchcommitMaxTime = searchcommitMaxTime;
    }

    public void setSearchcommitMinTime(Date searchcommitMinTime) {
        this.searchcommitMinTime = searchcommitMinTime;
    }

    /**
     * 设置查询的起始和结束时间
     */
    public void setSearchStartEndTime(Date startTime, Date endTime) {
        searchcommitMinTime = startTime;
        searchcommitMaxTime = endTime;
    }
}
