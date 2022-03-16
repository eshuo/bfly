package info.bfly.archer.user.service.impl;

import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.VerifyHistory;
import info.bfly.archer.user.service.VerifyHistoryService;
import info.bfly.core.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by XXSun on 3/9/2017.
 */
@Service
public class VerifyHistoryServiceImpl implements VerifyHistoryService {
    @Resource
    HibernateTemplate ht;

    @Autowired
    IdGenerator idGenerator;

    @Override
    public VerifyHistory getVerifyHistory(String id) {
        return ht.get(VerifyHistory.class, id);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addVerifyHistory(VerifyHistory verifyHistory) {
        verifyHistory.setId(idGenerator.nextId(VerifyHistory.class));
        verifyHistory.setVerifyTime(new Date());
        //TODO 强制使用登录用户？
        verifyHistory.setVerifyUser(new User(SecurityContextHolder.getContext().getAuthentication().getName()));
        //TODO 只能保存
        ht.save(verifyHistory);
    }
}
