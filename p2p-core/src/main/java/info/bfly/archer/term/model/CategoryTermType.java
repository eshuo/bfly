package info.bfly.archer.term.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * CategoryTermType entity. 分类术语类型，node的分类的大类
 */
@Entity
@Table(name = "category_term_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class CategoryTermType implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -125929119914680825L;
    private String id;
    private String name;
    private String description;
    private Set<CategoryTerm> categoryTerms = new HashSet<CategoryTerm>(0);

    // Constructors

    /**
     * default constructor
     */
    public CategoryTermType() {
    }

    public CategoryTermType(String id) {
        this.id = id;
    }

    /**
     * minimal constructor
     */
    public CategoryTermType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * full constructor
     */
    public CategoryTermType(String id, String name, String description, Set<CategoryTerm> categoryTerms) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryTerms = categoryTerms;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "categoryTermType")
    public Set<CategoryTerm> getCategoryTerms() {
        return categoryTerms;
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

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setCategoryTerms(Set<CategoryTerm> categoryTerms) {
        this.categoryTerms = categoryTerms;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
