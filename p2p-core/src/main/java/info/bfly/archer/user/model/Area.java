package info.bfly.archer.user.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import info.bfly.p2p.loan.model.Loan;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;


/**
 * Area entity. 省市县关联表，树形结构，用来选择或者展示区域用
 */
@Entity
@Table(name = "area")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Area implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 2215752093444443521L;
    private String  id;
    private String  name;
    private Area    parent;
    private String  comments;
    private Integer seqNum;
    private Set<User> users = new HashSet<User>(0);
    //众筹配送范围
    private Set<Loan> malls = new HashSet<Loan>(0);

    // Constructors

    /**
     * default constructor
     */
    public Area() {
    }

    /**
     * minimal constructor
     */
    public Area(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * full constructor
     */
    public Area(String id, String name, Area parent, String comments, Integer seqNum, Set<User> users) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.comments = comments;
        this.seqNum = seqNum;
        this.users = users;
    }

    @Lob
    @Column(name = "comments", columnDefinition = "CLOB")
    public String getComments() {
        return comments;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pid")
    public Area getParent() {
        return parent;
    }

    @Column(name = "seq_num")
    public Integer getSeqNum() {
        return seqNum;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "area")
    public Set<User> getUsers() {
        return users;
    }
    
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY,mappedBy="areas")
    public Set<Loan> getMalls() {
		return malls;
	}
    
    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Area parent) {
        this.parent = parent;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

	

	public void setMalls(Set<Loan> malls) {
		this.malls = malls;
	}
}
