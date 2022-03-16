package info.bfly.crowd.traceability.service;

import info.bfly.crowd.orders.model.Order;
import info.bfly.crowd.traceability.model.TraceColumn;
import info.bfly.crowd.traceability.model.TraceItem;
import info.bfly.crowd.traceability.model.TraceTemplate;

/**
 * Created by XXSun on 3/22/2017.
 */
public interface TraceService {

    /**
     * 合并两个模板
     * @return
     */
    TraceTemplate merge(TraceTemplate source,TraceTemplate target);
    
    /**
     * 
    * @Title: createTemplate 
    * @Description:  新建模板
    * @param @param template    设定文件 
    * @return void    返回类型 
    * @throws
     */
    void createTemplate(TraceTemplate template);
    
    
   /**
    * 
   * @Title: updateTemplate 
   * @Description:  修改模板名称
   * @param @param template    设定文件 
   * @return void    返回类型 
   * @throws
    */
    void updateTemplate(TraceTemplate template);
    
    /**
     * 
    * @Title: createColumn 
    * @Description:  
    * @param @param column    设定文件 
    * @return void    返回类型 
    * @throws
     */
    void createColumn(TraceColumn column);
    
    /**
     * 
    * @Title: findTemplateById 
    * @Description:  
    * @param @param id
    * @param @return    设定文件 
    * @return TraceTemplate    返回类型 
    * @throws
     */
    TraceTemplate findTemplateById(String id);
    
    /**
     * 
    * @Title: findColumnById 
    * @Description:  
    * @param @param id
    * @param @return    设定文件 
    * @return TraceColumn    返回类型 
    * @throws
     */
    TraceColumn findColumnById(String id);
    /**
     * 
    * @Title: updateColumn 
    * @Description:  
    * @param @param traceColumn    设定文件 
    * @return void    返回类型 
    * @throws
     */
    void updateColumn(TraceColumn column,String referrerId);
    /**
     * 
    * @Title: deleteColumn 
    * @Description:  
    * @param @param columnId    设定文件 
    * @return void    返回类型 
    * @throws
     */
    void deleteColumn(String columnId);
    
    /**
     * @author wangxt
     * @Title 添加项目
     */
    void addItem(TraceItem item, String templateId);
    
    /**
     * @author wangxt
     * @param traceTemplate
     * 新增一条档案
     */
    void saveRecord(TraceTemplate traceTemplate);
    
    void saveTraceColumnValue(TraceColumn column, String templateId);

    /**
     * 删除组，并删除关联档案中的组
     * @param id
     */
    void deleteAllconnTraceColumn(String id, String templateId);
    
    /**
     * 生成档案
     */
    void saveGoodsRecord(Order order);
    
    /**
     * 删除档案
     */
    void deleteTraceRecord(String id);
}
