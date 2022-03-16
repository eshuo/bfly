package info.bfly.archer.openauth.service.abs;

import info.bfly.archer.openauth.model.OpenAuth;
import info.bfly.archer.openauth.model.OpenAuthType;
import info.bfly.archer.openauth.service.OpenAuthService;
import info.bfly.archer.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

public abstract class OpenAuthServiceAbstract implements OpenAuthService {
    @Resource
    HibernateTemplate  ht;
    @Resource(name="customUserDetailsService")
    UserDetailsService userDetailsService;
    @Autowired
    SessionRegistry    sessionRegistry;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void binding(String userId, String accessToken, String openId, String openAuthTypeId) {
        OpenAuth oa = ht.get(OpenAuth.class, userId + "_" + openAuthTypeId);
        if (oa == null) {
            oa = new OpenAuth();
            oa.setId(userId + "_" + openAuthTypeId);
            oa.setType(new OpenAuthType(openAuthTypeId));
            oa.setUser(new User(userId));
        }
        oa.setAccessToken(accessToken);
        oa.setOpenId(openId);
        ht.saveOrUpdate(oa);
    }

    public boolean isBinded(String openId, String openAuthTypeId) {
        List<OpenAuth> oas = (List<OpenAuth>) ht.find("from OpenAuth oa where oa.openId=? and oa.type.id=?", new String[] { openId, openAuthTypeId });
        return oas.size() > 0;
    }

    public void login(String openId, String openAuthTypeId, HttpSession session) {
        List<OpenAuth> oas = (List<OpenAuth>) ht.find("from OpenAuth oa where oa.openId=? and oa.type.id=?", new String[]{openId, openAuthTypeId});
        if (oas.size() > 0) {
            User user = oas.get(0).getUser();
            // FIXME：用户不是激活状态，需要抛异常。
            if (user.getStatus().equals("1")) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                // Need to set this as thread locale as available throughout
                SecurityContextHolder.getContext().setAuthentication(token);
                // Set SPRING_SECURITY_CONTEXT attribute in session as Spring
                // identifies
                // context through this attribute
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
                sessionRegistry.registerNewSession(session.getId(), userDetails);
            }
        }
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void refreshAccessToken(String openId, String accessToken, String openAuthTypeId) {
        List<OpenAuth> oas = (List<OpenAuth>) ht.find("from OpenAuth oa where oa.openId=? and oa.type.id=?", new String[] { openId, openAuthTypeId });
        if (oas.size() > 0) {
            OpenAuth oa = oas.get(0);
            oa.setAccessToken(accessToken);
            ht.update(oa);
        }
    }

    public void unbinding(String userId, String openAuthTypeId) {
        String hql = "delete from OpenAuth oa where oa.id = ?";
        ht.bulkUpdate(hql, userId + "_" + openAuthTypeId);
    }
}
