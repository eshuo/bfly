package info.bfly.archer.comment.model;

import info.bfly.archer.comment.CommentConstants;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.node.service.WordFilterService;
import info.bfly.archer.user.model.User;
import info.bfly.core.util.SpringBeanUtil;
import info.bfly.p2p.loan.model.Loan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Comment entity. 评论表
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Comment implements java.io.Serializable, Comparable {
    // Fields
    private static final long serialVersionUID = 5465096717625398690L;
    private String  id;
    private Node    node;
    private Loan    loan;
    /**
     * 父评论
     */
    private Comment parentComment;
    /**
     * 发表评论的用户
     */
    private User    userByCreator;
    private String  title;
    private String  body;
    private String  ip;
    private Date    createTime;
    private Date    updateTime;
    /**
     * 用来记录该评论的状态 {@link CommentConstants}
     */
    private String  status;
    /**
     * 用字符串来记录该评论在树形结构中的位置，方便查询； 比如：一级评论为00.00，该评论的回复为00.00.00，00.00.01，以此类推
     */
    private String  thread;
    private String  name;
    private String  email;
    private String  homepage;
    private Integer seqNum;
    /**
     * 一级子评论
     */
    private List<Comment> childrenComments = new ArrayList<Comment>(0);
    // Constructors
    WordFilterService wfs;

    /**
     * default constructor
     */
    public Comment() {
    }
 
    public Comment(String id) {
       this.id = id;
    }

    @Override
    public int compareTo(Object o) {
        Comment comm = (Comment) o;
        // 将thread按照“.”拆开
        String[] threads1 = thread.split("[.]");
        String[] threads2 = comm.getThread().split("[.]");
        int length = threads1.length;
        if (length > threads2.length) {
            length = threads2.length;
        }
        // 比较数组中同一位上面的大小
        for (int i = 0; i < length; i++) {
            int t1 = Integer.parseInt(threads1[i]);
            int t2 = Integer.parseInt(threads2[i]);
            if (t1 > t2) {
                return 1;
            } else if (t1 < t2) {
                return -1;
            } else {
                continue;
            }
        }
        // 如果一个数组已经到了尽头，且比较到目前，两者相同，那么开始比较长度，长大于短
        if (threads1.length > threads2.length) {
            return 1;
        } else if (threads1.length == threads2.length) {
            return 0;
        } else {
            return -1;
        }
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "body", columnDefinition = "longtext")
    public String getBody() {
        return body;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentComment")
    public List<Comment> getChildrenComments() {
        return childrenComments;
    }

    @Column(name = "create_time", nullable = false, length = 19)
    public Date getCreateTime() {
        return createTime;
    }

    @Column(name = "email", length = 100)
    public String getEmail() {
        return email;
    }

    /**
     * 敏感词过滤后的body
     *
     * @return string body
     */
    @Transient
    public String getFilteredBody() {
        WordFilterService wfs = getWordFilterService();
        if (wfs != null && getBody() != null) {
            return wfs.wordFilter(getBody());
        }
        return null;
    }

    @Column(name = "homepage", length = 200)
    public String getHomepage() {
        return homepage;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "ip", length = 50)
    public String getIp() {
        return ip;
    }

    /**
     * 获取当前评论的级别
     *
     * @return
     */
    @Transient
    public int getLevel() {
        return thread.split("[.]").length - 1;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    public Loan getLoan() {
        return loan;
    }

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    public Node getNode() {
        return node;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    public Comment getParentComment() {
        return parentComment;
    }

    @Column(name = "seq_num")
    public Integer getSeqNum() {
        return seqNum;
    }

    @Column(name = "status", nullable = false, length = 20)
    public String getStatus() {
        return status;
    }

    @Column(name = "thread", length = 500)
    public String getThread() {
        return thread;
    }

    @Column(name = "title", length = 100)
    public String getTitle() {
        return title;
    }

    @Column(name = "update_time", nullable = false, length = 19)
    public Date getUpdateTime() {
        return updateTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUserByCreator() {
        return userByCreator;
    }

    @Transient
    private WordFilterService getWordFilterService() {
        if (wfs == null) {
            wfs = (WordFilterService) SpringBeanUtil.getBeanByName("wordFilterService");
        }
        return wfs;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setChildrenComments(List<Comment> comments) {
        childrenComments = comments;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Transient
    public void setFilteredBody(String body) {
        this.body = body;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setParentComment(Comment comment) {
        parentComment = comment;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setUserByCreator(User userByCreator) {
        this.userByCreator = userByCreator;
    }
}
