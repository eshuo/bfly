package info.bfly.p2p.message.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.message.model.UserMessageTemplate;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Scope(ScopeType.VIEW)
public class UserMessageTemplateHome extends EntityHome<UserMessageTemplate> implements java.io.Serializable {
    private static final long serialVersionUID = 2040714042736934069L;
    @Log
    private static Logger log;
    @Resource
    private        HibernateTemplate              ht;
    @Resource
    private        LoginUserInfo                  loginUser;

    public UserMessageTemplateHome() {
        setUpdateView(FacesUtil.redirect("/admin/userMessage/userMessageTemplateList"));
    }

    /**
     * 生成编号
     */
    public void generateId() {
        if (this.getInstance().getUserMessageNode() != null && this.getInstance().getUserMessageWay() != null) {
            this.getInstance().setId(this.getInstance().getUserMessageNode().getId() + "_" + this.getInstance().getUserMessageWay().getId());
        } else {
            this.getInstance().setId(null);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        return super.save();
    }
}
