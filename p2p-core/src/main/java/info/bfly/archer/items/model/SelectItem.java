package info.bfly.archer.items.model;

// default package
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * ๅ้้กน
 */
@Entity
@Table(name = "select_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@NamedQueries({@NamedQuery(name = "SelectItem.findItemsById", query = "from SelectItem where id = ?")})
public class SelectItem implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 7860646509619195217L;
    private String          id;
    private SelectItemGroup selectItemGroup;
    private String          name;
    private String          items;
    private String          description;

    // Constructors

    /**
     * default constructor
     */
    public SelectItem() {
    }

    /**
     * full constructor
     */
    public SelectItem(String id, SelectItemGroup selectItemGroup, String name, String items, String description) {
        this.id = id;
        this.selectItemGroup = selectItemGroup;
        this.name = name;
        this.items = items;
        this.description = description;
    }

    /**
     * minimal constructor
     */
    public SelectItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return description;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "items", length = 1000)
    public String getItems() {
        return items;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_group")
    public SelectItemGroup getSelectItemGroup() {
        return selectItemGroup;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelectItemGroup(SelectItemGroup selectItemGroup) {
        this.selectItemGroup = selectItemGroup;
    }
}
