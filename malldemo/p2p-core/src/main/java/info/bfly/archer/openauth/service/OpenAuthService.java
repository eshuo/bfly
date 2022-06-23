package info.bfly.archer.openauth.service;

import javax.servlet.http.HttpSession;

/**
 */
public interface OpenAuthService {
    /**
     * 绑定第三方账号
     *
     * @param userId      用户名
     * @param openId      用户在第三方应用的唯一标识
     * @param accessToken 访问令牌
     */
    void binding(String userId, String openId, String accessToken);

    /**
     * 获取第三方绑定请求的url
     *
     * @return 请求的url
     */
    String getAuthUrl();

    /**
     * 该openId是否已经被绑定
     *
     * @param openId 用户在第三方应用的唯一标识
     * @return
     */
    boolean isBinded(String openId);

    /**
     * 第三方登录
     *
     * @param openId  用户在第三方应用的唯一标识
     * @param session 登录的session
     */
    void login(String openId, HttpSession session);

    /**
     * 刷新已绑定的用户的accessToken。因为第三方登录授权有一定时间，过期以后，重新获得授权，accessToken会发生变化。
     *
     * @param openId
     *            用户在第三方应用的唯一标识
     * @param accessToken
     *            访问令牌
     */
    void refreshAccessToken(String openId, String accessToken);

    /**
     * 接触第三方绑定
     *
     * @param userId
     */
    void unbinding(String userId);
}
