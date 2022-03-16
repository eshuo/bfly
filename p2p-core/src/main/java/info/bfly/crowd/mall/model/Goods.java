package info.bfly.crowd.mall.model;

import info.bfly.archer.config.model.Config;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.traceability.model.TraceTemplate;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
* @ClassName: Goods 
* @Description: 回报物 
* @author zeminshao
* @date 2017年3月28日 下午5:20:42 
*
 */
@Entity
@Table(name = "goods")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Goods implements Serializable {

	private static final long serialVersionUID = -3543744887693589402L;
	private String id;
	/**
	 * 物品名称
	 */
	private String goodsName;
	/**
	 * 物品照片
	 */
	private String goodsPicture;
	/**
	 * 物品描述
	 */
	private String goodsDescribe;
	/**
	 * 可追溯模板
	 */
	private TraceTemplate traceTemplate;
	/**
	 * 排序
	 */
	private Integer order;
	/**
	 * 物品类型
	 */
	private Config type;
	/**
	 * 提交时间
	 */
	private Date commitTime;
	/**
     * 回报服务
     */
    private Set<GoodsCache> goodsCaches;

    /**
     * 物品的可追溯档案
     */
    private List<TraceTemplate> recordList;

    /**
	 * 物品的单个标价
	 */
	private List<Inventory> goodsInventories;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Id
	@Column(name = "id", nullable = false, unique = true, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "goods_name", nullable = false, length = 200)
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	@Column(name = "goods_picture", length = 500)
	public String getGoodsPicture() {
		return goodsPicture;
	}

	public void setGoodsPicture(String goodsPicture) {
		this.goodsPicture = goodsPicture;
	}

	@Column(name = "goods_describe", length = 500)
	public String getGoodsDescribe() {
		return goodsDescribe;
	}

	public void setGoodsDescribe(String goodsDescribe) {
		this.goodsDescribe = goodsDescribe;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.PERSIST)
	@JoinColumn(name = "template_id")
	public TraceTemplate getTraceTemplate() {
		return traceTemplate;
	}

	public void setTraceTemplate(TraceTemplate traceTemplate) {
		this.traceTemplate = traceTemplate;
	}

	@Column(name = "goods_order")
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_type")
	public Config getType() {
		return type;
	}

	public void setType(Config type) {
		this.type = type;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "goods")
	public List<Inventory> getGoodsInventories() {
		return goodsInventories;
	}

	public void setGoodsInventories(List<Inventory> goodsInventories) {
		this.goodsInventories = goodsInventories;
	}
	
	@Column(name="commit_time")
	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}
    
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "goods")
    public List<TraceTemplate> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<TraceTemplate> recordList) {
        this.recordList = recordList;
    }
	
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "goods")
	public Set<GoodsCache> getGoodsCaches() {
		return goodsCaches;
	}

	public void setGoodsCaches(Set<GoodsCache> goodsCaches) {
		this.goodsCaches = goodsCaches;
	}
	

}
