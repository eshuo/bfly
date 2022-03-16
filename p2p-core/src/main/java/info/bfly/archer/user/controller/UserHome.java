package info.bfly.archer.user.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.common.exception.*;
import info.bfly.archer.common.model.AuthInfo;
import info.bfly.archer.common.service.AuthService;
import info.bfly.archer.common.service.CaptchaService;
import info.bfly.archer.openauth.OpenAuthConstants;
import info.bfly.archer.openauth.service.OpenAuthService;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.system.model.IpsMapService;
import info.bfly.archer.system.service.SpringSecurityService;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.exception.ConfigNotFoundException;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.AdverLeague;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.AdverService;
import info.bfly.archer.user.service.UserService;
import info.bfly.archer.user.service.impl.UserBO;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.HashCrypt;
import info.bfly.core.util.ImageUploadUtil;
import info.bfly.core.util.SpringBeanUtil;
import info.bfly.core.util.StringManager;
import info.bfly.p2p.message.exception.SmsSendErrorException;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Component
@Scope(ScopeType.VIEW)
public class UserHome extends EntityHome<User> implements Serializable {
    private static final long serialVersionUID = 1296162388441087436L;
    @Log
    static Logger log;
    private static StringManager sm = StringManager.getManager(UserConstants.Package);
    @Resource
    private UserService userService;
    @Resource
    private AuthService authService;
    @Resource
    private LoginUserInfo loginUser;
    @Resource
    private UserBO userBO;
    // 广告联盟
    @Resource
    private AdverService adverService;
    @Resource
    private IpsMapService ipsMapService;
    @Resource
    private SpringSecurityService ssService;

    @Resource
    private CaptchaService captchaService;
    @Resource(name = "webAuthenticationManager")
    protected AuthenticationManager authenticationManager;
    @Resource
    private HttpServletRequest request;
    /**
     * 推荐人
     */
    private String referrer;
    private String mid; // 联盟ID
    private String uid; // 联盟用户ID
    // 验证认证码是否正确
    private boolean correctAuthCode = false;
    // 认证码
    private String authCode;
    // 认证码
    private String captchaAuthCode;
    // 实名认证绑定所要手机号
    private String mobileNumber;
    // 旧密码
    private String oldPassword;
    // 旧交易密码
    private String oldCashPassword;
    // 新邮箱
    private String newEmail;
    // 新手机号
    private String newMobileNumber;
    // 通过实名认证
    private Boolean ispass;
    //显示（导航找回密码页面）
    private String showType = "1";

    /**
     * 更改绑定邮箱第二步 验证认证码并更改绑定邮箱
     *
     * @return
     */
    public String changeBindingEmail() {
        User user;
        try {
            user = userService.getUserById(loginUser.getLoginUserId());
            userService.bindingEmail(user.getId(), newEmail, authCode);
            correctAuthCode = true;
        } catch (AuthInfoOutOfDateException | AuthInfoOutOfTimesException e) {
            FacesUtil.addErrorMessage("认证码已过期！");
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录");
            log.debug(e.getMessage());
        } catch (AuthCodeNotMatchException e) {
            FacesUtil.addErrorMessage("认证码错误！");
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
        } catch (AuthInfoAlreadyActivedException e) {
            FacesUtil.addErrorMessage("认证码已激活！");
        }
        return null;
    }

    /**
     * 更改绑定手机第二步 验证认证码并更改绑定手机
     *
     * @return
     */
    public String changeBindingMobileNumber() {
        User user;
        try {
            user = userService.getUserById(loginUser.getLoginUserId());
            userService.bindingMobileNumber(user.getId(), newMobileNumber, authCode);
            correctAuthCode = true;
        } catch (AuthInfoOutOfDateException | AuthInfoOutOfTimesException e) {
            FacesUtil.addErrorMessage("认证码已过期！");
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录");
            log.debug(e.getMessage());
        } catch (AuthCodeNotMatchException e) {
            FacesUtil.addErrorMessage("认证码错误！");
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage("输入认证码错误，请重新输入！");
        } catch (AuthInfoAlreadyActivedException e) {
            FacesUtil.addErrorMessage("认证码已激活！");
        }
        return null;
    }

