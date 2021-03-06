package info.bfly.archer.user.service.impl;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.service.BaseService;
import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.model.Config;
import info.bfly.archer.openauth.OpenAuthConstants;
import info.bfly.archer.openauth.service.OpenAuthService;
import info.bfly.archer.system.model.IpsMapService;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserLoginLog;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.SpringBeanUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

@Service("myAuthenticationManager")
public class MyAuthenticationManager extends DaoAuthenticationProvider {
    private static StringManager sm = StringManager.getManager(UserConstants.Package);
    @Resource(name = "baseService")
    private BaseService<User> userService;
    @Resource(name = "baseService")
    private BaseService<UserLoginLog> loginLogService;
    @Resource(name = "ipsMapService")
    private IpsMapService ipsMapService;
    @Resource
    private HibernateTemplate ht;

    private WebAuthenticationDetails authenticationDetails;

    private final HttpServletRequest request;

    @Autowired
    public MyAuthenticationManager(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        User user = null;
        List<User> users = (List<User>) ht.findByNamedQuery("User.findUserByUsername", userDetails.getUsername());
        if (users.size() != 0) {
            user = users.get(0);
        }
        try {
            super.additionalAuthenticationChecks(userDetails, authentication);
            handleLoginSuccess(user, request);
        } catch (AuthenticationException ae) {
            handleLoginFail(user, request);
            throw ae;
        }
    }

    /**
     * ????????????????????????
     *
     * @param user
     * @param request
     * @param isSuccess
     *            ??????????????????
     */
    private void addUserLoginLog(User user, HttpServletRequest request, String isSuccess) {
        // ??????user????????????
        UserLoginLog ull = new UserLoginLog();
        ull.setId(IdGenerator.randomUUID());
        ull.setIsSuccess(isSuccess);
        ull.setLoginIp(FacesUtil.getRequestIp(request));
        ull.setLoginTime(new Timestamp(System.currentTimeMillis()));
        ull.setUsername(user.getUsername());
        loginLogService.save(ull);
    }

    /**
     * ???????????????
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authenticationDetails = (WebAuthenticationDetails) authentication.getDetails();

        // ??????????????????????????????????????????
        this.checkUserNameAndPassword(authentication);

        // ?????????????????????
        Boolean needValidateCode = ipsMapService.needValidateCode(request);
        // check validate code
        if (needValidateCode) {
            request.getSession(true).setAttribute(UserConstants.AuthenticationManager.NEED_VALIDATE_CODE, true);
            checkValidateCode(request);
        }
        try {
            return super.authenticate(authentication);
        } catch (AuthenticationException ae) {
            // ??????additionalAuthenticationChecks???????????????????????????????????????????????????????????????????????????????????????
            handleLoginFail(null, request);
            throw ae;
        }
    }

    /**
     *
     * @Title: checkUserNameAndPassword
     * @Description: TODO(??????????????????????????????????????????)
     * @param @param authentication ????????????
     * @return void ????????????
     * @throws
     */
    private void checkUserNameAndPassword(Authentication authentication) {
        if (null == authentication.getName() || "".equals(authentication.getName()))
            throw new AuthenticationServiceException(MyAuthenticationManager.sm.getString("usernameNullError"));
        if (null == authentication.getCredentials().toString() || "".equals(authentication.getCredentials().toString()))
            throw new AuthenticationServiceException(MyAuthenticationManager.sm.getString("passwordNullError"));
    }

