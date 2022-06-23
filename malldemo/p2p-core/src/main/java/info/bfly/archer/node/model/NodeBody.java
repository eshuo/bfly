package info.bfly.archer.node.model;

import info.bfly.archer.node.service.WordFilterService;
import info.bfly.core.util.SpringBeanUtil;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * NodeBody entity. 节点主体
 */
@Entity
@Table(name = "node_body")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class NodeBody implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 7160334208280720523L;
    private String id;
    private String body;
    private Set<Node> nodes = new HashSet<Node>(0);
    // Constructors
    WordFilterService wfs;

    /**
     * default constructor
     */
    public NodeBody() {
    }

    /**
     * minimal constructor
     */
    public NodeBody(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public NodeBody(String id, String body, Set<Node> nodes) {
        this.id = id;
        this.body = body;
        this.nodes = nodes;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "body", columnDefinition = "longtext")
    public String getBody() {
        return body;
    }

    /**
     * 敏感词过滤后的body
     *
     * @return string body
     */
    @Transient
    public String getFilteredBody() {
        WordFilterService wfs = getWordFilterService();
        if (wfs != null && getBody() != null) {
            // long time = System.currentTimeMillis();
            body = wfs.wordFilter(getBody());
            // System.out.println(System.currentTimeMillis() - time);
            return body;
        }
        return null;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    // @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
    // "nodeBody")
    @OneToMany(fetch = FetchType.LAZY)
    public Set<Node> getNodes() {
        return nodes;
    }

    @Transient
    private WordFilterService getWordFilterService() {
        if (wfs == null) {
            wfs = (WordFilterService) SpringBeanUtil.getBeanByName("wordFilterService");
        }
        return wfs;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }
}
