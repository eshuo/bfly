package info.bfly.archer.product.controller;

import info.bfly.archer.product.ProductConstants;
import info.bfly.archer.product.model.ProductPicture;
import info.bfly.archer.product.service.ProductService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.StringManager;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class ProductPictureHome implements Serializable {
    private static final long serialVersionUID = -7387022166613641793L;
    @Log
    static Logger log;
    private static final StringManager sm = StringManager.getManager(ProductConstants.Package);
    @Resource
    private ProductService       productService;
    private List<ProductPicture> productPictures;
    private ProductPicture       selectedPicture;

    // private String productId;

    /**
     * 删除选中的图片
     */
    public void deleteSelectedPicture() {
        if (selectedPicture != null) {
            // FIXME:删除以后，不删除上传的图片?
            productService.deleteProductPicture(selectedPicture);
            productPictures.remove(selectedPicture);
        }
    }

    // public String getProductId() {
    // return productId;
    // }
    public List<ProductPicture> getProductPictures() {
        return productPictures;
    }

    public ProductPicture getSelectedPicture() {
        return selectedPicture;
    }

    /**
     * 处理产品图片上传
     *
     * @param event
     */
    public void handleProductPicturesUpload(FileUploadEvent event) {
        // FIXME:如何对每个产品的图片进行区分？
        UploadedFile uploadFile = event.getFile();
        // String folderPath = "upload" + File.separator + this.getProductId();
        String folderPath = "upload" + File.separator;
        // File folder = new File(FacesUtil.getAppRealPath() + folderPath);
        // if (!folder.exists()) {
        // folder.mkdirs();
        // }
        String filePath = folderPath + File.separator + IdGenerator.randomUUID() + ".jpg";
        String fileRealPath = FacesUtil.getAppRealPath() + filePath;
        try {
            InputStream iptS = uploadFile.getInputstream();
            FileOutputStream foptS = new FileOutputStream(fileRealPath);
            OutputStream optS = foptS;
            int c;
            while ((c = iptS.read()) != -1) {
                optS.write(c);
            }
            optS.flush();
            ProductPicture pp = new ProductPicture();
            pp.setId(IdGenerator.randomUUID());
            pp.setPicture(filePath.replace('\\', '/'));
            getProductPictures().add(pp);
        } catch (FileNotFoundException e) {
            log.debug(e.getMessage());
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        FacesUtil.addInfoMessage(event.getFile().getFileName() + ProductPictureHome.sm.getString("uploadSuccess"));
    }

    public void initProductPictures(List value) {
        productPictures = new ArrayList<ProductPicture>();
        productPictures.addAll(value);
    }

    /**
     * 下一张
     */
    public void nextPicture() {
        int index = productPictures.indexOf(selectedPicture);
        if (index == productPictures.size() - 1) {
            index = 0;
        } else {
            index++;
        }
        selectedPicture = productPictures.get(index);
    }

    /**
     * 上一张
     */
    public void previousPicture() {
        int index = productPictures.indexOf(selectedPicture);
        if (index == 0) {
            index = productPictures.size() - 1;
        } else {
            index--;
        }
        selectedPicture = productPictures.get(index);
    }

    // public void setProductId(String productId) {
    // this.productId = productId;
    // }
    public void setProductPictures(List<ProductPicture> productPictures) {
        this.productPictures = productPictures;
    }

    public void setSelectedPicture(ProductPicture selectedPicture) {
        this.selectedPicture = selectedPicture;
    }
}
