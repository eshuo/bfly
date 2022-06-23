package info.bfly.archer.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户积分历史
 */
@Entity
@Table(name = "user_point_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserPointHistory implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -6968221624925039503L;
    private String id;
    private User   user;
    /**
     * 使用属性time，尽量使用setTimeDate/getTimeDate这两个方法
     */
    private Date   time;
    // 增加还是扣除积分
    private String operateType;
    private String type;
    private String typeInfo;
    private String remark;
    private int    point;

    public UserPointHistory() {
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "operate_type", nullable = false, length = 50)
    public String getOperateType() {
        return operateType;
    }

    @Column(name = "point", nullable = false)
    public int getPoint() {
        return point;
    }

    @Lob
    @Column(name = "remark", columnDefinition = "CLOB")
    public String getRemark() {
        return remark;
    }

    // 跟数据库的类型一致,主要为了解决精确到毫秒问题
    @Column(name = "time", nullable = false)
    public Date getTime() {
        return time;
    }

    /**
     * @return 为了使time属性在系统中是方便添加的方法
     */
    @Transient
    // 禁止hibernate
    public Date getTimeDate() {
        return time;
    }

    @Column(name = "type", nullable = false, length = 100)
    public String getType() {
        return type;
    }

    @Column(name = "type_info", columnDefinition = "CLOB")
    public String getTypeInfo() {
        return typeInfo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setTimeDate(Date time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
