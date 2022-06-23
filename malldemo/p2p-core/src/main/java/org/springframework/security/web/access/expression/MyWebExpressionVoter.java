package org.springframework.security.web.access.expression;

import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * Description:
 */
public class MyWebExpressionVoter extends WebExpressionVoter {
    private DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
    private String rolePrefix;

    private WebExpressionConfigAttribute findConfigAttribute(Collection<ConfigAttribute> attributes) {
        for (ConfigAttribute attribute : attributes) {
            if (attribute instanceof WebExpressionConfigAttribute) {
                WebExpressionConfigAttribute weca = (WebExpressionConfigAttribute) attribute;
                if (weca.getAuthorizeExpression().getExpressionString().equals("MENU_PERMISSION") || weca.getAuthorizeExpression().getExpressionString().equals("URL_MAPPING_PERMISSION")||weca.getAuthorizeExpression().getExpressionString().equals("SECURITY_RESOURCES_PERMISSION") ) {
                    continue;
                }
                return weca;
            }
        }
        return null;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        assert authentication != null;
        assert fi != null;
        assert attributes != null;
        WebExpressionConfigAttribute weca = findConfigAttribute(attributes);
        if (weca == null) {
            return AccessDecisionVoter.ACCESS_ABSTAIN;
        }

        EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication, fi);
        return ExpressionUtils.evaluateAsBoolean(weca.getAuthorizeExpression(), ctx) ? AccessDecisionVoter.ACCESS_GRANTED : AccessDecisionVoter.ACCESS_DENIED;
    }

    public String getRolePrefix() {
        return this.rolePrefix;
    }

    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
        expressionHandler.setDefaultRolePrefix(rolePrefix);
    }

}
