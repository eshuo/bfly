package info.bfly.archer.system.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.system.model.EntityGroupBy;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 动作追踪，根据某一项groupBy查询
 *
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class MotionTrackingGroupByList extends EntityQuery<EntityGroupBy> implements Serializable {
    @Log
    static Logger log;
    private static final String[] RESTRICTIONS = {"motionTracking.fromType = #{motionTrackingGroupByList.example.fromType}"};
    private String groupByField;

    public String getGroupByField() {
        return groupByField;
    }

    public void init() {
        if (StringUtils.isEmpty(groupByField)) {
            throw new RuntimeException("groupByField is empty");
        }
        String lazyModelCountHql = "select count(motionTracking) from MotionTracking motionTracking group by motionTracking." + groupByField;
        String lazyModelHql = "select " + "new info.bfly.archer.system.model.EntityGroupBy" + "(" + "'info.bfly.archer.system.model.MotionTracking'," + "'" + groupByField + "'," + "count(mt.id),"
                + "null" + ")" + " from MotionTracking motionTracking group by motionTracking." + groupByField;
        setCountHql(lazyModelCountHql);
        setHql(lazyModelHql);
        setRestrictionExpressionStrings(Arrays.asList(MotionTrackingGroupByList.RESTRICTIONS));
    }

    public void setGroupByField(String groupByField) {
        this.groupByField = groupByField;
    }
}
