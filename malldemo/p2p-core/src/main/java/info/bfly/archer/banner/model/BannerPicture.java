package info.bfly.archer.banner.model;

import info.bfly.p2p.loan.model.Loan;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "banner_picture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BannerPicture implements Serializable {
    private static final long serialVersionUID  = -6888553622568788627L;
    private String            id;
    private Banner            banner;
    private String            title;
    private String            url;
    private Integer           seqNum;
    /**
     * 是否为外链
     */
    private Boolean           isOutSite;
    private String            picture;
    private List<Loan>        loanInfoPics      = new ArrayList<Loan>(0);
    private List<Loan>        loanGuaranteePics = new ArrayList<Loan>(0);

    // Constructors
    /** default constructor */
    public BannerPicture() {
    }

    /** full constructor */
    public BannerPicture(Banner banner, String picture) {
        this.banner = banner;
        this.picture = picture;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    public Banner getBanner() {
        return banner;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "is_out_site", columnDefinition = "BOOLEAN")
    public Boolean getIsOutSite() {
        return isOutSite;
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "loan_guarantee_pics", joinColumns = {@JoinColumn(name = "pic_id", nullable = false, updatable = true)}, inverseJoinColumns = {@JoinColumn(name = "loan_id", nullable = false, updatable = true)})
    public List<Loan> getLoanGuaranteePics() {
        return loanGuaranteePics;
    }

    // 如下配置，能搞定 只删除中间表
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "loan_info_pics", joinColumns = {@JoinColumn(name = "pic_id", nullable = false, updatable = true)}, inverseJoinColumns = {@JoinColumn(name = "loan_id", nullable = false, updatable = true)})
    public List<Loan> getLoanInfoPics() {
        return loanInfoPics;
    }

    @Column(name = "picture", length = 300)
    public String getPicture() {
        return picture;
    }

    @Column(name = "seq_num", nullable = false)
    public Integer getSeqNum() {
        return seqNum;
    }

    @Column(name = "title", length = 100)
    public String getTitle() {
        return title;
    }

    @Column(name = "url", length = 300)
    public String getUrl() {
        return url;
    }

    public void setBanner(Banner product) {
        banner = product;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsOutSite(Boolean isOutSite) {
        this.isOutSite = isOutSite;
    }

    public void setLoanGuaranteePics(List<Loan> loanGuaranteePics) {
        this.loanGuaranteePics = loanGuaranteePics;
    }

    public void setLoanInfoPics(List<Loan> loanInfoPics) {
        this.loanInfoPics = loanInfoPics;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
