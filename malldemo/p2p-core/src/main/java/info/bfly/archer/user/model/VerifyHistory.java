package info.bfly.archer.user.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by XXSun on 3/9/2017.
 */
@Entity
@Table(name = "verify_history")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "entityCache")
public class VerifyHistory {
    private String id;
    /**
     * 审核留言信息
     */
    private String verifyMessage;
    /**
     * 审核验证时间
     */
    private Date   verifyTime;
    /**
     * 审核类型
     */
    private String verifyType;
    /**
     * 审核目标
     */
    private String verifyTarget;

    /**
     * 审核结果
     */
    private String status;

    /**
     * 审核者
     */
    private User verifyUser;

    @Id
    @Column
    public String getId() {
        return id;
    }

    @Column(name = "verify_message", nullable = false)
    public String getVerifyMessage() {
        return verifyMessage;
    }

    @Column(name = "verify_time", nullable = false)
    public Date getVerifyTime() {
        return verifyTime;
    }

    @Column(name = "verify_type", nullable = false)
    public String getVerifyType() {
        return verifyType;
    }

    @Column(name = "verify_target", nullable = false)
    public String getVerifyTarget() {
        return verifyTarget;
    }

    @Column(name = "status", nullable = false)
    public String getStatus() {
        return status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getVerifyUser() {
        return verifyUser;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public void setVerifyTarget(String verifyTarget) {
        this.verifyTarget = verifyTarget;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVerifyUser(User verifyUser) {
        this.verifyUser = verifyUser;
    }
}
