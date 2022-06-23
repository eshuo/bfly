package info.bfly.archer.openauth.model;

import info.bfly.archer.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 第三方登录关联 id规则为：userId_typeId
 *
 * @author Administrator
 *
 */
@Entity
@Table(name = "open_auth")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class OpenAuth {
    private String       id;
    private User         user;
    private OpenAuthType type;
    private String       openId;
    private String       accessToken;

    @Column(name = "access_token", nullable = false, length = 1000)
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 规则为：userId_typeId
     *
     * @return
     */
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 65)
    public String getId() {
        return id;
    }

    @Column(name = "open_id", nullable = false, length = 1000)
    public String getOpenId() {
        return openId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "open_auth_type")
    public OpenAuthType getType() {
        return type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    public User getUser() {
        return user;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setType(OpenAuthType type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
