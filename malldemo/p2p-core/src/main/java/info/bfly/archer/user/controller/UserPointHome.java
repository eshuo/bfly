package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserPoint;
import info.bfly.archer.user.model.UserPointHistory;
import info.bfly.archer.user.service.UserPointService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Scope(ScopeType.VIEW)
public class UserPointHome extends EntityHome<UserPoint> implements Serializable {
    @Log
    private static Logger log;
    @Resource
    private        UserPointService               userPointService;
    @Resource
    private        LoginUserInfo                  loginUserInfo;
    private        String                         operatorType;
    private        UserPointHistory               userPointHistory; // 用户积分历史

    public UserPointHome() {
        userPointHistory = new UserPointHistory();
    }

    /**
     * 管理员从后台给用户增加积分操作
     *
     * @return
     */
    public String addPointByAdmin() {
        if (UserPointHome.log.isInfoEnabled()) UserPointHome.log.info(userPointHistory.getType().concat(",").concat(userPointHistory.getRemark()));
        userPointService.add(getInstance().getUser().getId(), userPointHistory.getPoint(), userPointHistory.getType(), userPointHistory.getType(), userPointHistory.getRemark());
        FacesUtil.addInfoMessage("增加积分成功！");
        return FacesUtil.redirect(UserConstants.View.POINT_HISTORY_LIST);
    }

    @Override
    protected UserPoint createInstance() {
        UserPoint point = new UserPoint();
        point.setUser(new User());
        return point;
    }

    public String getOperatorType() {
        return operatorType;
    }

    /**
     * 根据用户的编号和类型来获取用户积分
     *
     * @param type （升级积分、消费积分）
     * @return 用户积分对象
     */
    public UserPoint getPointByUserId(String type) {
        return userPointService.getPointByUserId(loginUserInfo.getLoginUserId(), type);
    }

    public UserPointHistory getUserPointHistory() {
        return userPointHistory;
    }

    /**
     * 管理员从后台减少用户的积分操作
     *
     * @return
     */
    public String minusPointByAdmin() {
        if (UserPointHome.log.isInfoEnabled()) UserPointHome.log.info(userPointHistory.getType().concat(",").concat(userPointHistory.getRemark()));
        try {
            userPointService.minus(getInstance().getUser().getId(), userPointHistory.getPoint(), userPointHistory.getType(), userPointHistory.getType(), userPointHistory.getRemark());
            FacesUtil.addInfoMessage("减少积分成功！");
        } catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage("积分不足！");
        }
        return FacesUtil.redirect(UserConstants.View.POINT_HISTORY_LIST);
    }

    public String modifyPointByAdmin() {
        if (StringUtils.isEmpty(getInstance().getUser().getId())) {
            FacesUtil.addErrorMessage("用户编号不能为空！");
            return null;
        }
        if (StringUtils.equals(operatorType, "ADD")) {// FIXME：改为系统可以配置
            return addPointByAdmin();
        } else if (StringUtils.equals(operatorType, "MINUS")) {
            return minusPointByAdmin();
        } else {
            FacesUtil.addErrorMessage("修改用户积分时，未知的操作类型，操作类型应该为增加或者减少");
            return null;
        }
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public void setUserPointHistory(UserPointHistory userPointHistory) {
        this.userPointHistory = userPointHistory;
    }
}
