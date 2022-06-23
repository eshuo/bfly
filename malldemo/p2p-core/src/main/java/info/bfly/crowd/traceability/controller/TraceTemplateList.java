/**   
* @Title: TraceTemplateList.java 
* @Package info.bfly.order.traceability 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月6日 上午9:53:23 
* @version V1.0   
*/package info.bfly.crowd.traceability.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.traceability.model.TraceTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @date 创建时间：2017年4月6日 上午9:53:23 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: TraceTemplateList 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月6日 上午9:53:23 
 *  
 */
@Component
@Scope(ScopeType.VIEW)
public class TraceTemplateList extends EntityQuery<TraceTemplate> implements
        Serializable {

    /** 
    * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
    */ 
    private static final long serialVersionUID = -2156907305027992562L;
    
    private static final String lazyModelCountHql = "select count(distinct traceTemplate) from TraceTemplate traceTemplate ";

    private static final String lazyModelHql = "select distinct traceTemplate from TraceTemplate traceTemplate ";
    
    private TreeNode                                  root;
    @Resource
    private HibernateTemplate                         ht;
    
    public TraceTemplateList(){
        setCountHql(lazyModelCountHql);
        setHql(lazyModelHql);
        final String[] RESTRICTIONS = {
                "traceTemplate.type = #{traceTemplateList.example.type}",
                "traceTemplate.id like #{traceTemplateList.example.id}",
                "traceTemplate.templateName like #{traceTemplateList.example.templateName}",
                "traceTemplate.status like 1"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
    
    
    
    
     private TreeNode createNewNode(TraceTemplate template, TreeNode parentNode) {
            TreeNode newNode = new DefaultTreeNode(template, parentNode);
            newNode.setExpanded(true);
            return newNode;
        }
     
     private void initTreeNode(List<TraceTemplate> templateList, TraceTemplate parentTemplate, TreeNode parentNode) {
            if (templateList != null && templateList.size() > 0) {
                for (TraceTemplate m : templateList) {
                    if (m.getParent() != null && m.getParent().getId().equals(m.getId())) {
                        TreeNode newNode = createNewNode(m, parentNode);
                        initTreeNode(templateList, m, newNode);
                    }
                }
            }
        }
    
     public TreeNode getRoot() {
            root = new DefaultTreeNode("root", null);
            root.setExpanded(true);
            List<TraceTemplate> templateList = new ArrayList<TraceTemplate>();
            addOrder("order", EntityQuery.DIR_ASC);
            templateList = (List<TraceTemplate>) ht.find(getRenderedHql(), getParameterValues());
            if (templateList != null &&templateList.size() > 0) {
                for (TraceTemplate m : templateList) {
                    if ((m.getReferrer() == null || StringUtils.isEmpty(m.getReferrer().getId())) || !templateList.contains(m.getReferrer())) {
                        TreeNode newChild = createNewNode(m, root);
                        initTreeNode(templateList, m, newChild);
                    }
                }
            }
            return root;
        }

        public void setRoot(TreeNode root) {
            this.root = root;
        }
}
