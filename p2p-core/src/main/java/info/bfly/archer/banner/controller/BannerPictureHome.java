package info.bfly.archer.banner.controller;

import info.bfly.archer.banner.model.BannerPicture;
import info.bfly.archer.banner.service.BannerService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.ImageUploadUtil;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class BannerPictureHome implements Serializable {
    /**
     *
     */
    private static final long   serialVersionUID = 907527822093223064L;
    @Log
    static  Logger log;
    @Resource
    private BannerService                  bannerService;
    private List<BannerPicture>            bannerPictures;
    /**
     * 需要被修改图片的bannerPic
     */
    private BannerPicture                  needChangedPic;

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('LOAN_PUBLISH')")
    public void changeBannerPic(FileUploadEvent event) {
        UploadedFile uploadFile = event.getFile();
        InputStream is = null;
        try {
            is = uploadFile.getInputstream();
            if (getNeedChangedPic() != null) {
                getNeedChangedPic().setPicture(ImageUploadUtil.upload(is, uploadFile.getFileName()));
            } else {
                FacesUtil.addErrorMessage("被更改的banner为空。");
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
            return;
        }
    }
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('LOAN_PUBLISH')")
    public void deletePicture(BannerPicture bp) {
        if (bp != null) {
            try {
                bannerService.deleteBannerPicture(bp);
                bannerPictures.remove(bp);
            }
            catch (Exception e) {
                FacesUtil.addInfoMessage("此图片已被使用，无法删除。");
            }
        }
    }

    public List<BannerPicture> getBannerPictures() {
        return bannerPictures;
    }

    public BannerPicture getNeedChangedPic() {
        return needChangedPic;
    }

    /**
     * 处理图片上传
     *
     * @param event
     */
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('LOAN_PUBLISH')")
    public void handleBannerPicturesUpload(FileUploadEvent event) {
        UploadedFile uploadFile = event.getFile();
        InputStream is = null;
        try {
            is = uploadFile.getInputstream();
            BannerPicture pp = new BannerPicture();
            pp.setId(IdGenerator.randomUUID());
            pp.setPicture(ImageUploadUtil.upload(is, uploadFile.getFileName()));
            pp.setSeqNum(getBannerPictures().size() + 1);
            getBannerPictures().add(pp);
        }
        catch (IOException e) {
            log.debug(e.getMessage());
            return;
        }
    }

    public void initBannerPictures(List<BannerPicture> value) {
        if (bannerPictures == null) {
            bannerPictures = new ArrayList<BannerPicture>();
        }
        // FIXME 如果value为null，则addAll方法抛异常
        if (value != null) {
            bannerPictures.addAll(value);
        }
        bannerPictures = sortBySeqNum(bannerPictures);
    }

    public void moveDown(BannerPicture bp) {
        int currentIndex = bannerPictures.indexOf(bp);
        if (currentIndex == bannerPictures.size() - 1) {
            return;
        }
        else {
            bannerPictures.remove(bp);
            bannerPictures.add(currentIndex + 1, bp);
        }
    }

    public void moveUp(BannerPicture bp) {
        int currentIndex = bannerPictures.indexOf(bp);
        if (currentIndex == 0) {
            return;
        }
        else {
            bannerPictures.remove(bp);
            bannerPictures.add(currentIndex - 1, bp);
        }
    }

    public void setBannerPictures(List<BannerPicture> productPictures) {
        bannerPictures = productPictures;
    }

    public void setNeedChangedPic(BannerPicture needChangedPic) {
        this.needChangedPic = needChangedPic;
    }

    private List<BannerPicture> sortBySeqNum(List<BannerPicture> pics) {
        Collections.sort(pics, new Comparator<BannerPicture>() {
            @Override
            public int compare(BannerPicture o1, BannerPicture o2) {
                return o1.getSeqNum() - o2.getSeqNum();
            }
        });
        return pics;
    }
}
