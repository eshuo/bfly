package info.bfly.core.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * UploadCategory entity. 上传文件的分类
 */
@Entity
@Table(name = "upload_category")
public class UploadCategory implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 1866639878276595508L;
    private String id;
    private String name;
    private String description;
    private Set<Upload> uploads = new HashSet<Upload>(0);

    // Constructors

    /**
     * default constructor
     */
    public UploadCategory() {
    }

    /**
     * minimal constructor
     */
    public UploadCategory(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public UploadCategory(String id, String name, String description, Set<Upload> uploads) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.uploads = uploads;
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

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "uploadCategory")
    public Set<Upload> getUploads() {
        return uploads;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUploads(Set<Upload> uploads) {
        this.uploads = uploads;
    }
}
