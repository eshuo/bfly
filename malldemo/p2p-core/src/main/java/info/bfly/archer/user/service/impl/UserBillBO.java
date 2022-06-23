package info.bfly.archer.user.service.impl;

import info.bfly.archer.user.UserBillConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserBill;
import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service(value = "userBillBO")
public class UserBillBO {
    private final String DEFAULT_ACCOUNT_TYPE = "BASIC";
    @Resource
    private HibernateTemplate ht;



    /**
     * 冻结金额
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     * @throws InsufficientBalance
     */
    @Deprecated
    public void freezeMoney(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        this.freezeMoney(userId, money, operatorInfo, operatorDetail, DEFAULT_ACCOUNT_TYPE);
    }

    /**
     * 冻结金额
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     * @throws InsufficientBalance
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void freezeMoney(String userId, double money, String operatorInfo, String operatorDetail, String accountType) throws InsufficientBalance {

        Assert.isTrue(money > 0, "金额不能小于0");
        UserBill ibLastest = getLastestBill(userId, accountType);
        UserBill ib = new UserBill();
        double balance = ibLastest.getBalance();
        if (balance < money) {
            throw new InsufficientBalance("freeze money:" + money + ", balance:" + balance);
        } else {
            ib.setId(IdGenerator.randomUUID());
            ib.setMoney(money);
            ib.setTime(new Date());
            ib.setDetail(operatorDetail);
            ib.setType(UserBillConstants.Type.FREEZE);
            ib.setTypeInfo(operatorInfo);
            ib.setUser(new User(userId));
            ib.setSeqNum(ibLastest.getSeqNum() + 1);
            ib.setAccountType(ibLastest.getAccountType());
            // 余额=上一条余额-将要被冻结的金额
            ib.setBalance(ArithUtil.sub(ibLastest.getBalance(), money));
            // 最新冻结金额=上一条冻结+将要冻结
            ib.setFrozenMoney(ArithUtil.add(ibLastest.getFrozenMoney(), money));
            ht.save(ib);
        }
    }
    

    /**
     * 获取余额
     * 默认账户类型BASIC
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Deprecated
    public double getBalance(String userId) {
        return this.getBalance(userId, DEFAULT_ACCOUNT_TYPE);
    }

    /**
     * 获取余额
     *
     * @param userId 用户名
     * @return 余额
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public double getBalance(String userId, String accountType) {
        UserBill ub = getLastestBill(userId, accountType);

        return ub == null ? 0D : ub.getBalance();
    }

    /**
     * 获取冻结金额
     *
     * @param userId
     * @return
     */
    @Deprecated
    public double getFrozenMoney(String userId) {
        return this.getFrozenMoney(userId, DEFAULT_ACCOUNT_TYPE);
    }

    /**
     * 获取冻结金额
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public double getFrozenMoney(String userId, String accountType) {
        UserBill ub = getLastestBill(userId, accountType);
        return ub == null ? 0D : ub.getFrozenMoney();

    }

    /**
     * 转入到余额
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     */
    @Deprecated
    public void transferIntoBalance(String userId, double money, String operatorInfo, String operatorDetail) {
        this.transferIntoBalance(userId, money, operatorInfo, operatorDetail, DEFAULT_ACCOUNT_TYPE);
    }

    /**
     * 转入到余额
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void transferIntoBalance(String userId, double money, String operatorInfo, String operatorDetail, String accountType) {

        Assert.isTrue(money > 0, "金额不能小于0");
        UserBill ibLastest = getLastestBill(userId, accountType);
        UserBill lb = new UserBill();
        lb.setId(IdGenerator.randomUUID());
        lb.setMoney(money);
        lb.setTime(new Date());
        lb.setDetail(operatorDetail);
        lb.setType(UserBillConstants.Type.TI_BALANCE);
        lb.setTypeInfo(operatorInfo);
        lb.setUser(new User(userId));
        lb.setSeqNum(ibLastest.getSeqNum() + 1);
        lb.setAccountType(ibLastest.getAccountType());
        // 余额=上一条余额+money
        lb.setBalance(ArithUtil.add(ibLastest.getBalance(), money));
        // 最新冻结金额=上一条冻结
        lb.setFrozenMoney(ibLastest.getFrozenMoney());
        ht.save(lb);
    }

    /**
     * 从余额转出
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     * @throws InsufficientBalance
     */
    @Deprecated
    public void transferOutFromBalance(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        this.transferOutFromBalance(userId, money, operatorInfo, operatorDetail, DEFAULT_ACCOUNT_TYPE);
    }