    /**
     * 第二部： 检查发送的修改密码验证码是否和数据库收到的验证码一致
     *
     * @param authCode     验证码
     * @param mobileNumber 手机号
     * @return
     * @author liuchun
     */
    @Transactional
    public String checkAuthCode(String authCode, String mobileNumber) {
        String hql = "from AuthInfo ai where ai.authCode =? and ai.authTarget=?";
        ArrayList<AuthInfo> list = (ArrayList<AuthInfo>) getBaseService().find(hql, authCode, mobileNumber);
        // FIXME:验证不够严谨，有可能出现重复数据
        if (list.size() > 0) {
            FacesUtil.setRequestAttribute("mobileNumber", mobileNumber);
            return "pretty:findPwdByEmail3";
        } else {
            FacesUtil.addErrorMessage("验证码输入错误！！");
            return null;
        }
    }

    /**
     * 更改绑定邮箱第一步 通过收到邮件认证码验证用户当前邮箱
     *
     * @return
     */
    public String checkCurrentEmail() {
        User user;
        try {
            user = userService.getUserById(loginUser.getLoginUserId());
            authService.verifyAuthInfo(user.getId(), user.getEmail(), authCode, CommonConstants.AuthInfoType.CHANGE_BINDING_EMAIL);
            correctAuthCode = true;
        } catch (AuthInfoOutOfDateException | AuthInfoOutOfTimesException e) {
            FacesUtil.addErrorMessage("认证码已过期！");
        } catch (AuthCodeNotMatchException e) {
            FacesUtil.addErrorMessage("认证码错误！");
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录");
            log.debug(e.getMessage());
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
        } catch (AuthInfoAlreadyActivedException e) {
            FacesUtil.addErrorMessage("输入认证码错误，认证码已经使用！");
        }
        return null;
    }

    /**
     * 更改绑定手机号第一步 通过收到手机认证码验证用户当前手机
     *
     * @return
     * @throws AuthInfoAlreadyActivedException
     */
    public String checkCurrentMobileNumber() throws AuthInfoAlreadyActivedException {
        User user;
        try {
            user = userService.getUserById(loginUser.getLoginUserId());
            authService.verifyAuthInfo(user.getId(), user.getMobileNumber(), authCode, CommonConstants.AuthInfoType.CHANGE_BINDING_MOBILE_NUMBER);
            correctAuthCode = true;
        } catch (AuthInfoOutOfDateException | AuthInfoOutOfTimesException e) {
            FacesUtil.addErrorMessage("认证码已过期！");
        } catch (AuthCodeNotMatchException e) {
            FacesUtil.addErrorMessage("认证码错误！");
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录");
            log.debug(e.getMessage());
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
        }
        return null;
    }

