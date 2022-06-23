package info.bfly.archer.link.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.link.LinkConstants;
import info.bfly.archer.link.model.Link;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class LinkList extends EntityQuery<Link> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 719089665602175434L;
    @Log
    static Logger log;

    public LinkList() {
        final String[] RESTRICTIONS = { "id like #{linkList.example.id}", "name like #{linkList.example.name}", "url like #{linkList.example.url}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public List<Link> getAllLinks() {
        return (List<Link>) getHt().find("from Link order by seqNum");
    }

    /**
     * 获取首页面所有的链接
     *
     * @return
     */
    public List<Link> getFrontLinks() {
        return getLinks(LinkConstants.LinkPosition.FRONT);
    }

    /**
     * 获取内页所有链接
     *
     * @return
     */
    public List<Link> getInnerLinks() {
        return getLinks(LinkConstants.LinkPosition.INNER);
    }

    public List<Link> getLinks(final String type) {
        return (List<Link>) getHt().findByNamedQuery("Link.findLinkByPositionOrderBySeqNumAndName", type);
    }
}