    /**
     *
     * <li>??????session??????????????????????????????????????????????????????</li>
     */
    protected void checkValidateCode(HttpServletRequest request) {
        String sessionValidateCode = obtainSessionValidateCode(request);
        String validateCodeParameter = obtainValidateCodeParameter(request);
        if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
            throw new AuthenticationServiceException(MyAuthenticationManager.sm.getString("verificationCodeError"));
        }
    }

    /**
     * ??????????????????
     *
     * @param user
     * @param request
     */
    private void handleLoginFail(User user, HttpServletRequest request) {
        int iploginFailLimit = Integer.parseInt(ht.get(Config.class, ConfigConstants.UserSafe.IP_LOGIN_FAIL_MAX_TIMES).getValue());
        String remoteAddress = authenticationDetails.getRemoteAddress();
        int failed_times = ipsMapService.addFailedTimes(remoteAddress).getFailed_times();
        if (iploginFailLimit <= failed_times) {
            ipsMapService.addTimeExpira(remoteAddress, new GregorianCalendar(TimeZone.getDefault()).getTimeInMillis() + IpsMapService.DEFAULT_TIMEEXPIRA);
            request.getSession(true).setAttribute(UserConstants.AuthenticationManager.NEED_VALIDATE_CODE, true);
        }
        if (user == null) {
            // ?????????????????????????????????????????????????????????
            int loginFailLimit = Integer.parseInt(ht.get(Config.class, ConfigConstants.UserSafe.LOGIN_FAIL_MAX_TIMES).getValue());
            Integer loginFailTime = (Integer) request.getSession(true).getAttribute(UserConstants.AuthenticationManager.LOGIN_FAIL_TIME);
            if (loginFailTime == null) {
                loginFailTime = 0;
            }
            loginFailTime++;
            request.getSession(true).setAttribute(UserConstants.AuthenticationManager.LOGIN_FAIL_TIME, loginFailTime);
            if (loginFailLimit <= loginFailTime) {
                request.getSession(true).setAttribute(UserConstants.AuthenticationManager.NEED_VALIDATE_CODE, true);
            }
        } else {
            handleUserState(user);
            addUserLoginLog(user, request, UserConstants.UserLoginLog.FAIL);
        }
    }

    /**
     * ??????????????????
     *
     * @param user
     * @param request
     */
    private void handleLoginSuccess(User user, HttpServletRequest request) {
        request.getSession(true).setAttribute(UserConstants.AuthenticationManager.LOGIN_FAIL_TIME, 0);
        request.getSession(true).setAttribute(UserConstants.AuthenticationManager.NEED_VALIDATE_CODE, false);
        String openAuthBidding = request.getParameter("open_auth_bidding_login");
        if (StringUtils.isNotEmpty(openAuthBidding) && openAuthBidding.trim().equals("true")) {
            // ??????????????????????????????????????????
            Object openId = request.getSession().getAttribute(OpenAuthConstants.OPEN_ID_SESSION_KEY);
            Object openAutyType = request.getSession().getAttribute(OpenAuthConstants.OPEN_AUTH_TYPE_SESSION_KEY);
            Object accessToken = request.getSession().getAttribute(OpenAuthConstants.ACCESS_TOKEN_SESSION_KEY);
            if (openId != null && openAutyType != null && accessToken != null) {
                OpenAuthService oas = null;
                if (OpenAuthConstants.Type.QQ.equals(openAutyType)) {
                    oas = (OpenAuthService) SpringBeanUtil.getBeanByName("qqOpenAuthService");
                } else if (OpenAuthConstants.Type.SINA_WEIBO.equals(openAutyType)) {
                    oas = (OpenAuthService) SpringBeanUtil.getBeanByName("sinaWeiboOpenAuthService");
                }
                // ????????????????????????
                if (oas != null) {
                    oas.binding(user.getId(), (String) openId, (String) accessToken);
                }
            }
        }

        addUserLoginLog(user, request, UserConstants.UserLoginLog.SUCCESS);
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param user
     */
    private void handleUserState(User user) {
        // ???????????????????????????????????????????????????????????????????????????????????????
        int passwordFailLimit = Integer.parseInt(ht.get(Config.class, ConfigConstants.UserSafe.PASSWORD_FAIL_MAX_TIMES).getValue());
        DetachedCriteria criteria = DetachedCriteria.forClass(UserLoginLog.class);
        criteria.addOrder(Order.desc("loginTime"));
        criteria.add(Restrictions.eq("username", user.getUsername()));
        List<UserLoginLog> ulls = (List<UserLoginLog>) ht.findByCriteria(criteria, 0, passwordFailLimit);
        if (ulls.size() == passwordFailLimit) {
            // ?????????????????????
            // ???????????????????????????????????????
            for (UserLoginLog ull : ulls) {
                if (ull.getIsSuccess().equals(UserConstants.UserLoginLog.SUCCESS)) {
                    return;
                }
            }
            // ????????????
            user.setStatus(UserConstants.UserStatus.DISABLE);
            userService.save(user);
        }
    }

    protected String obtainSessionValidateCode(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(CommonConstants.CaptchaFlag.CAPTCHA_SESSION);
        return null == obj ? "" : obj.toString();
    }

    private String obtainValidateCodeParameter(HttpServletRequest request) {
        return request.getParameter(CommonConstants.CaptchaFlag.CAPTCHA_INPUT);
    }

    @Override
    @Resource(name = "customUserDetailsService")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    @Resource(name = "passwordEncoder")
    public void setPasswordEncoder(Object passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);
    }
}
