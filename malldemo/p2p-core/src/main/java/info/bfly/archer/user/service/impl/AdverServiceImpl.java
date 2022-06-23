package info.bfly.archer.user.service.impl;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.AdverLeague;
import info.bfly.archer.user.model.AdverModel;
import info.bfly.archer.user.service.AdverService;
import info.bfly.core.annotations.Log; import org.slf4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 广告联盟接口实现类
 *
 * @author Administrator
 *
 */
@Service(value = "adverService")
@SuppressWarnings("unchecked")
public class AdverServiceImpl extends EntityQuery<AdverLeague> implements AdverService {
    @Resource
    private HibernateTemplate ht;

    @Log
    private static Logger log;

    @Override
    public AdverLeague getAdverByID(int id) {
        return ht.get(AdverLeague.class, id);
    }

    @Override
    public List<AdverLeague> getAlList(String mid, String uid, String stime, String etime) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from AdverLeague where mid='" + mid + "' and uid='" + uid + "' ");
        if (stime != null && etime == null) {
            hql.append(" and regDate like '" + stime + "%'");
        }
        if (stime == null && etime != null) {
            hql.append(" and regDate like '" + etime + "%'");
        }
        if (stime != null && etime != null) {
            hql.append(" and regDate between '" + stime + " 01:00:01'  and '" + etime + " 23:59:59'   ");
        }
        log.info(hql.toString());
        return (List<AdverLeague>) ht.find(hql.toString());
    }

    @Override
    public List<AdverModel> getCoungGroupMid(final String regStartDate, final String regEndDate) {
        List<AdverModel> ams = new ArrayList<AdverModel>();
        final String hql = "select count(*),al.regDate,al.mid  from AdverLeague al group by date_format(al.regDate,'%Y-%m-%d'),al.mid";
        final String hql1 = "select count(*),al.regDate,al.mid  from AdverLeague al where al.regDate between '" + regStartDate + " 01:00:01' and '" + regEndDate
                + " 23:59:59' group by date_format(al.regDate,'%Y-%m-%d'),al.mid";
        @SuppressWarnings("unchecked")
        List<Object[]> objs = ht.execute(new HibernateCallback<List<Object[]>>() {
            @Override
            public List<Object[]> doInHibernate(Session session) throws HibernateException {
                Query query = null;
                if (regStartDate == null || regEndDate == null) {
                    query = session.createQuery(hql);
                    // log.info(hql.toString());
                }
                else {
                    query = session.createQuery(hql1);
                    // log.info(hql1.toString());
                }
                return query.list();
            }
        });
        if (objs.size() > 0) {
            for (Object obj : objs) {
                Object[] objs2 = (Object[]) obj;
                AdverModel am = new AdverModel((Date) objs2[1], (String) objs2[2], (Long) objs2[0], 0);
                ams.add(am);
            }
        }
        return ams;
    }

    @Override
    public int getCountByMid(String mid) {
        return ht.find("from AdverLeague al where al.mid= '" + mid + "'").size();
    }

    @Override
    public List<AdverLeague> queryList() {
        return (List<AdverLeague>) ht.find("from AdverLeague");
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void save(AdverLeague adverLeague) {
        ht.save(adverLeague);
    }
}
