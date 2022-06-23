package info.bfly.archer.banner.controller;

import info.bfly.archer.banner.model.Banner;
import info.bfly.archer.banner.model.BannerPicture;
import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * banner查询
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class BannerList extends EntityQuery<Banner> implements Serializable {
    private static final long serialVersionUID = -1350682013319140386L;

    public BannerList() {
        final String[] RESTRICTIONS = { "id like #{bannerList.example.id}", "description like #{bannerList.example.description}" };
        ArrayList<String> a = new ArrayList(Arrays.asList(RESTRICTIONS));
        setRestrictionExpressionStrings(a);
    }

    public Banner getBannerById(String bannerId) {
        return getHt().get(Banner.class, bannerId);
    }

    public BannerPicture getFirstPic(String bannerId) {
        Banner banner = getBannerById(bannerId);
        if (banner != null) {
            List<BannerPicture> pics = banner.getPictures();
            if (pics != null && pics.size() > 0) {
                return pics.get(0);
            }
        }
        return new BannerPicture();
    }
}
