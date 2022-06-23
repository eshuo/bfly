/**   
 * @Title: TraceServiceImpl.java 
 * @Package info.bfly.order.traceability.impl 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月10日 下午3:37:12 
 * @version V1.0   
 */
package info.bfly.crowd.traceability.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.bfly.core.annotations.Log;
import info.bfly.crowd.mall.model.Goods;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.model.Order;
import info.bfly.crowd.orders.model.OrderCache;
import info.bfly.crowd.traceability.model.TraceColumn;
import info.bfly.crowd.traceability.model.TraceItem;
import info.bfly.crowd.traceability.model.TraceTemplate;
import info.bfly.crowd.traceability.service.TraceService;


/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月10日 下午3:37:12 
 * @version 1.0 
 * @ClassName: TraceServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月10日 下午3:37:12
 * 
 */
@Service("traceService")
public class TraceServiceImpl implements TraceService {

    @Resource
    private TraceTemplateBo templateBo;

    @Resource
    private TraceColumnBo columnBo;
    
    @Resource
    private TraceItemBo itemBo;

    @Log
    private static Logger log;

    @Resource
    HibernateTemplate ht;
    
    List<TraceColumn> traceColumnList = null;
    
    List<TraceItem> traceItemList = null;
    
    List<TraceColumn> traceColumnPathList = null;
    
    List<TraceColumn> orderColumnList = null;
    
    TraceColumn finalColumn = null;

    @Override
    public TraceTemplate merge(TraceTemplate source, TraceTemplate target) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createTemplate(TraceTemplate template) {
        template.setId(templateBo.generateId());
        template.setCommitTime(new Date());
        template.setStatus(1);
        ht.save(template);
        TraceServiceImpl.log.debug("保存模板成功，编号[" + template.getId() + "]");
    }

