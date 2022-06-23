package info.bfly.p2p.message.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.message.model.UserMessage;
import info.bfly.p2p.message.model.UserMessageTemplate;
import info.bfly.p2p.message.service.MessageService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class UserMessageHome extends EntityHome<UserMessage> implements java.io.Serializable {
    private static final long serialVersionUID = 3529894113836360385L;
    @Log
    private static Logger log;
    @Resource
    private        LoginUserInfo                  loginUser;
    @Resource
    private        MessageService                 messageService;
    private        List<UserMessageTemplate>      umts;

    public List<UserMessageTemplate> getUmts() {
        return umts;
    }

    public void initUmts(String userId) {
        umts = messageService.getUserMessagesTemplatesByUserId(userId);
    }

    public String saveUserMessages() {
        messageService.saveUserMessages(loginUser.getLoginUserId(), umts);
        FacesUtil.addInfoMessage("设置成功！");
        return "pretty:userMessageTypeSet";
    }

    public void setUmts(List<UserMessageTemplate> umts) {
        this.umts = umts;
    }
}
