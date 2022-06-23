package info.bfly.core.util;

import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailUtil {
    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);
    private static       String host;
    private static       String username;
    private static       String password;
    private static       String personal;

    public static Session getMailSession() {
        // 根据属性新建一个邮件会话
        return Session.getInstance(MailUtil.getProperties(), new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailUtil.username, MailUtil.password);
            }
        });
    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        // 设置邮件服务器
        HibernateTemplate ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
        MailUtil.host = ht.get(Config.class, ConfigConstants.Mail.MAIL_SMTP).getValue();
        MailUtil.username = ht.get(Config.class, ConfigConstants.Mail.MAIL_USER_NAME).getValue();
        MailUtil.password = ht.get(Config.class, ConfigConstants.Mail.MAIL_PASSWORD).getValue();
        MailUtil.personal = ht.get(Config.class, ConfigConstants.Mail.MAIL_PERSONAL).getValue();
        properties.put("mail.smtp.host", MailUtil.host);
        // 验证
        properties.put("mail.smtp.auth", "true");
        return properties;
    }

    public static boolean sendMail(String toAddress, String title, String content) {
        return MailUtil.sendMail(toAddress, MailUtil.personal, title, content);
    }

    @SuppressWarnings("static-access")
    // FIXME:添加异常，显示邮件发送情况。另外，邮件发送，改为异步发送；如果异步，发送成功与否的消息怎么处理？
    public static boolean sendMail(String toAddress, String personal, String title, String content) {
        final MimeMessage mailMessage;
        final Transport trans;
        Session mailSession = MailUtil.getMailSession();
        // 建立消息对象
        mailMessage = new MimeMessage(mailSession);
        try {
            // 发件人
            mailMessage.setFrom(new InternetAddress(MailUtil.username, personal));
            // 收件人
            mailMessage.setRecipient(RecipientType.TO, new InternetAddress(toAddress));
            // 主题
            mailMessage.setSubject(title);
            // 内容
            // mailMessage.setText(content);
            mailMessage.setContent(content, "text/html;charset=utf-8");
            // 发信时间
            mailMessage.setSentDate(new Date());
            // 存储信息
            mailMessage.saveChanges();
            trans = mailSession.getTransport("smtp");
            // FIXME 需要修改为异步发送消息
            Transport.send(mailMessage);
        }
        catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
        return true;
    }
}