    @Override
    @Transactional(readOnly = true)
    public TraceTemplate findTemplateById(String id) {
        if (StringUtils.isNotEmpty(id))
            return ht.get(TraceTemplate.class, id);
        return null;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createColumn(TraceColumn column) {
        saveColumn(column);
        TraceServiceImpl.log.debug("保存模板组成功，编号[" + column.getId() + "]");
    }
    
    private void saveColumn(TraceColumn column){
        column.setId(columnBo.generateId());
        column.setCommitTime(new Date());
        column.setStatus(1);
        if(column.getTraceItems()!=null&&column.getTraceItems().size()>0){
            TraceItem item = new TraceItem();
            for(int i = 0; i < column.getTraceItems().size(); i++){
                item = column.getTraceItems().get(i);
                item.setId(itemBo.generateId());
                item.setReferrer(column.getParent());
                item.setItemName(column.getColumnName());
                item.setParent(column);
            }
        }
        ht.save(column);
        List<TraceTemplate> recordList = (List<TraceTemplate>)ht.find("from TraceTemplate trace where trace.referrer like ? and trace.status like 1", column.getParent());
        for(TraceTemplate template : recordList){
            for(TraceColumn traceColumn : template.getTraceColumns())
            {
                if(column.getReferrer()==null||traceColumn.getColumnName().equals(column.getReferrer().getColumnName())){
                    TraceColumn toColumn = copyColumn(column, template, traceColumn);
                    ht.save(toColumn);
                    break;
                }
            }
        }
    }
    
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteAllconnTraceColumn(String columnId, String templateId) {
        List<TraceTemplate> recordList = (List<TraceTemplate>)ht.find("from TraceTemplate trace where trace.referrer like ? and trace.status like 1", findTemplateById(templateId));
        TraceColumn column = findColumnById(columnId);
        traceColumnPathList = new ArrayList<TraceColumn>();
        findTreePath(column);
        
        for(TraceTemplate template1 : recordList)
        {
            List<TraceColumn> parentColumnList = new ArrayList<TraceColumn>();
            for(TraceColumn tmpColumn : template1.getTraceColumns())
            {
                if(tmpColumn.getReferrer() == null)
                    parentColumnList.add(tmpColumn);
            }
            finalColumn = new TraceColumn();
            compareToTree(traceColumnPathList, parentColumnList);
            if(finalColumn!=null&&finalColumn.getId()!=null){
                delColumn(findColumnById(finalColumn.getId()));
            }
        }
        delColumn(column);
    }
    
    /**
     * 将档案下的所有column按照父节点，子节点排序
     * @param columnlist
     */
    private void compareToTree(List<TraceColumn> pathColumnList, List<TraceColumn> parentColumnList){
        int i = pathColumnList.size();
        if(i > 0)
            for(TraceColumn column : parentColumnList){
                if(pathColumnList.size()>0&&(i-1)<pathColumnList.size()&&pathColumnList.get(i-1).getColumnName().equals(column.getColumnName())){
                    if(i==1){
                        finalColumn = column;
                        System.out.println(finalColumn.getChildren().size());
                    }
                    else{
                        pathColumnList.remove(i-1);
                        compareToTree(pathColumnList, column.getChildren());
                    }
                }
            }
    }
    
    /**
     * 查找该column的路径并记录，通过这个路径查找所有关联此模板的档案的相同位置节点
     * @param column
     */
    private void findTreePath(TraceColumn column){
        traceColumnPathList.add(column);
        if(column.getReferrer()!=null){
            findTreePath(column.getReferrer());
        }
    }
    
    private List<TraceColumn> columnList;
    private void delAllChild(TraceColumn column){
        if(column.getChildren()!=null&&column.getChildren().size()>0){
            for(TraceColumn tmpColumn : column.getChildren())
            {
                columnList.add(tmpColumn);
                delAllChild(tmpColumn);
            }
        }else if(column.getTraceItems().size()>0){
            for(TraceItem item : column.getTraceItems()){
                item.setReferrer(null);
                item.setParent(null);
                ht.delete(item);
            }
        }
    }
    
    private void delColumn(TraceColumn column){
        columnList = new ArrayList<TraceColumn>();
        columnList.add(column);
        delAllChild(column);
        if(columnList!=null&&columnList.size()>0){
            for(int i = columnList.size()-1; i >= 0; i--){
                TraceColumn tmpcolumn = columnList.get(i);
                tmpcolumn.setParent(null);
                ht.delete(tmpcolumn);
            }
        }
    }
    
    @Override
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void updateTemplate(TraceTemplate template) {
        if(StringUtils.isNotEmpty(template.getId())){
            TraceTemplate resultTemplate = ht.get(TraceTemplate.class, template.getId());
            if(null != resultTemplate){
                resultTemplate.setTemplateName(template.getTemplateName());
                ht.update(resultTemplate);
                TraceServiceImpl.log.debug("修改模板名称成功，编号[" + template.getId() + "]");
            }
            
        }
        
    }
    
    
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public TraceColumn findColumnById(String id) {
        if (StringUtils.isNotEmpty(id))
            return ht.get(TraceColumn.class, id);
        return null;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateColumn(TraceColumn column, String referrerId) {
        if (StringUtils.isNotEmpty(column.getId())) {
            TraceColumn result = this.findColumnById(column.getId());
            if(StringUtils.isNotEmpty(referrerId))
                result.setReferrer(this.findColumnById(referrerId));
            result.setColumnName(column.getColumnName());
            if(column.getTraceItems()!=null&&column.getTraceItems().size()>0){
                for(TraceItem item : column.getTraceItems()){
                    if(!item.getItemName().equals(result.getColumnName())){
                        item.setItemName(result.getColumnName());
                    }
                    item.setParent(result);
                    ht.merge(item);
                }
            }
            ht.saveOrUpdate(result);
            TraceServiceImpl.log.debug("修改模板字段成功，编号[" + column.getId() + "]");
        }
    }
    
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteColumn(String columnId) {
        if (StringUtils.isNotEmpty(columnId)) {
            TraceColumn column = this.findColumnById(columnId);
            delColumn(column);
            TraceServiceImpl.log.debug("删除模板字段成功，编号[" + column.getId() + "]");
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void addItem(TraceItem item, String templateId) {
        item.setId(itemBo.generateId());
        item.setReferrer(findTemplateById(templateId));
        ht.save(item);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveRecord(TraceTemplate traceTemplate) {
        generateRecord(traceTemplate, null, null);
    }
    
    private void generateRecord(TraceTemplate traceTemplate, Goods good, List<OrderCache> caches){
     // TODO 新增一条档案方法
        TraceTemplate record = new TraceTemplate();
        record.setId(templateBo.generateId());
        record.setCommitTime(new Date());
        record.setStatus(1);//status=1表示正常显示
        record.setReferrer(traceTemplate);
        record.setType("record");
        if(good!=null){
            record.setGoods(good);
        }
        if(caches!=null&&caches.size()>0){
            record.setOrderCaches(caches);
        }
        record.setOrder(traceTemplate.getOrder());
        traceColumnList = new ArrayList<TraceColumn>();
        traceItemList = new ArrayList<TraceItem>();
        traceTemplate = findTemplateById(traceTemplate.getId());
        if(traceTemplate.getTraceColumns() != null && traceTemplate.getTraceColumns().size() > 0){
            for(int i = 0; i < traceTemplate.getTraceColumns().size(); i++){
                if(traceTemplate.getTraceColumns().get(i).getReferrer() == null){
                    TraceColumn column = copyColumn(traceTemplate.getTraceColumns().get(i),record, null);
                    if(column!=null){
                        ht.save(column);
                        traceColumnList.add(column);
                    }
                    if(traceTemplate.getTraceColumns().get(i).getTraceItems() != null&&traceTemplate.getTraceColumns().get(i).getTraceItems().size() > 0){
                        for(TraceItem traceItem : traceTemplate.getTraceColumns().get(i).getTraceItems()){
                            TraceItem item = copyItem(traceItem, column, record);
                            ht.save(item);
                            traceItemList.add(item);
                        }
                    }
                    if(traceTemplate.getTraceColumns().get(i).getChildren() != null&&traceTemplate.getTraceColumns().get(i).getChildren().size() > 0){
                        findColumn(traceTemplate.getTraceColumns().get(i).getChildren(), record, column);
                    }
                }
            }
            record.setTraceColumns(traceColumnList);
            record.setTraceItems(traceItemList);
        }
        ht.save(record);
    }
    
    private TraceItem copyItem(TraceItem item, TraceColumn column, TraceTemplate record){
        TraceItem toItem = new TraceItem();
        toItem.setId(itemBo.generateId());
        toItem.setItemName(item.getItemName());
        toItem.setItemValue(item.getItemValue());
        toItem.setStatus(1);
        toItem.setOrder(item.getOrder());
        toItem.setItemType(item.getItemType());
        toItem.setParent(column);
        toItem.setReferrer(record);
        return toItem;
    }
    
    private void findColumn(List<TraceColumn> columnList, TraceTemplate parent, TraceColumn parentColumn){
        for(int i = 0; i < columnList.size(); i++){
            TraceColumn column = copyColumn(columnList.get(i), parent, parentColumn);
            if(column!=null)
            {
                ht.save(column);
                traceColumnList.add(column);
            }
            if(columnList.get(i).getTraceItems() != null&&columnList.get(i).getTraceItems().size() > 0){
                for(TraceItem traceItem : columnList.get(i).getTraceItems()){
                    TraceItem item = copyItem(traceItem, column, parent);
                    ht.save(item);
                    traceItemList.add(item);
                }
            }
            if(columnList.get(i).getChildren() != null && columnList.get(i).getChildren().size() > 0){                  
                findColumn(columnList.get(i).getChildren(), parent, column);
            }
        }
    }
    
    private TraceColumn copyColumn(TraceColumn fromColumn, TraceTemplate parent, TraceColumn parentColumn){
        if(fromColumn.getStatus()==1){
            TraceColumn toColumn = new TraceColumn();
            toColumn.setId(columnBo.generateId());
            toColumn.setColumnName(fromColumn.getColumnName());
            toColumn.setCommitTime(new Date());
            toColumn.setOrder(fromColumn.getOrder());
            toColumn.setStatus(1);
            toColumn.setReferrer(parentColumn);
            toColumn.setParent(parent);
            return toColumn;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveTraceColumnValue(TraceColumn column, String templateId) {
        // TODO 模板或档案添加项目
        createColumn(column);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveGoodsRecord(Order order) {
        List<OrderCache> orderCacheList = (List<OrderCache>)ht.find("from OrderCache cache where cache.mallOrder like ?", order);
        if(orderCacheList.size() > 0){
            for(OrderCache orderCache : orderCacheList){
                if(orderCache.getMallStageCache()!=null&&orderCache.getMallStageCache().getMallStage()!=null&&orderCache.getMallStageCache().getMallStage().getGoodsCache().size()>0)
                    for(GoodsCache cache : orderCache.getMallStageCache().getMallStage().getGoodsCache())
                    {
                        for(int i = 0; i < cache.getNum(); i++)
                            generateRecord(cache.getGoods().getTraceTemplate(),cache.getGoods(),orderCacheList);
                    }
            }
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteTraceRecord(String id) {
        // TODO Auto-generated method stub
        TraceTemplate record = findTemplateById(id);
        record.setOrderCaches(null);
        record.setReferrer(null);
        if(record.getTraceColumns()!=null&&record.getTraceColumns().size()>0){
            for(TraceColumn tmpColumn : record.getTraceColumns()){
                if(tmpColumn.getReferrer()==null){
                    delColumn(tmpColumn);
                }
            }
        }
        ht.delete(record);
    }

}
