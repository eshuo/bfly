package info.bfly.archer.system.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.system.model.MotionTracking;
import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@Scope(ScopeType.REQUEST)
public class MotionTrackingList extends EntityQuery<MotionTracking> implements Serializable {
    public List<MotionTracking> getMotionTrackingsByWhoFromType(String who, String fromType) {
        return (List<MotionTracking>) getHt().find("from MotionTracking mt where mt.who = ? and mt.fromType = ?",  who, fromType );
    }
}