    /**
     * 从余额转出
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     * @throws InsufficientBalance
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void transferOutFromBalance(String userId, double money, String operatorInfo, String operatorDetail, String accountType) throws InsufficientBalance {
        if (money < 0) {
            throw new RuntimeException("money cannot be less than zero!");
        }
        UserBill ibLastest = getLastestBill(userId, accountType);
        double balance = ibLastest.getBalance();
        UserBill ib = new UserBill();
        if (balance < money) {
            throw new InsufficientBalance("transfer out money:" + money + ",balance:" + balance);
        } else {
            ib.setId(IdGenerator.randomUUID());
            ib.setMoney(money);
            ib.setTime(new Date());
            ib.setDetail(operatorDetail);
            ib.setType(UserBillConstants.Type.TO_BALANCE);
            ib.setTypeInfo(operatorInfo);
            ib.setUser(new User(userId));
            ib.setSeqNum(ibLastest.getSeqNum() + 1);
            ib.setAccountType(ibLastest.getAccountType());
            // 余额=上一条余额-money
            ib.setBalance(ArithUtil.sub(ibLastest.getBalance(), money));
            // 最新冻结金额=上一条冻结
            ib.setFrozenMoney(ibLastest.getFrozenMoney());
            ht.save(ib);
        }
    }

    @Deprecated
    public void transferOutFromFrozen(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        this.transferOutFromFrozen(userId, money, operatorInfo, operatorDetail, DEFAULT_ACCOUNT_TYPE);
    }

    /**
     * 从冻结金额中转出
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     * @throws InsufficientBalance
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void transferOutFromFrozen(String userId, double money, String operatorInfo, String operatorDetail, String accountType) throws InsufficientBalance {
        if (money < 0) {
            throw new RuntimeException("money cannot be less than zero!");
        }
        UserBill ibLastest = getLastestBill(userId, accountType);
        double frozen = ibLastest.getFrozenMoney();
        UserBill ib = new UserBill();
        if (frozen < money) {
            throw new InsufficientBalance("transfer from frozen money:" + money + ", frozen money:" + frozen);
        }
        ib.setId(IdGenerator.randomUUID());
        ib.setMoney(money);
        ib.setTime(new Date());
        ib.setDetail(operatorDetail);
        ib.setType(UserBillConstants.Type.TO_FROZEN);
        ib.setTypeInfo(operatorInfo);
        ib.setUser(new User(userId));
        ib.setSeqNum(ibLastest.getSeqNum() + 1);
        ib.setAccountType(ibLastest.getAccountType());
        // 余额=上一条余额
        ib.setBalance(ibLastest.getBalance());
        // 最新冻结金额=上一条冻结-取出的
        ib.setFrozenMoney(ArithUtil.sub(ibLastest.getFrozenMoney(), money));
        ht.save(ib);
    }

    /**
     * 解冻金额
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     * @throws InsufficientBalance
     */
    @Deprecated
    public void unfreezeMoney(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        this.unfreezeMoney(userId, money, operatorInfo, operatorDetail, DEFAULT_ACCOUNT_TYPE);
    }

