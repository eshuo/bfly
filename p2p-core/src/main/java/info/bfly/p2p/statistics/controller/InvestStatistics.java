package info.bfly.p2p.statistics.controller;

import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.model.InvestPulished;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 投资统计
 *
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.REQUEST)
@Service
public class InvestStatistics {
    @Resource
    private HibernateTemplate ht;

    /**
     * 计算在平台上所有用户已经投资成功的总的收益
     *
     * @return
     */
    public double getAllInvestsInterest() {
        String hql = "Select sum(interest+defaultInterest-fee) from InvestRepay where time is not null";
        List<Object> oos = (List<Object>) ht.find(hql);
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 计算在平台上所有用户已经投资成功的总的金额
     *
     * @return
     */
    public double getAllInvestsMoney() {
        String hql = "select sum(invest.investMoney) from Invest invest " + "where invest.status not in (?,?)";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { InvestConstants.InvestStatus.WAIT_AFFIRM, InvestConstants.InvestStatus.CANCEL });
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 计算在平台上所有用户已经投资成功的总的金额
     *
     * @return
     */
    public double getAllInvestsMoney(String businessType) {
        String hql = "select sum(invest.investMoney) from Invest invest " + "where invest.status not in (?,?) and invest.loan.businessType=?";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { InvestConstants.InvestStatus.WAIT_AFFIRM, InvestConstants.InvestStatus.CANCEL, businessType });
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 获取所有成功的投资数量
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public long getAllSuccessInvestsNum() {
        String hql = "select count(invest) from Invest invest " + "where invest.status not in (?,?)";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { InvestConstants.InvestStatus.WAIT_AFFIRM, InvestConstants.InvestStatus.CANCEL });
        if (oos.get(0) == null) {
            return 0;
        }
        return (Long) oos.get(0);
    }

    /**
     * 获取所有成功的投资数量
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public long getAllSuccessInvestsNum(String businessType) {
        String hql = "select count(invest) from Invest invest " + "where invest.status not in (?,?) and invest.loan.businessType=?";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { InvestConstants.InvestStatus.WAIT_AFFIRM, InvestConstants.InvestStatus.CANCEL, businessType });
        if (oos.get(0) == null) {
            return 0;
        }
        return (Long) oos.get(0);
    }

    /**
     * 投资排行榜
     *
     * @return
     */
    public List<InvestPulished> getIps() {
        Calendar c1 = Calendar.getInstance();
        c1.set(1000, 1, 1);
        Calendar c2 = Calendar.getInstance();
        c2.set(9000, 12, 31);
        return getIps(c1.getTime(), c2.getTime());
    }

    /**
     * 投资排行榜
     *
     * @return
     */
    public List<InvestPulished> getIps(final Date startTime, final Date endTime) {
        final String hql = "SELECT new InvestPulished(invest.user.photo,invest.user.id, SUM(ir.corpus), SUM(ir.interest) ) FROM InvestRepay ir where ir.invest.time >= ? and ir.invest.time <= ? GROUP BY ir.invest.user ORDER BY SUM(ir.corpus) desc";
        @SuppressWarnings("unchecked")
        List<InvestPulished> ips = ht.execute(new HibernateCallback<List<InvestPulished>>() {
            @Override
            public List<InvestPulished> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setParameter(0, startTime);
                query.setParameter(1, endTime);
                // 从第0行开始
                query.setFirstResult(0);
                query.setMaxResults(5);
                return query.list();
            }
        });
        return ips;
    }

    /**
     * 应收（待收）本金
     *
     * @return
     */
    public double getReceivableCorpus(String userId) {
        String hql = "Select sum(corpus) from InvestRepay where time is null and invest.user.id = ? ";
        List<Object> result = (List<Object>) ht.find(hql, userId);
        double money = 0;
        if (result != null && result.get(0) != null) {
            money = (Double) result.get(0);
        }
        return money;
    }

    /**
     * 应收（待收）本金，还款日在repayDay之前
     *
     * @return
     */
    public double getReceivableCorpus(String userId, Date repayDay) {
        String hql = "Select sum(corpus) from InvestRepay where time is null and invest.user.id = ? and repayDay <? ";
        List<Object> result = (List<Object>) ht.find(hql, userId, repayDay);
        double money = 0;
        if (result != null && result.get(0) != null) {
            money = (Double) result.get(0);
        }
        return money;
    }

    /**
     * 应收（待收）利息
     *
     * @return
     */
    public double getReceivableInterest(String userId) {
        String hql = "Select sum(interest+defaultInterest-fee) from InvestRepay where time is null and invest.user.id = ?";
        List<Object> result = (List<Object>) ht.find(hql, userId);
        double money = 0;
        if (result != null && result.get(0) != null) {
            money = (Double) result.get(0);
        }
        return money;
    }

    /**
     * 应收（待收）利息 ，还款日在repayDay之前
     *
     * @return
     */
    public double getReceivableInterest(String userId, Date repayDay) {
        String hql = "Select sum(interest+defaultInterest-fee) from InvestRepay where time is null and invest.user.id = ? and repayDay <? ";
        List<Object> result = (List<Object>) ht.find(hql, userId, repayDay);
        double money = 0;
        if (result != null && result.get(0) != null) {
            money = (Double) result.get(0);
        }
        return money;
    }

    /**
     * 已收本金
     *
     * @return
     */
    public double getReceivedCorpus(String userId) {
        // InvestRepay
        String hql = "Select sum(corpus) from InvestRepay where time is not null and invest.user.id = ? and time <= ?";
        List<Object> result = (List<Object>) ht.find(hql, userId, new Date());
        double money = 0;
        if (result != null && result.get(0) != null) {
            money = (Double) result.get(0);
        }
        return money;
    }

    /**
     * 已收利息
     *
     * @return
     */
    public double getReceivedInterest(String userId) {
        // InvestRepay
        String hql = "Select sum(interest+defaultInterest-fee) from InvestRepay where time is not null and invest.user.id = ? and time <= ?";
        List<Object> result = (List<Object>) ht.find(hql, userId, new Date());
        double money = 0;
        if (result != null && result.get(0) != null) {
            money = (Double) result.get(0);
        }
        return money;
    }
}
