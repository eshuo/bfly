package info.bfly.archer.node.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.node.NodeConstants;
import info.bfly.archer.node.model.NodeType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 菜单类型查询
 * 
 * @author wanghm
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class NodeTypeList extends EntityQuery<NodeType> implements java.io.Serializable {
    private static final long          serialVersionUID = 9057256450216810237L;
    static               StringManager sm               = StringManager.getManager(NodeConstants.Package);
    @Log
    static Logger log;

    public NodeTypeList() {
        final String[] RESTRICTIONS = {"id like #{nodeTypeList.example.id}", "name like #{nodeTypeList.example.name}", "description like #{nodeTypeList.example.description}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
