package info.bfly.archer.node.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.node.NodeConstants;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.node.model.NodeAttr;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Component
@Scope(ScopeType.VIEW)
public class NodeAttrHome extends EntityHome<NodeAttr> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1227991173414623876L;
    @Log
    static Logger log;
    private static final StringManager sm = StringManager.getManager(NodeConstants.Package);

    public NodeAttrHome() {
        setUpdateView(FacesUtil.redirect(NodeConstants.View.NODE_ATTR_LIST));
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (NodeAttrHome.log.isInfoEnabled()) {
            NodeAttrHome.log.info(NodeAttrHome.sm.getString("log.info.deleteNodeAttr", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        Set<Node> nodeSets = getInstance().getNodes();
        if (nodeSets != null && nodeSets.size() > 0) {
            FacesUtil.addWarnMessage(NodeAttrHome.sm.getString("canNotDeleteNodeAttr"));
            if (NodeAttrHome.log.isInfoEnabled()) {
                NodeAttrHome.log.info(NodeAttrHome.sm.getString("log.info.deleteNodeAttrUnsuccessful", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
            }
            return null;
        } else {
            return super.delete();
        }
    }
}
