package info.bfly.archer.common.controller;

import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.ImageUploadUtil;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.tooltip.Tooltip;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlPanelGroup;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class UploadFileHome implements Serializable {
    // FIXME:目前仅实现了单个图片（文件）的上传组件，需实现多个图片（文件）上传。
    private static final long serialVersionUID = -1531462606650854352L;
    @Log
    static  Logger log;
    private List<MyUploadedFile>           files;
    private MyUploadedFile                 oneFile;
    private HtmlPanelGroup                 panelGroup;

    public List<MyUploadedFile> getFiles() {
        return files;
    }

    public MyUploadedFile getOneFile() {
        return oneFile;
    }

    public HtmlPanelGroup getPanelGroup() {
        return panelGroup;
    }

    /**
     * 处理多个上传
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        MyUploadedFile file = handleUpload(event);
        if (file != null) {
            files.add(file);
            FacesUtil.addInfoMessage("上传成功！");
        } else {
            FacesUtil.addErrorMessage("上传失败！");
        }
    }

    /**
     * 处理单个文件上传
     *
     * @param event
     */
    public void handleOneFileUpload(FileUploadEvent event) {
        MyUploadedFile file = handleUpload(event);
        if (file != null) {
            oneFile = file;
            List<UIComponent> components = panelGroup.getChildren();
            for (UIComponent uiComp : components) {
                if (uiComp.getId().equals("thumbText")) {
                    UIInput url = (UIInput) uiComp;
                    url.setValue(file.getUrl());
                } else if (uiComp.getId().equals("thumbTooltip")) {
                    Tooltip tooltip = (Tooltip) uiComp;
                    for (UIComponent uiCompT : tooltip.getChildren()) {
                        if (uiCompT.getId().equals("thumbImage")) {
                            GraphicImage image = (GraphicImage) uiCompT;
                            image.setValue(file.getUrl());
                            image.setRendered(true);
                        }
                    }
                }
            }
            FacesUtil.getCurrentInstance().getPartialViewContext().getRenderIds().add(panelGroup.getClientId());
            FacesUtil.addInfoMessage("上传成功！");
        } else {
            FacesUtil.addErrorMessage("上传失败！");
        }
    }

    private MyUploadedFile handleUpload(FileUploadEvent event) {
        UploadedFile uploadFile = event.getFile();
        InputStream is = null;
        try {
            is = uploadFile.getInputstream();
            String url = ImageUploadUtil.upload(is, uploadFile.getFileName());
            return new MyUploadedFile(url, uploadFile);
        } catch (IOException e) {
            log.debug(e.getMessage());
            return null;
        }
    }

    public void setFiles(List<MyUploadedFile> files) {
        this.files = files;
    }

    public void setOneFile(MyUploadedFile oneFile) {
        this.oneFile = oneFile;
    }

    public void setPanelGroup(HtmlPanelGroup panelGroup) {
        this.panelGroup = panelGroup;
    }
}
