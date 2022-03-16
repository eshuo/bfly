package info.bfly.archer.common.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by XXSun on 2/24/2017.
 */
public class AuthInfoPK implements Serializable {
    private static final long serialVersionUID = -2986036249024743034L;
    private String authTarget;
    private String authType;
    private String authSource;


    @Column(name = "auth_target")
    @Id
    public String getAuthTarget() {
        return authTarget;
    }

    public void setAuthTarget(String authTarget) {
        this.authTarget = authTarget;
    }

    @Column(name = "auth_type")
    @Id
    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    @Column(name = "auth_source")
    @Id
    public String getAuthSource() {
        return authSource;
    }

    public void setAuthSource(String authSource) {
        this.authSource = authSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthInfoPK that = (AuthInfoPK) o;

        return (authTarget != null ? authTarget.equals(that.authTarget) : that.authTarget == null) && (authType != null ? authType.equals(that.authType) : that.authType == null) && (authSource != null ? authSource.equals(that.authSource) : that.authSource == null);
    }

    @Override
    public int hashCode() {
        int result = authTarget != null ? authTarget.hashCode() : 0;
        result = 31 * result + (authType != null ? authType.hashCode() : 0);
        result = 31 * result + (authSource != null ? authSource.hashCode() : 0);
        return result;
    }

    public AuthInfoPK() {
    }

    public AuthInfoPK(String authSource, String authTarget, String authType) {
        this.authSource = authSource;
        this.authTarget = authTarget;
        this.authType = authType;
    }
}
