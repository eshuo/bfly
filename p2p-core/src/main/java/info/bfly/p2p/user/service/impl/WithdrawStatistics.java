package info.bfly.p2p.user.service.impl;

import info.bfly.archer.user.UserConstants.WithdrawStatus;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service
public class WithdrawStatistics {
    @Resource
    HibernateTemplate ht;

    /**
     * cap资金池累计提现
     */
    public double getSuccessCapWithdrawMoney(String userId) {
        String hql = "select sum(withdraw.money) from WithdrawCash withdraw " + "where withdraw.status =? and withdraw.user.id=? and withdraw.type='cap'";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { WithdrawStatus.SUCCESS, userId });
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * ips环讯累计提现
     */
    public double getSuccessIpsWithdrawMoney(String userId) {
        String hql = "select sum(withdraw.money) from WithdrawCash withdraw " + "where withdraw.status =? and withdraw.user.id=? and withdraw.type='ips'";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { WithdrawStatus.SUCCESS, userId });
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 累计提现
     */
    public double getSuccessWithdrawMoney(String userId) {
        String hql = "select sum(withdraw.money) from WithdrawCash withdraw " + "where withdraw.status =? and withdraw.user.id=?";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { WithdrawStatus.SUCCESS, userId });
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }
}
