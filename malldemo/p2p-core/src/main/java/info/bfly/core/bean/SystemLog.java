package info.bfly.core.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * SystemLog entity. 系统日志，利用Log4j直接写入数据库
 */
@Entity
@Table(name = "system_log")
public class SystemLog implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 6155222699667883554L;
    private String    id;
    private Timestamp logDate;
    private String    logLevel;
    private String    ip;
    private String    info;

    // Constructors

    /**
     * default constructor
     */
    public SystemLog() {
    }

    /**
     * minimal constructor
     */
    public SystemLog(String id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public SystemLog(String id, Timestamp logDate, String logLevel, String ip, String info) {
        this.id = id;
        this.logDate = logDate;
        this.logLevel = logLevel;
        this.ip = ip;
        this.info = info;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Lob
    @Column(name = "info", columnDefinition = "CLOB")
    public String getInfo() {
        return info;
    }

    @Column(name = "ip", length = 64)
    public String getIp() {
        return ip;
    }

    @Column(name = "log_date", length = 19)
    public Timestamp getLogDate() {
        return logDate;
    }

    @Column(name = "log_level", length = 20)
    public String getLogLevel() {
        return logLevel;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLogDate(Timestamp logDate) {
        this.logDate = logDate;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
}
