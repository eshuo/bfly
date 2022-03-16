package info.bfly.p2p.coupon.service.impl;

import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.coupon.model.Coupon;
import info.bfly.p2p.coupon.service.CouponService;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Resource
    private HibernateTemplate ht;

    @Resource
    private    IdGenerator idGenerator;


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Coupon createInstance(Coupon coupon) {
        coupon.setId(idGenerator.nextId(Coupon.class));
        coupon.setGenerateTime(new Date());
        ht.save(coupon);
        return coupon;
    }
}
