package info.bfly.archer.openauth.servlet;

import info.bfly.archer.openauth.OpenAuthConstants;
import info.bfly.archer.openauth.service.OpenAuthService;
import info.bfly.core.annotations.Log;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class SinaWeiboAuthCallbackServlet extends HttpServlet {
    private static final long serialVersionUID = -7407496137745937699L;
    @Log
    Logger log;
    @Resource(name = "sinaWeiboOpenAuthService")
    private OpenAuthService oas;

    /**
     * Constructor of the object.
     */
    public SinaWeiboAuthCallbackServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    @Override
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * The doPost method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (StringUtils.isNotEmpty(code)) {
            Oauth oauth = new Oauth();
            try {
                AccessToken at = oauth.getAccessTokenByCode(code);
                request.getSession().setAttribute(OpenAuthConstants.ACCESS_TOKEN_SESSION_KEY, at.getAccessToken());
                request.getSession().setAttribute(OpenAuthConstants.OPEN_ID_SESSION_KEY, at.getUid());
                request.getSession().setAttribute(OpenAuthConstants.OPEN_AUTH_TYPE_SESSION_KEY, OpenAuthConstants.Type.SINA_WEIBO);
                // Users um = new Users();
                // um.client.setToken(at.getAccessToken());
                // User user = um.showUserById(at.getUid());
                // Timeline tm = new Timeline();
                // tm.client.setToken(at.getAccessToken());
                // try {
                // URLEncodeUtils.encodeURL(str)
                // OperationStatus status = tm
                // .UpdateStatus("润金新浪登录成功，关注关注哦~~~http://www.runjinfund.com/");
                // } catch (WeiboException e) {
                // log.debug(e.getMessage());
                // }
                // OpenAuthService oas = (OpenAuthService)
                // SpringBeanUtil.getBean("sinaWeiboOpenAuthService");
                String redirectPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
                if (oas.isBinded(at.getUid())) {
                    oas.login(at.getUid(), request.getSession());
                    oas.refreshAccessToken(at.getUid(), at.getAccessToken());
                    response.sendRedirect(redirectPath + "/");
                } else {
                    response.sendRedirect(redirectPath + "/oauth_bidding");
                }
                return;
            } catch (WeiboException e) {
                if (401 == e.getStatusCode()) {
                } else {
                    log.debug(e.getMessage());
                }
            }
        }
        // 用户在授权页面，点击取消。或者新浪授权出错
        String redirectPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/memberLoginPage";
        response.sendRedirect(redirectPath);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    @Override
    public void init() throws ServletException {
        // Put your code here
    }
}
