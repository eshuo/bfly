package info.bfly.archer.user.service;

import info.bfly.archer.user.model.UserPoint;
import info.bfly.p2p.loan.exception.InsufficientBalance;

/**
 * Description:用户积分service
 */
public interface UserPointService {
    /**
     * 增加积分
     *
     * @param userId         用户id
     * @param point          金额
     * @param type           积分类型
     * @param operatorInfo   操作信息
     * @param operatorDetail 操作详情
     */
    void add(String userId, int point, String type, String operatorInfo, String operatorDetail);

    /**
     * 获取用户积分对象
     *
     * @param userId 用户id
     * @param type   积分类型
     */
    UserPoint getPointByUserId(String userId, String type);

    /**
     * 获取用户积分数值
     *
     * @param userId 用户id
     * @param type   积分类型
     */
    int getPointsByUserId(String userId, String type);

    /**
     * 减少积分
     *
     * @param userId
     *            用户id
     * @param point
     *            金额
     * @param type
     *            积分类型
     * @param operatorInfo
     *            操作信息
     * @param operatorDetail
     *            操作详情
     * @throws InsufficientBalance
     *             积分不足
     */
    void minus(String userId, int point, String type, String operatorInfo, String operatorDetail) throws InsufficientBalance;
}
