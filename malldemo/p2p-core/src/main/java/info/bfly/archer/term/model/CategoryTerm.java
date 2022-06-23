package info.bfly.archer.term.model;

import info.bfly.archer.node.model.Node;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CategoryTerm entity. 分类术语，node的分类
 *
 */
@Entity
@Table(name = "category_term")
@NamedQueries({@NamedQuery(name = "CategoryTerm.findByType", query = "select c from CategoryTerm c where c.categoryTermType.id = ?"),
        @NamedQuery(name = "CategoryTerm.findByParentId", query = "select c from CategoryTerm c where c.parent.id = ?"),
        @NamedQuery(name = "CategoryTerm.getTermCountByParentId", query = "select count(c) from CategoryTerm c where c.parent.id =:pId"),
        @NamedQuery(name = "CategoryTerm.findByParentIdOrderBySeqNum", query = "select c from CategoryTerm c where c.parent.id =:pId order by c.seqNum asc")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class CategoryTerm implements java.io.Serializable {
    private static final long serialVersionUID = -4573430309029721341L;
    // Fields
    private String           id;
    private CategoryTermType categoryTermType;
    private String           name;
    private String           thumb;
    private CategoryTerm     parent;
    private String           description;
    private Integer            seqNum   = 0;
    private List<CategoryTerm> children = new ArrayList<CategoryTerm>(0);
    private Set<Node>          nodes    = new HashSet<Node>(0);
    // Constructors
    private String name1;
    private String title;
    private String description1;

    /**
     * default constructor
     */
    public CategoryTerm() {
    }

    public CategoryTerm(String id) {
        super();
        this.id = id;
    }

    /**
     * minimal constructor
     */
    public CategoryTerm(String id, CategoryTermType categoryTermType) {
        this.id = id;
        this.categoryTermType = categoryTermType;
    }

    /**
     * full constructor
     */
    public CategoryTerm(String id, CategoryTermType categoryTermType, String name, String description, Integer seqNum, Set<Node> nodes) {
        this.id = id;
        this.categoryTermType = categoryTermType;
        this.name = name;
        this.description = description;
        this.seqNum = seqNum;
        this.nodes = nodes;
    }

    public CategoryTerm(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", nullable = false)
    public CategoryTermType getCategoryTermType() {
        return categoryTermType;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    public List<CategoryTerm> getChildren() {
        return children;
    }

    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    public String getDescription() {
        return description;
    }

    public String getDescription1() {
        return description1;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    public String getName1() {
        return name1;
    }

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "categoryTerms")
    public Set<Node> getNodes() {
        return nodes;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    public CategoryTerm getParent() {
        return parent;
    }

    @Column(name = "seq_num")
    public Integer getSeqNum() {
        return seqNum;
    }

    @Column(name = "thumb", length = 80)
    public String getThumb() {
        return thumb;
    }

    public String getTitle() {
//        System.out.println(id);
        // String type = getId();
        // 平台介绍
        if ("ptjs".equals(id)) {
            setTitle("平台介绍_新手指引");
            setName1("平台介绍");
            setDescription1("平台介绍可以让大家了解平台原理、平台操作流程等相关信息");
            // 平台原理
        }
        return title;
    }

    public void setCategoryTermType(CategoryTermType categoryTermType) {
        this.categoryTermType = categoryTermType;
    }

    public void setChildren(List<CategoryTerm> children) {
        this.children = children;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public void setParent(CategoryTerm parent) {
        this.parent = parent;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
