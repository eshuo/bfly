package info.bfly.archer.theme.model;

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

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * Theme entity. 主题，一个主题包含多个模板
 */
@Entity
@Table(name = "theme")
@NamedQueries(@NamedQuery(name = "Theme.findAllOrderByStatus", query = "from Theme order by status desc"))
public class Theme implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -2110977218544783001L;
    private String id;
    private String name;
    private String screenshotUri;
    /**
     * ENABLE = 1 , DISABLE = 0 , DEFAULT = 2
     */
    private String status;
    private String description;
    private Set<Template> templates = new HashSet<Template>(0);

    // Constructors

    /**
     * default constructor
     */
    public Theme() {
    }

    /**
     * minimal constructor
     */
    public Theme(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * full constructor
     */
    public Theme(String id, String name, String screenshotUri, String description, Set<Template> templates) {
        this.id = id;
        this.name = name;
        this.screenshotUri = screenshotUri;
        this.description = description;
        this.templates = templates;
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

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    @Column(name = "screenshot_uri", length = 100)
    public String getScreenshotUri() {
        return screenshotUri;
    }

    @Column(name = "status", nullable = true, length = 1)
    public String getStatus() {
        return status;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "theme")
    public Set<Template> getTemplates() {
        return templates;
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

    public void setScreenshotUri(String screenshotUri) {
        this.screenshotUri = screenshotUri;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTemplates(Set<Template> templates) {
        this.templates = templates;
    }
}
