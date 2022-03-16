package info.bfly.archer.menu.model;

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
 * MenuType entity. 菜单类型
 */
@Entity
@Table(name = "menu_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MenuType implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -984173838951799657L;
    private String id;
    private String name;
    private String enable;
    private String description;
    private Set<Menu> menus = new HashSet<Menu>(0);

    // Constructors

    /**
     * default constructor
     */
    public MenuType() {
    }

    /**
     * minimal constructor
     */
    public MenuType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * full constructor
     */
    public MenuType(String id, String name, String description, Set<Menu> menus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.menus = menus;
    }

    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    public String getDescription() {
        return description;
    }

    @Column(name = "enable", nullable = false, length = 1)
    public String getEnable() {
        return enable;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "menuType")
    public Set<Menu> getMenus() {
        return menus;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnable(String enable) {
        this.enable = enable;
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
}
