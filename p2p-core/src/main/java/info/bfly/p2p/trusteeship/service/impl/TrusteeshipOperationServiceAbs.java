package info.bfly.p2p.trusteeship.service.impl;

import info.bfly.p2p.trusteeship.service.TrusteeshipOperationService;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

public abstract class TrusteeshipOperationServiceAbs<T> implements TrusteeshipOperationService<T> {
    public void sendOperation(String content, String charset, FacesContext facesContext) throws IOException {
        ExternalContext ec = facesContext.getExternalContext();
        ec.responseReset();
        ec.setResponseCharacterEncoding(charset);
        ec.setResponseContentType("text/html");
        ec.getResponseOutputWriter().write(content);
        facesContext.responseComplete();
    }

}
