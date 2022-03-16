package info.bfly.pay.controller;

import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.coupon.event.UserCouponSyncEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by XXSun on 6/8/2017.
 */

@Controller
@Scope(ScopeType.REQUEST)
@RequestMapping("/event")
public class EventPublisherController {

    @Autowired
    ApplicationContext applicationContext;

    @Log
    Logger log;

    /**
     * 4.0默认callBack;
     *
     * @return
     */
    @RequestMapping(value = "/{eventName}/{id}")
    @ResponseBody
    public String callBack(@PathVariable  String eventName,@PathVariable   String id) {

        log.info("get event={},value={}",eventName,id);
        if("userCoupon".equals(eventName)){
            applicationContext.publishEvent(new UserCouponSyncEvent(id));
        }
        return "success";
    }
}
