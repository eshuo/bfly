package info.bfly.archer.banner.service.impl;

import info.bfly.archer.banner.model.BannerPicture;
import info.bfly.archer.banner.service.BannerService;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.ImageUploadUtil;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class BannerServiceImpl implements BannerService {
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('LOAN_PUBLISH')")
    public void deleteBannerPicture(BannerPicture bannerPicture) {
        ImageUploadUtil.delete(FacesUtil.getAppRealPath() + bannerPicture.getPicture());
        BannerPicture pp = ht.get(BannerPicture.class, bannerPicture.getId());
        if (pp != null) {
            ht.delete(pp);
        }
    }
}
