package info.bfly.archer.user.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * UserLoginLog entity. 用户登录日志
 */
@Entity
@Table(name = "user_login_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserLoginLog implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 1863806974093564617L;
    private String    id;
    private String    username;
    private Timestamp loginTime;
    private String    loginIp;
    /**
     * 登录是否成功，可以用来判断登录失败次数，进而结合其他数据对账号进行相关处理。
     */
    private String    isSuccess;
    /**
     * 登录信息
     */
    private String    loginInfo;

    // Constructors

    /**
     * default constructor
     */
    public UserLoginLog() {
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "is_success", nullable = false, length = 1)
    public String getIsSuccess() {
        return isSuccess;
    }

    @Column(name = "login_info", length = 200)
    public String getLoginInfo() {
        return loginInfo;
    }

    @Column(name = "login_ip", length = 70)
    public String getLoginIp() {
        return loginIp;
    }

    @Column(name = "login_time", nullable = false, length = 19)
    public Timestamp getLoginTime() {
        return loginTime;
    }

    @Column(name = "username", nullable = false, length = 100)
    public String getUsername() {
        return username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setLoginInfo(String loginInfo) {
        this.loginInfo = loginInfo;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
