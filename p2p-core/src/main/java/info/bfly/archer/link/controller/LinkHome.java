package info.bfly.archer.link.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.link.LinkConstants;
import info.bfly.archer.link.model.Link;
import info.bfly.archer.link.model.LinkType;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

@Component
@Scope(ScopeType.VIEW)
public class LinkHome extends EntityHome<Link> implements Serializable {
    private static final long          serialVersionUID = -5661050223988953680L;
    @Log
    static Logger log;
    private final static StringManager sm = StringManager.getManager(LinkConstants.Package);
    @Resource
    LoginUserInfo                      loginUserInfo;

    public LinkHome() {
        setUpdateView(FacesUtil.redirect(LinkConstants.View.LINK_LIST));
        setDeleteView(FacesUtil.redirect(LinkConstants.View.LINK_LIST));
    }

    @Override
    protected Link createInstance() {
        Link link = new Link();
        link.setType(new LinkType());
        link.setUrl("http://");
        link.setSeqNum(0);
        return link;
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (LinkHome.log.isInfoEnabled()) {
            LinkHome.log.info(LinkHome.sm.getString("log.info.deleteLink", loginUserInfo.getLoginUserId(), new Date(), getId()));
        }
        return super.delete();
    }
}
