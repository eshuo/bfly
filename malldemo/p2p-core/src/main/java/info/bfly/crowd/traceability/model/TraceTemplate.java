package info.bfly.crowd.traceability.model;

import info.bfly.crowd.mall.model.Goods;
import info.bfly.crowd.orders.model.OrderCache;
import info.bfly.p2p.loan.model.Loan;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created by XXSun on 3/21/2017.
 * 可追溯系统的模板
 * 模板结构如下
 * ///////////////Template//////////////
 * ////////Column//////////
 * /////Item////////
 * /////Item////////
 * /////Item////////
 * .............更多Item
 * ......//////////////更多Column
 */
@Entity
@Table(name = "trace_template")
@NamedQueries({
@NamedQuery(name="TraceTemplate.getTraceTemplate",query="Select traceTemplate from TraceTemplate traceTemplate,Goods goods where goods.id=:goodsId and goods.traceTemplate=traceTemplate.id and traceTemplate.status like 1"),
@NamedQuery(name="TraceTemplate.getTraceTemplateByType",query="Select trace from TraceTemplate trace where trace.type like ? and trace.status like 1")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class TraceTemplate implements Serializable {
    private static final long serialVersionUID = -7860939211495511606L;
    private String            id;
    /**
     * 目标
     */
    private String            target;
    /**
     * 所属投资项目
     */
    private Loan              parent;

    /**
     * 排序
     */
    private Integer            order;
    /**
     * 模板名称
     */
    private String            templateName;
    /**
     * 模板状态:0 已删除；1 存在
     */
    private Integer            status;
    /**
     * 模板类型，template-模板，record-可追溯档案
     */
    private String            type;
    /**
     * 模板继承来源
     */
    private TraceTemplate     referrer;
    /**
     * 提交时间
     */
    private Date              commitTime;
    
    /**
     * 模板项目
     */
    private List<TraceColumn> traceColumns;
    
    /**
     * 回报物
     */
    private Set<Goods>        goodsList;
    
    /**
     * 档案包含项目
     */
    private List<TraceItem>   traceItems;
    
    /**
     * 档案关联物品
     */
    private Goods goods;
    
    /**
     * 档案关联的订单
     */
    private List<OrderCache> orderCaches;

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    public Loan getParent() {
        return parent;
    }

    public void setParent(Loan parent) {
        this.parent = parent;
    }

    @Column(name = "template_order")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Column(name = "template_name", length = 100)
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "template_type", length = 50)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inherit_template_id")
    public TraceTemplate getReferrer() {
        return referrer;
    }

    public void setReferrer(TraceTemplate referrer) {
        this.referrer = referrer;
    }
    
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "parent")
    public List<TraceColumn> getTraceColumns() {
        return traceColumns;
    }

    public void setTraceColumns(List<TraceColumn> traceColumns) {
        this.traceColumns = traceColumns;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
    
    @Column(name="commit_time")
    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "traceTemplate")
    public Set<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(Set<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "referrer")
    public List<TraceItem> getTraceItems() {
        return traceItems;
    }

    public void setTraceItems(List<TraceItem> traceItems) {
        this.traceItems = traceItems;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinColumn(name = "goods_id")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "order_cache_trace_template", joinColumns = {@JoinColumn(name = "trace_template_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "order_cache_id", nullable = false, updatable = false)})
    public List<OrderCache> getOrderCaches() {
        return orderCaches;
    }

    public void setOrderCaches(List<OrderCache> orderCaches) {
        this.orderCaches = orderCaches;
    }
    
}
