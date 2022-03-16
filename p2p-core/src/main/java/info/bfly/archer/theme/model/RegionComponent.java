package info.bfly.archer.theme.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * RegionComponent entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "region_component")
public class RegionComponent implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 4065209086728252161L;
    private RegionComponentId id;
    private Region            region;
    private Component         component;
    private Integer           seqNum;

    // Constructors

    /**
     * default constructor
     */
    public RegionComponent() {
    }

    /**
     * minimal constructor
     */
    public RegionComponent(RegionComponentId id, Region region, Component component) {
        this.id = id;
        this.region = region;
        this.component = component;
    }

    /**
     * full constructor
     */
    public RegionComponent(RegionComponentId id, Region region, Component component, Integer seqNum) {
        this.id = id;
        this.region = region;
        this.component = component;
        this.seqNum = seqNum;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false, insertable = false, updatable = false)
    public Component getComponent() {
        return component;
    }

    // Property accessors
    @EmbeddedId
    @AttributeOverrides({@AttributeOverride(name = "componentId", column = @Column(name = "component_id", nullable = false, length = 32)),
            @AttributeOverride(name = "regionId", column = @Column(name = "region_id", nullable = false, length = 32))})
    public RegionComponentId getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false, insertable = false, updatable = false)
    public Region getRegion() {
        return region;
    }

    @Column(name = "seq_num")
    public Integer getSeqNum() {
        return seqNum;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public void setId(RegionComponentId id) {
        this.id = id;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }
}
