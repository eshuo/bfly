/**   
* @Title: GoodsCacheServiceImpl.java 
* @Package info.bfly.order.mall.service.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月5日 上午9:45:25 
* @version V1.0   
*/package info.bfly.crowd.orders.service.impl;

import info.bfly.core.annotations.Log;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.service.GoodsCacheService;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月5日 上午9:45:25 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: GoodsCacheServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月5日 上午9:45:25 
 *  
 */
@Service("goodsCacheService")
public class GoodsCacheServiceImpl implements GoodsCacheService {
	
    @Log
    private static Logger log;
    
    @Resource
    private GoodsCacheBO goodsCacheBo;
    
    
    @Resource
    private MallStageService mallStageService;
    
    @Resource
    HibernateTemplate ht;
	
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void createGoodsCache(GoodsCache goodsCache) {
		goodsCache.setId(goodsCacheBo.generateId());
		goodsCache.setCommitTime(new Date());
		ht.save(goodsCache);
		GoodsCacheServiceImpl.log.debug("保存回报服务成功，编号[" + goodsCache.getId() + "]");
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void updateGoodsCache(GoodsCache goodsCache) {
		 // 只能更改不影响流程的字段
        ht.merge(goodsCache);
        if (GoodsCacheServiceImpl.log.isDebugEnabled()) 
        	GoodsCacheServiceImpl.log.debug("修改回报服务成功，编号[" + goodsCache.getId() + "]");
	}

    @Override
    public List<GoodsCache> QueryGoodsCacheById(String Id) {
        // TODO 暂时先用一个id查询一个List以后再改
        List<GoodsCache> goodscaches = (List<GoodsCache>) ht.get(GoodsCache.class, Id);
        return goodscaches;
    }

    @Override
    public void save(GoodsCache goodsCache) {
        ht.saveOrUpdate(goodsCache);
    }

	@Override
	public List<GoodsCache> QueryGoodsCacheByMallStageId(String mallStageId) {
		List<GoodsCache> goodsCaches = (List<GoodsCache>)ht.find("from GoodsCache goodsCache where goodsCache.mallStage.id = ?", mallStageId);
		return goodsCaches;
	}



	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void cleanByMallStageId(String mallStageId) {
		if(null!=mallStageId){
			ht.clear();
			ht.bulkUpdate("update from GoodsCache goodsCache set goodsCache.mallStage = null where goodsCache.mallStage.id = ?", mallStageId);
			ht.flush();
		}
		
	}




    @Override
    public void cleanAllGoodsCacheByMallStageId(String mallStageId) {
        if(null != mallStageId){
            ht.clear();
            ht.bulkUpdate("update from GoodsCache goodsCache set goodsCache.mallStage = null where goodsCache.mallStage.id", mallStageId);
            ht.flush();
        }
    }

    @Override
    public void updateGoodsCacheAll(List<GoodsCache> goodsCacheList, String mallStageId) {
        MallStage mallStage = ht.get(MallStage.class,mallStageId);
        GoodsCache cache = null;
        ht.clear();
        for(GoodsCache goodsCache :goodsCacheList){
            cache = ht.get(GoodsCache.class, goodsCache.getId());
            cache.setMallStage(mallStage);
            ht.update(cache);
        }
    }


    @Override
    public List<GoodsCache> getNotChooseGoodsCacheList() {
        List<GoodsCache> goodsCache = (List<GoodsCache>)ht.find("from GoodsCache goodsCache where goodsCache.mallStage is null", null);
        return goodsCache;
    }
   
}
