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
import info.bfly.crowd.mall.model.Goods;
import info.bfly.crowd.traceability.TraceabilityConstants;
import info.bfly.crowd.traceability.model.TraceColumn;
import info.bfly.crowd.traceability.model.TraceTemplate;
import info.bfly.crowd.traceability.service.TraceService;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
public class TraceTemplateHome extends EntityHome<TraceTemplate> implements
        Serializable {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = -864947400686983692L;

    @Resource
    private TraceService traceService;

    public TraceTemplateHome() {
        this.setUpdateView(FacesUtil
                .redirect(TraceabilityConstants.TemplateView.TEMPLATE_LIST));
        this.setDeleteView(FacesUtil
                .redirect(TraceabilityConstants.TemplateView.TEMPLATE_LIST));
        this.setSaveView(FacesUtil
                .redirect(TraceabilityConstants.TraceRecordView.RECORD_LIST));
    }

    /**
     * 
     * @Title: deleteTemplate
     * @Description: 删除模板
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public String deleteTemplate(String id) {
        TraceTemplate template = traceService.findTemplateById(id);
        Set<Goods> goods = template.getGoodsList();
        TraceTemplate referrer = template.getReferrer();
        List<TraceColumn> columnList = template.getTraceColumns();
        if (null != goods && goods.size() != 0) {
            FacesUtil.addErrorMessage("删除失败，请先删除关联的回报物");
            return null;
        }
        if (null != referrer || (null != columnList && columnList.size() != 0)) {
            FacesUtil.addErrorMessage("删除失败，请先删除关联的可追溯模板。");
            return null;
        }
        super.delete(id);
        return FacesUtil.redirect("/admin/traceTemplate/traceTemplateList?type=" + template.getType());
    }
    
    /**
     * 删除档案
     * @param id
     * @return
     */
    public String deleteTraceRecord(String id){
        traceService.deleteTraceRecord(id);
        return getSaveView();
    }

    /**
     * 
     * @Title: createTemplate
     * @Description:
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public String createTemplate() {
        traceService.createTemplate(this.getInstance());
        return FacesUtil
                .redirect("/admin/traceTemplate/traceTemplateList?type="
                        + this.getInstance().getType());
    }

    /**
     * 
     * @Title: updateTemplate
     * @Description: 修改模板名称
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public String updateTemplate() {
        traceService.updateTemplate(this.getInstance());
        return FacesUtil.redirect("/admin/traceTemplate/traceTemplateList?type="
                + this.getInstance().getType());
    }
    
    public String saveTraceRecord(){
        traceService.saveRecord(this.getInstance());
        return getSaveView();
    }
}
