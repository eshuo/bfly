/**   
 * @Title: TraceTemplateHome.java 
 * @Package info.bfly.order.traceability.controller 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月6日 上午9:55:03 
 * @version V1.0   
 */
package info.bfly.crowd.traceability.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;




import info.bfly.crowd.traceability.TraceabilityConstants;
import info.bfly.crowd.traceability.model.TraceColumn;
import info.bfly.crowd.traceability.model.TraceItem;
import info.bfly.crowd.traceability.model.TraceTemplate;
import info.bfly.crowd.traceability.service.TraceService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月6日 上午9:55:03 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: TraceTemplateHome
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月6日 上午9:55:03
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class TraceColumnHome extends EntityHome<TraceColumn> implements
        Serializable {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = -864947400686983692L;

    @Resource
    private TraceService traceService;
    @Resource
    HibernateTemplate ht;

    private String templateId;


    public TraceColumnHome() {
        this.setUpdateView(FacesUtil
                .redirect(TraceabilityConstants.TemplateView.TEMPLATE_LIST));
        this.setDeleteView(FacesUtil
                .redirect(TraceabilityConstants.TemplateView.TEMPLATE_LIST));
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }


    @Override
    protected void initInstance() {
        super.setInstance(this.createInstance());
    };

    @Override
    protected TraceColumn createInstance() {
        TraceColumn column = null;
        if (super.isIdDefined()) {
            column = traceService.findColumnById(this.getId());
            if (null == column.getReferrer())
                column.setReferrer(new TraceColumn());
        } else {
            column = new TraceColumn();
            column.setReferrer(new TraceColumn());
            column.setOrder(0);
        }

        String templateId = FacesUtil.getParameter("templateId");
        if (StringUtils.isNotEmpty(templateId)) {
            TraceTemplate template = traceService.findTemplateById(templateId);
            column.setParent(template);
        } else {
            column.setParent(new TraceTemplate());
        }
        
        String parentId = FacesUtil.getParameter("parentId");
        if(StringUtils.isNotEmpty(parentId)){
            TraceColumn parent = new TraceColumn();
            parent.setId(parentId);
            column.setReferrer(parent);
        }

        if(StringUtils.isNotEmpty(FacesUtil.getParameter("type"))){
            if("add".equals(FacesUtil.getParameter("type"))){
                TraceItem traceItem = new TraceItem();
                List<TraceItem> traceItems = new ArrayList<TraceItem>();
                traceItems.add(traceItem);
                column.setTraceItems(traceItems);
            }
        }
        return column;
    };

    private boolean parentIsChild(String columnId, String parentId) {
        boolean result = false;
        List<TraceColumn> columnList = (List<TraceColumn>) ht.findByNamedQuery(
                "TraceColumn.findColumnByReferrerId", columnId);
        if (columnList != null) {
            for (int i = 0; i < columnList.size(); i++) {
                String childId = columnList.get(i).getId();
                if (childId.equals(parentId)) {
                    FacesUtil.addWarnMessage("child_error");
                    result = true;
                    return result;
                } else {
                    result = parentIsChild(childId, parentId);
                    if (result)
                        return result;
                }
            }
        }
        return result;
    }

    private boolean parentIsItself(String columnId, String parentId) {
        boolean result = false;
        if (columnId.equals(parentId)) {
            FacesUtil.addWarnMessage("isItSelf_error ");
            result = true;
        }
        return result;
    }
    
    /**
     * 
    * @Title: saveTraceColumn 
    * @Description:  保存节点
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public String saveTraceColumn() {
        String referrerId = this.getInstance().getReferrer().getId();
        if (!StringUtils.isNotEmpty(referrerId)){
            this.getInstance().setReferrer(null);
        }else{
            this.getInstance().setReferrer(traceService.findColumnById(referrerId));
        }
        traceService.createColumn(this.getInstance());
        return FacesUtil.redirect("/admin/traceTemplate/traceTemplateEdit.htm?id=" + templateId);
    }
    
    /**
     * 
    * @Title: updateTraceColumn 
    * @Description:  修改节点
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public String updateTraceColumn() {
        
         // // 当前菜单的上级菜单不能是它本身
         if (parentIsItself(getInstance().getId(), getInstance().getReferrer().getId())) {
             return FacesUtil.redirect("/admin/traceTemplate/traceTemplateEdit.htm?id="+ templateId);
         }
         // 父菜单不能是它的子菜单
         if (parentIsChild(getInstance().getId(), getInstance().getReferrer().getId())) {
             return FacesUtil.redirect("/admin/traceTemplate/traceTemplateEdit.htm?id="+ templateId);
         }

        traceService.updateColumn(this.getInstance(),this.getInstance().getReferrer().getId());
        
        return FacesUtil.redirect("/admin/traceTemplate/traceTemplateEdit.htm?id=" + templateId);
    }
    
    /**
     * 
    * @Title: deleteTraceColumn 
    * @Description:  删除档案字段
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public String deleteTraceColumn(){
        traceService.deleteColumn(this.getId());
        return FacesUtil.redirect("/admin/trace/traceEdit.htm?id=" + templateId);
    }
	
	/**
	 * 删除模板中的组，并删除所有关联档案中的组
	 * @return
	 */
	public String deleteAllConnTraceColumn(){
	    traceService.deleteAllconnTraceColumn(this.getId(), templateId);
	    return FacesUtil.redirect("/admin/traceTemplate/traceTemplateEdit.htm?id=" + templateId);
	}
	
	public String saveTraceColumnValue(){
	    traceService.saveTraceColumnValue(getInstance(),templateId);
	    return FacesUtil.redirect("/admin/traceTemplate/traceTemplateEdit.htm?id=" + templateId);
	}
}
