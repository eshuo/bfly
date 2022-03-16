package info.bfly.crowd.orders.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created by XXSun on 3/22/2017.
 * 配送信息
 */

@Entity
@Table(name = "freight_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class FreightInfo implements Serializable {
    private static final long serialVersionUID = 6285481730891582775L;
    private String id;
    /**
     * 父级订单
     */
    private Order  order;
    /**
     * 创建时间
     */
    private Date   createTime;
    /**
     * 最后更新时间
     */
    private Date   updateTime;
    /**
     * 完成时间
     */
    private Date   finishTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 备注信息
     */
    private String info;

    /**
     * 订单路由
     */
    private List<String> freightRoute;

    /**
     * 配送信息  配送公司
     */
    private String freightCompany;
    /**
     * 配送信息 订单号
     */
    private String freightOrderNo;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")  
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    @Column(name = "status", length = 50)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "info", length = 1000)
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @ElementCollection(targetClass = java.lang.String.class) 
    @JoinTable(name = "freight_route", joinColumns = { @JoinColumn(nullable = false, name = "route_id") })
    @Column(name = "freight_route")
    public List<String> getFreightRoute() {
        return freightRoute;
    }

    public void setFreightRoute(List<String> freightRoute) {
        this.freightRoute = freightRoute;
    }

    @Column(name = "freight_company", length = 1000)
    public String getFreightCompany() {
        return freightCompany;
    }

    public void setFreightCompany(String freightCompany) {
        this.freightCompany = freightCompany;
    }

    @Column(name = "freight_order_no", length = 50)
    public String getFreightOrderNo() {
        return freightOrderNo;
    }

    public void setFreightOrderNo(String freightOrderNo) {
        this.freightOrderNo = freightOrderNo;
    }
}
