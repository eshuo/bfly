package info.bfly.p2p.message.service;

import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.p2p.message.model.UserMessageTemplate;

import java.util.List;

/**
 * Description: 消息service<br/>
 */
public interface MessageService {
    /**
     * 查询用户所有启用的消息模板
     *
     * @param userId 用户编号
     * @return 该用户启用的消息模板
     */
    List<UserMessageTemplate> getUserMessagesTemplatesByUserId(String userId);

    /**
     * 保存用户消息设置
     *
     * @param userId 用户编号
     * @param umts   所选定的消息模板
     */
    void saveUserMessages(String userId, List<UserMessageTemplate> umts);

    /**
     * 发送邮件
     *
     * @param userId  接收用户id
     * @param title   邮件标题
     * @param content 邮件内容
     */
    void sendEmailMsg(String userId, String title, String content) throws UserNotFoundException;

    /**
     * 发送手机短信
     *
     * @param userId
     *            接收用户id
     * @param msg
     *            短信内容
     */
    void sendMobileMsg(String userId, String msg) throws UserNotFoundException;

    /**
     * 发送站内信
     *
     * @param userId
     *            接收用户id
     * @param title
     *            站内信标题
     * @param msg
     *            站内信内容
     */
    void sendStationMsg(String reveiverId, String senderId, String title, String msg) throws UserNotFoundException;
}
