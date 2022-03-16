package info.bfly.pay.service.impl;

import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.order.OrderSinaEntity;
import info.bfly.pay.service.AbstractPayOperationService;
import org.springframework.stereotype.Service;

/**
 * 订单服务类 on 2017/2/17 0017.
 */
@Service
public class OrderOperationService<T extends SinaInEntity, E extends OrderSinaEntity> extends AbstractPayOperationService<T, E> {

}
