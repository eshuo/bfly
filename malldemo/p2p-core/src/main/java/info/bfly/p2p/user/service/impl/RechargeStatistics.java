package info.bfly.p2p.user.service.impl;

import info.bfly.archer.user.UserConstants.RechargeStatus;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service
public class RechargeStatistics {
    @Resource
    HibernateTemplate ht;

    /**
     * 资金池累计充值
     */
    public double getPaidCapRechargeMoney(String userId) {
        String hql = "select sum(recharge.actualMoney) from Recharge recharge " + "where recharge.status =? and recharge.user.id=? and recharge.rechargeWay='cap'";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { RechargeStatus.SUCCESS, userId });
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 环讯累计充值
     */
    public double getPaidIpsRechargeMoney(String userId) {
        String hql = "select sum(recharge.actualMoney) from Recharge recharge " + "where recharge.status =? and recharge.user.id=? and recharge.rechargeWay='ips'";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { RechargeStatus.SUCCESS, userId });
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 累计充值
     */
    public double getPaidRechargeMoney(String userId) {
        String hql = "select sum(recharge.actualMoney) from Recharge recharge " + "where recharge.status =? and recharge.user.id=? ";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { RechargeStatus.SUCCESS, userId });
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }
}
