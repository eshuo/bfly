package info.bfly.archer.config.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Config entity. 系统配置
 */
@Entity
@Table(name = "config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class Config implements Serializable {
    // Fields
    /**
     *
     */
    private static final long serialVersionUID = -4033234980139663926L;
    private String     id;
    private ConfigType configType;
    private String     name;
    private String     value;
    private String     description;

    // Constructors

    /**
     * default constructor
     */
    public Config() {
    }

    /**
     * minimal constructor
     */
    public Config(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public Config(String id, ConfigType configType, String name, String value, String description) {
        this.id = id;
        this.configType = configType;
        this.name = name;
        this.value = value;
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    public ConfigType getConfigType() {
        return configType;
    }

    @Column(name = "description", columnDefinition = "CLOB")
    public String getDescription() {
        return description;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 128)
    public String getId() {
        return id;
    }

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    @Lob
    @Column(name = "value", columnDefinition = "CLOB")
    public String getValue() {
        return value;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
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

    public void setValue(String value) {
        this.value = value;
    }
}
