package info.bfly.archer.node.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * NodeBodyHistory entity. 节点主体的历史版本。
 */
@Entity
@Table(name = "node_body_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class NodeBodyHistory implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 3990040517060351405L;
    private String id;
    private Node   node;
    private Date   createTime;
    private Double version;
    private String body;

    // Constructors

    /**
     * default constructor
     */
    public NodeBodyHistory() {
    }

    /**
     * minimal constructor
     */
    public NodeBodyHistory(String id, Node node, Date createTime, Double version) {
        this.id = id;
        this.node = node;
        this.createTime = createTime;
        this.version = version;
    }

    /**
     * full constructor
     */
    public NodeBodyHistory(String id, Node node, Date createTime, Double version, String body) {
        this.id = id;
        this.node = node;
        this.createTime = createTime;
        this.version = version;
        this.body = body;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "body", columnDefinition = "longtext")
    public String getBody() {
        return body;
    }

    @Column(name = "create_time", nullable = false, length = 8)
    public Date getCreateTime() {
        return createTime;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    public Node getNode() {
        return node;
    }

    @Column(name = "version", nullable = false, precision = 22, scale = 0)
    public Double getVersion() {
        return version;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setVersion(Double version) {
        this.version = version;
    }
}
