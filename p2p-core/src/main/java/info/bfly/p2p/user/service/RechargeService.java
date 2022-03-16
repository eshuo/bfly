package info.bfly.p2p.user.service;

import info.bfly.archer.user.model.RechargeBankCard;
import info.bfly.archer.user.model.UserBill;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.loan.model.Recharge;

import java.util.List;

/**
 * Description:充值接口
 */
public interface RechargeService {


    /**
     * 初始化只有类型与金额的充值
     * @param recharge
     * @return
     */
    Recharge initRecharge(Recharge recharge);

    /**
     * 计算充值手续费
     *
     * @param amount 充值金额
     * @return double
     */
    double calculateFee(double amount);

    /**
     * 线下充值
     *
     * @param recharge
     * @return
     */
    String createOfflineRechargeOrder(Recharge recharge);

    /**
     * 生成一个充值订单
     *
     * @return 充值订单
     */
    Recharge createRechargeOrder(Recharge recharge) throws ExceedDeadlineException, UnreachedMoneyLimitException;

    /**
     * 获取一个订单
     * @param rechargeId
     * @return
     */
    Recharge get(String rechargeId);

    /**
     * 保存
     * @param recharge
     */
    void update(Recharge recharge);
    /**
     * 获取银行卡直连的列表
     */
    List<RechargeBankCard> getBankCardsList();

    /**
     * 通过银行编号获取银行名称，比如 农业银行-ABC；工商银行-ICBC
     *
     * @param bankNo
     * @return
     */
    String getBankNameByNo(String bankNo);

    /**
     * 管理员充值（把充值置为成功）
     *
     * @param rechargeId 充值编号
     */
    void rechargeByAdmin(String rechargeId);

    /**
     * 管理员充值
     *
     * @param userBill 资金转移对象
     */
    void rechargeByAdmin(UserBill userBill);



    /**
     * 充值支付成功回调，可能回调多次
     *
     * @param rechargeId 充值编号
     */
    void rechargePaySuccess(String rechargeId,String orderNo);

    /**

     * 充值支付失败回调
     * @param rechargeId 充值编号
     */
    void  rechargePayFail(String rechargeId,String orderNo);

}
