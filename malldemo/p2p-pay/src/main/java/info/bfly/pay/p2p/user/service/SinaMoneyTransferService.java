package info.bfly.pay.p2p.user.service;

import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.trusteeship.exception.TrusteeshipFormException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipTicketException;
import info.bfly.pay.p2p.user.model.MoneyTransfer;

/**
 * Created by XXSun on 6/5/2017.
 */
public interface SinaMoneyTransferService  {

    /**
     * 初始化一个转账
     *
     * @param fromUserId        转出用户ID
     * @param fromAccountType 转出账户类型
     * @param money           转账金额
     * @param toUserId         转入用户ID
     * @param toAccountType   转入账户类型
     * @return 初始状态的转账
     */
    MoneyTransfer initTransfer(String fromUserId, String fromAccountType, double money, String toUserId, String toAccountType) throws InsufficientBalance;

    /**
     * 初始化一个转账
     *
     * @param fromUserId 转出用户ID
     * @param money    转账金额
     * @param toUserId   转入用户ID
     * @return 初始状态的转账
     */
    MoneyTransfer initTransfer(String fromUserId, double money, String toUserId) throws InsufficientBalance;

    /**
     * 初始化一个系统转账
     * @param money 转账金额
     * @param toUserId 转入用户ID
     *                 默认账户
     * @return 初始状态的转账
     */
    MoneyTransfer initSystemTransfer(double money,String toUserId) throws InsufficientBalance;

    /**
     *  初始化一个系统转账
     * @param money 转账金额
     * @param toUserId 转入用户ID
     * @param toAccountType  转入账户类型
     * @return 初始状态的转账
     */
    MoneyTransfer initSystemTransfer(double money,String toUserId,String toAccountType) throws InsufficientBalance;
    /**
     * 申请转账
     *
     * @param transfer 转账请求
     * @return 链接
     */
    String applyMoneyTransfer(MoneyTransfer transfer) throws InsufficientBalance, TrusteeshipFormException, TrusteeshipTicketException, TrusteeshipReturnException;

    /**
     * 通过一个转账请求
     *
     * @param transfer 转账请求
     */
    void passMoneyTransfer(MoneyTransfer transfer) throws TrusteeshipReturnException;

    /**
     * 拒绝一个转账请求
     *
     * @param transfer 转账请求
     */
    void refuseMoneyTransfer(MoneyTransfer transfer) throws InsufficientBalance;


    /**
     * 完成一个转账请求
     *
     * @param transfer 转账请求
     */
    void finishMoneyTransfer(MoneyTransfer transfer) throws InsufficientBalance;



    /**
     * 绑定转账请求
     * @param type 转账类型
     * @param target 转账目标
     * @return
     */
    MoneyTransfer bindTarget(MoneyTransfer transfer, String type, String target);

}
