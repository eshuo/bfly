package info.bfly.app.protocol.controller;

import com.fasterxml.jackson.databind.module.SimpleModule;

import info.bfly.api.model.BaseResource;
import info.bfly.api.model.In;
import info.bfly.api.model.Out;
import info.bfly.api.service.ApiService;
import info.bfly.app.protocol.model.request.BannerValue;
import info.bfly.app.protocol.model.serializer.AreaItemSerializer;
import info.bfly.app.protocol.model.serializer.AreaToJsonSerializer;
import info.bfly.app.protocol.model.serializer.BannerPictureSerializer;
import info.bfly.app.protocol.model.serializer.ContactUsSerializer;
import info.bfly.app.protocol.service.ApiBannerService;
import info.bfly.app.protocol.service.ApiConfigService;
import info.bfly.archer.banner.model.BannerPicture;
import info.bfly.archer.user.model.AreaItem;
import info.bfly.archer.user.model.AreaToJson;
import info.bfly.archer.user.service.AreaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * Created by XXSun on 2016/12/28.
 */
@Controller
@RequestMapping("/v1.0")
public class ConfigController extends BaseResource {
    @Autowired
    private ApiService apiService;

    @Autowired
    private ApiConfigService configService;
    
    @Autowired
    private AreaService areaService;
    /**
     * config页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/config")
    @ResponseBody
    public Out demoPage(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        configService.getConfigsByType("app");
        Out out = apiService.parseOut(request);
        return out;
    }
    
    /**
     * 给前端传送具体区域参数
     * @param request
     * @return
     */
    @RequestMapping("/getAreas")
    @ResponseBody
    public Out getAreas(HttpServletRequest request){
        Out out = apiService.parseOut(request);
        SimpleModule module = new SimpleModule();
        module.addSerializer(AreaToJson.class,new AreaToJsonSerializer()).addSerializer(AreaItem.class, new AreaItemSerializer());
        out.setResult(areaService.getAreaToJson(), module);
        return out;
    }
    
    
    @Autowired
    private ApiBannerService apiBannerService;


    /**
     * 获取页面Banner
     *
     * @param request
     * @return
     */
    @RequestMapping("/banner")
    @ResponseBody
    public Out getBanner(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        BannerValue bannerValue = in.getFinalValue(BannerValue.class);
        List<BannerPicture> pictures = apiBannerService.getBannerById(bannerValue.getBannerId()).getPictures();
        Out out = apiService.parseOut(request);
        SimpleModule module = new SimpleModule();
        module.addSerializer(BannerPicture.class, new BannerPictureSerializer());
        out.setResult(pictures, module);
        return out;
    }

    @Autowired
    private ContactUsSerializer contactUsSerializer;

    /**
     * 获取联系我们内容
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/contactUs")
    public Out getContactUs(HttpServletRequest request) {
        Out out = apiService.parseOut(request);
        out.setResult(new Object(), new SimpleModule().addSerializer(Object.class , contactUsSerializer));
        return out;
    }
}
