package info.bfly.archer.user.service.impl;

import info.bfly.archer.user.service.UserBillService;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Service(value = "userBillService")
public class UserBillServiceImpl implements UserBillService {
    @Resource
    private HibernateTemplate ht;
    @Resource
    private UserBO            userBO;
    @Resource
    private UserBillBO        userBillBO;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Deprecated
    public void freezeMoney(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.freezeMoney(userId, money, operatorInfo, operatorDetail);
    }

    @Override
    @Deprecated
    public double getBalance(String userId) {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        return userBillBO.getBalance(userId);
    }

    @Override
    @Deprecated
    public double getFrozenMoney(String userId) {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        return userBillBO.getFrozenMoney(userId);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Deprecated
    public void transferIntoBalance(String userId, double money, String operatorInfo, String operatorDetail) {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.transferIntoBalance(userId, money, operatorInfo, operatorDetail);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Deprecated
    public void transferOutFromBalance(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.transferOutFromBalance(userId, money, operatorInfo, operatorDetail);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Deprecated
    public void transferOutFromFrozen(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.transferOutFromFrozen(userId, money, operatorInfo, operatorDetail);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Deprecated
    public void unfreezeMoney(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.unfreezeMoney(userId, money, operatorInfo, operatorDetail);
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void freezeMoney(String userId, double money, String operatorInfo, String operatorDetail, String accountType) throws InsufficientBalance {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.freezeMoney(userId, money, operatorInfo, operatorDetail,accountType);
    }

    @Override
    public double getBalance(String userId, String accountType) {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        return userBillBO.getBalance(userId,accountType);
    }

    @Override
    public double getFrozenMoney(String userId, String accountType) {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        return userBillBO.getFrozenMoney(userId,accountType);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void transferIntoBalance(String userId, double money, String operatorInfo, String operatorDetail, String accountType) {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.transferIntoBalance(userId, money, operatorInfo, operatorDetail,accountType);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void transferOutFromBalance(String userId, double money, String operatorInfo, String operatorDetail, String accountType) throws InsufficientBalance {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.transferOutFromBalance(userId, money, operatorInfo, operatorDetail,accountType);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void transferOutFromFrozen(String userId, double money, String operatorInfo, String operatorDetail, String accountType) throws InsufficientBalance {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.transferOutFromFrozen(userId, money, operatorInfo, operatorDetail,accountType);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void unfreezeMoney(String userId, double money, String operatorInfo, String operatorDetail, String accountType) throws InsufficientBalance {
        Assert.notNull(userBO.getUserById(userId), "用户" + userId + "不存在");
        userBillBO.unfreezeMoney(userId, money, operatorInfo, operatorDetail,accountType);
    }

}
