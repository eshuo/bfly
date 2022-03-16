package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.RefereeModel;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.invest.InvestConstants.InvestStatus;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.primefaces.model.LazyDataModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author hch 推荐人模型控制器
 */
@Component
@Scope(ScopeType.REQUEST)
public class RefereeModelList extends EntityQuery<RefereeModel> implements Serializable {
    private static final String lazyModelCount = "select count(distinct mt.fromWhere) from MotionTracking mt,Invest invest " + "where mt.who=invest.user.id" + " and " + "invest.status in ('"
            + InvestStatus.BID_SUCCESS + "','" + InvestStatus.REPAYING + "','" + InvestStatus.OVERDUE + "','" + InvestStatus.COMPLETE + "','"
            + InvestStatus.BAD_DEBT + "')";
    // 查询数据如果为空会报错，需要修改
    private static final String lazyModel      = "select " + "new info.bfly.archer.user.model.RefereeModel(mt.fromWhere,sum(invest.money),min(invest.time),max(invest.time)) "
            + "from MotionTracking mt,Invest invest " + "where mt.who=invest.user.id and " + "invest.status in ('" + InvestStatus.BID_SUCCESS + "','"
            + InvestStatus.REPAYING + "','" + InvestStatus.OVERDUE + "','" + InvestStatus.COMPLETE + "','" + InvestStatus.BAD_DEBT + "')";
    // 查询条件 start
    private String referee;                                                                                                                                              // 推荐人
    private Date   searchCommitMinTime, searchCommitMaxTime;

    // 查询条件 end
    public RefereeModelList() {
        setCountHql(RefereeModelList.lazyModelCount);
        setHql(RefereeModelList.lazyModel);
        final String[] RESTRICTIONS = {"invest.time >= #{refereeModelList.searchCommitMinTime}", "invest.time <= #{refereeModelList.searchCommitMaxTime}", "mt.fromWhere=#{refereeModelList.referee}",
                "1=1 group by mt.fromWhere"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @Override
    public void dealResultList(List<RefereeModel> resultList) {
        LazyDataModel<RefereeModel> a = super.getLazyModel();
        super.removeRestriction("1=1 group by mt.fromWhere");
        Long count;
        if (getHt().find(getRenderedCountHql(), getParameterValues()).size() > 0) {
            count = (Long) getHt().find(getRenderedCountHql(), getParameterValues()).get(0);
        } else {
            count = 0L;
        }
        a.setRowCount(count.intValue());
    }

    @Override
    public LazyDataModel<RefereeModel> getLazyModel() {
        super.addRestriction("1=1 group by mt.fromWhere");
        return super.getLazyModel();
    }

    public String getReferee() {
        return referee;
    }

    public Date getSearchCommitMaxTime() {
        return searchCommitMaxTime;
    }

    public Date getSearchCommitMinTime() {
        return searchCommitMinTime;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public void setSearchCommitMaxTime(Date searchCommitMaxTime) {
        this.searchCommitMaxTime = searchCommitMaxTime;
    }

    public void setSearchCommitMinTime(Date searchCommitMinTime) {
        this.searchCommitMinTime = searchCommitMinTime;
    }
}
