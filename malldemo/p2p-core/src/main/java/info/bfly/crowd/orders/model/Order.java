package info.bfly.crowd.orders.model;

import info.bfly.archer.user.model.User;
import info.bfly.crowd.user.model.UserAddress;
import info.bfly.p2p.invest.model.Invest;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
* @ClassName: Order 
* @Description: 订单表
* @author zeminshao
* @date 2017年3月28日 下午5:21:55 
*
 */
@Entity
@Table(name = "mall_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Order implements Serializable {

	private static final long serialVersionUID = 2295493015319347718L;

	private String id;
	private User user;
	/**
	 * 订单状态
	 */
	private String orderStatus;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 结束时间
	 */
	private Date finishTime;
	/**
	 * 收货地址
	 */
	private UserAddress userAddress;
	/**
	 * 订单列表
	 */
	private List<OrderCache> orderCaches;
	/**
	 * 订单支付信息
	 */
	private PayInfo orderPayInfo;
	/**
	 * 物流信息
	 */
	private FreightInfo freightInfo;
	/**
	 * 父级订单
	 */
	private Order parentOrder;
	/**
	 * 退款
	 */
	private List<RefundInfo> refundInfos;
	/**
	 * 退货/换货物流信息
	 */
	private List<FreightInfo> refundFreightInfos;
	/**
	 * 众筹项目订单投资信息，暂时使用投资记录表
	 */
	private Invest invest;
	/*
	 * 项目信息
	 */
	//private Mall mall;

	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invest_id")
    public Invest getInvest() {
        return invest;
    }

    public void setInvest(Invest invest) {
        this.invest = invest;
    }

    @Id
	@Column(name = "id", nullable = false, unique = true, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "order_status", length = 60, nullable = false)
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "finish_time")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_address_id")
	public UserAddress getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mallOrder")
	public List<OrderCache> getOrderCaches() {
		return orderCaches;
	}

	public void setOrderCaches(List<OrderCache> orderCaches) {
		this.orderCaches = orderCaches;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_payinfo")
	public PayInfo getOrderPayInfo() {
		return orderPayInfo;
	}

	public void setOrderPayInfo(PayInfo orderPayInfo) {
		this.orderPayInfo = orderPayInfo;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "freight_info")
	public FreightInfo getFreightInfo() {
		return freightInfo;
	}

	public void setFreightInfo(FreightInfo freightInfo) {
		this.freightInfo = freightInfo;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_order")
	public Order getParentOrder() {
		return parentOrder;
	}

	public void setParentOrder(Order parentOrder) {
		this.parentOrder = parentOrder;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
	public List<RefundInfo> getRefundInfos() {
		return refundInfos;
	}

	public void setRefundInfos(List<RefundInfo> refundInfos) {
		this.refundInfos = refundInfos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
	public List<FreightInfo> getRefundFreightInfos() {
		return refundFreightInfos;
	}

	public void setRefundFreightInfos(List<FreightInfo> refundFreightInfos) {
		this.refundFreightInfos = refundFreightInfos;
	}

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mall_id")
    public Mall getMall() {
        return mall;
    }

    public void setMall(Mall mall) {
        this.mall = mall;
    }*/
}