    /**
     * 解冻金额
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     * @throws InsufficientBalance
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void unfreezeMoney(String userId, double money, String operatorInfo, String operatorDetail, String accountType) throws InsufficientBalance {
        if (money < 0) {
            throw new RuntimeException("money cannot be less than zero!");
        }
        UserBill ibLastest = getLastestBill(userId, accountType);

        double frozen = ibLastest.getFrozenMoney();
        UserBill ib = new UserBill();
        if (frozen < money) {
            //TODO 抛出的异常需要重新设计
            throw new InsufficientBalance("unfreeze money:" + money + ", frozen money:" + frozen);
        } else {
            ib.setId(IdGenerator.randomUUID());
            ib.setMoney(money);
            ib.setTime(new Date());
            ib.setDetail(operatorDetail);
            ib.setType(UserBillConstants.Type.UNFREEZE);
            ib.setTypeInfo(operatorInfo);
            ib.setUser(new User(userId));
            ib.setSeqNum(ibLastest.getSeqNum() + 1);
            ib.setAccountType(ibLastest.getAccountType());
            // 余额=上一条余额+解冻的金额
            ib.setBalance(ArithUtil.add(ibLastest.getBalance(), money));
            // 最新冻结金额=上一条冻结-解冻的金额
            ib.setFrozenMoney(ArithUtil.sub(ibLastest.getFrozenMoney(), money));
            ht.save(ib);
        }
    }

    private UserBill getLastestBill(String userId, String accountType) {
        Assert.hasText(userId, "用户名不能为空");
        Assert.hasText(accountType, "账户类型不能为空");
        DetachedCriteria criteria = DetachedCriteria.forClass(UserBill.class);
        criteria.addOrder(Order.desc("seqNum"));
        criteria.setLockMode(LockMode.PESSIMISTIC_WRITE);
        criteria.add(Restrictions.eq("user.id", userId));
        criteria.add(Restrictions.eq("accountType", accountType));
        List<UserBill> ibs = (List<UserBill>) ht.findByCriteria(criteria, 0, 1);
        if (ibs.size() > 0) {
            UserBill ub = ibs.get(0);
            if (ub.getBalance() == null) {
                double freeze = getSumByType(userId, UserBillConstants.Type.FREEZE, accountType);
                double transferIntoBalance = getSumByType(userId, UserBillConstants.Type.TI_BALANCE, accountType);
                double transferOutFromBalance = getSumByType(userId, UserBillConstants.Type.TO_BALANCE, accountType);
                double unfreeze = getSumByType(userId, UserBillConstants.Type.UNFREEZE, accountType);
                ub.setBalance(ArithUtil.add(ArithUtil.sub(ArithUtil.sub(transferIntoBalance, transferOutFromBalance), freeze), unfreeze));
            }
            if (ub.getFrozenMoney() == null) {
                double freeze = getSumByType(userId, UserBillConstants.Type.FREEZE, accountType);
                double transferOutFromFrozen = getSumByType(userId, UserBillConstants.Type.TO_FROZEN, accountType);
                double unfreeze = getSumByType(userId, UserBillConstants.Type.UNFREEZE, accountType);
                ub.setFrozenMoney(ArithUtil.sub(ArithUtil.sub(freeze, unfreeze), transferOutFromFrozen));
            }
            ht.update(ub);
            return ub;
        } else {
            UserBill ub = new UserBill();
            ub.setId(IdGenerator.randomUUID());
            ub.setSeqNum(1L);
            ub.setMoney(0d);
            ub.setFrozenMoney(0d);
            ub.setBalance(0d);
            ub.setTime(new Date());
            ub.setDetail("初始化用户账户");
            ub.setType(UserBillConstants.Type.INIT);
            ub.setTypeInfo(UserBillConstants.OperatorInfo.INIT_BILL);
            ub.setAccountType(accountType);
            ub.setUser(new User(userId));
            ht.save(ub);
            return ub;
        }
    }

    private double getSumByType(String userId, String type, String accountType) {
        String hql = "select sum(ub.money) from UserBill ub where ub.user.id =? and ub.type=? and ub.accountType=?";
        Double sum = (Double) ht.find(hql, userId, type, accountType).get(0);
        if (sum == null) {
            return 0;
        }
        return ArithUtil.round(sum, 2);
    }

}
