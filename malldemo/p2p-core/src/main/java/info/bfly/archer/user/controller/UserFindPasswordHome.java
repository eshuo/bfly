package info.bfly.archer.user.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.exception.*;
import info.bfly.archer.common.service.AuthService;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserInfoService;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Description: 用户找回密码
 */
@Component
@Scope(ScopeType.VIEW)
@Deprecated
public class UserFindPasswordHome implements Serializable {
    private static final long serialVersionUID = 8070682775479496429L;
    @Log
    static Logger log;
    private static StringManager sm = StringManager.getManager(UserConstants.Package);
    // 认证码
    private String authCode;
    // 新密码
    private String newPassword;
    // 新提现密码
    private String newCashPwd;
    // 用于找回密码的邮箱
    private String email;
    private boolean showResetPassword = false;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserService     userService;
    @Resource
    private AuthService     authService;
    @Resource
    private LoginUserInfo   loginUserInfo;

    /**
     * 通过邮箱找回密码
     *
     * @return
     */
    public String findPwdByEmail() {
        if (!userInfoService.isEmailExist(email)) {
            String message = "对不起，邮箱验证失败！" + getEmail() + "尚未注册";
            FacesUtil.addErrorMessage(message);
            return null;
        }
        // 发送找回密码的邮件
        try {
            userService.sendFindLoginPasswordEmail(email);
            FacesUtil.addInfoMessage("验证码已发送到你邮箱。");
        } catch (UserNotFoundException e) {
            FacesUtil.addInfoMessage("未找到该邮箱。");
        }
        catch (AuthInfoAlreadyInColdException e) {
            FacesUtil.addErrorMessage("请求过于频繁");
        }
        FacesUtil.setSessionAttribute("confirmEmail", email);
        return FacesUtil.getThemePath() + "findPwdbyMailCode";
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getEmail() {
        return email;
    }

    public String getNewCashPwd() {
        return newCashPwd;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public boolean isShowResetPassword() {
        return showResetPassword;
    }

    /**
     * 根据邮箱修改密码
     *
     * @return
     */
    public String modifyPasswordByEmail() {
        try {
            User user = userService.getUserByEmail(email);
            userService.modifyPassword(user.getId(), newPassword);
            FacesUtil.addInfoMessage("修改密码成功！");
            return "pretty:memberLogin";
        } catch (UserNotFoundException e) {
            log.debug(e.getMessage());
            return "pretty:findPwdFail";
        }
    }

    /**
     * 重新发送邮件
     *
     * @return
     */
    public String resendEmail() {
        try {
            userService.sendFindLoginPasswordEmail(email);
        } catch (UserNotFoundException e) {
            log.debug(e.getMessage());
        } catch (AuthInfoAlreadyInColdException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNewCashPwd(String newCashPwd) {
        this.newCashPwd = newCashPwd;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setShowResetPassword(boolean showResetPassword) {
        this.showResetPassword = showResetPassword;
    }

    /**
     * 检验认证码
     *
     * @return
     */
    public void verifyAuthInfo() {
        try {
            User user = userService.getUserByEmail(email);
            authService.verifyAuthInfo(user.getId(), email, authCode, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_EMAIL);
            showResetPassword = true;
        } catch (AuthInfoOutOfDateException|AuthInfoOutOfTimesException e) {
            FacesUtil.addErrorMessage("认证码已过期！");
        } catch (AuthCodeNotMatchException e) {
            FacesUtil.addErrorMessage("认证码错误！");
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage(UserConstants.Errormsg.USERVALIDATECODEERRORMSG);
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户不存在！");
        } catch (AuthInfoAlreadyActivedException e) {
            FacesUtil.addErrorMessage("认证码已激活！");
        }
    }
}
