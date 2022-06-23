package info.bfly.archer.user.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "attention_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AttentionUser {
    private String id;
    private User   user;
    private User   attentionUser;
    private Date   attentionTime;

    @Column(name = "attention_time")
    public Date getAttentionTime() {
        return attentionTime;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attention_user_id")
    public User getAttentionUser() {
        return attentionUser;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setAttentionTime(Date attentionTime) {
        this.attentionTime = attentionTime;
    }

    public void setAttentionUser(User attentionUser) {
        this.attentionUser = attentionUser;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
