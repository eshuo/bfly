package info.bfly.p2p.borrower.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.borrower.model.CreditRatingLog;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Component
@Scope(ScopeType.REQUEST)
public class CreditRatingLogList extends EntityQuery<CreditRatingLog> implements Serializable {
    private static final long serialVersionUID = -9149012897542553735L;
    private Date startTime;
    private Date endTime;

    public CreditRatingLogList() {
        final String[] RESTRICTIONS = {"userId like #{loanerInfoList.example.userId}", "user = #{creditRatingLogList.example.user}", "operator >= #{creditRatingLogList.example.operator}",
                "time >= #{creditRatingLogList.startTime}", "time <= #{creditRatingLogList.endTime}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public Date getEndTime() {
        return endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
