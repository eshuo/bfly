/**   
* @Title: GoodsCacheService.java 
* @Package info.bfly.order.mall.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月5日 上午9:44:11 
* @version V1.0   
*/package info.bfly.crowd.orders.service;

import java.util.List;

import info.bfly.crowd.orders.model.GoodsCache;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月5日 上午9:44:11 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: GoodsCacheService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月5日 上午9:44:11 
 *  
 */
public interface GoodsCacheService {
	
	/**
	 * 
	* @Title: createGoodsCache 
	* @Description:  新增回报服务
	* @param @param goodsCache    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void createGoodsCache(GoodsCache goodsCache);
	
	/**
	 * 
	* @Title: updateGoodsCache 
	* @Description:  修改回报服务
	* @param @param goodsCache    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void updateGoodsCache(GoodsCache goodsCache);
	/**
	 * @Title:QueryGoodsCacheById
	 * @Description: 查询回报服务
	 * @return void 返回类型
	 * @throw
	 * @author hdy
	 */
	List<GoodsCache> QueryGoodsCacheById(String Id);
	/**
	 * @Title :save
	 * @Description :保存回报服务
	 * @author hdy
	 * @throw
	 * @return void
	 */
	void save(GoodsCache goodsCache);
	/**
	 * @Description : 根据档位id查询回报服务
	 * @param mallStageId
	 */
	List<GoodsCache>  QueryGoodsCacheByMallStageId(String mallStageId);
	/**
	 * update档位和回报服务的关联关系为null
	 */
	void cleanByMallStageId(String mallStageId);
	/**
	 * update新的选中的关系进去
	 */
	void updateGoodsCacheAll(List<GoodsCache> goodsCacheList ,String mallStageId);
	/**
	 * 查询没有被选中的回报服务列表
	 */
	List<GoodsCache> getNotChooseGoodsCacheList();
	/**
	 * 根据mallstageid清除数据表
	 */
	void cleanAllGoodsCacheByMallStageId(String mallStageId);
	
}
