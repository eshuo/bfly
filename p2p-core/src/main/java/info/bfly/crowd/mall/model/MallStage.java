package info.bfly.crowd.mall.model;

import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.model.MallStageCache;
import info.bfly.p2p.loan.model.Loan;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created by XXSun on 3/22/2017.
 * 档位
 */
@Entity
@Table(name = "mall_stage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class MallStage implements Serializable {
    private static final long serialVersionUID = 1070141458323655641L;
    private String id;
    /**
     * 档位名称
     */
    private String stageName;
    /**
     * 档位照片
     */
    private String stagePicture;
    /**
     * 档位描述
     */
    private String stageDescribe;
    
    
    private MallStageCache mallStageCache;
    

    /**
     * 档位的物品配置
     */
    private List<Inventory> goodsInventories = new ArrayList<Inventory>(0);
    /**
     * 档位配置
     */
    private Inventory       stageInventory;
    
    /**
     * 回报服务
     */
    private List<GoodsCache> goodsCache = new ArrayList<GoodsCache>(0);
    /**
     * 项目
     */
    private Loan loan;
    /**
     * 提交时间
     */
    private Date commitTime;
    /**
     * 排序
     */
    private  Integer mallorder;

    
    public MallStage(){
        
    }
    
    public MallStage(String id){
        this.id = id;
    }
    
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name = "stage_name", nullable = false, length = 50)
    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }
    
    @Column(name = "stage_picture", length = 300)
    public String getStagePicture() {
        return stagePicture;
    }

    public void setStagePicture(String stagePicture) {
        this.stagePicture = stagePicture;
    }
    
    @Column(name = "stage_description", length = 1000)
    public String getStageDescribe() {
        return stageDescribe;
    }

    public void setStageDescribe(String stageDescribe) {
        this.stageDescribe = stageDescribe;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mallStage")
    public List<Inventory> getGoodsInventories() {
        return goodsInventories;
    }

    public void setGoodsInventories(List<Inventory> goodsInventories) {
        this.goodsInventories = goodsInventories;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_inventory")
    public Inventory getStageInventory() {
        return stageInventory;
    }

    public void setStageInventory(Inventory stageInventory) {
        this.stageInventory = stageInventory;
    }


    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mallStage")
    public List<GoodsCache> getGoodsCache() {
        return goodsCache;
    }

    public void setGoodsCache(List<GoodsCache> goodsCache) {
        this.goodsCache = goodsCache;
    }
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mall_stage_cache")
	public MallStageCache getMallStageCache() {
		return mallStageCache;
	}

	public void setMallStageCache(MallStageCache mallStageCache) {
		this.mallStageCache = mallStageCache;
	}
	
	@Column(name="mall_stage_order")
	public Integer getMallorder() {
		return mallorder;
	}

	public void setMallorder(Integer order) {
		this.mallorder = order;
	}
    
    @Column(name = "commit_time")
    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }
}
