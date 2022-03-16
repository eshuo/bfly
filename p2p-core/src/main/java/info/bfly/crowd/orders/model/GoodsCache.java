package info.bfly.crowd.orders.model;

import info.bfly.crowd.mall.model.Goods;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.traceability.model.TraceTemplate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @ClassName: GoodsCache
 * @Description: 订单中的回报物列表
 * @author zeminshao
 * @date 2017年3月28日 下午5:21:02
 *
 */
@Entity
@Table(name = "goods_cache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class GoodsCache implements Serializable {
	private static final long serialVersionUID = -703553723404856164L;

	private String id;
	/**
	 * 档案
	 */
	private List<TraceTemplate> traceTemplates;
	/**
	 * 档位
	 */
	private MallStage mallStage;
	
	/**
	 * 回报物
	 */
	private Goods goods;
	
	/**
	 * 数量
	 */
	private Integer num;
	/**
	 * 单价
	 */
	private Double money;
	/**
	 * 物品信息的缓存 价格名字图片等等
	 */
	private String cache;
	
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 提交时间
	 */
	private Date commitTime;
	
	
	public GoodsCache(){
	    
	}
	
	@Id
	@Column(name = "id", nullable = false, unique = true, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<TraceTemplate> getTraceTemplates() {
		return traceTemplates;
	}

	public void setTraceTemplates(List<TraceTemplate> traceTemplates) {
		this.traceTemplates = traceTemplates;
	}

	@Column(name = "cache", length = 400)
	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	@Column(name = "money")
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "num")
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	@Column(name="commit_time")
	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name = "goods_id")
	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}
    
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "mall_stage_id")
    public MallStage getMallStage() {
        return mallStage;
    }

    public void setMallStage(MallStage mallStage) {
        this.mallStage = mallStage;
    }
    
    @Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
