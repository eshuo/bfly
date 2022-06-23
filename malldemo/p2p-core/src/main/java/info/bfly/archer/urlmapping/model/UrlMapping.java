package info.bfly.archer.urlmapping.model;

import info.bfly.archer.user.model.Permission;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * UrlMapping entity. 访问路径别名
 *
 * #{pageName} 单页的路径 eg： <!-- 单页 --> <url-mapping id="page"> <pattern
 * value="/m/#{menuId}/p/#{pageName}" /> <view-id
 * value="themepath:#{pageName}.htm" /> </url-mapping>
 */
@Entity
@Table(name = "url_mapping")
@NamedQueries({ @NamedQuery(name = "UrlMapping.findByPattern", query = "Select urlMapping from UrlMapping urlMapping " + " where urlMapping.pattern=?") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UrlMapping implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4467873044037417071L;
    // Fields
    private String id;
    private String pattern;
    private String viewId;
    private String description;
    private List<Permission> permissions = new ArrayList<Permission>(0);

    // Constructors
    /** default constructor */
    public UrlMapping() {
    }

    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    public String getDescription() {
        return description;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "pattern", unique = true, nullable = false, length = 500)
    public String getPattern() {
        return pattern;
    }

    // Property accessors
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "url_mapping_permission", joinColumns = {@JoinColumn(name = "url_mapping_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "permission_id", nullable = false, updatable = false)})
    public List<Permission> getPermissions() {
        return permissions;
    }

    @Column(name = "view_id", nullable = false, length = 500)
    public String getViewId() {
        return viewId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
}
