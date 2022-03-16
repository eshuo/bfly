package info.bfly.archer.system.model;

import info.bfly.archer.user.model.User;

import java.sql.Timestamp;

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
 * AccessStatistics entity. 访问统计表
 */
@Entity
@Table(name = "access_statistics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AccessStatistics implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -2861944028835058005L;
    private String    id;
    private User      user;
    private Timestamp accessTime;
    private String    accessUrl;
    private String    visitorIp;
    /**
     * 该访问的响应时间
     */
    private Double    responseTime;

    // Constructors

    /**
     * default constructor
     */
    public AccessStatistics() {
    }

    /**
     * minimal constructor
     */
    public AccessStatistics(String id, Timestamp accessTime, String accessUrl) {
        this.id = id;
        this.accessTime = accessTime;
        this.accessUrl = accessUrl;
    }

    /**
     * full constructor
     */
    public AccessStatistics(String id, User user, Timestamp accessTime, String accessUrl, String visitorIp, Double responseTime) {
        this.id = id;
        this.user = user;
        this.accessTime = accessTime;
        this.accessUrl = accessUrl;
        this.visitorIp = visitorIp;
        this.responseTime = responseTime;
    }

    @Column(name = "access_time", nullable = false, length = 19)
    public Timestamp getAccessTime() {
        return accessTime;
    }

    @Column(name = "access_url", nullable = false, length = 500)
    public String getAccessUrl() {
        return accessUrl;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "response_time", precision = 22, scale = 0)
    public Double getResponseTime() {
        return responseTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    public User getUser() {
        return user;
    }

    @Column(name = "visitor_ip", length = 64)
    public String getVisitorIp() {
        return visitorIp;
    }

    public void setAccessTime(Timestamp accessTime) {
        this.accessTime = accessTime;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVisitorIp(String visitorIp) {
        this.visitorIp = visitorIp;
    }
}
