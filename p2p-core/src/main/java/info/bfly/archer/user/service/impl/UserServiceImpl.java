package info.bfly.archer.user.service.impl;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.exception.*;
import info.bfly.archer.common.service.AuthService;
import info.bfly.archer.common.service.ValidationService;
import info.bfly.archer.common.service.impl.AuthInfoBO;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.openauth.OpenAuthConstants;
import info.bfly.archer.openauth.service.OpenAuthService;
import info.bfly.archer.system.SystemConstants;
import info.bfly.archer.system.model.MotionTracking;
import info.bfly.archer.system.service.SpringSecurityService;
import info.bfly.archer.user.LoginIdTypeEnum;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.exception.ConfigNotFoundException;
import info.bfly.archer.user.exception.NotConformRuleException;
import info.bfly.archer.user.exception.RoleNotFoundException;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserInfoService;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.DateStyle;
import info.bfly.core.util.DateUtil;
import info.bfly.core.util.HashCrypt;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.message.MessageConstants;
import info.bfly.p2p.message.model.UserMessageTemplate;
import info.bfly.p2p.message.service.MessageService;
import info.bfly.p2p.message.service.impl.MessageBO;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Log
    Logger log;
    @Resource
    private HibernateTemplate ht;
    @Resource
    private AuthService authService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserBO userBO;
    @Resource(name = "qqOpenAuthService")
    private OpenAuthService qqOAS;
    @Resource(name = "sinaWeiboOpenAuthService")
    private OpenAuthService sinaWeiboOAS;
    @Resource
    private MessageBO messageBO;
    @Resource
    private AuthInfoBO authInfoBO;
    @Resource
    private SpringSecurityService springSecurityService;
    @Resource
    private MessageService messageService;
    @Resource
    private ConfigService configService;
    @Resource
    private ValidationService vdService;

    @Override
    public User authenticate(String username, String password) throws UserNotFoundException {
        User user = userBO.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("user.name:" + username);
        }
        if (!StringUtils.equals(user.getPassword(), password)) {
            throw new UserNotFoundException("user.name:" + username);
        };
        return user;
    }

    // ////////////////////
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void activateUserByEmailActiveCode(String activeCode) throws UserNotFoundException, NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        // base64解码
        activeCode = new String(Base64.decodeBase64(activeCode));
        String[] aCodes = activeCode.split("&");
        if (aCodes.length != 2) {
            throw new UserNotFoundException("activeCode has error" + activeCode);
        }
        String email = aCodes[0];
        String code = aCodes[1];
        User user = userBO.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("user.email:" + email);
        }
        authService.verifyAuthInfo(user.getId(), email, code, CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL);
        userBO.enableUser(user);
        // 去掉inactive角色（inactive role has inactive permission）
        Role role = new Role("INACTIVE");
        userBO.removeRole(user, role);
        // /////////////////
        authInfoBO.activate(user.getId(), email, CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL);
        // 刷新用户权限
        springSecurityService.refreshLoginUserAuthorities(user.getId());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void addRole(String userId, String roleId) throws UserNotFoundException, RoleNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("userId:" + userId);
        }
        Role role = ht.get(Role.class, roleId);
        if (role == null) {
            throw new RoleNotFoundException("roleId:" + roleId);
        }
        userBO.addRole(user, role);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void bindingEmail(String userId, String email, String authCode) throws UserNotFoundException, NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        authService.verifyAuthInfo(userId, email, authCode, CommonConstants.AuthInfoType.BINDING_EMAIL);
        authInfoBO.activate(userId, email, CommonConstants.AuthInfoType.BINDING_EMAIL);
        user.setEmail(email);
        userBO.update(user);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void bindingMobileNumber(String userId, String mobileNumber, String authCode) throws UserNotFoundException, NoMatchingObjectsException, AuthInfoOutOfDateException,
            AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        authService.verifyAuthInfo(userId, mobileNumber, authCode, CommonConstants.AuthInfoType.BINDING_MOBILE_NUMBER);
        authInfoBO.activate(userId, mobileNumber, CommonConstants.AuthInfoType.BINDING_MOBILE_NUMBER);
        user.setMobileNumber(mobileNumber);
        userBO.update(user);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void changeUserStatus(String userId, String status) throws UserNotFoundException, ConfigNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("userId:" + userId);
        }
        user.setStatus(status);
        userBO.update(user);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createBorrowerByAdmin(User user) {
        // FIXME:缺验证，这里应该同时创建borrowerInfo，或者该方法应该放在borrowerInfoService中。
        user.setRegisterTime(new Date());
        user.setPassword(HashCrypt.getDigestHash(user.getPassword()));
        user.setStatus(UserConstants.UserStatus.ENABLE);
        userBO.save(user);
        // 添加普通用户权限
        userBO.addRole(user, new Role("MEMBER"));
        // 添加借款者权限
        userBO.addRole(user, new Role("LONAER"));
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void disableUser(String userId) throws UserNotFoundException {
        try {
            changeUserStatus(userId, CommonConstants.DISABLE);
        } catch (ConfigNotFoundException e) {
            log.debug(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void enableUser(String userId) throws UserNotFoundException {
        try {
            changeUserStatus(userId, CommonConstants.ENABLE);
        } catch (ConfigNotFoundException e) {
            log.debug(e.getMessage());
        }
    }

    private Date getDeadTime(String minute) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, Integer.parseInt(minute));
        return c.getTime();
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userBO.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("user.email:" + email);
        }
        return user;
    }

    @Override
    public User getUserById(String userId) throws UserNotFoundException {
        Assert.hasText(userId);
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        return user;
    }

    @Override
    @Transactional
    public User getUserByIdWithOutCache(String userId) throws UserNotFoundException {
        Assert.hasText(userId);
        User user = getUserByLoginId(userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        ht.evict(user);
        user = ht.merge(user);
        return user;
    }

    @Override
    public User getUserByMobileNumber(String mobileNumber) throws UserNotFoundException {
        User user = userBO.getUserByMobileNumber(mobileNumber);
        if (user == null) {
            throw new UserNotFoundException("user.mobileNumber:" + mobileNumber);
        }
        return user;
    }

    @Override
    public boolean hasRole(String userId, String roleId) {
        String hql = "select user from User user left join user.roles r where user.id=? and r.id = ?";
        return ht.find(hql, userId, roleId).size() > 0;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void modifyCashPassword(String userId, String newCashPassword) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("id:" + userId);
        }
        user.setCashPassword(HashCrypt.getDigestHash(newCashPassword));
        userBO.update(user);
    }

    @Override
    public boolean isSetCashPassword(String userId) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("id:" + userId);
        }
        return StringUtils.isNotEmpty(user.getCashPassword());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void modifyPassword(String userId, String newPassword) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("id:" + userId);
        }
        user.setPassword(HashCrypt.getDigestHash(newPassword));
        userBO.update(user);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void realNameCertification(User user) {
        user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
        userBO.update(user);
        //userBO.addRole(user, new Role("INVESTOR"));
        // 刷新登录用户权限
        springSecurityService.refreshLoginUserAuthorities(user.getId());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void realNameCertification(User user, String authCode) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        // FIXME:缺验证
        authService.verifyAuthInfo(user.getId(), user.getMobileNumber(), authCode, CommonConstants.AuthInfoType.BINDING_MOBILE_NUMBER);
        user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
        userBO.update(user);
        //userBO.addRole(user, new Role("INVESTOR"));
        // 刷新登录用户权限
        springSecurityService.refreshLoginUserAuthorities(user.getId());
    }

    /**
     * 通过邮箱进行实名认证
     *
     * @throws AuthInfoAlreadyActivedException
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void realNameCertificationByEmail(User user, String authCode) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        // FIXME:缺验证
        authService.verifyAuthInfo(user.getId(), user.getEmail(), authCode, CommonConstants.AuthInfoType.BINDING_EMAIL);
        user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
        userBO.update(user);
        userBO.addRole(user, new Role("INVESTOR"));
        // 刷新登录用户权限
        springSecurityService.refreshLoginUserAuthorities(user.getId());
    }

    @Override
    public void refreshAuthorities(String userID) {
        // 刷新登录用户权限
        springSecurityService.refreshLoginUserAuthorities(userID);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void register(User user) {
        // FIXME:缺验证
        user.setRegisterTime(new Date());
        user.setPassword(HashCrypt.getDigestHash(user.getPassword()));
        user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
        // FIXME:用户给予激活状态 active???
        user.setStatus(UserConstants.UserStatus.ENABLE);
        // /////////
        // user.setStatus(UserConstants.UserStatus.NOACTIVE);
        userBO.save(user);
        // 添加普通用户权限
        Role role = new Role("MEMBER");
        // 添加inactive角色（inactive role has inactive permission）
        Role role2 = new Role("INACTIVE");
        userBO.addRole(user, role2);
        // //////////
        userBO.addRole(user, role);
        // sendActiveEmail(user, authService.createAuthInfo(user.getId(),
        // user.getEmail(), null,
        // CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL).getAuthCode());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void register(User user, String referrer) {
        register(user);
        saveReferrerInfo(user.getId(), referrer);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void registerByMobileNumber(User user, String authCode) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        authService.verifyAuthInfo("register", user.getMobileNumber(), authCode, CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER);
        user.setRegisterTime(new Date());
        user.setPassword(HashCrypt.getDigestHash(user.getPassword()));
        user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
        user.setStatus(UserConstants.UserStatus.ENABLE);
        userBO.save(user);
        // 添加普通用户权限
        Role role = new Role("MEMBER");
        userBO.addRole(user, role);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void registerByMobileNumber(User user, String authCode, String referrer) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        registerByMobileNumber(user, authCode);
        saveReferrerInfo(user.getId(), referrer);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void registerByOpenAuth(User user) {
        register(user);
        // 第三方首次登录，绑定已有账号
        Object openId = FacesUtil.getSessionAttribute(OpenAuthConstants.OPEN_ID_SESSION_KEY);
        Object openAutyType = FacesUtil.getSessionAttribute(OpenAuthConstants.OPEN_AUTH_TYPE_SESSION_KEY);
        Object accessToken = FacesUtil.getSessionAttribute(OpenAuthConstants.ACCESS_TOKEN_SESSION_KEY);
        if (openId != null && openAutyType != null && accessToken != null) {
            if (OpenAuthConstants.Type.QQ.equals(openAutyType)) {
                qqOAS.binding(user.getId(), (String) openId, (String) accessToken);
            } else if (OpenAuthConstants.Type.SINA_WEIBO.equals(openAutyType)) {
                sinaWeiboOAS.binding(user.getId(), (String) openId, (String) accessToken);
            }
        }
    }

    @Override
    public void resetPassword(String userId) {
        // TODO Auto-generated method stub
    }

    /**
     * 保存注册时候的推荐人信息
     *
     * @param userId
     * @param referrer
     */
    private void saveReferrerInfo(String userId, String referrer) {
        if (StringUtils.isNotEmpty(referrer)) {
            // 保存推荐人信息
            MotionTracking mt = new MotionTracking();
            mt.setId(IdGenerator.randomUUID());
            mt.setFromWhere(referrer.trim());
            mt.setFromTime(new Date());
            mt.setFromType(SystemConstants.MotionTrackingConstants.FromType.REFERRER);
            mt.setWho(userId);
            mt.setWhoType(SystemConstants.MotionTrackingConstants.WhoType.USER);
            mt.setActionType(SystemConstants.MotionTrackingConstants.ActionType.REGISTER);
            ht.save(mt);
        }
    }

    @Override
    public void sendActiveEmail(String userId, String authCode) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("userId:" + userId);
        }
        sendActiveEmail(user, authCode);
    }

    private void sendActiveEmail(User user, String authCode) {
        final String email = user.getEmail();
        // 发送账号激活邮件
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", user.getUsername());
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        String activeCode = email + "&" + authCode;
        // base64编码
        activeCode = Base64.encodeBase64URLSafeString(activeCode.getBytes());
        String activeLink = FacesUtil.getCurrentAppUrl() + "/activateAccount?activeCode=" + activeCode;
        params.put("active_url", activeLink);
        messageBO.sendEmail(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.REGISTER_ACTIVE + "_email"), params, email);
    }

    // ///////////////////

    /**
     * 再次发送激活邮件
     *
     * @param user
     */
    @Override
    public void sendActiveEmailAgain(User user) {
        try {
            sendActiveEmail(user, authService.createAuthInfo(user.getId(), user.getEmail(), null, CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL).getAuthCode());
        } catch (AuthInfoAlreadyInColdException e) {
            FacesUtil.addErrorMessage("发送间隔过短");
            return;
        }
    }

    @Override
    public void sendBindingEmail(String userId, String email) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        // 发送绑定邮箱确认邮件
        Map<String, String> params = new HashMap<String, String>();
        // TODO:实现模板
        params.put("username", user.getUsername());
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        try {
            params.put("authCode", authService.createAuthInfo(userId, email, null, CommonConstants.AuthInfoType.BINDING_EMAIL).getAuthCode());
        } catch (AuthInfoAlreadyInColdException e) {
            FacesUtil.addErrorMessage("发送间隔过短");
            return;
        }
        messageBO.sendEmail(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.BINDING_EMAIL + "_email"), params, email);
    }

    @Override
    public void sendBindingMobileNumberSMS(String userId, String mobileNumber) throws UserNotFoundException {
        // FIXME:验证手机号码的合法性
        // 发送手机验证码
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        Map<String, String> params = new HashMap<String, String>();
        // TODO:实现模板
        params.put("username", user.getUsername());
        params.put("time", "30");
        try {
            params.put("authCode", authService.createAuthInfo(userId, mobileNumber, null, CommonConstants.AuthInfoType.BINDING_MOBILE_NUMBER).getAuthCode());
        } catch (AuthInfoAlreadyInColdException e) {
            log.debug(e.getMessage());
        }
        messageBO.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.BINDING_MOBILE_NUMBER + "_sms"), params, mobileNumber);
    }

    @Override
    public void sendChangeBindingEmail(String userId, String oriEmail) throws UserNotFoundException, AuthInfoAlreadyInColdException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        Map<String, String> params = new HashMap<String, String>();
        // TODO:实现模板
        params.put("username", user.getUsername());
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        params.put("authCode", authService.createAuthInfo(userId, oriEmail, null, CommonConstants.AuthInfoType.CHANGE_BINDING_EMAIL).getAuthCode());
        messageBO.sendEmail(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.CHANGE_BINDING_EMAIL + "_email"), params, oriEmail);
    }

    @Override
    public void sendChangeBindingMobileNumberSMS(String userId, String oriMobileNumber) throws UserNotFoundException, AuthInfoAlreadyInColdException {
        // FIXME:验证手机号码的合法性
        // 发送手机验证码
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        Map<String, String> params = new HashMap<String, String>();
        // TODO:实现模板
        params.put("username", user.getUsername());
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        params.put("authCode", authService.createAuthInfo(userId, oriMobileNumber, null, CommonConstants.AuthInfoType.CHANGE_BINDING_MOBILE_NUMBER).getAuthCode());
        messageBO.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.CHANGE_BINDING_MOBILE_NUMBER + "_sms"), params, oriMobileNumber);
    }

    @Override
    public void sendFindLoginPasswordEmail(String email) throws UserNotFoundException, AuthInfoAlreadyInColdException {
        User user = userBO.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("user.email:" + email);
        }
        // 发送登录密码找回邮件
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", user.getUsername());
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        params.put("authCode", authService.createAuthInfo(user.getId(), email, null, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_EMAIL).getAuthCode());
        messageBO.sendEmail(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.FIND_LOGIN_PASSWORD_BY_EMAIL + "_email"), params, email);
    }

    @Override
    public void sendFindLoginPasswordLinkEmail(String email) throws UserNotFoundException, AuthInfoAlreadyInColdException {
        User user = userBO.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("user.email:" + email);
        }
        // 发送登录密码找回邮件
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", user.getUsername());
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        String authCode = authService.createAuthInfo(user.getId(), email, null, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_EMAIL).getAuthCode();
        String activeCode = email + "&" + authCode;
        // base64编码
        activeCode = Base64.encodeBase64URLSafeString(activeCode.getBytes());
        String resetPasswrodUrl = FacesUtil.getCurrentAppUrl() + "/find_pwd_by_email3/" + activeCode;
        params.put("reset_password_url", resetPasswrodUrl);
        messageBO.sendEmail(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.FIND_LOGIN_PASSWORD_BY_EMAIL + "_email"), params, email);
    }

    @Override
    public void sendFindLoginPasswordByMobileNumberSMS(String userId, String mobileNumber) throws UserNotFoundException, AuthInfoAlreadyInColdException {

        User user = userBO.getUserByMobileNumber(mobileNumber);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        if (!userId.equals(user.getId())) {
            throw new UserNotFoundException("UserId Not eg " + userId);
        }
        Map<String, String> params = new HashMap<String, String>();
        // TODO:实现模板
        params.put("username", user.getUsername());
        params.put("time", "30");
        params.put("authCode", authService.createAuthInfo(userId, mobileNumber, getDeadTime("30"), CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE).getAuthCode());
        messageBO.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.FIND_LOGIN_PASSWORD_BY_MOBILE + "_sms"), params, mobileNumber);

    }

    @Override
    public void sendRegisterByMobileNumberSMS(String mobileNumber) throws AuthInfoAlreadyInColdException {
        String deadtime = StringUtils.defaultString(configService.getConfigValue("sms_register_deadtime"), "30");
        Map<String, String> params = new HashMap<String, String>();
        params.put("time", deadtime);
        params.put("authCode", authService.createAuthInfo("register", mobileNumber, getDeadTime(deadtime), CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER).getAuthCode());
        messageBO.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.REGISTER_BY_MOBILE_NUMBER + "_sms"), params, mobileNumber);
    }


    @Override
    public void updatePasswordByMobileNumberSMS(String userId, String mobileNumber) throws AuthInfoAlreadyInColdException {
        String deadtime = StringUtils.defaultString(configService.getConfigValue("sms_update_passwrod_deadtime"), "30");
        Map<String, String> params = new HashMap<String, String>();
        params.put("time", deadtime);
        params.put("authCode", authService.createAuthInfo(userId, mobileNumber, getDeadTime(deadtime), CommonConstants.AuthInfoType.UPDATE_PASSWORD_BY_MOBILE_NUMBER).getAuthCode());
        messageBO.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.UPDATE_PASSWORD_BY_MOBILE_NUMBER + "_sms"), params, mobileNumber);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updatePasswordByMobileNumberSubmit(User user, String authCode) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        authService.verifyAuthInfo(user.getId(), user.getMobileNumber(), authCode, CommonConstants.AuthInfoType.UPDATE_PASSWORD_BY_MOBILE_NUMBER);

    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public User verifyFindLoginPasswordActiveCode(String activeCode) throws UserNotFoundException, NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        // base64解码
        activeCode = new String(Base64.decodeBase64(activeCode));
        String[] aCodes = activeCode.split("&");
        if (aCodes.length != 2) {
            throw new UserNotFoundException("activeCode has error" + activeCode);
        }
        String email = aCodes[0];
        String code = aCodes[1];
        User user = getUserByEmail(email);
        authService.verifyAuthInfo(user.getId(), email, code, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_EMAIL);
        return user;
    }

    @Override
    public boolean verifyOldCashPassword(String userId, String oldCashPassword) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("id:" + userId);
        }
        if (user.getCashPassword() != null && user.getCashPassword().equals(HashCrypt.getDigestHash(oldCashPassword))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyOldPassword(String userId, String oldPassword) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("id:" + userId);
        }
        if (user.getPassword().equals(HashCrypt.getDigestHash(oldPassword))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyPasswordRule(String password) throws NotConformRuleException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean verifyUsernameRule(String username) throws NotConformRuleException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void save(User user) {
        ht.save(user);
    }

    @Override
    public User getUserByLoginId(String loginId) {
        Assert.hasText(loginId);
        LoginIdTypeEnum typeName = null;
        // 循环遍历类型，放回匹配的类型值
        for (LoginIdTypeEnum loginIdType : LoginIdTypeEnum.values()) {
            try {
                vdService.inputRuleValidation(loginIdType.getName(), loginId);
                typeName = loginIdType;
                break;
            } catch (NoMatchingObjectsException | InputRuleMatchingException e) {
                continue;
            }
        }
        if (typeName == null)
            throw new UserNotFoundException("user.id:" + loginId);

        // 根据匹配的类型查询
            switch (typeName) {
            case USERNAME_TYPE:
                return this.getUserById(loginId);
            case EMAIL_TYPE:
                return this.getUserByEmail(loginId);
            case MOBILE_TYPE:
                return this.getUserByMobileNumber(loginId);
            default:
                throw new UserNotFoundException("user.id:" + loginId);
            }
    }


    @Override
    public boolean isSystem(User user) {
        if(user.getId().equals("system"))
            return true;
        return false;
    }
}

