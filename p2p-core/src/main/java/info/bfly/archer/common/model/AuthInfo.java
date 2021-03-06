package info.bfly.archer.common.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * 认证信息实体
 *
 * @author Administrator
 */
@Entity
@Table(name = "auth_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@IdClass(AuthInfoPK.class)
public class AuthInfo {
    /**
     * 生成时间
     */
    private Date    generationTime;
    /**
     * 截止日期
     */
    private Date    deadline;
    /**
     * 认证次数
     */
    private Integer deadtime;
    /**
     * 认证码(不可重复)
     */
    private String  authCode;
    /**
     * 验证目标，记录手机号或者邮箱等
     */
    private String  authTarget;
    /**
     * 验证来源，userId之类
     */
    private String  authSource;
    /**
     * 认证码类型
     */
    private String  authType;
    private String  status;


    @Id
    @Column(name = "auth_source", length = 100)
    public String getAuthSource() {
        return authSource;
    }

    @Id
    @Column(name = "auth_target", nullable = false, length = 100)
    public String getAuthTarget() {
        return authTarget;
    }

    @Id
    @Column(name = "auth_type", nullable = false, length = 200)
    public String getAuthType() {
        return authType;
    }

    @Column(name = "auth_code", nullable = false, length = 100)
    public String getAuthCode() {
        return authCode;
    }

    @Column(name = "deadline")
    public Date getDeadline() {
        return deadline;
    }

    @Column(name = "deadtime")
    public Integer getDeadtime() {
        return deadtime;
    }

    @Column(name = "generation_time", nullable = false)
    public Date getGenerationTime() {
        return generationTime;
    }


    @Column(name = "status", nullable = false, length = 100)
    public String getStatus() {
        return status;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setAuthSource(String authSource) {
        this.authSource = authSource;
    }

    public void setAuthTarget(String authTarget) {
        this.authTarget = authTarget;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setDeadtime(Integer deadtime) {
        this.deadtime = deadtime;
    }

    public void setGenerationTime(Date generationTime) {
        this.generationTime = generationTime;
    }


    public void setStatus(String status) {
        this.status = status;
    }
}
