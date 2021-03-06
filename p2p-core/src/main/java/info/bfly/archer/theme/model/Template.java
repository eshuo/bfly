package info.bfly.archer.theme.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Template entity. 模板。一个主题包含多个模板，一个模板包含多个部位。
 */
@Entity
@Table(name = "template")
public class Template implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -3035144944281491576L;
    private String id;
    private Theme  theme;
    private String name;
    private String description;
    private List<Region> regions = new ArrayList<Region>(0);

    // Constructors

    /**
     * default constructor
     */
    public Template() {
    }

    /**
     * minimal constructor
     */
    public Template(String id, Theme theme, String name) {
        this.id = id;
        this.theme = theme;
        this.name = name;
    }

    /**
     * full constructor
     */
    public Template(String id, Theme theme, String name, String description, List<Region> regions) {
        this.id = id;
        this.theme = theme;
        this.name = name;
        this.description = description;
        this.regions = regions;
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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "template_region", joinColumns = {@JoinColumn(name = "template_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "region_id", nullable = false, updatable = false)})
    public List<Region> getRegions() {
        return regions;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    public Theme getTheme() {
        return theme;
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

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
