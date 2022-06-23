package info.bfly.archer.product.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * ProductPicture entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "product_picture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ProductPicture implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 3901338902664007708L;
    private String  id;
    private Product product;
    private String  picture;

    // Constructors

    /**
     * default constructor
     */
    public ProductPicture() {
    }

    /**
     * full constructor
     */
    public ProductPicture(Product product, String picture) {
        this.product = product;
        this.picture = picture;
    }

    // Property accessors
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "picture", length = 300)
    public String getPicture() {
        return picture;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    public Product getProduct() {
        return product;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
