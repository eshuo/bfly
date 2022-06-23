
package info.bfly.archer.system.model;

import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.model.Config;
import info.bfly.archer.user.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.http.HttpServletRequest;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XXSun
 */
// TODO 需要放到数据库么？
@ApplicationScope
@Service("ipsMapService")
public class IpsMapService {

    @Autowired
    HibernateTemplate ht;

    public class Ips {
        private int  failed_times  = 0;
        private long failed_expira = 0L;

        Ips() {
        }

        Ips(int i, long timeExpira) {
        }

        Long getFailed_expira() {
            return failed_expira;
        }

        public int getFailed_times() {
            return failed_times;
        }

        void setFailed_expira(long failed_expira) {
            this.failed_expira = failed_expira;
        }

        void setFailed_times(int failed_times) {
            this.failed_times = failed_times;
        }
    }

    public static long                           DEFAULT_TIMEEXPIRA = 86400000L;
    private       ConcurrentHashMap<String, Ips> ipsMap             = new ConcurrentHashMap<String, Ips>();

    public Ips addFailedTimes(String ip) {
        Ips ips = ipsMap.get(ip);
        if (ips == null) {
            ips = new Ips();
        }
        ips.setFailed_times(ips.getFailed_times() + 1);
        ipsMap.put(ip, ips);
        return ips;
    }

    public Ips addTimeExpira(String ip, long timeExpira) {
        Ips ips = ipsMap.get(ip);
        if (ips == null) {
            ips = new Ips();
        }
        ips.setFailed_expira(timeExpira);
        ipsMap.put(ip, ips);
        return ips;
    }

    public long getFailedTimes(String ip) {
        Ips ips = ipsMap.get(ip);
        if (ips == null) return 0;
        return ipsMap.get(ip).getFailed_times();
    }

    public long getTimeExpira(String ip) {
        Ips ips = ipsMap.get(ip);
        if (ips == null) return 0;
        return ips.getFailed_expira();
    }

    public void removeTimeExpira(String ip) {
        ipsMap.remove(ip);
    }

    public void setIpsMap(ConcurrentHashMap<String, Ips> ipsMap) {
        this.ipsMap = ipsMap;
    }


    public boolean needValidateCode(HttpServletRequest request) {
        WebAuthenticationDetails authenticationDetails = new WebAuthenticationDetails(request);
        // 全局判断是否需要验证码
        Boolean globalneedVcode = Integer.parseInt(ht.get(Config.class, ConfigConstants.UserSafe.LOGIN_FAIL_MAX_TIMES).getValue()) != 0;
        // 请求是否需要验证码
        Boolean needValidateCode = (Boolean) request.getSession(true).getAttribute(UserConstants.AuthenticationManager.NEED_VALIDATE_CODE);
        // ip是否需要验证码
        Boolean ipneedVcode = getTimeExpira(authenticationDetails.getRemoteAddress()) > new GregorianCalendar(TimeZone.getDefault()).getTimeInMillis();
        if (!ipneedVcode && getTimeExpira(authenticationDetails.getRemoteAddress()) != 0) removeTimeExpira(authenticationDetails.getRemoteAddress());
        globalneedVcode = globalneedVcode == null ? false : globalneedVcode;
        needValidateCode = needValidateCode == null ? false : needValidateCode;
        ipneedVcode = ipneedVcode == null ? false : ipneedVcode;
        return globalneedVcode && (needValidateCode || ipneedVcode);
    }
}
