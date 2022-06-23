package info.bfly.archer.node.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.node.NodeConstants;
import info.bfly.archer.node.model.NodeBody;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(ScopeType.VIEW)
public class NodeBodyHome extends EntityHome<NodeBody> implements Serializable {
    private static final long serialVersionUID = -4887256797617142278L;
    @Log
    static Logger log;
    private static final StringManager sm = StringManager.getManager(NodeConstants.Package);

    public NodeBody getBodyById() {
        NodeBody body = getBaseService().get(NodeBody.class, "7d806664cd904a2c97e9d133e0b7b96f");
        return body;
    }
}
