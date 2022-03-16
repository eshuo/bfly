package info.bfly.archer.user.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Role entity. 角色
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Role implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8996771579280751507L;
    // Fields
    private String id;
    private String name;
    private String description;
    private List<Permission> permissions = new ArrayList<Permission>(0);
    private List<User>       users       = new ArrayList<User>(0);

    // Constructors

    /**
     * default constructor
     */
    public Role() {
    }

    public Role(String id) {
        this.id = id;
    }

    /**
     * minimal constructor
     */
    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * full constructor
     */
    public Role(String id, String name, String description, List<Permission> permissions, List<User> users) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.users = users;
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

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission", joinColumns = {@JoinColumn(name = "role_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "permission_id", nullable = false, updatable = false)})
    public List<Permission> getPermissions() {
        return permissions;
    }

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "roles")
    public List<User> getUsers() {
        return users;
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

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
