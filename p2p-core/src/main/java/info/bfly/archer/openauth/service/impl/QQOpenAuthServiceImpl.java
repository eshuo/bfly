package info.bfly.archer.openauth.service.impl;

import com.qq.connect.QQConnectException;
import com.qq.connect.oauth.Oauth;
import info.bfly.archer.openauth.OpenAuthConstants;
import info.bfly.archer.openauth.service.abs.OpenAuthServiceAbstract;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Service(value = "qqOpenAuthService")
public class QQOpenAuthServiceImpl extends OpenAuthServiceAbstract {
    @Log
    Logger log;

    @Resource
    private HibernateTemplate ht;

    @Override
    @Transactional(readOnly = false)
    public void binding(String userId, String openId, String accessToken) {
        super.binding(userId, accessToken, openId, OpenAuthConstants.Type.QQ);
    }

    @Override
    public String getAuthUrl() {
        try {
            return new Oauth().getAuthorizeURL(FacesUtil.getHttpServletRequest());
        }
        catch (QQConnectException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean isBinded(String openId) {
        return super.isBinded(openId, OpenAuthConstants.Type.QQ);
    }

    @Override
    public void login(String openAuthId, HttpSession session) {
        super.login(openAuthId, OpenAuthConstants.Type.QQ, session);
    }

    @Override
    @Transactional(readOnly = false)
    public void refreshAccessToken(String openId, String accessToken) {
        super.refreshAccessToken(openId, accessToken, OpenAuthConstants.Type.QQ);
    }

    @Override
    public void unbinding(String userId) {
        super.unbinding(userId, OpenAuthConstants.Type.QQ);
    }
}
