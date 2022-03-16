package info.bfly.app.protocol.service;

import info.bfly.archer.banner.controller.BannerList;
import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@Scope(ScopeType.REQUEST)
@Service
public class ApiBannerService extends BannerList implements Serializable {

    public ApiBannerService(){
        final String[] RESTRICTIONS = { "id like #{apiBannerService.example.id}", "description like #{apiBannerService.example.description}" };
        ArrayList<String> a = new ArrayList(Arrays.asList(RESTRICTIONS));
        setRestrictionExpressionStrings(a);
    }

}
