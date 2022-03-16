package info.bfly.archer.user.service;

import java.util.List;

/**
 */
public interface UserInfoService {
    /**
     * 查询当前用户的消息设置
     *
     * @param 用户id
     * @return list
     */
    List getUserMessage(String uid);

    /**
     * 邮箱是否存在 存在返回true 否则返回false
     *
     * @param email 邮箱
     * @return boolean
     */
    boolean isEmailExist(String email);

    /**
     * 手机号是否存在 存在返回true 否则返回false
     *
     * @param mobileNumber 手机号
     * @return boolean
     */
    boolean isMobileNumberExist(String mobileNumber);

    /**
     * 是否发送消息给用户
     *
     * @param 用户id
     * @param 当前节点id
     * @return boolean
     */
    boolean isSendMessageToUser(String uid, String nodeId);

    /**
     * 用户名是否存在 存在返回true 否则返回false
     *
     * @param username 用户名
     * @return boolean
     */
    boolean isUsernameExist(String username);

    /**
     * 用户是否为VIP
     *
     * @param userId 用户id
     * @return boolean
     */
    boolean isVip(String userId);

    /**
     * 修改交易密码
     *
     * @param userId      用户id
     * @param newPassword 新密码
     */
    void modifyCashPassWord(String userId, String newPassword);

    /**
     * 修改绑定邮箱
     *
     * @param userId   用户id
     * @param newEmail 新邮箱
     */
    void modifyEmail(String userId, String newEmail);

    /**
     * 重置交易密码
     *
     * @param userId 用户id
     */
    void resetCashPassword(String userId);

    /**
     * 保存用户消息设置
     *
     * @param 用户id
     * @param 消息列表
     * @return void
     */
    void saveMessageSet(String uid, List list);

    /**
     * 用户风险等级计算
     *
     * @param 用户id
     * @return String
     */
    String userRiskRankCalculation(String uid);

    /**
     * 验证旧交易密码 正确返回true，错误返回false
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @return boolean
     */
    boolean verifyOldPassword(String userId, String oldPassword);
}
