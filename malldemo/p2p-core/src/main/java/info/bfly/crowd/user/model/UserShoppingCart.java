package info.bfly.crowd.user.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;;

/**
 * Created by XXSun on 3/22/2017.
 * 购物车
 */
@Entity
@Table(name = "user_shopping_cart")
public class UserShoppingCart implements Serializable {
    private static final long serialVersionUID = -6712047296588987334L;
    private String id;
    
    private String User;
    
    private List<ShoppingCardGroup> cardGroupList;
    
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "user", length = 200)
    public String getUser() {
        return User;
    }
    
    public void setUser(String user) {
        User = user;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usershoppingcart")
    public List<ShoppingCardGroup> getCardGroupList() {
        return cardGroupList;
    }
    
    public void setCardGroupList(List<ShoppingCardGroup> cardGroupList) {
        this.cardGroupList = cardGroupList;
    }
    
}
