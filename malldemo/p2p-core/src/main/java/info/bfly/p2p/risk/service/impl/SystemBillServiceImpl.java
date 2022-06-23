package info.bfly.p2p.risk.service.impl;

import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.risk.SystemBillConstants;
import info.bfly.p2p.risk.model.SystemBill;
import info.bfly.p2p.risk.service.SystemBillService;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service(value = "systemBillService")
@SuppressWarnings("unchecked")
public class SystemBillServiceImpl implements SystemBillService {
    @Resource
    private HibernateTemplate ht;

    @Override
    public double getBalance() {

        SystemBill sb = getLastestBill();
        return sb == null ? 0D : sb.getBalance();
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private SystemBill getLastestBill() {
        DetachedCriteria criteria = DetachedCriteria.forClass(SystemBill.class);
        criteria.setLockMode(LockMode.PESSIMISTIC_WRITE);
        criteria.addOrder(Order.desc("seqNum"));
        List<SystemBill> ibs = (List<SystemBill>) ht.findByCriteria(criteria, 0, 1);
        if (ibs.size() > 0) {
            SystemBill sb = ibs.get(0);
            if (sb.getBalance() == null) {
                double in = getSumByType(SystemBillConstants.Type.IN);
                double out = getSumByType(SystemBillConstants.Type.OUT);
                sb.setBalance(ArithUtil.sub(in, out));
                ht.update(sb);
            }
            return sb;
        }
        return null;
    }

    private double getSumByType(String type) {
        String hql = "select sum(sb.money) from SystemBill sb where sb.type =?";
        Double sum = (Double) ht.find(hql, type).get(0);
        if (sum == null) {
            return 0;
        }
        return sum.doubleValue();
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void transferInto(double money, String reason, String detail) {
        if (money < 0) {
            throw new RuntimeException("money cannot be less than zero!");
        }
        SystemBill ibLastest = getLastestBill();
        SystemBill lb = new SystemBill();
        lb.setId(IdGenerator.randomUUID());
        lb.setMoney(money);
        lb.setTime(new Date());
        lb.setDetail(detail);
        lb.setType(SystemBillConstants.Type.IN);
        lb.setReason(reason);
        if (ibLastest == null) {
            // 第一条数据
            lb.setSeqNum(1L);
            // 余额=money
            lb.setBalance(money);
        } else {
            lb.setSeqNum(ibLastest.getSeqNum() + 1);
            // 余额=上一条余额+money
            lb.setBalance(ArithUtil.add(ibLastest.getBalance(), money));
        }
        ht.save(lb);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void transferOut(double money, String reason, String detail) throws InsufficientBalance {
        if (money < 0) {
            throw new RuntimeException("money cannot be less than zero!");
        }
        SystemBill ibLastest = getLastestBill();
        double balance = getBalance();
        SystemBill ib = new SystemBill();
        if (balance < money) {
            throw new InsufficientBalance("transfer out money:" + money + ", balance:" + balance);
        }
        else {
            ib.setId(IdGenerator.randomUUID());
            ib.setMoney(money);
            ib.setTime(new Date());
            ib.setDetail(detail);
            ib.setReason(reason);
            ib.setType(SystemBillConstants.Type.OUT);
            if (ibLastest == null) {
                // 第一条数据
                ib.setSeqNum(1L);
                // 余额=0
                ib.setBalance(0D);
            }
            else {
                // 余额=上一条余额-取出的钱
                ib.setBalance(ArithUtil.sub(ibLastest.getBalance(), money));
                ib.setSeqNum(ibLastest.getSeqNum() + 1);
            }
            ht.save(ib);
        }
    }
}
