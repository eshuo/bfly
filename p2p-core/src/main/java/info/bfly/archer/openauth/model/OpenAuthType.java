package info.bfly.archer.openauth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 第三方登录类型
 *
 * @author Administrator
 *
 */
@Entity
@Table(name = "open_auth_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class OpenAuthType implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 6876548125195898523L;
    private String id;
    private String name;
    private String description;
    private String status;

    public OpenAuthType() {
    }

    public OpenAuthType(String typeId) {
        id = typeId;
    }

    @Lob
    @Column(name = "description", columnDefinition = "CLOB")
    public String getDescription() {
        return description;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return status;
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

    public void setStatus(String status) {
        this.status = status;
    }
}
