package info.bfly.archer.banner.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "banner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Banner implements Serializable {
    // Fields
    private static final long serialVersionUID = 2489798649352321423L;
    private String id;
    private String description;
    private List<BannerPicture> pictures = new ArrayList<BannerPicture>(0);

    /**
     * default constructor
     */
    public Banner() {
    }

    /**
     * full constructor
     */
    public Banner(String description, List<BannerPicture> bannerPictures) {
        this.description = description;
        pictures = bannerPictures;
    }

    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    public String getDescription() {
        return description;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "banner")
    @OrderBy(value = "seqNum")
    public List<BannerPicture> getPictures() {
        return pictures;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPictures(List<BannerPicture> bannerPictures) {
        pictures = bannerPictures;
    }
}
