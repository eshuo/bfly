package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.UserConstants.UserPointOperateType;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserPoint;
import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

@Component
@Scope(ScopeType.VIEW)
public class UserPointList extends EntityQuery<UserPoint> implements Serializable {
    public UserPointList() {
        final String[] RESTRICTIONS = {"id like #{userPointList.example.id}", "user.username like #{userPointList.example.user.username}", "type like #{userPointList.example.type}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    /**
     * 获取用户的已使用积分
     *
     * @param userId
     * @param type   积分类型
     * @return
     */
    public Long getUsedPoints(String userId, String type) {
        String hql = "select sum(uph.point) from UserPointHistory uph where uph.type=? and uph.operateType=? and uph.user.id = ?";
        Object o = getHt().find(hql, type, UserPointOperateType.MINUS, userId).get(0);
        if (o == null) {
            return 0L;
        }
        return (Long) o;
    }

    @Override
    protected void initExample() {
        UserPoint example = new UserPoint();
        example.setUser(new User());
        setExample(example);
    }
}
