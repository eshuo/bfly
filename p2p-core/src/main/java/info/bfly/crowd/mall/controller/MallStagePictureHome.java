package info.bfly.crowd.mall.controller;

import info.bfly.archer.banner.controller.BannerPictureHome;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class MallStagePictureHome extends BannerPictureHome implements Serializable{

    /**
     * 
     */
    
    private static final long serialVersionUID = 1L;
  

}
