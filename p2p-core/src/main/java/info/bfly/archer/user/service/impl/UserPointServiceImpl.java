package info.bfly.archer.user.service.impl;

import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.UserConstants.UserPointOperateType;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserPoint;
import info.bfly.archer.user.model.UserPointHistory;
import info.bfly.archer.user.service.UserPointService;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import org.hibernate.LockMode;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service(value = "userPointService")
public class UserPointServiceImpl implements UserPointService {
    @Resource
    private HibernateTemplate ht;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void add(String userId, int point, String type, String operatorInfo, String operatorDetail) {
        // FIXME:如果是添加的升级积分，则根据情况，需要调用userLevelService，进行升级
        if (point < 0) {
            throw new RuntimeException("point cannot be less than zero!");
        }
        UserPoint up = getPointByUserId(userId, type);
        if (up == null) {
            up = new UserPoint();
            up.setId(IdGenerator.randomUUID());
            up.setUser(new User(userId));
            up.setType(type);
            up.setPoint(0);
        } else {
            ht.lock(up, LockMode.PESSIMISTIC_WRITE);
        }
        up.setLastUpdateTime(new Date());
        up.setPoint(up.getPoint() + point);
        ht.saveOrUpdate(up);
        // 历史 start
        UserPointHistory history = new UserPointHistory();
        history.setId(IdGenerator.randomUUID());
        history.setTimeDate(new Date());
        history.setOperateType(UserPointOperateType.ADD);
        history.setType(type);
        history.setUser(new User(userId));
        history.setTypeInfo(operatorInfo);
        history.setRemark(operatorDetail);
        history.setPoint(point);
        ht.save(history);
        // 历史 end
        if (type.equals(UserConstants.UserPointType.LEVEL)) {
            // TODO:处理用户级别信息
        }
    }

    @Override
    public UserPoint getPointByUserId(String userId, String type) {
        String hql = "from UserPoint up where up.user.id =? and up.type=?";
        List<UserPoint> ups = (List<UserPoint>) ht.find(hql, userId, type);
        if (ups.size() == 0) {
            return null;
        } else if (ups.size() == 1) {
            ht.lock(ups.get(0), LockMode.PESSIMISTIC_WRITE);
            return ups.get(0);
        } else {
            throw new DuplicateKeyException("user.id:'" + userId + "', type:'" + type + "'");
        }
    }

    @Override
    public int getPointsByUserId(String userId, String type) {
        UserPoint up = getPointByUserId(userId, type);
        if (up == null) {
            return 0;
        }
        return up.getPoint();
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void minus(String userId, int point, String type, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        if (point < 0) {
            throw new RuntimeException("point cannot be less than zero!");
        }
        UserPoint up = getPointByUserId(userId, type);
        if (up == null) {
            up = new UserPoint();
            up.setId(IdGenerator.randomUUID());
            up.setUser(new User(userId));
            up.setType(type);
            up.setPoint(0);
        }
        if (up.getPoint() < point) {
            throw new InsufficientBalance("minus point:" + point + ",remain point:" + up.getPoint());
        }
        up.setPoint(up.getPoint() - point);
        up.setLastUpdateTime(new Date());
        // 历史 start
        UserPointHistory history = new UserPointHistory();
        history.setId(IdGenerator.randomUUID());
        history.setTimeDate(new Date());
        history.setOperateType(UserPointOperateType.MINUS);
        history.setType(type);
        history.setUser(new User(userId));
        history.setTypeInfo(operatorInfo);
        history.setRemark(operatorDetail);
        history.setPoint(point);
        ht.save(history);
        // 历史end
        ht.saveOrUpdate(up);
        if (type.equals(UserConstants.UserPointType.LEVEL)) {
            // TODO:处理用户级别信息
        }
    }
}
