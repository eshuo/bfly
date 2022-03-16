package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 推荐人相关用户模型控制器
 *
 * @author hch
 */
@Component
@Scope(ScopeType.REQUEST)
public class RefereeUserList extends EntityQuery<User> implements Serializable {
    private static final String lazyModelCount = "select count(user.id) from User user,MotionTracking mt";
    private static final String lazyModel      = "select user from User user,MotionTracking mt";
    private String referee;

    public RefereeUserList() {
        setCountHql(RefereeUserList.lazyModelCount);
        setHql(RefereeUserList.lazyModel);
        final String[] RESTRICTIONS = {"mt.fromWhere=#{refereeUserList.referee}", "user.id=mt.who"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }
}
