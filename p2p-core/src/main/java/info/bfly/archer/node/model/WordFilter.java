package info.bfly.archer.node.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * WordFilter entity. 敏感词过滤
 */
@Entity
@Table(name = "word_filter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@NamedQueries(@NamedQuery(name = "WordFilter.findAllWordFilters", query = "from WordFilter"))
public class WordFilter implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -4292374272441149597L;
    private String id;
    private String word;
    private String description;

    // Constructors

    /**
     * default constructor
     */
    public WordFilter() {
    }

    /**
     * minimal constructor
     */
    public WordFilter(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public WordFilter(String id, String word, String description) {
        this.id = id;
        this.word = word;
        this.description = description;
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

    @Column(name = "word", length = 50)
    public String getWord() {
        return word;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
