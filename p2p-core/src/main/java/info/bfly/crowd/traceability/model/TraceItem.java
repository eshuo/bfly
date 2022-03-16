package info.bfly.crowd.traceability.model;

import info.bfly.archer.config.model.Config;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created by XXSun on 3/21/2017.
 * 可追溯系统项目表
 */
@Entity
@Table(name = "trace_item")@NamedQueries({
    @NamedQuery(name = "TraceItem.findColumnByReferrerId", query = "Select distinct m from TraceColumn m left join fetch m.children where m.referrer.id = ?  order by m.order"),
    @NamedQuery(name="TraceItem.findItemByTemplateId",query="Select distinct m from TraceItem m where m.referrer.id = ? order by m.order")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class TraceItem implements Serializable {
    private static final long serialVersionUID = 1769222641895260485L;
    private String      id;
    /**
     * 条目类型
     */
    private Config      itemType;
    /**
     * 条目值
     */
    private String      itemValue;
    /**
     * 条目名称
     */
    private String      itemName;
    /**
     * 条目排序
     */
    private Integer     order;
    /**
     * 条目状态
     */
    private Integer     status;
    /**
     * 父级栏目
     */
    private TraceColumn parent;
    /**
     * 所属档案
     */
    private TraceTemplate   referrer;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id")
    public Config getItemType() {
        return itemType;
    }

    public void setItemType(Config itemType) {
        this.itemType = itemType;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "item_value", columnDefinition = "longtext")
    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    @Column(name = "item_name", nullable = false, length = 50)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Column(name = "ordernum")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trace_column_id")
    public TraceColumn getParent() {
        return parent;
    }

    public void setParent(TraceColumn parent) {
        this.parent = parent;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name = "trace_template_id")
    public TraceTemplate getReferrer() {
        return referrer;
    }

    public void setReferrer(TraceTemplate referrer) {
        this.referrer = referrer;
    }
}
