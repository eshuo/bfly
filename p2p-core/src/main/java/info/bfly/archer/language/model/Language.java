package info.bfly.archer.language.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * Language entity. 语言
 */
@Entity
@Table(name = "language")
@NamedQueries({@NamedQuery(name = "Language.findEnableLanguage", query = "from Language where enable = 1"), @NamedQuery(name = "Language.findAllLanguage", query = "from Language")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Language implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 8836055461833235277L;
    private String id;
    private String name;
    private String enable;
    /**
     * 访问某些URL，则使用该语言
     */
    private String urlRole;
    private String country;

    // Constructors

    /**
     * default constructor
     */
    public Language() {
    }

    /**
     * minimal constructor
     */
    public Language(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public Language(String id, String name, String enable, String urlRole, String country) {
        this.id = id;
        this.name = name;
        this.enable = enable;
        this.urlRole = urlRole;
        this.country = country;
    }

    @Column(name = "country", length = 50)
    public String getCountry() {
        return country;
    }

    @Column(name = "enable", length = 1)
    public String getEnable() {
        return enable;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    @Column(name = "url_role", length = 100)
    public String getUrlRole() {
        return urlRole;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrlRole(String urlRole) {
        this.urlRole = urlRole;
    }
}