    /**
     * 禁止用户
     *
     * @return
     */
    public String forbid(String userId) {
        try {
            userService.changeUserStatus(userId, UserConstants.UserStatus.DISABLE);
        } catch (ConfigNotFoundException e) {
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("该用户不存在");
        }
        return getSaveView();
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getCaptchaAuthCode() {
        return captchaAuthCode;
    }

    /**
     * 获取投资权限,即实名认证
     *
     * @return
     */
    public String getInvestorPermission() {
        if (StringUtils.equals(HashCrypt.getDigestHash(getInstance().getCashPassword()), getInstance().getPassword())) {
            FacesUtil.addErrorMessage("交易密码不能与登录密码相同");
            return null;
        }
        userService.realNameCertification(getInstance());

        FacesUtil.addInfoMessage("保存成功，你已提交了实名认证！请等待审核");
        if (FacesUtil.isMobileRequest()) {
            return "pretty:mobile_user_center";
        }
        return "pretty:userCenter";
    }

    /**
     * 第二部： 获取投资权限,即实名认证(通过邮箱进行实名认证 )
     *
     * @return
     */
    public String getInvestorPermissionByEmail() {
        try {
            userService.realNameCertificationByEmail(getInstance(), authCode);
            FacesUtil.addInfoMessage("保存成功，你已通过了实名认证！");
            return "pretty:userCenter";
        } catch (AuthInfoOutOfDateException | AuthInfoOutOfTimesException e) {
            FacesUtil.addErrorMessage("认证码已过期！");
        } catch (AuthCodeNotMatchException e) {
            FacesUtil.addErrorMessage("认证码错误！");
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage("输入认证码错误，实名认证失败！");
        } catch (AuthInfoAlreadyActivedException e) {
            FacesUtil.addErrorMessage("输入认证码错误，实名认证失败！");
        }
        return null;
    }

    public String getMid() {
        return mid;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public String getNewMobileNumber() {
        return newMobileNumber;
    }

    public String getOldCashPassword() {
        return oldCashPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getReferrer() {
        return referrer;
    }

    public String getUid() {
        return uid;
    }

    public boolean isCorrectAuthCode() {
        return correctAuthCode;
    }

    /**
     * 第三部：通过手机修改密码
     *
     * @param mobileNumber
     * @param newPwd
     * @return
     */
    @Transactional
    public String ModificationPwdByMobileNum(String mobileNumber, String newPwd) {
        try {
            User user = userService.getUserByMobileNumber(mobileNumber);
            userService.modifyPassword(user.getId(), newPwd);
            FacesUtil.addInfoMessage("修改密码成功");
            return "pretty:memberLogin";
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("此号码未注册");
            log.debug(e.getMessage());
        }
        return authCode;
    }

    /**
     * 管理员修改用户，修改普通信息和邮箱、手机，不可修改密码等
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasRole('ROLE_EDIT')")
    public String modifyByAdmin() {
        if (StringUtils.isNotEmpty(getInstance().getEmail())) {
            User user = userBO.getUserByEmail(getInstance().getEmail());
            if (user != null && !user.getId().equals(getInstance().getId())) {
                FacesUtil.addErrorMessage("该邮箱已存在！");
                return null;
            }
        }
        if (StringUtils.isNotEmpty(getInstance().getMobileNumber())) {
            User user = userBO.getUserByMobileNumber(getInstance().getMobileNumber());
            if (user != null && !user.getId().equals(getInstance().getId())) {
                FacesUtil.addErrorMessage("该手机号已存在！");
                return null;
            }
        }
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("用户信息修改成功！");
        return FacesUtil.redirect("/admin/user/userList");
    }

    /**
     * 修改交易密码
     *
     * @return
     */
    public String modifycashPassword() {
        String userId = loginUser.getLoginUserId();
        try {
            if (!userService.verifyOldCashPassword(userId, oldCashPassword)) {
                FacesUtil.addErrorMessage("输入旧交易密码错误，请重新输入！");
                return null;
            }
            if (HashCrypt.getDigestHash(getInstance().getCashPassword()).equals(getInstance().getPassword())) {
                FacesUtil.addErrorMessage("交易密码不能与登录密码相同");
                return null;
            }
            userService.modifyCashPassword(userId, getInstance().getCashPassword());
            return "pretty:userCenter";
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录！");
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * 修改密码
     *
     * @return
     */
    public String modifyPassword() {
        String userId = loginUser.getLoginUserId();
        try {
            if (!userService.verifyOldPassword(userId, oldPassword)) {
                FacesUtil.addErrorMessage("输入旧密码错误，请重新输入！");
                return null;
            }
            userService.modifyPassword(loginUser.getLoginUserId(), getInstance().getPassword());
            FacesUtil.addInfoMessage("密码修改成功！");
            return "pretty:userCenter";
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录！");
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * 手机用户修改密码
     */
    @PreAuthorize("hasRole('USER')")
    public String modifyPassword(User user, String newPassword) {
        String userId = user.getId();
        try {
            if (!userService.verifyOldPassword(userId, oldPassword)) {
                return "1";// 输入旧密码错误，请重新输入！
            }

            userService.modifyPassword(userId, newPassword);
        } catch (UserNotFoundException e) {
            return "2";// 用户未登陆!
        }
        return "0";// 密码修改成功!
    }

    /**
     * 用户注册
     *
     * @return
     * @deprecated
     */
    @Deprecated
    public String register() {
        // 保存用户
        userService.register(getInstance(), referrer);
        // regSuccess = true;
        FacesUtil.setSessionAttribute("userEmail", getInstance().getEmail());
        return "pretty:userRegActiveuser";
    }

    /**
     * 通过邮箱注册用户
     *
     * @return
     * @since 2.0
     */
    public String registerByEmail() {
        // 保存用户
        userService.register(getInstance(), referrer);
        // 跳转到“提示通过邮箱激活页面”
        FacesUtil.setRequestAttribute("email", getInstance().getEmail());
        return "pretty:emailActiveNotice";
    }

    /**
     * 通过手机注册
     *
     * @return
     */
    public String registerByMobileNumber() {
        try {
            if (mid != null && uid != null) {
                // 实例化对象
                AdverLeague adverLeague = new AdverLeague();
                adverLeague.setRegDate(new Date());
                adverLeague.setMid(mid);
                adverLeague.setUid(uid);
                adverLeague.setStatus("2");// 2表示没有实名认证
                adverLeague.setUserName(getInstance().getUsername());
                // 保存
                adverService.save(adverLeague);
            }
            String password = getInstance().getPassword();
            userService.registerByMobileNumber(getInstance(), authCode, referrer);
            if (FacesUtil.isMobileRequest()) {
                return "pretty:mobile_user_center";
            }
            if (ipsMapService.needValidateCode(request)) {
                return "pretty:memberLogin";
            }
            loginUser(getInstance().getUsername(), password);
            return "pretty:userRegSuccess";
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage("输入的验证码错误，验证失败！");
        } catch (AuthCodeNotMatchException e) {
            FacesUtil.addErrorMessage("认证码错误！");
        } catch (AuthInfoOutOfDateException | AuthInfoOutOfTimesException e) {
            FacesUtil.addErrorMessage("验证码已过期！");
        } catch (AuthInfoAlreadyActivedException e) {
            FacesUtil.addErrorMessage("验证码已被使用！");
        }
        return null;
    }

    /**
     * 第三方登录 绑定账号注册 QQ、新浪微博
     *
     * @return
     */
    public String registerByOpenAuth() {
        userService.register(getInstance());
        Object openId = FacesUtil.getSessionAttribute(OpenAuthConstants.OPEN_ID_SESSION_KEY);
        Object openAutyType = FacesUtil.getSessionAttribute(OpenAuthConstants.OPEN_AUTH_TYPE_SESSION_KEY);
        Object accessToken = FacesUtil.getSessionAttribute(OpenAuthConstants.ACCESS_TOKEN_SESSION_KEY);
        if (openId != null && openAutyType != null && accessToken != null) {
            OpenAuthService oas = null;
            // QQ
            if (OpenAuthConstants.Type.QQ.equals(openAutyType)) {
                oas = (OpenAuthService) SpringBeanUtil.getBeanByName("qqOpenAuthService");
                // weibo
            } else if (OpenAuthConstants.Type.SINA_WEIBO.equals(openAutyType)) {
                oas = (OpenAuthService) SpringBeanUtil.getBeanByName("sinaWeiboOpenAuthService");
            }
            // 找不到应该抛异常
            if (oas != null) {
                oas.binding(getInstance().getId(), (String) openId, (String) accessToken);
            }
        }
        FacesUtil.addInfoMessage("注册成功，请登录邮箱，激活您的账户！");
        return "pretty:userRegActiveuser";
    }

    /**
     * 解禁用户
     *
     * @return
     */
    public String release(String userId) {
        if (UserHome.log.isInfoEnabled()) {
            UserHome.log.info(UserHome.sm.getString("log.info.releaseUser", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), userId));
        }
        try {
            userService.changeUserStatus(userId, UserConstants.UserStatus.ENABLE);
            // FIXME:下面异常不合理
        } catch (ConfigNotFoundException e) {
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("该用户不存在");
        }
        return getSaveView();
    }

    /**
     * 后台保存用户
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('USER_EDIT')")
    public String save() {
        // FIXME:放在service中
        if (StringUtils.isEmpty(getInstance().getId())) {
            getInstance().setId(getInstance().getUsername());
            getInstance().setPassword(HashCrypt.getDigestHash("123abc"));
            FacesUtil.addInfoMessage("用户创建成功，初始密码为123abc，请及时通知用户修改密码！");
            getInstance().setRegisterTime(new Date());
        }
        setUpdateView(FacesUtil.redirect("/admin/user/userList"));
        return super.save();
    }

    /**
     * 后台创建或修改借款者
     *
     * @return
     */
    public String saveBorrower() {
        // TODO:用户风险等级
        this.getInstance().setPassword("123456");
        userService.createBorrowerByAdmin(this.getInstance());
        FacesUtil.addInfoMessage("借款者创建成功，初始密码为123456");
        return FacesUtil.redirect("/admin/user/userList");
    }

    /**
     * 再次发送激活邮件
     *
     * @return
     */
    public void sendActiveEmailAgain() {
        User user;
        try {
            user = userService.getUserById(loginUser.getLoginUserId());
            userService.sendActiveEmailAgain(user);
            FacesUtil.setSessionAttribute("userEmail", getInstance().getEmail());
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录");
            log.debug(e.getMessage());
        }
        FacesUtil.addInfoMessage("邮件已发送请登录邮箱激活");
    }

    /*
     * =================================通过邮箱进行实名认证================================
     */

    /**
     * 第一步： 给邮箱发送验证码(实名认证的时候用)
     *
     * @author liuchun
     */
    public void sendAuthCodeToEmail() {
        try {
            userService.sendBindingEmail(loginUser.getLoginUserId(), getInstance().getEmail());
            FacesUtil.addInfoMessage("验证码已经发送，请注意查收！");
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户尚未登录！");
            log.debug(e.getMessage());
        }
    }

    /* =======================通过手机修改密码=============================== */

    /**
     * 第一步： 给手机发送验证码
     *
     * @param mobileNumber 注册时候用的手机号码
     * @author liuchun
     */
    public String sendAuthCodeToMobile(String mobileNumber) {
        String hql = "from User u where u.mobileNumber = ?";
        if (0 != getBaseService().find(hql, mobileNumber).size()) {
            try {
                userService.sendRegisterByMobileNumberSMS(mobileNumber);
            } catch (AuthInfoAlreadyInColdException e) {
                FacesUtil.addErrorMessage("请求过于频繁");
                log.debug(e.getMessage());
            }
            RequestContext.getCurrentInstance().addCallbackParam("sendSuccess", true);
            FacesUtil.setSessionAttribute("mobileNumber", mobileNumber);
            FacesUtil.addInfoMessage("短信已发送，请注意查收！");
            // FIXME:专属页面没有做，直接在原来页面上修改的
            return "pretty:findPwdByEmail2";
        } else {
            FacesUtil.addErrorMessage("此用户没有注册!!");
            return null;
        }
    }

    /**
     * 手机用户注册获取验证码
     *
     * @param mobileNumber
     * @return
     */
    public String saveUserToMobile(String mobileNumber) {
        String hql = "from User u where u.mobileNumber = ?";
        if (0 != getBaseService().find(hql, mobileNumber).size()) {
            return "1";// 用户已存在
        } else {
            try {
                userService.sendRegisterByMobileNumberSMS(mobileNumber);
            } catch (AuthInfoAlreadyInColdException e) {
                return "2";// 请求过于频繁!
            }
            return "0";// 短信已发送，请注意查收！
        }

    }


    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    /**
     * 找回密码发送验证码
     */
    public void sendBackMobileNumber() {

        String mobileNumber = getInstance().getMobileNumber();

        if (mobileNumber == null || mobileNumber.trim().length() < 0) {
            FacesUtil.addErrorMessage("手机号输入有误！");
            throw new UserNotFoundException("手机号输入有误！");
        }

        User user = userService.getUserByMobileNumber(mobileNumber);

        try {
            userService.sendFindLoginPasswordByMobileNumberSMS(user.getId(), mobileNumber);
            FacesUtil.addInfoMessage("验证码短信已经发送！");
        } catch (AuthInfoAlreadyInColdException e) {
            FacesUtil.addErrorMessage("请求过于频繁");
            log.debug(e.getMessage());
        }

    }

    /**
     * 验证短信验证码
     */
    public void checkFindPwdAuthCode() {

        String hql = "from AuthInfo ai where ai.authCode =? and ai.authTarget=?";
        ArrayList<AuthInfo> list = (ArrayList<AuthInfo>) getBaseService().find(hql, authCode, getInstance().getMobileNumber());

        if (list.size() > 0 && list.size() == 1) {

            Date deadline = list.get(0).getDeadline();

            if (deadline == null || deadline.after(new Date())) {
                setShowType("2");
            } else {
                FacesUtil.addErrorMessage("验证码已过期！");
            }


        } else {
            FacesUtil.addErrorMessage("验证码错误！");
        }


    }


    /**
     * 确认设置交易密码
     */

    public String comitSetThePwd() {
        try {
            userService.modifyPassword(userService.getUserByMobileNumber(getInstance().getMobileNumber()).getId(), getInstance().getPassword());
            ssService.cleanSpringSecurityContext();
            setShowType("3");
            FacesUtil.addInfoMessage("密码修改成功！");
            return "";
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("密码修改失败！");
            log.debug(e.getMessage());
        }
        return null;
    }


    /**
     * 跳转到登录页面
     */

    public String returnLogin() {
        return "pretty:memberLogin";
    }


    /**
     * 给绑定手机发送认证码
     */
    public void sendBdMobileNumber() {
        try {
            userService.sendBindingMobileNumberSMS(loginUser.getLoginUserId(), getInstance().getMobileNumber());
            FacesUtil.addInfoMessage("验证码短信已经发送！");
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户尚未登录！");
            log.debug(e.getMessage());
        }
    }

    /**
     * 更改绑定邮箱第一步 给用户当前邮箱发送认证码
     */
    public void sendCurrentBindingEmail() {
        User user;
        try {
            user = userService.getUserById(loginUser.getLoginUserId());
            userService.sendChangeBindingEmail(user.getId(), user.getEmail());
            FacesUtil.addInfoMessage("验证码已经发送至邮箱！");
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录");
            log.debug(e.getMessage());
        } catch (AuthInfoAlreadyInColdException e) {
            FacesUtil.addErrorMessage("请求过于频繁");
            log.debug(e.getMessage());
        }
    }

    /**
     * 更改绑定手机号第一步 给用户当前手机发送认证码
     */
    public void sendCurrentBindingMobileNumberSMS() {
        User user;
        try {
            user = userService.getUserById(loginUser.getLoginUserId());
            userService.sendChangeBindingMobileNumberSMS(user.getId(), user.getMobileNumber());
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录");
            e.printStackTrace();
        } catch (AuthInfoAlreadyInColdException e) {
            FacesUtil.addErrorMessage("请求过于频繁");
            e.printStackTrace();
        }
    }

    /**
     * 更改绑定邮箱第二步 给新邮箱发送验证码
     */
    public void sendNewBindingEmail() {
        User user;
        try {
            user = userService.getUserById(loginUser.getLoginUserId());
            if (user.getEmail().equals(newEmail)) {
                FacesUtil.addErrorMessage("新邮箱不能与当前邮箱相同！");
                return;
            }
            // FIXME 缺发送绑定新邮箱接口 、 新邮箱需要验证唯一性
            userService.sendBindingEmail(user.getId(), newEmail);
            FacesUtil.addInfoMessage("验证码已经发送至新邮箱！");
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录");
            log.debug(e.getMessage());
        }
    }

    /**
     * 更改绑定手机号第二步 给新手机发送验证码（修改绑定手机）
     */
    public void sendNewBindingMobileNumber() {
        User user;
        try {
            user = userService.getUserById(loginUser.getLoginUserId());
            if (user.getMobileNumber().equals(newMobileNumber)) {
                FacesUtil.addErrorMessage("新手机号不能与当前手机相同！");
                return;
            }
            // FIXME 缺发送绑定新手机接口 、 新手机需要验证唯一性
            userService.sendBindingMobileNumberSMS(user.getId(), newMobileNumber);
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录");
            log.debug(e.getMessage());
        }
    }

    /**
     * 用户注册操作，发送手机验证码验证（用户注册时）
     *
     * @param mobileNumber
     * @param jsCode       成功后执行的js代码
     */
    public void sendRegisterAuthCodeToMobile(String mobileNumber, String jsCode) {
        try {
            if (captchaService.verifyCaptcha(captchaAuthCode, FacesUtil.getHttpSession())) {
                captchaService.clearCaptcha(FacesUtil.getHttpSession());
                userService.sendRegisterByMobileNumberSMS(mobileNumber);
                FacesUtil.addInfoMessage("短信已发送，请注意查收！");
            } else {
                FacesUtil.addErrorMessage("验证码不正确");
                return;
            }
        } catch (SmsSendErrorException e) {
            FacesUtil.addInfoMessage("系统繁忙，请稍后再试！");
            return;
        } catch (AuthInfoAlreadyInColdException e) {
            FacesUtil.addErrorMessage("短信发生失败，请稍后再试！");
            return;
        }
        RequestContext.getCurrentInstance().execute(jsCode);
    }

    /**
     * (修改密码发送手机短信提醒)
     *
     * @param mobileNumber
     * @param jsCode       设定文件
     * @return void 返回类型
     * @Title: sendUpdatePasswordAuthCodeToMobile
     */
    public void sendUpdatePasswordAuthCodeToMobile(String mobileNumber, String jsCode) {
        try {
            if (captchaService.verifyCaptcha(captchaAuthCode, FacesUtil.getHttpSession())) {
                captchaService.clearCaptcha(FacesUtil.getHttpSession());
                userService.updatePasswordByMobileNumberSMS(getInstance().getId(), mobileNumber);
                FacesUtil.addInfoMessage("短信已发送，请注意查收！");
            } else {
                FacesUtil.addErrorMessage("验证码不正确");
                return;
            }
        } catch (SmsSendErrorException e) {
            FacesUtil.addInfoMessage("系统繁忙，请稍后再试！");
            return;
        } catch (AuthInfoAlreadyInColdException e) {
            FacesUtil.addErrorMessage("短信发生失败，请稍后再试！");
            return;
        }
        RequestContext.getCurrentInstance().execute(jsCode);
    }

    /**
     * (修改密码手机校验提交)
     *
     * @return String 返回类型
     * @Title: updatePasswordByMobileNumberSubmit
     */
    public String updatePasswordByMobileNumberSubmit() {
        try {
            userService.updatePasswordByMobileNumberSubmit(getInstance(), authCode);
            if (ipsMapService.needValidateCode(request)) {
                return "pretty:userUPassword";
            }
            return "pretty:userRPassword";
        } catch (NoMatchingObjectsException e) {
            FacesUtil.addErrorMessage("输入的验证码错误，验证失败！");
        } catch (AuthCodeNotMatchException e) {
            FacesUtil.addErrorMessage("认证码错误！");
        } catch (AuthInfoOutOfDateException | AuthInfoOutOfTimesException e) {
            FacesUtil.addErrorMessage("验证码已过期！");
        } catch (AuthInfoAlreadyActivedException e) {
            FacesUtil.addErrorMessage("验证码已被使用！");
        }
        return null;
    }

    /**
     * (重置密码)
     *
     * @param resetPassword
     * @return String 返回类型
     * @Title: resetPassword
     */
    public String resetPassword(String resetPassword) {
        try {
            userService.modifyPassword(loginUser.getLoginUserId(), resetPassword);
            // TODO 是否是将所有的用户的session都清除？？ 问题已经解决，2个用户，当前用户注销不会影响后面的用户
            ssService.cleanSpringSecurityContext();
            FacesUtil.addInfoMessage("密码修改成功！");
            return "pretty:memberLogin";
        } catch (UserNotFoundException e) {
            FacesUtil.addErrorMessage("用户未登录！");
            log.debug(e.getMessage());
        }
        return null;
    }

    public void setAdverService(AdverService adverService) {
        this.adverService = adverService;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setCaptchaAuthCode(String captchaAuthCode) {
        this.captchaAuthCode = captchaAuthCode;
    }

    public void setCorrectAuthCode(boolean correctAuthCode) {
        this.correctAuthCode = correctAuthCode;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public void setNewMobileNumber(String newMobileNumber) {
        this.newMobileNumber = newMobileNumber;
    }

    public void setOldCashPassword(String oldCashPassword) {
        this.oldCashPassword = oldCashPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * 上传图片
     *
     * @return
     */
    @Transactional(readOnly = false)
    public void uploadPhoto(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        InputStream is = null;
        try {
            is = file.getInputstream();
            // 删除之前的头像
            ImageUploadUtil.delete(this.getInstance().getPhoto());
            this.getInstance().setPhoto(ImageUploadUtil.upload(is, file.getFileName()));
            getBaseService().merge(getInstance());
            FacesUtil.addInfoMessage("上传成功！");
        } catch (IOException e) {
            FacesUtil.addErrorMessage("上传失败！");
            log.debug(e.getMessage());
        }
    }

    /**
     * 用户注册
     *
     * @return
     */
    public String weixinRegister() {
        // 保存用户
        userService.register(getInstance(), referrer);
        FacesUtil.setSessionAttribute("userEmail", getInstance().getEmail());
        return "pretty:weixin_login";
    }

    public Boolean getIspass() {
        return ispass;
    }

    public void setIspass(Boolean ispass) {
        this.ispass = ispass;
    }

    @Autowired
    SpringSecurityService springSecurityService;

    private void loginUser(String userName, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        springSecurityService.refreshLoginUserAuthorities(userName);
    }
}
