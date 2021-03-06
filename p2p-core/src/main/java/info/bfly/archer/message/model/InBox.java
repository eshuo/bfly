package info.bfly.archer.message.model;

import info.bfly.archer.user.model.User;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 收信箱
 *
 * @author yinjl
 *
 */
@Entity
@Table(name = "inbox")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@NamedQueries({@NamedQuery(name = "InBox.finInBoxNoReadByLoginUser", query = "from InBox where status = 0 and recevier.id = ? order by receiveTime desc"),
        @NamedQuery(name = "InBox.finInBoxByLoginUser", query = "from InBox where recevier.id = ? order by receiveTime desc")})
public class InBox {
    private String id;
    /**
     * 发信人
     */
    private User   sender;
    /**
     * 收信人
     */
    private User   recevier;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 收信时间
     */
    private Date   receiveTime;
    /**
     * 信息状态
     */
    private String status;
    /**
     * 是否删除
     */
    private String isdelete;

    public InBox() {
    }

    public InBox(String id, User sender, User recevier, String title, String content, Date receiveTime, String status, String isdelete) {
        this.id = id;
        this.sender = sender;
        this.recevier = recevier;
        this.title = title;
        this.content = content;
        this.receiveTime = receiveTime;
        this.status = status;
        this.isdelete = isdelete;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "isdelete")
    public String getIsdelete() {
        return isdelete;
    }

    @Column(name = "receive_time")
    public Date getReceiveTime() {
        return receiveTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recevier")
    public User getRecevier() {
        return recevier;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender")
    public User getSender() {
        return sender;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public void setRecevier(User recevier) {
        this.recevier = recevier;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
