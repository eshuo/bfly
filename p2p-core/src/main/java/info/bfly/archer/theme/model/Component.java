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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * Component entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "component")
@NamedQueries({
        // @NamedQuery(name="Component.findComponentByRegionId",
        // query="Select c from Component c, RegionComponent rc" +
        // " where c in elements(rc.component) and rc.region.id=?" +
        // " order by rc.seqNum")
        @NamedQuery(name = "Component.findByUrl", query = "Select distinct(c) from Component c left join fetch c.componentParameters where c.scriptUrl = ?"),
        @NamedQuery(name = "Component.findAll", query = "Select c from Component c")})
public class Component implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -3024639742382384601L;
    private String id;
    private String name;
    private String scriptUrl;
    private String enable;
    private String description;
    private List<ComponentParameter> componentParameters = new ArrayList<ComponentParameter>(0);
    private Set<RegionComponent>     regionComponents    = new HashSet<RegionComponent>(0);

    // Constructors

    /**
     * default constructor
     */
    public Component() {
    }

    /**
     * minimal constructor
     */
    public Component(String name, String enable) {
        this.name = name;
        this.enable = enable;
    }

    /**
     * full constructor
     */
    public Component(String name, String scriptUrl, String enable, String description, List<ComponentParameter> componentParameters, Set<RegionComponent> regionComponents) {
        this.name = name;
        this.scriptUrl = scriptUrl;
        this.enable = enable;
        this.description = description;
        this.componentParameters = componentParameters;
        this.regionComponents = regionComponents;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "component")
    public List<ComponentParameter> getComponentParameters() {
        return componentParameters;
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

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "component")
    public Set<RegionComponent> getRegionComponents() {
        return regionComponents;
    }

    @Column(name = "script_url", length = 500)
    public String getScriptUrl() {
        return scriptUrl;
    }

    public void setComponentParameters(List<ComponentParameter> componentParameters) {
        this.componentParameters = componentParameters;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setRegionComponents(Set<RegionComponent> regionComponents) {
        this.regionComponents = regionComponents;
    }

    public void setScriptUrl(String scriptUrl) {
        this.scriptUrl = scriptUrl;
    }
}
