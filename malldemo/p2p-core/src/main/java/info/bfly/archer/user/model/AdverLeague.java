package info.bfly.archer.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * cpa 广告联盟
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "adver_league")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AdverLeague implements java.io.Serializable {
    private static final long serialVersionUID = -1083788599555977878L;
    private int    id;
    // 联盟ID
    private String mid;
    // 联盟用户ID
    private String uid;
    // 注册日期
    private Date   regDate;
    // 查询状态 默认值2 查询注册成功
    private String status;
    // 用户是否已经认证，1：已认证，2：未认证
    private String userName;

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 11)
    public int getId() {
        return id;
    }

    @Column(name = "mid", length = 255)
    public String getMid() {
        return mid;
    }

    @Column(name = "regDate")
    public Date getRegDate() {
        return regDate;
    }

    @Column(name = "status", length = 255)
    public String getStatus() {
        return status;
    }

    @Column(name = "uid", length = 255)
    public String getUid() {
        return uid;
    }

    @Column(name = "userName", length = 255)
    public String getUserName() {
        return userName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
