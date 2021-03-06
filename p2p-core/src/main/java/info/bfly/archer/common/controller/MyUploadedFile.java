package info.bfly.archer.common.controller;

import org.primefaces.model.UploadedFile;

/**
 * 上传的图片
 *
 * @author Administrator
 *
 */
public class MyUploadedFile {
    private String       url;
    private UploadedFile file;

    public MyUploadedFile(String url) {
        super();
        this.url = url;
    }

    public MyUploadedFile(String url, UploadedFile file) {
        super();
        this.url = url;
        this.file = file;
    }

    public UploadedFile getFile() {
        return file;
    }

    public String getUrl() {
        return url;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
