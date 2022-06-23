package info.bfly.core.util;

import javax.faces.bean.ManagedBean;

/**
 */
@ManagedBean
public class EmailUtil {
    /**
     * 通过email，获取邮箱登录地址
     *
     * @param email
     * @return
     */
    public static String getUrlByEmail(String email) {
        int offect = email.indexOf("@");
        String suffix = email.substring(offect + 1, email.length());
        String emailLoginUrl;
        if (suffix.indexOf("gmail") > -1) {
            emailLoginUrl = "http://mail.google.com";
        } else if (suffix.indexOf("hotmail") > -1) {
            emailLoginUrl = "http://login.live.com";
        } else {
            emailLoginUrl = "http://mail." + suffix;
        }
        return emailLoginUrl;
    }
}
