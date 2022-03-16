package info.bfly.archer.node.model;

import info.bfly.archer.comment.model.Comment;
import info.bfly.archer.term.model.CategoryTerm;
import info.bfly.archer.user.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * Node entity. 节点表，该节点可以是文章，也可以使论坛主题，也可以是商品展示，等等。
 */
@Entity
@Table(name = "node")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
        @NamedQuery(name = "Node.findNodeByTermOrderByCreateTime", query = "Select node from Node node, CategoryTerm term "
                + " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId" + " order by node.createTime desc"),
        @NamedQuery(name = "Node.findNodeByTermOrderBySeqNumAndCreateTime", query = "Select node from Node node, CategoryTerm term "
                + " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId" + " order by node.seqNum desc, node.createTime desc"),
        @NamedQuery(name = "Node.findNodeByTermOrderByUpdateTime", query = "Select node from Node node, CategoryTerm term "
                + " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId" + " order by node.updateTime desc"),
        @NamedQuery(name = "Node.findNodeByTermOrderBySeqNumAndUpdateTime", query = "Select node from Node node, CategoryTerm term "
                + " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId" + " order by node.seqNum desc, node.updateTime desc"),
        @NamedQuery(name = "Node.getNodeCountByTermId", query = "Select count(node) from Node node, CategoryTerm term "
                + " where node.status = 1 and node.nodeType.id =:nodeTypeId and node in elements(term.nodes) and term.id=:termId"),
        @NamedQuery(name = "Node.getNodeCount", query = "Select count(node) from Node node where node.status = 1 and node.nodeType.id =:nodeTypeId"),
        @NamedQuery(name = "Node.findNodeOrderByUpdateTime", query = "from Node node where node.nodeType.id =:nodeTypeId order by updateTime desc"),
        @NamedQuery(name = "Node.findNodeOrderBySeqNumAndUpdateTime", query = "from Node node where node.nodeType.id =:nodeTypeId order by seqNum, updateTime desc"),
        @NamedQuery(name = "Node.findAllNodeAndNodeBody", query = "Select distinct n from Node n left join fetch n.nodeBody"),
        @NamedQuery(name = "Node.pingInfo", query = "Select distinct n from Node n , CategoryTerm term where  date(create_time) = curdate()")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Node implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6252764463690992922L;
    // Fields
    private String   id;
    private User     userByLastModifyUser;
    private NodeType nodeType;
    private NodeBody nodeBody;
    private User     userByCreator;
    private String   title;
    private String   subtitle;
    private String   language;
    /**
     * 缩略图
     */
    private String   thumb;
    /**
     * 1 = 已发表；0 = 未发表
     */
    private String   status;
    private String   keywords;
    private String   description;
    private Date     createTime;
    private Date     updateTime;
    private Double   version;
    private Integer  seqNum;
    private List<NodeBodyHistory> nodeBodyHistories = new ArrayList<NodeBodyHistory>(0);
    private List<NodeAttr>        nodeAttrs         = new ArrayList<NodeAttr>(0);
    private List<Comment>         comments          = new ArrayList<Comment>(0);
    private List<CategoryTerm>    categoryTerms     = new ArrayList<CategoryTerm>(0);

    // Constructors

    /**
     * default constructor
     */
    public Node() {
    }

    public Node(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public Node(String id, User userByLastModifyUser, NodeType nodeType, NodeBody nodeBody, User userByCreator, String title, String language, String status, String keywords, String description,
                Date createTime, Date updateTime, Double version, Integer seqNum, List<NodeBodyHistory> nodeBodyHistories, List<NodeAttr> nodeAttrs, List<Comment> comments,
                List<CategoryTerm> categoryTerms) {
        this.id = id;
        this.userByLastModifyUser = userByLastModifyUser;
        this.nodeType = nodeType;
        this.nodeBody = nodeBody;
        this.userByCreator = userByCreator;
        this.title = title;
        this.language = language;
        this.status = status;
        this.keywords = keywords;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.version = version;
        this.seqNum = seqNum;
        this.nodeBodyHistories = nodeBodyHistories;
        this.nodeAttrs = nodeAttrs;
        this.comments = comments;
        this.categoryTerms = categoryTerms;
    }

    /**
     * minimal constructor
     */
    public Node(String id, User userByLastModifyUser, NodeType nodeType, User userByCreator, String status) {
        this.id = id;
        this.userByLastModifyUser = userByLastModifyUser;
        this.nodeType = nodeType;
        this.userByCreator = userByCreator;
        this.status = status;
    }

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "category_term_node", joinColumns = {@JoinColumn(name = "node_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "term_id", nullable = false, updatable = false)})
    public List<CategoryTerm> getCategoryTerms() {
        return categoryTerms;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "node")
    public List<Comment> getComments() {
        return comments;
    }

    @Column(name = "create_time", length = 19, nullable = false, updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

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

    @Lob
    @Column(name = "keywords", columnDefinition = "CLOB")
    public String getKeywords() {
        return keywords;
    }

    @Column(name = "language", length = 10, nullable = false)
    public String getLanguage() {
        return language;
    }

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "node_node_attr", joinColumns = {@JoinColumn(name = "node_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "node_attr_id", nullable = false, updatable = false)})
    public List<NodeAttr> getNodeAttrs() {
        return nodeAttrs;
    }

    // @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "body")
    public NodeBody getNodeBody() {
        return nodeBody;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "node")
    public List<NodeBodyHistory> getNodeBodyHistories() {
        return nodeBodyHistories;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_type", nullable = false)
    public NodeType getNodeType() {
        return nodeType;
    }

    @Column(name = "seq_num")
    public Integer getSeqNum() {
        return seqNum;
    }

    /**
     * <li>1 = 已经发表 （default）</li> <li>0 = 未发表</li> <li>2 = 已经删除</li>
     *
     * @return
     */
    @Column(name = "status", nullable = false, length = 20)
    public String getStatus() {
        return status;
    }

    @Column(name = "subtitle", length = 100)
    public String getSubtitle() {
        return subtitle;
    }

    @Column(name = "thumb", length = 80)
    public String getThumb() {
        return thumb;
    }

    @Column(name = "title", length = 100)
    public String getTitle() {
        return title;
    }

    @Column(name = "update_time", length = 19, nullable = false)
    public Date getUpdateTime() {
        return updateTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator", nullable = false)
    public User getUserByCreator() {
        return userByCreator;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modify_user")
    public User getUserByLastModifyUser() {
        return userByLastModifyUser;
    }

    @Column(name = "version", precision = 22, scale = 0)
    public Double getVersion() {
        return version;
    }

    public void setCategoryTerms(List<CategoryTerm> categoryTerms) {
        this.categoryTerms = categoryTerms;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setNodeAttrs(List<NodeAttr> nodeAttrs) {
        this.nodeAttrs = nodeAttrs;
    }

    public void setNodeBody(NodeBody nodeBody) {
        this.nodeBody = nodeBody;
    }

    public void setNodeBodyHistories(List<NodeBodyHistory> nodeBodyHistories) {
        this.nodeBodyHistories = nodeBodyHistories;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public void setUserByLastModifyUser(User userByLastModifyUser) {
        this.userByLastModifyUser = userByLastModifyUser;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Node [id=" + id + ", title=" + title + "]";
    }
}
