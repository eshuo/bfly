/**   
 * @Title: TraceColumnList.java 
 * @Package info.bfly.order.traceability.controller 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月10日 上午10:11:27 
 * @version V1.0   
 */
package info.bfly.crowd.traceability.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;



import info.bfly.crowd.traceability.model.TraceColumn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月10日 上午10:11:27 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: TraceColumnList
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月10日 上午10:11:27
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class TraceColumnList extends EntityQuery<TraceColumn> implements
        Serializable {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = -3061212055958927630L;
    
    private List<TraceColumn> columnTree;
    
    private String templateId;
    
    private String groupAndName;
    
    private TreeNode root;
    @Resource
    private HibernateTemplate ht;
    
    public List<TraceColumn> getColumnTree() {
        columnTree = new ArrayList<TraceColumn>();
        List<TraceColumn> list = (List<TraceColumn>)ht.findByNamedQuery("TraceColumn.findColumnByTemplateAndItem", templateId);
        ht.clear();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TraceColumn column = list.get(i);
                if (column.getReferrer() == null && !column.getId().equals(((TraceColumn) FacesUtil.getExpressionValue("#{traceColumnHome.instance}")).getId())) {
                    columnTree.add(column);
                    buildMenuTree(column, list);
                }
            }
        }
        return columnTree;
    }
    
    
    private void buildMenuTree(TraceColumn parent, List<TraceColumn> columnList) {
                
        for (int i = 0; i < columnList.size(); i++) {
            TraceColumn column = columnList.get(i);
            if (column.getReferrer() != null && StringUtils.isNotEmpty(column.getReferrer().getId()) && column.getReferrer().getId().equals(parent.getId())
                    && !column.getId().equals(((TraceColumn) FacesUtil.getExpressionValue("#{traceColumnHome.instance}")).getId())) {
                int level = 0;// 记录parent是第几级菜单
                TraceColumn newColumn = parent.getReferrer();
                while (newColumn != null) {
                    level++;
                    newColumn = newColumn.getReferrer();
                }
                String prefixStr = "";
                int prod = (int) Math.pow(2, (level + 1));
                for (int j = 0; j < prod; j++) {
                    if (j == (prod - 1)) {
                        prefixStr = prefixStr + "&nbsp;-";
                    } else {
                        prefixStr = prefixStr + "&nbsp;";
                    }
                }
                column.setColumnName(prefixStr + column.getColumnName());
                columnTree.add(column);
                buildMenuTree(column, columnList);
            }
        }
    }
    
    private TreeNode createNewNode(TraceColumn column, TreeNode parentNode) {
        TreeNode newNode = new DefaultTreeNode(column, parentNode);
        newNode.setExpanded(true);
        return newNode;
    }
    
    private void initTreeNode(List<TraceColumn> columnList,
            TraceColumn parentColumn, TreeNode parentNode) {
        if (columnList != null && columnList.size() > 0) {
            for (TraceColumn m : columnList) {
                if (m.getReferrer() != null
                        && m.getReferrer().getId().equals(parentColumn.getId())) {
                    TreeNode newNode = createNewNode(m, parentNode);
                    initTreeNode(columnList, m, newNode);
                }
            }
        }
    }

    public TreeNode getRoot() {
        root = new DefaultTreeNode("root", null);
        root.setExpanded(true);
        List<TraceColumn> columnList = null;
        //根据模板编号查询所属的模板字段
        columnList = (List<TraceColumn>)ht.findByNamedQuery("TraceColumn.findColumnByTemplateId", templateId);
        if (columnList != null && columnList.size() > 0) {
            for (TraceColumn m : columnList) {
                if ((m.getReferrer() == null || StringUtils.isEmpty(m
                        .getReferrer().getId()))
                        || !columnList.contains(m.getReferrer())) {
                    TreeNode newChild = createNewNode(m, root);
                    initTreeNode(columnList, m, newChild);
                }
            }
        }
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
    public void setColumnTree(List<TraceColumn> columnTree) {
        this.columnTree = columnTree;
    }

    public String getGroupAndName() {
        return groupAndName;
    }

    public void setGroupAndName(String groupAndName) {
        this.groupAndName = groupAndName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    
    
}
