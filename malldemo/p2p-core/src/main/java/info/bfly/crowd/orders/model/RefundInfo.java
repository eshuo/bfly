package info.bfly.crowd.orders.model;

import java.math.BigDecimal;

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
 */
@Entity
@Table(name = "refund_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class RefundInfo {
    private String     id;
    private Order      order;
    private OrderCache orderCache;

    private BigDecimal refundMoney;
    private String     status;
    private String     operationOrderNo;
    
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_cache_id")
    public OrderCache getOrderCache() {
        return orderCache;
    }
    
    public void setOrderCache(OrderCache orderCache) {
        this.orderCache = orderCache;
    }
    
    @Column(name= "refund_money")
    public BigDecimal getRefundMoney() {
        return refundMoney;
    }
    
    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }
    
    @Column(name = "status", length = 50)
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Column(name = "operation_order_no", length = 30)
    public String getOperationOrderNo() {
        return operationOrderNo;
    }
    
    public void setOperationOrderNo(String operationOrderNo) {
        this.operationOrderNo = operationOrderNo;
    }
}
