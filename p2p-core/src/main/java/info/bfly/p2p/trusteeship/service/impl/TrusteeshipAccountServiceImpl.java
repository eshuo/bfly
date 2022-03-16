package info.bfly.p2p.trusteeship.service.impl;

import info.bfly.archer.user.model.User;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.model.TrusteeshipAccount;
import info.bfly.p2p.trusteeship.service.TrusteeshipAccountService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by XXSun on 3/9/2017.
 */
@Service
public class TrusteeshipAccountServiceImpl implements TrusteeshipAccountService {

    @Autowired
    HibernateTemplate ht;
    @Autowired
    IdGenerator       idGenerator;

    @Override
    public TrusteeshipAccount getTrusteeshipAccount(String userId, String trusteeship) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TrusteeshipAccount.class);
        criteria.add(Restrictions.eq("user.id", userId));
        criteria.add(Restrictions.eq("trusteeship", trusteeship));
        List<TrusteeshipAccount> trusteeshipAccounts = (List<TrusteeshipAccount>) ht.findByCriteria(criteria);

        switch (trusteeshipAccounts.size()) {
            case 0:
                return null;
            case 1:
                return trusteeshipAccounts.get(0);
            default:
                //TODO 抛出找到多个的异常

        }
        return null;
    }

    @Override
    public TrusteeshipAccount getSystemTrusteeshipAccount(String trusteeship) {
        return getTrusteeshipAccount("system",trusteeship);
    }

    @Override
    public void bindTrusteeshipAccount(String userId, String trusteeship, String accountId) {
        this.bindTrusteeshipAccount(userId, trusteeship, accountId, "UID");
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void bindTrusteeshipAccount(String userId, String trusteeship, String accountId, String type) {
        TrusteeshipAccount ta = new TrusteeshipAccount();
        ta.setId(idGenerator.nextId(TrusteeshipAccount.class));
        ta.setUser(new User(userId));
        ta.setAccountId(userId);
        ta.setDefaultAccountType("BASIC");
        ta.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
        ta.setCreateTime(new Date());
        ta.setType(type);
        ta.setStatus(TrusteeshipConstants.TrusteeshipStatus.ACTIVE);
        ht.save(ta);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void bindTrusteeshipAccount(TrusteeshipAccount trusteeshipAccount) {
        ht.save(trusteeshipAccount);
    }
}
