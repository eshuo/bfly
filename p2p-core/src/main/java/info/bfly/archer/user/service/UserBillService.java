package info.bfly.archer.user.service;

import info.bfly.p2p.loan.exception.InsufficientBalance;

/**
 * Description:用户账户service
 */
public interface UserBillService {
    /**
     * 冻结金额.
     *
     * @param userId
     * @param money          金额
     * @param operatorInfo   操作信息
     * @param operatorDetail 操作详情
     * @throws InsufficientBalance 余额不足
     */
    void freezeMoney(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance;
 /**
     * 冻结金额.
     *
     * @param userId
     * @param money          金额
     * @param operatorInfo   操作信息
     * @param operatorDetail 操作详情
     * @throws InsufficientBalance 余额不足
     */
    void freezeMoney(String userId, double money, String operatorInfo, String operatorDetail,String accountType) throws InsufficientBalance;

    /**
     * 获取用户账户余额
     *
     * @param userId 用户id
     * @return 余额
     */
    double getBalance(String userId);
    /**
     * 获取用户账户余额
     *
     * @param userId 用户id
     * @return 余额
     */
    double getBalance(String userId,String accountType);

    /**
     * 获取用户账户冻结金额
     *
     * @param userId 用户id
     * @return 余额
     */
    double getFrozenMoney(String userId);
    /**
     * 获取用户账户冻结金额
     *
     * @param userId 用户id
     * @return 余额
     */
    double getFrozenMoney(String userId,String accountType);

    /**
     * 转入到余额，例如借款成功时候。
     *
     * @param userId
     *            用户id
     * @param money
     *            金额
     * @param operatorInfo
     *            操作信息
     * @param operatorDetail
     *            操作详情
     *
     */
    void transferIntoBalance(String userId, double money, String operatorInfo, String operatorDetail);
    /**
     * 转入到余额，例如借款成功时候。
     *
     * @param userId
     *            用户id
     * @param money
     *            金额
     * @param operatorInfo
     *            操作信息
     * @param operatorDetail
     *            操作详情
     *
     */
    void transferIntoBalance(String userId, double money, String operatorInfo, String operatorDetail,String accountType);

    /**
     * 从余额中转出
     *
     * @param userId
     *            用户id
     * @param money
     *            金额
     * @param operatorInfo
     *            操作信息
     * @param operatorDetail
     *            操作详情
     * @throws InsufficientBalance
     *             余额不足
     */
    void transferOutFromBalance(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance;
    /**
     * 从余额中转出
     *
     * @param userId
     *            用户id
     * @param money
     *            金额
     * @param operatorInfo
     *            操作信息
     * @param operatorDetail
     *            操作详情
     * @throws InsufficientBalance
     *             余额不足
     */
    void transferOutFromBalance(String userId, double money, String operatorInfo, String operatorDetail,String accountType) throws InsufficientBalance;

    /**
     * 从冻结金额中转出
     *
     * @param userId
     *            用户id
     * @param money
     *            金额
     * @param operatorInfo
     *            操作信息
     * @param operatorDetail
     *            操作详情
     * @throws InsufficientBalance
     *             余额不足
     */
    void transferOutFromFrozen(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance;
    /**
     * 从冻结金额中转出
     *
     * @param userId
     *            用户id
     * @param money
     *            金额
     * @param operatorInfo
     *            操作信息
     * @param operatorDetail
     *            操作详情
     * @throws InsufficientBalance
     *             余额不足
     */
    void transferOutFromFrozen(String userId, double money, String operatorInfo, String operatorDetail,String accountType) throws InsufficientBalance;

    /**
     * 解冻金额.
     *
     * @param userId         用户id
     * @param money          金额
     * @param operatorInfo   操作信息
     * @param operatorDetail 操作详情
     * @throws InsufficientBalance 余额不足
     */
    void unfreezeMoney(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance;
    /**
     * 解冻金额.
     *
     * @param userId         用户id
     * @param money          金额
     * @param operatorInfo   操作信息
     * @param operatorDetail 操作详情
     * @throws InsufficientBalance 余额不足
     */
    void unfreezeMoney(String userId, double money, String operatorInfo, String operatorDetail,String accountType) throws InsufficientBalance;
}
