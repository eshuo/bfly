package info.bfly.crowd.traceability.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created by XXSun on 3/21/2017.
 * 可追溯系统的组
 */
@Entity
@Table(name = "trace_column")
@NamedQueries({
	@NamedQuery(name = "TraceColumn.findColumnByReferrerId", query = "Select distinct m from TraceColumn m left join fetch m.children where m.referrer.id = ? and m.status like 1 order by m.order"),
	@NamedQuery(name="TraceColumn.findColumnByTemplateId",query="Select distinct m from TraceColumn m where m.parent.id = ? and m.status like 1 order by m.order"),
	@NamedQuery(name="TraceColumn.findColumnByTemplateAndItem",query="Select m from TraceColumn m left join m.traceItems t where m.parent.id = ? and t.itemValue is null and m.status like 1 order by m.order")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class TraceColumn implements Serializable {
    private static final long serialVersionUID = -4973390420420578008L;
    private String          id;
    /**
     * 栏目名称
     */
    private String          columnName;
    /**
     * 排序
     */
    private Integer         order;
    /**
     * 状态
     */
    private Integer          status;
    /**
     * 所属模板
     */
    private TraceTemplate   parent;
    /**
     * 来源模板
     */
    private TraceColumn     referrer;
    
    
    private Date 			commitTime;
    /**
     * 数据节点
     */
    private List<TraceColumn> children = new ArrayList<TraceColumn>(0);
    
    /**
     * 列的默认值
     */
    private List<TraceItem> traceItems;

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

    @Column(name = "column_name", length = 100)
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Column(name = "column_order")
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

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinColumn(name = "trace_template_id")  
    public TraceTemplate getParent() {
        return parent;
    }

    public void setParent(TraceTemplate parent) {
        this.parent = parent;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinColumn(name = "trace_column_id")
    public TraceColumn getReferrer() {
        return referrer;
    }

    public void setReferrer(TraceColumn referrer) {
        this.referrer = referrer;
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy="referrer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OrderBy(value = "order")
    public List<TraceColumn> getChildren() {
        return children;
    }

    public void setChildren(List<TraceColumn> children) {
        this.children = children;
    }
    
    @Column(name="commit_time")
	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parent")
    public List<TraceItem> getTraceItems() {
        return traceItems;
    }

    public void setTraceItems(List<TraceItem> traceItems) {
        this.traceItems = traceItems;
    }
}
