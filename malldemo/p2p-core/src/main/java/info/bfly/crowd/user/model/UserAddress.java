package info.bfly.crowd.user.model;

import info.bfly.archer.user.model.User;
import info.bfly.crowd.orders.model.Order;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
 * Created by XXSun on 3/22/2017. 用户收货地址
 */
@Entity
@Table(name = "user_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserAddress implements Serializable {
    private static final long serialVersionUID = 2416127396048808084L;

    private String id;

    private User user;
    /**
     * 区域 保存的数组 [110000,111100,111111] 省，市，县
     */
    private String[] areas;
    /**
     * 订单信息
     */

    private Order order;
    /**
     * 详细地址
     */

    private String detailAddress;
    /**
     * 收货人
     */
    private String consigneeName;
    /**
     * 收货人联系电话
     */

    private String consigneePhone;
    /**
     * 默认收货地址
     */
    private Boolean isDefault = true;
    
    /**
     * 收货地址是否可以，1-可用，0-不可用
     */
    private Integer status = 1;

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "detail_address", length = 200)
    public String getDetailAddress() {
        return detailAddress;
    }

    @Column(name = "consignee_name", length = 200)
    public String getConsigneeName() {
        return consigneeName;
    }

    @Column(name = "consignee_phone", length = 200)
    public String getConsigneePhone() {
        return consigneePhone;
    }

    @Column(name = "is_default")
    public Boolean getIsDefault() {
        return isDefault;
    }

    @Column(name = "areas", length = 50)
    public String[] getAreas() {
        return areas;
    }

    @OneToOne(fetch = FetchType.LAZY,cascade=CascadeType.MERGE)
    @JoinColumn(name = "mall_order")
    public Order getOrder() {
        return order;
    }

    @ManyToOne(cascade =CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "consignee_user")
    public User getUser() {
        return user;
    }
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }
    
    
    public void setId(String id) {
        this.id = id;
    }    
    
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }	
	
    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setAreas(String[] areas) {
        this.areas = areas;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }

}
