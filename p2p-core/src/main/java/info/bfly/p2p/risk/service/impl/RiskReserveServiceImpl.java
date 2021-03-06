package info.bfly.p2p.risk.service.impl;

import info.bfly.archer.common.service.impl.BaseServiceImpl;
import info.bfly.archer.user.model.User;
import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.risk.model.RiskReserve;
import info.bfly.p2p.risk.service.RiskReserveService;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "riskReserveService")
@SuppressWarnings("unchecked")
public class RiskReserveServiceImpl extends BaseServiceImpl<RiskReserve> implements RiskReserveService {
    @Resource
    private HibernateTemplate ht;

    @Override
    public double getBalance() {
        RiskReserve ibLastest = getLastestBill();
        if (ibLastest == null) {
            return 0;
        } else {
            return ibLastest.getBalance();
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public RiskReserve getLastestBill() {
        DetachedCriteria criteria = DetachedCriteria.forClass(RiskReserve.class);
        // criteria.addOrder(Order.desc("time"));
        criteria.addOrder(Order.desc("seqNum"));
        criteria.setLockMode(LockMode.UPGRADE);
        List<RiskReserve> ibs = (List<RiskReserve>) ht.findByCriteria(criteria, 0, 1);
        if (ibs.size() > 0) {
            return ibs.get(0);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public RiskReserve getLastestBillByUser(String userId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(RiskReserve.class);
        // criteria.addOrder(Order.desc("time"));
        criteria.addOrder(Order.desc("seqNum"));
        criteria.setLockMode(LockMode.UPGRADE);
        criteria.add(Restrictions.eq("user.id", userId));
        List<RiskReserve> ibs = (List<RiskReserve>) ht.findByCriteria(criteria, 0, 1);
        if (ibs.size() > 0) {
            return ibs.get(0);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean takeOut(String userId, double money, String operatorType, String operatorDetail) {
        RiskReserve ibLastest = getLastestBill();
        RiskReserve ib = new RiskReserve();
        if (ibLastest != null && ibLastest.getBalance() >= money) {
            ib.setId(IdGenerator.randomUUID());
            ib.setMoney(money);
            ib.setTime(new Date());
            ib.setDetail(operatorDetail);
            ib.setType(operatorType);
            ib.setSeqNum(ibLastest.getSeqNum() + 1);
            if (StringUtils.isNotEmpty(userId)) {
                ib.setUser(new User(userId));
            }
            // ??????=???????????????-????????????
            ib.setBalance(ArithUtil.sub(ibLastest.getBalance(), money));
            ht.save(ib);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean transferInto(String userId, double money, String operatorType, String operatorDetail) {
        RiskReserve ibLastest = getLastestBill();
        RiskReserve lb = new RiskReserve();
        lb.setId(IdGenerator.randomUUID());
        lb.setMoney(money);
        lb.setTime(new Date());
        lb.setDetail(operatorDetail);
        lb.setType(operatorType);
        if (StringUtils.isNotEmpty(userId)) {
            lb.setUser(new User(userId));
        }
        if (ibLastest == null) {
            // ???????????????
            lb.setSeqNum(1);
            // ??????=money
            lb.setBalance(money);
        }
        else {
            lb.setSeqNum(ibLastest.getSeqNum() + 1);
            // ??????=???????????????+money
            lb.setBalance(ArithUtil.add(ibLastest.getBalance(), money));
        }
        ht.save(lb);
        return true;
    }
}
