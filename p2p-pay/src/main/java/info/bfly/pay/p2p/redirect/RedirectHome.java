package info.bfly.pay.p2p.redirect;

import info.bfly.archer.common.service.AuthService;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

import static info.bfly.core.jsf.util.FacesUtil.sendRedirect;

/**
 * Created by Administrator on 2017/5/3 0003.
 */
@Component
@Scope(ScopeType.VIEW)
public class RedirectHome implements Serializable{


    private static final long serialVersionUID = 1852341109454237784L;
    @Autowired
    private AuthService authService;

    @Autowired
    private LoginUserInfo userInfo;

    @Resource
    HibernateTemplate ht;


    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    private String authType;

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getRedirectUrl() {

        String loginUserId = userInfo.getLoginUserId();

        if (loginUserId == null || loginUserId.length() == 0) {
            FacesUtil.addErrorMessage("用户未登陆！");
            return "";
        }

        if (ticket == null || ticket.length() == 0) {
            return "";
        }

        if (authType == null ||  authType.length() == 0) {
            return "";
        }


        String authCode = authService.getAuthCode(loginUserId, getTicket(), getAuthType());

        try {
            authService.verifyAuthInfo(loginUserId, getTicket(), authCode, getAuthType());
        } catch (Exception e) {
            //todo 跳转到错误页面
            FacesUtil.addErrorMessage("操作错误！");
//            return "";
        }
        if(authCode==null || authCode.length()==0){
            FacesUtil.addErrorMessage("地址为空！");
            return "";
        }
        sendRedirect(authCode);

        return "";

    }


}
