/**   
 * @Title: GoodsBO.java 
 * @Package info.bfly.order.mall.service.impl 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月1日 上午9:10:00 
 * @version V1.0   
 */
package info.bfly.crowd.orders.service.impl;

import info.bfly.core.util.IdGenerator;
import info.bfly.crowd.orders.model.MallStageCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月1日 上午9:10:00 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: GoodsBO
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月1日 上午9:10:00
 * 
 */
@Service("mallStageCacheBO")
public class MallStageCacheBO {

	@Autowired
	IdGenerator idGenerator;

	/**
	 * 
	 * @Title: generateId
	 * @Description: 获取序列号
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String generateId() {
		return idGenerator.nextId(MallStageCache.class, "");
	}
}
