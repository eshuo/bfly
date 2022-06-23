package info.bfly.archer.user.service.impl;

import info.bfly.archer.common.service.BaseService;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserLoginLog;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.SpringBeanUtil;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.stereotype.Service;

@Service("rememberMeAuthenticationFilter")
public class MyRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {
    @Resource
    private HibernateTemplate ht;

    /**
     * @param authenticationManager
     * @param rememberMeServices
     */
    public MyRememberMeAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);
    }

    private HibernateTemplate getHt() {
        return ht;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        String username = authResult.getName();
        List<User> users = (List<User>) getHt().findByNamedQuery("User.findUserByUsername", username);
        if (users.size() != 0) {
            User user = users.get(0);
            // 放入session
            // request.getSession(true).setAttribute(
            // UserConstants.SESSION_KEY_LOGIN_USER, user);
            // 记录user登录信息
            UserLoginLog ull = new UserLoginLog();
            ull.setId(IdGenerator.randomUUID());
            ull.setIsSuccess(UserConstants.UserLoginLog.SUCCESS);
            ull.setLoginIp(FacesUtil.getRequestIp(request));
            ull.setLoginTime(new Timestamp(System.currentTimeMillis()));
            ull.setUsername(user.getUsername());
            BaseService<UserLoginLog> loginLogService = (BaseService<UserLoginLog>) SpringBeanUtil.getBeanByName("baseService");
            loginLogService.save(ull);
        }
    }
}
