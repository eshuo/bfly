package info.bfly.core.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * EmailTemplate entity. 邮件模板，系统提供给管理员某些占位符，然后管理员可以根据占位符自定义模板。
 */
@Entity
@Table(name = "email_template")
public class EmailTemplate implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 5493149553391880414L;
    private String id;
    private String name;
    private String body;
    private String description;

    // Constructors

    /**
     * default constructor
     */
    public EmailTemplate() {
    }

    /**
     * minimal constructor
     */
    public EmailTemplate(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public EmailTemplate(String id, String name, String body, String description) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.description = description;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "body", columnDefinition = "longtext")
    public String getBody() {
        return body;
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

    public void setBody(String body) {
        this.body = body;
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
}
