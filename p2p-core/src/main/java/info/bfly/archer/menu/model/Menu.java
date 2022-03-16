package info.bfly.archer.menu.model;

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
 * Menu entity. 菜单
 */
@Entity
@Table(name = "menu")
@NamedQueries({
        @NamedQuery(name = "Menu.findMenusByType", query = "Select distinct m from Menu m left join fetch m.children "
                + "where m.enable = 1 and m.parent.id is null and m.menuType.id=? and m.menuType.enable = 1 order by m.seqNum"),
        @NamedQuery(name = "Menu.findMenusByTypeId", query = "Select m from Menu m where m.menuType.id like ?"),
        @NamedQuery(name = "Menu.getMenuByUrl", query = "select menu from Menu menu where menu.url=:url"),
        @NamedQuery(name = "Menu.getNavMenuByUrl", query = "select menu from Menu menu where menu.url=? and menu.menuType.id=?"),
        @NamedQuery(name = "Menu.findMenusByUrl", query = "Select menu from Menu menu where menu.url like ? and menu.parent is not null"),
        @NamedQuery(name = "Menu.findMenusByParentId", query = "Select distinct m from Menu m left join fetch m.children where m.parent.id = ?  order by m.seqNum") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class Menu implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1456592219787638678L;
    // Fields
    private String   id;
    private MenuType menuType;
    private Menu     parent;
    /**
     * 此菜单可用的权限
     */
    private List<Permission> permissions = new ArrayList<Permission>(0);
    private String  label;
    private String  url;
    private String  enable;
    private String  icon;
    /**
     * 菜单是否以展开的方式使用，如果不展开查询菜单则不查询该节点下的子菜单（可选值：1/0）
     */
    private String  expanded;
    private String  description;
    private Integer seqNum;
    private String  language;
    private String  target;
    private String  rel;
    private List<Menu> children = new ArrayList<Menu>(0);

    // Constructors
    /** default constructor */
    public Menu() {
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OrderBy(value = "seqNum")
    public List<Menu> getChildren() {
        return children;
    }

    @Column(name = "description", length = 1000)
    public String getDescription() {
        return description;
    }

    @Column(name = "enable", nullable = false, length = 1)
    public String getEnable() {
        return enable;
    }

    @Column(name = "expanded", nullable = false, length = 1)
    public String getExpanded() {
        return expanded;
    }

    @Column(name = "icon", length = 80)
    public String getIcon() {
        return icon;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "label", nullable = false, length = 100)
    public String getLabel() {
        return label;
    }

    @Column(name = "language", length = 10, nullable = false)
    public String getLanguage() {
        return language;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", nullable = false)
    public MenuType getMenuType() {
        return menuType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    public Menu getParent() {
        return parent;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "menu_permission", joinColumns = { @JoinColumn(name = "menu_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "permission_id", nullable = false, updatable = false) })
    public List<Permission> getPermissions() {
        return permissions;
    }

    @Column(name = "rel", length = 20)
    public String getRel() {
        return rel;
    }

    @Column(name = "seq_num")
    public Integer getSeqNum() {
        return seqNum;
    }

    @Column(name = "target", length = 20)
    public String getTarget() {
        return target;
    }

    @Column(name = "url", length = 1000)
    public String getUrl() {
        return url;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public void setExpanded(String expanded) {
        this.expanded = expanded;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
