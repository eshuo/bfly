package info.bfly.archer.user.service;


/**
 * Description:用户等级service
 */
public interface UserLevelService {
    /**
     * 改变等级
     *
     * @param userId         用户id
     * @param levelId        等级id
     * @param validityPeriod 有效期（秒）
     * @param description    描述
     */
    void change(String userId, String levelId, int validityPeriod, String description);

    /**
     * 刷新用户等级，主要用户用户升级积分发生变化后
     *
     * @param userId 被刷新的用户编号
     */
    void refreshUserLevel(String userId);
}
