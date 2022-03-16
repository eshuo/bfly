package info.bfly.core.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SearchDatasetId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class SearchDatasetId implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 7774209296497400885L;
    private String id;
    private String type;
    private String data;

    // Constructors

    /**
     * default constructor
     */
    public SearchDatasetId() {
    }

    /**
     * full constructor
     */
    public SearchDatasetId(String id, String type, String data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    // Property accessors
    @Override
    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof SearchDatasetId)) return false;
        SearchDatasetId castOther = (SearchDatasetId) other;
        return ((getId() == castOther.getId()) || (getId() != null && castOther.getId() != null && getId().equals(castOther.getId())))
                && ((getType() == castOther.getType()) || (getType() != null && castOther.getType() != null && getType().equals(castOther.getType())))
                && ((getData() == castOther.getData()) || (getData() != null && castOther.getData() != null && getData().equals(castOther.getData())));
    }

    @Column(name = "DATA", length = 10)
    public String getData() {
        return data;
    }

    @Column(name = "ID", length = 10)
    public String getId() {
        return id;
    }

    @Column(name = "TYPE", length = 10)
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (getId() == null ? 0 : getId().hashCode());
        result = 37 * result + (getType() == null ? 0 : getType().hashCode());
        result = 37 * result + (getData() == null ? 0 : getData().hashCode());
        return result;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }
}
