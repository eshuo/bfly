package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserPointHistory;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.DateUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class UserPointHistoryList extends EntityQuery<UserPointHistory> implements Serializable {
    private Date startTime;
    private Date endTime;

    public UserPointHistoryList() {
        final String[] RESTRICTIONS = {"userPointHistory.id like #{userPointHistoryList.example.id}", "userPointHistory.operateType like #{userPointHistoryList.example.operateType}",
                "userPointHistory.time >= #{userPointHistoryList.startTime}", "userPointHistory.time <= #{userPointHistoryList.endTime}",
                "userPointHistory.user.id like #{userPointHistoryList.example.user.id}", "userPointHistory.type like #{userPointHistoryList.example.type}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public Date getEndTime() {
        return endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param value
     * @return 为了获取字符串的长度，为了方便页面调用
     * @author hch
     */
    public int getStrLength(String value) {
        if (value != null) {
            return value.length();
        } else {
            return 0;
        }
    }

    /**
     * @param type
     * @return 根据积分类型找到对应的积分类型名称
     * @author hch
     */
    public String getTypeName(String type) {
        return UserConstants.UserPointType.historyTypeMap.get(type);
    }

    /**
     * @return 根据指定用户获取总积分
     * @author hch
     */
    public long getUserSumPoint(String id) {
        if (id != null && id.length() > 0) {
            StringBuilder sql = new StringBuilder("select sum(h.point) from UserPointHistory h where h.user.id=?");
            Long sum = (Long) getHt().find(sql.toString(), id).get(0);
            if (sum != null) {
                return sum;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    protected void initExample() {
        UserPointHistory example = new UserPointHistory();
        example.setUser(new User());
        setExample(example);
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 设置查询的起始和结束时间
     */
    public void setSearchStartEndTime(Date startTime, Date endTime) {
        this.startTime = startTime;
        if (endTime != null) {
            this.endTime = DateUtil.addDay(endTime, 1);
        }
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
