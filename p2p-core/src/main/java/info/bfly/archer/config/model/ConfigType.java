package info.bfly.archer.config.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
 * ConfigType entity. 系统配置的分类
 */
@Entity
@Table(name = "config_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ConfigType implements Serializable {
    // Fields
    private static final long serialVersionUID = 8396235650822686654L;
    private String id;
    private String name;
    private String description;
    private List<Config> configs = new ArrayList<Config>(0);

    // Constructors

    /**
     * default constructor
     */
    public ConfigType() {
    }

    /**
     * minimal constructor
     */
    public ConfigType(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public ConfigType(String id, String name, String description, List<Config> configs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.configs = configs;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configType")
    public List<Config> getConfigs() {
        return configs;
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

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
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
}
