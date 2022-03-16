/**
 * @Title: GoodsCacheHome.java
 * @Package info.bfly.order.mall.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author zeminshao
 * @date 2017年4月1日 下午2:55:36
 * @version V1.0
 */
package info.bfly.crowd.orders.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;

import info.bfly.crowd.orders.OrdersConstants;
import info.bfly.crowd.orders.model.Order;
import info.bfly.crowd.orders.service.OrderService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author zeminshao:
 * @date 创建时间：2017年4月1日 下午2:55:36
 * @version 1.0
 * @parameter
 * @return
 */

/**
 * @author zeminshao
 * @ClassName: GoodsCacheHome
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017年4月1日 下午2:55:36
 */
@Component
@Scope(ScopeType.VIEW)
public class OrdersHome extends EntityHome<Order> implements
        Serializable {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = -5676979247812990708L;

    @Resource
    private OrderService orderService;
    @Resource
    private LoginUserInfo loginUserInfo;





    public OrdersHome() {
        setUpdateView(FacesUtil.redirect(OrdersConstants.View.ORDER_LIST));
        setDeleteView(FacesUtil.redirect(OrdersConstants.View.ORDER_LIST));
    }




    public void createOrder() {

        try {
            orderService.createOrder("", "2017042417000000", loginUserInfo.getLoginUserId(), "2017042017000002");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
