package info.bfly.pay.p2p.user.controller;

import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.pay.controller.SinaUserController;
import info.bfly.pay.p2p.user.service.SinaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

import static info.bfly.core.jsf.util.FacesUtil.sendRedirect;

/**
 * Created by Administrator on 2017/4/12 0012.
 */
@Component
@Scope(ScopeType.VIEW)
public class SinaPayPasswordHome implements Serializable{


    private static final long serialVersionUID = 2928430510163723084L;
    @Resource
    SinaUserController sinaUserController;

    @Resource
    private LoginUserInfo loginUser;


    @Autowired
    private SinaUserService sinaUserService;


    public boolean queryPayPassword() {
        return sinaUserService.queryIsSetPayPasswordSinaPay(loginUser.getLoginUserId());
    }


    public String setPayPasswordSinaPay() {

        String url = null;
        try {

            if (queryPayPassword()) {

                FacesUtil.addErrorMessage("您已经设置过交易密码!");
                return "";
            } else {
                url = sinaUserController.setPayPasswordSinaPay(loginUser.getLoginUserId());
            }


        } catch (TrusteeshipReturnException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotEmpty(url))
            sendRedirect(sinaUserController.getMainUrl() + url);


        return "";
    }


    public String modifyPayPasswordSinaPay() {


        String url = null;
        try {

            if (queryPayPassword()) {
                url = sinaUserController.modifyPayPasswordSinaPay(loginUser.getLoginUserId());
            } else {
                url = sinaUserController.setPayPasswordSinaPay(loginUser.getLoginUserId());
            }


        } catch (TrusteeshipReturnException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotEmpty(url))
            sendRedirect(sinaUserController.getMainUrl() + url);

        return "";
    }


    public String findPayPasswordSinaPay() {


        String url = null;
        try {

            if (queryPayPassword()) {
                url = sinaUserController.findPayPasswordSinaPay(loginUser.getLoginUserId());
            } else {
                url = sinaUserController.setPayPasswordSinaPay(loginUser.getLoginUserId());
            }
        } catch (TrusteeshipReturnException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotEmpty(url))
            sendRedirect(sinaUserController.getMainUrl() + url);


        return "";
    }

}
