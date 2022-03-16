package info.bfly.archer.theme.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RegionComponentId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class RegionComponentId implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 3096754879486737709L;
    private String componentId;
    private String regionId;

    // Constructors

    /**
     * default constructor
     */
    public RegionComponentId() {
    }

    /**
     * full constructor
     */
    public RegionComponentId(String componentId, String regionId) {
        this.componentId = componentId;
        this.regionId = regionId;
    }

    // Property accessors
    @Override
    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof RegionComponentId)) return false;
        RegionComponentId castOther = (RegionComponentId) other;
        return ((getComponentId() == castOther.getComponentId()) || (getComponentId() != null && castOther.getComponentId() != null && getComponentId().equals(castOther.getComponentId())))
                && ((getRegionId() == castOther.getRegionId()) || (getRegionId() != null && castOther.getRegionId() != null && getRegionId().equals(castOther.getRegionId())));
    }

    @Column(name = "component_id", nullable = false, length = 32)
    public String getComponentId() {
        return componentId;
    }

    @Column(name = "region_id", nullable = false, length = 32)
    public String getRegionId() {
        return regionId;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (getComponentId() == null ? 0 : getComponentId().hashCode());
        result = 37 * result + (getRegionId() == null ? 0 : getRegionId().hashCode());
        return result;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
