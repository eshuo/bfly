package info.bfly.archer.system.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 */
@Entity
@Table(name = "motion_tracking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class MotionTracking {
    private String id;
    // 产生动作者
    private String who;
    // 产生动作者类型
    private String whoType;
    // 来自
    private String fromWhere;
    // 来的类型
    private String fromType;
    // 来的时间
    private Date   fromTime;
    // 动作
    private String action;
    // 动作类型
    private String actionType;
    // 动作时间
    private Date   actionTime;
    // 目的
    private String toWhere;
    // 目的类型
    private String toType;
    // 目的时间
    private Date   toTime;

    @Column(name = "action", length = 512)
    public String getAction() {
        return action;
    }

    @Column(name = "action_time")
    public Date getActionTime() {
        return actionTime;
    }

    @Column(name = "action_type", length = 512)
    public String getActionType() {
        return actionType;
    }

    @Column(name = "from_time")
    public Date getFromTime() {
        return fromTime;
    }

    @Column(name = "from_type", length = 512)
    public String getFromType() {
        return fromType;
    }

    @Column(name = "from_where", length = 512)
    public String getFromWhere() {
        return fromWhere;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 64)
    public String getId() {
        return id;
    }

    @Column(name = "to_time")
    public Date getToTime() {
        return toTime;
    }

    @Column(name = "to_type", length = 512)
    public String getToType() {
        return toType;
    }

    @Column(name = "to_where", length = 512)
    public String getToWhere() {
        return toWhere;
    }

    @Column(name = "who", length = 512)
    public String getWho() {
        return who;
    }

    @Column(name = "who_type", length = 512)
    public String getWhoType() {
        return whoType;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public void setToWhere(String toWhere) {
        this.toWhere = toWhere;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setWhoType(String whoType) {
        this.whoType = whoType;
    }
}
