package info.bfly.p2p.message.service.impl;

import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.MailUtil;
import info.bfly.p2p.message.MessageConstants;
import info.bfly.p2p.message.model.UserMessage;
import info.bfly.p2p.message.model.UserMessageTemplate;
import info.bfly.p2p.message.service.MessageService;
import info.bfly.p2p.message.service.SmsService;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Log
    private static Logger log;
    @Resource
    private        HibernateTemplate              ht;
    @Resource
    private        MessageBO                      messageBO;
    @Resource
    private        SmsService                     smsService;

    @Override
    public List<UserMessageTemplate> getUserMessagesTemplatesByUserId(String userId) {
        String hql = "select um.userMessageTemplate from UserMessage um where um.user.id=?";
        return (List<UserMessageTemplate>) ht.find(hql, userId);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveUserMessages(String userId, List<UserMessageTemplate> umts) {
        ht.bulkUpdate("delete from UserMessage um where um.user.id=?", userId);
        for (UserMessageTemplate umt : umts) {
            UserMessage um = new UserMessage();
            um.setId(IdGenerator.randomUUID());
            um.setUser(new User(userId));
            um.setUserMessageTemplate(umt);
            ht.save(um);
        }
    }

    @Override
    public void sendEmailMsg(String userId, String title, String msg) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        if (MessageServiceImpl.log.isDebugEnabled()) MessageServiceImpl.log.debug("Send email message to User ," + "id = " + userId + ",title=" + title + ",msg = " + msg);
        if (messageBO.isMessageWayenable(MessageConstants.UserMessageWayId.EMAIL)) {
            MailUtil.sendMail(user.getEmail(), title, msg);
        }
    }

    @Override
    public void sendMobileMsg(String userId, String msg) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("user.id:" + userId);
        }
        if (messageBO.isMessageWayenable(MessageConstants.UserMessageWayId.SMS)) {
            if (MessageServiceImpl.log.isDebugEnabled()) MessageServiceImpl.log.debug("Send mobile message to User ," + "id = " + userId + ",msg = " + msg);
            smsService.send(msg, user.getMobileNumber());
        }
    }

    @Override
    public void sendStationMsg(String reveiverId, String senderId, String title, String msg) throws UserNotFoundException {
        User receiver = ht.get(User.class, reveiverId);
        if (receiver == null) {
            throw new UserNotFoundException("user.id:" + reveiverId);
        }
        User sender = ht.get(User.class, senderId);
        if (sender == null) {
            throw new UserNotFoundException("user.id:" + sender);
        }
        if (messageBO.isMessageWayenable(MessageConstants.UserMessageWayId.LETTER)) {
            if (MessageServiceImpl.log.isDebugEnabled())
                MessageServiceImpl.log.debug("Send private message to User ," + "reveiverid = " + reveiverId + ",senderId=" + senderId + ",title=" + title + ",msg = " + msg);
            messageBO.sendStationMsg(receiver, sender, title, msg);
        }
    }
}
