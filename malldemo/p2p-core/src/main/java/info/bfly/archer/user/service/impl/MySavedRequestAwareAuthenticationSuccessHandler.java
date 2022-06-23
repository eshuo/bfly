package info.bfly.archer.user.service.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

/**
 * 处理登录成功后返回页面，如果没有spring-security-redirect参数返回根目录
 * 
 * @author Administrator
 *
 */
public class MySavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private String defaultTargetUrl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		redirectUrl(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	protected void redirectUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		if (response.isCommitted()) { return; }
		String toTargetUrl = request.getParameter("redirect");
		if (StringUtils.isEmpty(toTargetUrl)) toTargetUrl = defaultTargetUrl == null ? "/" : defaultTargetUrl;
		redirectStrategy.sendRedirect(request, response, toTargetUrl);
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) { return; }
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	public String getDefaultTargetUrl() {
		return defaultTargetUrl;
	}

	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}

}
