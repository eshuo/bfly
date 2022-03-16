package info.bfly.archer.message.service;

/**
 * Description: 站内信模块接口
 */
public interface StationMessageService {
    /**
     * 删除一条站内消息
     *
     * @param messageId 消息ID
     */
    void deleteStationMsg(String messageId);

    /**
     * 将站内信息标记为已读
     *
     * @param messageId 消息ID
     */
    void markRead(String messageId);
}
