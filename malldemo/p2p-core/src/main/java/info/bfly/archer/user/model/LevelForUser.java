package info.bfly.archer.user.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 等级，给用户使用
 */
@Entity
@Table(name = "level_for_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class LevelForUser implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 1345120281219268516L;
    private String  id;
    private String  name;
    /**
     * 序号（越大，级别越高）
     */
    private int     seqNum;
    /**
     * 积分下线（达到此下线，才能获得该等级） 0或者null，视为此项无限制
     */
    private Integer minPointLimit;
    /**
     * 每个等级对应多少积分值
     */
    private int     pointValue;
    /**
     * 有效期（秒）
     */
    private int     validityPeriod;
    private String  description;
    private List<User> users = new ArrayList<User>(0);

    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    public String getDescription() {
        return description;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "min_point_limit")
    public Integer getMinPointLimit() {
        return minPointLimit;
    }

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    @Column(name = "point_value")
    public int getPointValue() {
        return pointValue;
    }

    @Column(name = "seq_num")
    public int getSeqNum() {
        return seqNum;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "level")
    public List<User> getUsers() {
        return users;
    }

    @Column(name = "validity_period")
    public int getValidityPeriod() {
        return validityPeriod;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMinPointLimit(Integer minPointLimit) {
        this.minPointLimit = minPointLimit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }
}
