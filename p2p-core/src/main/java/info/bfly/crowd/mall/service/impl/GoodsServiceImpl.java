/**   
 * @Title: GoodsServiceImpl.java 
 * @Package info.bfly.order.mall.service.impl 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月1日 上午9:09:10 
 * @version V1.0   
 */
package info.bfly.crowd.mall.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.crowd.mall.model.Goods;
import info.bfly.crowd.mall.service.GoodsService;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.service.GoodsCacheService;
import info.bfly.crowd.traceability.model.TraceTemplate;


/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月1日 上午9:09:10 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: GoodsServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月1日 上午9:09:10
 * 
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Log
    private static Logger log;

    @Resource
    private GoodsBO goodsBo;
    
    @Resource
    private GoodsCacheService goodsCacheService;

    @Resource
    HibernateTemplate ht;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createGoods(Goods goods) {
        goods.setId(goodsBo.generateId());
        goods.setCommitTime(new Date());
        ht.save(goods);
        GoodsServiceImpl.log.debug("保存回报物成功，编号[" + goods.getId() + "]");
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateGoods(Goods goods) {
        // 只能更改不影响流程的字段
        ht.merge(goods);
        if (GoodsServiceImpl.log.isDebugEnabled())
            GoodsServiceImpl.log.debug("修改回报物成功，编号[" + goods.getId() + "]");
    }

    @Override
    @Transactional(readOnly=true,rollbackFor=Exception.class)
    public TraceTemplate getTraceTemplateByGoods(final Goods goods) {
        TraceTemplate traceTemplate = ht.execute(new HibernateCallback<TraceTemplate>() {
            @Override
            public TraceTemplate doInHibernate(Session session)
                    throws HibernateException {
                Query query = null;
                query = session.getNamedQuery("TraceTemplate.getTraceTemplate");
                query.setParameter("goodsId", goods.getId());
                return (TraceTemplate) query.uniqueResult();
            }
        });
        return traceTemplate;
    }

    @Override
    public Goods findGoodsById(String id) {
        if(StringUtils.isNotEmpty(id))
            return ht.get(Goods.class, id);
        return null;
    }

    @Override
    public void deleteGoods(String id) {
        if(StringUtils.isNotEmpty(id)){
            Goods goods = this.findGoodsById(id);
            if (null != goods){
                Set<GoodsCache> goodsCacheSets = goods.getGoodsCaches();
                if (goodsCacheSets != null && goodsCacheSets.size() > 0) {
                    FacesUtil.addErrorMessage("请先删除关联的回报服务！");
                } else {
                    ht.delete(goods);
                    if (GoodsServiceImpl.log.isDebugEnabled())
                        GoodsServiceImpl.log.debug("删除回报物成功，编号[" + goods.getId() + "]");
                }
            }
                
        }
    }

    @Override
    public List<Goods> chooseGoods(String goodsCacheId) {
        List<Goods> gooodsList = (List<Goods>) ht
                .find("Select goods from Goods goods where goods not in(Select distinct c1.goods from GoodsCache c1)",
                        null);
        if(StringUtils.isNoneEmpty(goodsCacheId)){
            GoodsCache goodsCache = ht.get(GoodsCache.class, goodsCacheId);
            gooodsList.add(goodsCache.getGoods());
        }
        return gooodsList;
    }

    

}
