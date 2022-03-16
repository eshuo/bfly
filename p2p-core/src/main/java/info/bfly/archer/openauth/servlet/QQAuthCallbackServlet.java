package info.bfly.archer.openauth.servlet;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import info.bfly.archer.openauth.OpenAuthConstants;
import info.bfly.archer.openauth.service.OpenAuthService;
import info.bfly.core.annotations.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class QQAuthCallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 7405698352107356918L;

    @Log
    private static Logger log;

    @Resource(name = "qqOpenAuthService")
    private OpenAuthService oas;

    /**
     * Constructor of the object.
     */
    public QQAuthCallbackServlet() {
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        try {
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            String accessToken = null, openID = null;
            long tokenExpireIn = 0L;
            if (accessTokenObj.getAccessToken().equals("")) {
                // 我们的网站被CSRF攻击了或者用户取消了授权
                // 做一些数据统计工作
                log.info("没有获取到响应参数");
            } else {
                accessToken = accessTokenObj.getAccessToken();
                tokenExpireIn = accessTokenObj.getExpireIn();
                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj = new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();
                request.getSession().setAttribute(OpenAuthConstants.ACCESS_TOKEN_SESSION_KEY, accessToken);
                request.getSession().setAttribute(OpenAuthConstants.OPEN_ID_SESSION_KEY, openID);
                request.getSession().setAttribute(OpenAuthConstants.OPEN_AUTH_TYPE_SESSION_KEY, OpenAuthConstants.Type.QQ);
                // request.getSession().setAttribute("qq_token_expirein",
                // String.valueOf(tokenExpireIn));
                String redirectPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
                if (oas.isBinded(openID)) {
                    oas.login(openID, request.getSession());
                    oas.refreshAccessToken(openID, accessToken);
                    response.sendRedirect(redirectPath + "/");
                } else {
                    response.sendRedirect(redirectPath + "/oauth_bidding");
                }
                return;
            }
        } catch (QQConnectException e) {
        }
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
