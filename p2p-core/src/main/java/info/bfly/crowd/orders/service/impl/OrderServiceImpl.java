/**
* @Title: OrderServiceImpl.java
* @Package info.bfly.crowd.orders.service.impl
* @Description: TODO(用一句话描述该文件做什么)
* @author zeminshao
* @date 2017年4月24日 下午3:09:02
* @version V1.0
*/package info.bfly.crowd.orders.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.util.MapUtil;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.crowd.orders.OrdersConstants;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.model.MallStageCache;
import info.bfly.crowd.orders.model.Order;
import info.bfly.crowd.orders.model.OrderCache;
import info.bfly.crowd.orders.service.OrderService;
import info.bfly.crowd.user.model.UserAddress;

/**
 * @author  zeminshao:
 * @date 创建时间：2017年4月24日 下午3:09:02
 * @version 1.0
 * @parameter
 * @since
 * @return  */
/**
 * @ClassName: OrderServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月24日 下午3:09:02
 *
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    HibernateTemplate ht;
    @Resource
    private UserService userService;
    @Resource
    private OrderBO orderBo;
    @Resource
    private OrderCacheBO orderCacheBo;
    @Resource
    private MallStageService mallStageService;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String createOrder(String mallStageCacheId, String mallStageId,
            String userId, String userAddressId) throws IOException {
        
        
        // 针对固定模式档位
        if (StringUtils.isEmpty(mallStageCacheId)) {
            mallStageCacheId = mallStageService.setPrivateMallStage(
                    mallStageId, null);
        }
        User user = userService.getUserByLoginId(userId);
        MallStageCache stageCache = ht.get(MallStageCache.class,
                mallStageCacheId);
        UserAddress userAddress = ht.get(UserAddress.class, userAddressId);
        String orderId = orderBo.generateId();
        String orderCacheId = orderCacheBo.generateId();

        if (null != stageCache) {
            Order order = new Order();
            order.setId(orderId);
            order.setCreateTime(new Date());
            order.setOrderStatus(OrdersConstants.OrderStatus.WAITING_PAY);
            order.setUserAddress(userAddress);
            order.setUser(user);
            ht.save(order);
            ht.flush();

            OrderCache orderCache = new OrderCache();
            orderCache.setId(orderCacheId);
            orderCache.setMallOrder(order);
            orderCache.setMallStageCache(stageCache);
            orderCache.setCache(this.setOrderCache(order, stageCache));
            ht.save(orderCache);
            ht.flush();
            return orderId;
        }

        return null;
    }

    /**
     * 组装ordercache内容
     * @param order
     * @param mallStageCache
     * @return
     */
    private String setOrderCache(Order order, MallStageCache mallStageCache) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 收货人信息id
        map.put("userAddressId", order.getUserAddress().getId());
        // 收货人
        map.put("consigneeName", order.getUserAddress().getConsigneeName());
        // 收货人联系号码
        map.put("consigneePhone", order.getUserAddress().getConsigneePhone());
        // 收货人地址编码
        map.put("area", order.getUserAddress().getAreas()[0]);
        // 收货人具体地址
        map.put("detailAddress", order.getUserAddress().getDetailAddress());
        // 下单用户id
        map.put("user", order.getUser().getId());
        // 下单用户名
        map.put("userName", order.getUser().getUsername());
        // 项目id
        map.put("loanId", mallStageCache.getMallStage().getLoan().getId());
        // 项目名称
        map.put("loanName", mallStageCache.getMallStage().getLoan().getName());
        // 档位id
        map.put("mallStageId", mallStageCache.getMallStage().getId());
        // 档位名称
        map.put("mallStageName", mallStageCache.getMallStage().getStageName());
        // 档位描述
        map.put("mallStageDescribe", mallStageCache.getMallStage()
                .getStageDescribe());
        // 订制内容
        map.put("content", this.getMapByMallStageCache(mallStageCache));

        return new JSONObject(map).toString();

    }
    
    
    /**
     * 
     * @param mallStageCache
     * @return
     */
    private List<Map<String, String>> getMapByMallStageCache(
            MallStageCache mallStageCache) {

        if (null == mallStageCache)
            return null;

        // 档位记录
        Map<String, String> cacheMap = new HashMap<String, String>();
        try {
            cacheMap = MapUtil.jsonStringToHashMap(mallStageCache.getCache());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<GoodsCache> goodsCacheList = (List<GoodsCache>) ht
                .findByNamedParam(
                        "Select g1 from GoodsCache g1 where g1.id in (:ids)",
                        "ids", cacheMap.keySet());

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> newMap = null;
        for (GoodsCache goodsCache : goodsCacheList) {
            newMap = new HashMap<String, String>();
            newMap.put("goodsCacheId", goodsCache.getId());
            newMap.put("goodsId", goodsCache.getGoods().getId());
            newMap.put("goodsName", goodsCache.getGoods().getGoodsName());
            newMap.put("goodsNum", cacheMap.get(goodsCache.getId()));
            list.add(newMap);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = false)
    public void update(Order order) {
        ht.update(order);
    }



    @Override
    public Order get(String orderId) {
        return ht.get(Order.class, orderId);
    }


    @Override
    public String convertGoodsContent(String orderId) {
        if (StringUtils.isEmpty(orderId))
            return "";
        Order order = this.get(orderId);

        if (null == order)
            return "";

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, Object> newMap = null;
        
        
        if(StringUtils.isEmpty(order.getOrderCaches().get(0)
                    .getCache()))
            return "";
        
        try {
            newMap = new ObjectMapper().readValue(order.getOrderCaches().get(0)
                    .getCache(), HashMap.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        list = (List<Map<String, String>>) newMap.get("content");
        StringBuilder sb = new StringBuilder();
        for (Map<String, String> map : list) {
            sb.append(map.get("goodsName").toString()).append("*")
                    .append(map.get("goodsNum").toString()).append("  ");
        }
        return sb.toString();
    }

    @Override
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void updateFailOrderStatus(String orderId) {
        if(StringUtils.isNotEmpty(orderId)){
            Order order =  this.get(orderId);
            order.setOrderStatus(OrdersConstants.OrderStatus.PAY_FAILURE);
            this.update(order);
        }
    }
    
}
