package info.bfly.p2p.trusteeship.model;

import info.bfly.archer.user.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Description: 资金托管账户
 */
@Entity
@Table(name = "trusteeship_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class TrusteeshipAccount {
    private String id;
    private User   user;
    /**
     * 托管方
     */
    private String trusteeship;
    /**
     * 用户在托管方的编号
     */
    private String accountId;
    /**
     * 用户在托管方的账户标志类型
     */
    private String type;

    /**
     * 默认账户类型
     */
    private String defaultAccountType;
    /**
     * 在托管方的开户时间
     */
    private Date   createTime;
    private String status;

    @Column(name = "account_id", length = 50)
    public String getAccountId() {
        return accountId;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "status", length = 500, nullable = false)
    public String getStatus() {
        return status;
    }

    @Column(name = "trusteeship", length = 50, nullable = false)
    public String getTrusteeship() {
        return trusteeship;
    }

    @Column(name = "type", length = 50, nullable = false)
    public String getType() {
        return type;
    }

    @Column(name = "default_account_type", length = 50)
    public String getDefaultAccountType() {
        return defaultAccountType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTrusteeship(String trusteeship) {
        this.trusteeship = trusteeship;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDefaultAccountType(String defaultAccountType) {
        this.defaultAccountType = defaultAccountType;
    }
}
