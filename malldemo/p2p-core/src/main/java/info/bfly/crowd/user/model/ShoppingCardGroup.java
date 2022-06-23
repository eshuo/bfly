package info.bfly.crowd.user.model;

import info.bfly.crowd.orders.model.OrderCache;

import java.io.Serializable;
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

/**
 * Created by XXSun on 3/23/2017.
 */
@Entity
@Table(name = "shopping_card_group")
public class ShoppingCardGroup implements Serializable{
    private static final long serialVersionUID = -6497234332591018050L;
    
    private String           id;
    
    private String           Company;
    
    private List<OrderCache> orderCaches;
    
    private UserShoppingCart usershoppingcart;

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "company" , length = 200)
    public String getCompany() {
        return Company;
    }
    
    public void setCompany(String company) {
        Company = company;
    }
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "shopcg")
    public List<OrderCache> getOrderCaches() {
        return orderCaches;
    }
    
    public void setOrderCaches(List<OrderCache> orderCaches) {
        this.orderCaches = orderCaches;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_card_group_id")
    public UserShoppingCart getUsershoppingcart() {
        return usershoppingcart;
    }

    public void setUsershoppingcart(UserShoppingCart usershoppingcart) {
        this.usershoppingcart = usershoppingcart;
    }
    
}
