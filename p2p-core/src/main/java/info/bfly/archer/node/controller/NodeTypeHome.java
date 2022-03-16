package info.bfly.archer.node.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.node.NodeConstants;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.node.model.NodeType;
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
@Scope(ScopeType.REQUEST)
public class NodeTypeHome extends EntityHome<NodeType> implements Serializable {
    private static final long serialVersionUID = 3223980910989273371L;
    @Log
    static Logger log;
    private static final StringManager sm = StringManager.getManager(NodeConstants.Package);

    public NodeTypeHome() {
        setUpdateView(FacesUtil.redirect(NodeConstants.View.NODE_TYPE_LIST));
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (NodeTypeHome.log.isInfoEnabled()) {
            NodeTypeHome.log.info(NodeTypeHome.sm.getString("log.info.deleteNodeType", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        Set<Node> nodeSets = getInstance().getNodes();
        if (nodeSets != null && nodeSets.size() > 0) {
            FacesUtil.addWarnMessage(NodeTypeHome.sm.getString("canNotDeleteNodeType"));
            if (NodeTypeHome.log.isInfoEnabled()) {
                NodeTypeHome.log.info(NodeTypeHome.sm.getString("log.info.deleteNodeTypeUnsuccessful", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
            }
            return null;
        } else {
            return super.delete();
        }
    }
}
