package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.UserLoginLog;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Component
@Scope(ScopeType.VIEW)
public class UserLoginLogList extends EntityQuery<UserLoginLog> implements Serializable {
    private static final long          serialVersionUID = 3019489287089714933L;
    private static       StringManager sm               = StringManager.getManager(UserConstants.Package);
    @Log
    private static Logger log;
    private        Date                           loginTimeStart;
    private        Date                           loginTimeEnd;

    public UserLoginLogList() {
        final String[] RESTRICTIONS = {"username like #{userLoginLogList.example.username}", "loginIp like #{userLoginLogList.example.loginIp}", "isSuccess = #{userLoginLogList.example.isSuccess}",
                "loginTime >= #{userLoginLogList.loginTimeStart}", "loginTime <= #{userLoginLogList.loginTimeEnd}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public Date getLoginTimeEnd() {
        return loginTimeEnd;
    }

    public Date getLoginTimeStart() {
        return loginTimeStart;
    }

    public void setLoginTimeEnd(Date loginTimeEnd) {
        this.loginTimeEnd = loginTimeEnd;
    }

    public void setLoginTimeStart(Date loginTimeStart) {
        this.loginTimeStart = loginTimeStart;
    }
}
