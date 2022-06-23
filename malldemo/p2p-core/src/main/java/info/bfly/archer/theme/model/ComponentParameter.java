package info.bfly.archer.theme.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * ComponentParameter entity. 元件参数
 */
@Entity
@Table(name = "component_parameter")
@NamedQueries({@NamedQuery(name = "ComponentParameter.findByCompanent", query = "Select c from ComponentParameter c where c.component.id = ?")})
public class ComponentParameter implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 6630608320299314412L;
    private String    id;
    private Component component;
    private String    name;
    private String    value;
    private String    description;

    // Constructors

    /**
     * default constructor
     */
    public ComponentParameter() {
    }

    /**
     * minimal constructor
     */
    public ComponentParameter(String id, Component component) {
        this.id = id;
        this.component = component;
    }

    /**
     * full constructor
     */
    public ComponentParameter(String id, Component component, String name, String value, String description) {
        this.id = id;
        this.component = component;
        this.name = name;
        this.value = value;
        this.description = description;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    public Component getComponent() {
        return component;
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

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    @Lob
    @Column(name = "value", columnDefinition = "CLOB")
    public String getValue() {
        return value;
    }

    public void setComponent(Component component) {
        this.component = component;
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
