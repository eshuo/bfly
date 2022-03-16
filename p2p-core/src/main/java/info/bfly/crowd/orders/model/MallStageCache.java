package info.bfly.crowd.orders.model;

import info.bfly.crowd.mall.model.MallStage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created by XXSun on 3/23/2017.
 * 档位信息
 */
@Entity
@Table(name = "mall_stage_cache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class MallStageCache implements Serializable {
    private static final long serialVersionUID = -7103180066511314408L;
    private String id;

    private MallStage mallStage;
    
    private OrderCache orderCache;
    
    private String    cache;

    /**
     * 当前档位所有物品
     */


    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mall_stage")
    public MallStage getMallStage() {
        return mallStage;
    }

    public void setMallStage(MallStage mallStage) {
        this.mallStage = mallStage;
    }

    @Column(name = "cache", length = 1000)
    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_cache")
	public OrderCache getOrderCache() {
		return orderCache;
	}

	public void setOrderCache(OrderCache orderCache) {
		this.orderCache = orderCache;
	}
}
