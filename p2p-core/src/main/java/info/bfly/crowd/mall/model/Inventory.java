package info.bfly.crowd.mall.model;

import info.bfly.archer.config.model.Config;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created by XXSun on 3/22/2017.
 * 项目
 */
@Entity
@Table(name = "inventory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Inventory implements Serializable {
    private static final long serialVersionUID = -669863905620369852L;
    
    private String    id;
    /**
     * 物品
     */
    private Goods     goods;
    /**
     * 档位
     */
    private MallStage mallStage;
    /**
     * 档位配置    
     */
    private MallStage inventoryStage;
    /**
     * 类型
     */
    private Config    type;
    /**
     * 价格
     */
    private Double    fee;
    /**
     * 运费
     */
    private Double    freightFee;
    /**
     * 最少购买数量
     */
    private Integer   minNum;
    /**
     * 递增数量
     */
    private Integer   stepNum;
    /**
     * 最多购买数量
     */
    private Integer   maxNum;
    /**
     * 最少购买金额
     */
    private Integer   minFee;

    /**
     * 最多购买金额
     */
    private Integer maxFee;

    /**
     * 这里指的当前档位包括了多少该物品
     */
    private Integer goodsNum;

    /**
     * 档位或者物品的库存
     */
    private Integer num;

    //区域
    /**
     * 配送区域
     */
    private String[] areas;
    //调货区域
    /**
     * 调货区域
     */
    private String[] backAreas;

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mall_stage_id")
    public MallStage getMallStage() {
        return mallStage;
    }

    public void setMallStage(MallStage mallStage) {
        this.mallStage = mallStage;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public Config getType() {
        return type;
    }

    public void setType(Config type) {
        this.type = type;
    }

    @Column(name = "fee")
    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    @Column(name = "freight_fee")
    public Double getFreightFee() {
        return freightFee;
    }

    public void setFreightFee(Double freightFee) {
        this.freightFee = freightFee;
    }

    @Column(name = "min_num")
    public Integer getMinNum() {
        return minNum;
    }

    public void setMinNum(Integer minNum) {
        this.minNum = minNum;
    }

    @Column(name = "step_num" )
    public Integer getStepNum() {
        return stepNum;
    }

    public void setStepNum(Integer stepNum) {
        this.stepNum = stepNum;
    }

    @Column(name = "max_num")
    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    @Column(name = "goods_num")
    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    @Column(name = "min_fee")
    public Integer getMinFee() {
        return minFee;
    }

    public void setMinFee(Integer minFee) {
        this.minFee = minFee;
    }

    @Column(name = "max_fee")
    public Integer getMaxFee() {
        return maxFee;
    }

    public void setMaxFee(Integer maxFee) {
        this.maxFee = maxFee;
    }

    @Column(name = "num")
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Column(name = "areas")
    public String[] getAreas() {
        return areas;
    }

    public void setAreas(String[] areas) {
        this.areas = areas;
    }

    @Column(name = "back_areas")
    public String[] getBackAreas() {
        return backAreas;
    }

    public void setBackAreas(String[] backAreas) {
        this.backAreas = backAreas;
    }
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_stage")
	public MallStage getInventoryStage() {
		return inventoryStage;
	}

	public void setInventoryStage(MallStage inventoryStage) {
		this.inventoryStage = inventoryStage;
	}
}
