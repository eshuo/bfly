package info.bfly.core.jsf.tag;

import info.bfly.core.jsf.el.SpringSecurityELLibrary;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagHandler;

/**
 * Taglib to combine the Spring-Security Project with Facelets <br />
 *
 * This is the Class responsible for making the <br />
 * <code><br />
 *     &lt;sec:isAnonymous;&gt;<br />
 *         The components you want to show only when the user is anonymous<br />
 *     lt;/sec:isAnonymous&gt;<br />
 * </code> work.
 *
 *
 * @author Grzegorz Blaszczyk - http://www.blaszczyk-consulting.com/
 * @version %I%, %G%
 * @since 0.5
 */
public class IsAnonymousTag extends TagHandler {
    public IsAnonymousTag(ComponentConfig componentConfig) {
        super(componentConfig);
    }

    @Override
    public void apply(FaceletContext faceletContext, UIComponent uiComponent) throws IOException, FacesException, ELException {
        if (SpringSecurityELLibrary.isAnonymous()) {
            nextHandler.apply(faceletContext, uiComponent);
        }
    }
}
