package info.bfly.core.jsf.lifecycle;

import java.util.List;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Components;
import org.omnifaces.util.Messages;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.webapp.MultipartRequest;

/**
 * 检查上传文件的扩展名是否合法
 *
 */
public class FileUploadFilenameExtensionValidation implements PhaseListener {
    /**
     *
     */
    private static final long serialVersionUID = -2911995074412280443L;

    @Override
    public void afterPhase(PhaseEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        final FacesContext fc = event.getFacesContext();
        HttpServletRequestWrapper requestWarpper = (HttpServletRequestWrapper) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletRequest request = (HttpServletRequest) requestWarpper.getRequest();
        if (request instanceof MultipartRequest) {
            List<FileUpload> fileUploads = Components.findComponentsInChildren(fc.getViewRoot(), FileUpload.class);
            for (FileUpload fileUpload : fileUploads) {
                String allowType = fileUpload.getAllowTypes();
                allowType = allowType.substring(1, allowType.length());
                allowType = allowType.substring(0, allowType.length() - 1);
                FileItem file = ((MultipartRequest) request).getFileItem(fileUpload.getClientId());
                if (file == null) {
                    continue;
                }
                // 判断文件扩展名是否被允许
                boolean isExtValid = false;
                String filenameExt = null;
                int dot = file.getName().lastIndexOf('.');
                if ((dot > -1) && (dot < (file.getName().length() - 1))) {
                    filenameExt = "." + file.getName().substring(dot + 1);
                }
                if (StringUtils.isNotEmpty(filenameExt) && Pattern.matches(allowType, filenameExt)) {
                    isExtValid = true;
                }
                if (!isExtValid) {
                    fc.addMessage(null, Messages.createError("filename extension:{0} is illegal!", file.getName()));
                    fc.renderResponse();
                }
            }
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.APPLY_REQUEST_VALUES;
    }
}
