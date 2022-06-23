package info.bfly.archer.oauth2.controller;


import info.bfly.api.exception.ParameterExection;
import info.bfly.api.model.BaseResource;
import info.bfly.api.model.In;
import info.bfly.api.model.Out;
import info.bfly.api.service.ApiService;
import info.bfly.archer.key.ResponseMsg;
import info.bfly.archer.oauth2.model.request.Login;
import info.bfly.archer.oauth2.service.impl.OAuth2ServiceTokenStore;
import info.bfly.archer.system.service.SpringSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 31/05/2013
 */
@Controller
public class OAuth2Controller extends BaseResource {
    @Autowired
    private ApiService apiService;

    @Autowired
    private SpringSecurityService springSecurityService;

    @Autowired
    private OAuth2ServiceTokenStore oAuth2ServiceTokenStore;

    /**
     * get token
     *
     * @return
     */
    @RequestMapping("/token")
    public ModelAndView getToken(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        Login login = in.getFinalValue(Login.class);

        return new ModelAndView("forward:/oauth/token?" + login.toString());
    }

    /**
     * revoke token
     *
     * @return
     */
    @RequestMapping("/v1.0/logout")
    @ResponseBody
    public Out doLogOut(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        in.getFinalValue();
        OAuth2RefreshToken oAuth2RefreshToken = oAuth2ServiceTokenStore.readRefreshToken(in.getValue());

        OAuth2AccessToken oAuth2AccessToken = oAuth2ServiceTokenStore.getAccessToken(oAuth2ServiceTokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken));
        if (oAuth2RefreshToken == null)
            throw new ParameterExection(ResponseMsg.TOKEN_INVALID);
        oAuth2ServiceTokenStore.removeAccessToken(oAuth2AccessToken);
        if (oAuth2AccessToken != null)
            oAuth2ServiceTokenStore.removeRefreshToken(oAuth2RefreshToken);
        request.getSession().invalidate();
        Out out = apiService.parseOut(request);
        springSecurityService.cleanSpringSecurityContext();
        out.setResult();
        return out;
    }

    /**
     * revoke token
     *
     * @return
     */
    @RequestMapping("/v1.0/logout/{token:[a-z0-9\\-]*}")
    @ResponseBody
    public String doLogOut(@PathVariable String token, HttpServletRequest request) {
        OAuth2RefreshToken oAuth2RefreshToken = oAuth2ServiceTokenStore.readRefreshToken(token);

        OAuth2AccessToken oAuth2AccessToken = oAuth2ServiceTokenStore.getAccessToken(oAuth2ServiceTokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken));
        if (oAuth2RefreshToken == null)
           return "fail";
        oAuth2ServiceTokenStore.removeAccessToken(oAuth2AccessToken);
        if (oAuth2AccessToken != null)
            oAuth2ServiceTokenStore.removeRefreshToken(oAuth2RefreshToken);
        request.getSession().invalidate();
        return "success";
    }

}