package info.bfly.archer.node.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.node.NodeConstants;
import info.bfly.archer.node.model.NodeAttr;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 菜单类型查询
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class NodeAttrList extends EntityQuery<NodeAttr> implements Serializable {
    private static final long          serialVersionUID = 1400036483687980854L;
    static               StringManager sm               = StringManager.getManager(NodeConstants.Package);
    @Log
    static Logger log;

    public NodeAttrList() {
        final String[] RESTRICTIONS = {"id like #{nodeAttrList.example.id}", "name like #{nodeAttrList.example.name}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
