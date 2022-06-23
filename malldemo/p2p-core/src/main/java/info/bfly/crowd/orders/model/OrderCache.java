package info.bfly.crowd.orders.model;

import info.bfly.crowd.traceability.model.TraceTemplate;
import info.bfly.crowd.user.model.ShoppingCardGroup;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @ClassName: OrderCache
 * @Description: 单条订单项目信息
 * @author zeminshao
 * @date 2017年3月28日 下午5:22:11
 *
 */
@Entity
@Table(name = "order_cache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class OrderCache implements Serializable {
	private static final long serialVersionUID = 8601851484776571293L;
	private String id;
	private Order mallOrder;
	private String cache;
	/**
	 * 档位
	 */
	private MallStageCache mallStageCache;

	/**
	 * 物品
	 */
	private GoodsCache goodsCache;
	/**
	 * 数量
	 */
	private Integer num;
	/**
	 * 单价
	 */
	private Double money;

	/**
	 * 赠品
	 */
	private List<OrderCache> giftGoods;
	
	/**
	 * 订单关联档案
	 */
	private List<TraceTemplate> traceTemplates;

    private ShoppingCardGroup shopcg;

	@Id
	@Column(name = "id", nullable = false, unique = true, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	@Column(name = "cache",length=1000)
	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mall_stage_cache")
	public MallStageCache getMallStageCache() {
		return mallStageCache;
	}

	public void setMallStageCache(MallStageCache mallStageCache) {
		this.mallStageCache = mallStageCache;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "goods")
	public GoodsCache getGoodsCache() {
		return goodsCache;
	}

	public void setGoodsCache(GoodsCache goodsCache) {
		this.goodsCache = goodsCache;
	}

	@Column(name = "num")
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	@Column(name = "money")
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mallOrder")
	public List<OrderCache> getGiftGoods() {
		return giftGoods;
	}

	public void setGiftGoods(List<OrderCache> giftGoods) {
		this.giftGoods = giftGoods;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_card_group_id")
    public ShoppingCardGroup getShopcg() {
        return shopcg;
    }

    public void setShopcg(ShoppingCardGroup shopcg) {
        this.shopcg = shopcg;
    }
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mall_order")
	public Order getMallOrder() {
		return mallOrder;
	}

	public void setMallOrder(Order mallOrder) {
		this.mallOrder = mallOrder;
	}
    
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "order_cache_trace_template", joinColumns = {@JoinColumn(name = "order_cache_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "trace_template_id", nullable = false, updatable = false)})
    public List<TraceTemplate> getTraceTemplates() {
        return traceTemplates;
    }

    public void setTraceTemplates(List<TraceTemplate> traceTemplates) {
        this.traceTemplates = traceTemplates;
    }
}
