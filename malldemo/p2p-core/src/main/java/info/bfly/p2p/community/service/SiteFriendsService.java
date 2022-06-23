package info.bfly.p2p.community.service;

/**
 * Description: 站内好友管理接口
 */
public interface SiteFriendsService {
    /**
     * 把某个用户加入到黑名单
     *
     * @param curUserId    当前用户id
     * @param friendUserId 要加入到黑名单的用户id
     */
    void addUserToBlacklist(String curUserId, String friendUserId);

    /**
     * 同意好友请求
     *
     * @param curUserId    当前用户id
     * @param friendUserId 好友id
     */
    void agreeFriendApply(String curUserId, String friendUserId);

    /**
     * 申请加其他用户为好友
     *
     * @param curUserId    当前用户id
     * @param friendUserId 要加好友的用户Id
     * @param remarks      好友请求备注信息
     */
    void applyForFriend(String curUserId, String friendUserId, String remarks);

    /**
     * 从黑名单里删除用户
     *
     * @param curUserId
     *            当前用户id
     * @param friendUserId
     *            好友用户id
     */
    void deleteUserFromBlacklist(String curUserId, String friendUserId);

    /**
     * 判断用户是否在黑名单里
     *
     * @param curUserId
     *            当前用户id
     * @param friendUserId
     *            好友用户id
     * @return 布尔值 true为属于黑名单 false为不属于黑名单
     */
    boolean isUserInBlacklist(String curUserId, String friendUserId);

    /**
     * 拒绝好友请求
     *
     * @param curUserId
     *            当前用户id
     * @param friendUserId
     *            好友id
     * @param remarks
     *            拒绝请求的备注信息
     */
    void refuseFriendApply(String curUserId, String friendUserId, String remarks);
}
