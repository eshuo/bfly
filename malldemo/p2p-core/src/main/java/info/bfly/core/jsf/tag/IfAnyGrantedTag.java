package info.bfly.core.jsf.tag;

import info.bfly.core.jsf.el.SpringSecurityELLibrary;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.*;
import java.io.IOException;

/**
 *
 *
 *
 * @version: 1.0 Create at: 2014-3-13 下午5:14:30
 */
public class IfAnyGrantedTag extends TagHandler {
    private final TagAttribute roles;

    public IfAnyGrantedTag(ComponentConfig componentConfig) {
        super(componentConfig);
        roles = getRequiredAttribute("roles");
        if (roles == null) throw new TagAttributeException(roles, "The `roles` attribute has to be specified!");
    }

    @Override
    public void apply(FaceletContext faceletContext, UIComponent uiComponent) throws IOException, FacesException, ELException {
        if (roles == null) {
            nextHandler.apply(faceletContext, uiComponent);
        }
        String roles = this.roles.getValue(faceletContext);
        if (roles == null || "".equals(roles.trim())) {
            nextHandler.apply(faceletContext, uiComponent);
        }
            if (SpringSecurityELLibrary.ifAnyGranted(roles)) {
            nextHandler.apply(faceletContext, uiComponent);
        }
    }
}
