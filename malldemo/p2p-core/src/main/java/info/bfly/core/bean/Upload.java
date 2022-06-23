package info.bfly.core.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Upload entity. 上传文件
 */
@Entity
@Table(name = "upload")
public class Upload implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 6261256593578825426L;
    private String         id;
    private UploadCategory uploadCategory;
    private String         fileName;
    private String         uri;
    private String         fileMime;
    private Double         size;
    private String         status;
    private Timestamp      uploadTime;
    private String         description;

    // Constructors

    /**
     * default constructor
     */
    public Upload() {
    }

    /**
     * minimal constructor
     */
    public Upload(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public Upload(String id, UploadCategory uploadCategory, String fileName, String uri, String fileMime, Double size, String status, Timestamp uploadTime, String description) {
        this.id = id;
        this.uploadCategory = uploadCategory;
        this.fileName = fileName;
        this.uri = uri;
        this.fileMime = fileMime;
        this.size = size;
        this.status = status;
        this.uploadTime = uploadTime;
        this.description = description;
    }

    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    public String getDescription() {
        return description;
    }

    @Column(name = "file_mime", length = 50)
    public String getFileMime() {
        return fileMime;
    }

    @Column(name = "file_name", length = 100)
    public String getFileName() {
        return fileName;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "size", precision = 22, scale = 0)
    public Double getSize() {
        return size;
    }

    @Column(name = "status", length = 10)
    public String getStatus() {
        return status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_category")
    public UploadCategory getUploadCategory() {
        return uploadCategory;
    }

    @Column(name = "upload_time", length = 19)
    public Timestamp getUploadTime() {
        return uploadTime;
    }

    @Column(name = "uri", length = 500)
    public String getUri() {
        return uri;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFileMime(String fileMime) {
        this.fileMime = fileMime;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUploadCategory(UploadCategory uploadCategory) {
        this.uploadCategory = uploadCategory;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
