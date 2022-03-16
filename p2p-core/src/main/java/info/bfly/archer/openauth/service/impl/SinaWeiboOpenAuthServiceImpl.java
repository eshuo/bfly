package info.bfly.archer.openauth.service.impl;

import info.bfly.archer.openauth.OpenAuthConstants;
import info.bfly.archer.openauth.service.abs.OpenAuthServiceAbstract;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import weibo4j.util.WeiboConfig;

@Service(value = "sinaWeiboOpenAuthService")
public class SinaWeiboOpenAuthServiceImpl extends OpenAuthServiceAbstract {
    @Resource
    private HibernateTemplate ht;

    @Override
    @Transactional(readOnly = false)
    public void binding(String userId, String openId, String accessToken) {
        super.binding(userId, accessToken, openId, OpenAuthConstants.Type.SINA_WEIBO);
    }

    @Override
    public String getAuthUrl() {
        return WeiboConfig.getValue("authorizeURL").trim() + "?client_id=" + WeiboConfig.getValue("client_ID").trim() + "&response_type=code&redirect_uri="
                + WeiboConfig.getValue("redirect_URI").trim();
    }

    @Override
    public boolean isBinded(String openId) {
        return super.isBinded(openId, OpenAuthConstants.Type.SINA_WEIBO);
    }

    @Override
    public void login(String openId, HttpSession session) {
        super.login(openId, OpenAuthConstants.Type.SINA_WEIBO, session);
    }

    @Override
    @Transactional(readOnly = false)
    public void refreshAccessToken(String openId, String accessToken) {
        super.refreshAccessToken(openId, accessToken, OpenAuthConstants.Type.SINA_WEIBO);
    }

    @Override
    public void unbinding(String userId) {
        super.unbinding(userId, OpenAuthConstants.Type.SINA_WEIBO);
    }
}
