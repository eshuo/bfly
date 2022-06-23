package info.bfly.archer.user.model;

import info.bfly.archer.menu.model.Menu;
import info.bfly.archer.urlmapping.model.UrlMapping;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Permission entity. 权限
 */
@Entity
@Table(name = "permission")
@NamedQueries({@NamedQuery(name = "Permission.findPermissionsByMenuId", query = "select distinct permission from Permission permission left join permission.menus menu where menu.id=:menuId"),
        @NamedQuery(name = "Permission.findPermissionsByUrlMappingId", query = "select distinct permission from Permission permission left join permission.urlMappings um where um.id=:urlMappingId")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Permission implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5346641272989857095L;
    // Fields
    private String id;
    private String name;
    private String description;
    private Set<Role>       roles       = new HashSet<Role>(0);
    private Set<Menu>       menus       = new HashSet<Menu>(0);
    private Set<UrlMapping> urlMappings = new HashSet<UrlMapping>(0);

    // Constructors

    /**
     * default constructor
     */
    public Permission() {
    }

    /**
     * minimal constructor
     */
    public Permission(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * full constructor
     */
    public Permission(String id, String name, String description, Set<Role> roles, Set<Menu> menus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.roles = roles;
        this.menus = menus;
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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    public Set<Menu> getMenus() {
        return menus;
    }

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    public Set<Role> getRoles() {
        return roles;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    public Set<UrlMapping> getUrlMappings() {
        return urlMappings;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUrlMappings(Set<UrlMapping> urlMappings) {
        this.urlMappings = urlMappings;
    }
}
