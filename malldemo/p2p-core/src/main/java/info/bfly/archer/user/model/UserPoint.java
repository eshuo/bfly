package info.bfly.archer.user.model;

import java.util.Date;

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
 * 用户积分
 */
@Entity
@Table(name = "user_point")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserPoint implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 4596223445712958908L;
    private String id;
    private User   user;
    /**
     * 上次更新时间
     */
    private Date   lastUpdateTime;
    /**
     * 积分类型（升级积分、消费积分等等）
     */
    private String type;
    private int    point;

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "last_update_time", nullable = false)
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Column(name = "point", nullable = false)
    public int getPoint() {
        return point;
    }

    @Column(name = "type", nullable = false, length = 100)
    public String getType() {
        return type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
