package info.bfly.archer.banner.controller;

import info.bfly.archer.banner.BannerConstants;
import info.bfly.archer.banner.model.Banner;
import info.bfly.archer.banner.model.BannerPicture;
import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(ScopeType.REQUEST)
public class BannerHome extends EntityHome<Banner> implements Serializable {
    private static final long          serialVersionUID = 2373410201504708160L;
    @Log
    static Logger log;
    private static final StringManager sm               = StringManager.getManager(BannerConstants.Package);

    public BannerHome() {
        setUpdateView(FacesUtil.redirect(BannerConstants.View.BANNER_LIST));
    }

    @Override
    public Banner createInstance() {
        Banner banner = new Banner();
        banner.setPictures(new ArrayList<BannerPicture>());
        return banner;
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String save() {
        if (this.getInstance().getId() == null) {
            this.getInstance().setId(IdGenerator.randomUUID());
        }
        Banner banner = this.getInstance();
        List<BannerPicture> bps = banner.getPictures();
        if (bps == null || bps.size() == 0) {
            FacesUtil.addErrorMessage(BannerHome.sm.getString("pictureNullError"));
            return null;
        }
        for (int i = 0; i < bps.size(); i++) {
            bps.get(i).setBanner(banner);
            bps.get(i).setSeqNum(i + 1);
        }
        getBaseService().merge(getInstance());
        return getSaveView();
    }
}
