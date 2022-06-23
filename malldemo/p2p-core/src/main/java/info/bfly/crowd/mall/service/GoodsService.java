/**   
* @Title: GoodsService.java 
* @Package info.bfly.order.mall.service.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月1日 上午9:08:37 
* @version V1.0   
*/package info.bfly.crowd.mall.service;

import java.util.List;

import info.bfly.crowd.mall.model.Goods;
import info.bfly.crowd.traceability.model.TraceTemplate;





/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月1日 上午9:08:37 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: GoodsService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月1日 上午9:08:37 
 *  
 */
public interface GoodsService {
    
    /**
     * 
    * @Title: findGoodsById 
    * @Description:  
    * @param @param id
    * @param @return    设定文件 
    * @return Goods    返回类型 
    * @throws
     */
    Goods findGoodsById(String id);
    
    /**
     * 
    * @Title: createGoods 
    * @Description:  管理员新增回报物
    * @param @param goods    设定文件 
    * @return void    返回类型 
    * @throws
     */
    void createGoods(Goods goods);
    
    /**
     * 
    * @Title: updateGoods 
    * @Description:  修改回报物
    * @param @param goods    设定文件 
    * @return void    返回类型 
    * @throws
     */
    void updateGoods(Goods goods);
    
    /**
     * 
    * @Title: getTraceTemplateByGoods 
    * @Description:  根据回报物id查询可追溯模板
    * @param @param goods
    * @param @return    设定文件 
    * @return TraceTemplate    返回类型 
    * @throws
     */
    TraceTemplate getTraceTemplateByGoods(Goods goods);
    
    /**
     * 
    * @Title: deleteGoods 
    * @Description:  
    * @param @param id    设定文件 
    * @return void    返回类型 
    * @throws
     */
    void deleteGoods(String id);
    
    /**
     * 获取
     * @param goodsCacheId
     * @return
     */
    List<Goods> chooseGoods(String goodsCacheId);
}
