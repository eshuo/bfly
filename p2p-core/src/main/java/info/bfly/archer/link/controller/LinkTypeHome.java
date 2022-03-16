package info.bfly.archer.link.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.link.LinkConstants;
import info.bfly.archer.link.model.Link;
import info.bfly.archer.link.model.LinkType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class LinkTypeHome extends EntityHome<LinkType> implements Serializable {
    private static final long serialVersionUID = -8797360750051118119L;
    @Log
    static Logger log;
    private static final StringManager sm = StringManager.getManager(LinkConstants.Package);
    @Resource
    private HibernateTemplate          ht;

    public LinkTypeHome() {
        setUpdateView(FacesUtil.redirect(LinkConstants.View.LINK_TYPE_LIST));
    }

    @Override
    // FIXME: 查询方式换成HQL 。
    @Transactional(readOnly = false)
    public String delete() {
        String typeId = this.getInstance().getId();
        DetachedCriteria criteria = DetachedCriteria.forClass(Link.class);
        criteria.add(Restrictions.eq("type.id", typeId));
        ht.setCacheQueries(true);
        List<Link> links = (List<Link>) ht.findByCriteria(criteria);
        if (links.size() > 0) {
            FacesUtil.addWarnMessage(LinkTypeHome.sm.getString("canNotDeleteLinkType"));
            if (LinkTypeHome.log.isInfoEnabled()) {
                LinkTypeHome.log.info(LinkTypeHome.sm.getString("log.info.deleteLinkTypeUnsuccessful", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
            }
            return null;
        }
        return super.delete();
    }

    public List<LinkType> getAllLinkType() {
        return super.findAll();
    }
}
