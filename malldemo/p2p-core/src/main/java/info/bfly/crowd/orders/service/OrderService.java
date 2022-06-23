/**   
* @Title: OrderService.java 
* @Package info.bfly.crowd.orders.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月24日 下午3:08:40 
* @version V1.0   
*/package info.bfly.crowd.orders.service;

import info.bfly.crowd.orders.model.Order;

import java.io.IOException;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月24日 下午3:08:40 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: OrderService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月24日 下午3:08:40 
 *  
 */
public interface OrderService {    
    /**
     * 新增订单
     * @param mallStageCacheId
     * @param mallStageId
     * @param userId
     * @param userAddressId
     * @return
     * @throws IOException
     */
    String createOrder(String mallStageCacheId,String mallStageId,String userId,String userAddressId) throws IOException;
    /**
     * 修改订单
     * @param order
     */
    public void update(Order order);
    /**
     * 查询订单
     * @param orderId
     * @return
     */
    public Order get(String orderId);
    /**
     * 转换订制内容字典内容
     * @param orderId
     * @return
     */
    public String convertGoodsContent(String orderId);
    
    /**
     * 修改订单状态为失败
     * @param orderId
     */
    public void updateFailOrderStatus(String orderId);
}
