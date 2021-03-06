package info.bfly.archer.node.model;

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
 * NodeType entity. 节点类型
 */
@Entity
@Table(name = "node_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class NodeType implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 6736524407971990688L;
    private String id;
    private String name;
    private String description;
    private Set<Node> nodes = new HashSet<Node>(0);

    // Constructors

    /**
     * default constructor
     */
    public NodeType() {
    }

    public NodeType(String id) {
        this.id = id;
    }

    /**
     * minimal constructor
     */
    public NodeType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * full constructor
     */
    public NodeType(String id, String name, String description, Set<Node> nodes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.nodes = nodes;
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

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "nodeType")
    public Set<Node> getNodes() {
        return nodes;
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

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }
}
