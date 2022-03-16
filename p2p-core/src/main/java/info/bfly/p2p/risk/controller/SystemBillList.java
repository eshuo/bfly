package info.bfly.p2p.risk.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.risk.model.SystemBill;
import info.bfly.p2p.risk.service.SystemBillService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class SystemBillList extends EntityQuery<SystemBill> implements Serializable {
    @Resource
    private SystemBillService ssb;
    private Date              startTime;
    private Date              endTime;

    public SystemBillList() {
        final String[] RESTRICTIONS = {"id like #{systemBillList.example.id}", "type = #{systemBillList.example.type}", "reason = #{systemBillList.example.reason}",
                "time >= #{systemBillList.startTime}", "time <= #{systemBillList.endTime}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public double getBalance() {
        return ssb.getBalance();
    }

    public Date getEndTime() {
        return endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public double getSumInByType(String type) {
        String hql = "select sum(sb.money) from SystemBill sb where sb.type =?";
        Double sum = (Double) getHt().find(hql, type).get(0);
        if (sum == null) {
            return 0;
        }
        return sum.doubleValue();
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
