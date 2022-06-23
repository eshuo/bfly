package info.bfly.archer.theme.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * Region entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "region")
@NamedQueries({@NamedQuery(name = "Region.findRegionByTemplateId", query = "Select r from Region r, Template t " + " where r in elements(t.regions) and t.id=?" + " order by r.title")})
public class Region implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 5045606053397197474L;
    private String id;
    private String title;
    private String description;
    private Set<Template>         templates        = new HashSet<Template>(0);
    private List<RegionComponent> regionComponents = new ArrayList<RegionComponent>(0);

    // Constructors

    /**
     * default constructor
     */
    public Region() {
    }

    /**
     * minimal constructor
     */
    public Region(String title) {
        this.title = title;
    }

    /**
     * full constructor
     */
    public Region(String title, String description, Set<Template> templates, List<RegionComponent> regionComponents) {
        this.title = title;
        this.description = description;
        this.templates = templates;
        this.regionComponents = regionComponents;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "region")
    public List<RegionComponent> getRegionComponents() {
        return regionComponents;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "regions")
    public Set<Template> getTemplates() {
        return templates;
    }

    @Column(name = "title", nullable = false, length = 50)
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRegionComponents(List<RegionComponent> regionComponents) {
        this.regionComponents = regionComponents;
    }

    public void setTemplates(Set<Template> templates) {
        this.templates = templates;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
