package info.bfly.p2p.loan.model;

import info.bfly.archer.system.model.ExtensionField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 申请企业借款
 *
 * @author Administrator
 *
 */
@Entity
@Table(name = "apply_enterprise_loan_extension")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ApplyEnterpriseLoanExtension {
    private String              id;
    private ApplyEnterpriseLoan applyEnterpriseLoan;
    private ExtensionField      extensionField;
    private String              value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_enterprise_loan_id")
    public ApplyEnterpriseLoan getApplyEnterpriseLoan() {
        return applyEnterpriseLoan;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extension_field")
    public ExtensionField getExtensionField() {
        return extensionField;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Lob
    @Column(name = "value", columnDefinition = "CLOB")
    public String getValue() {
        return value;
    }

    public void setApplyEnterpriseLoan(ApplyEnterpriseLoan applyEnterpriseLoan) {
        this.applyEnterpriseLoan = applyEnterpriseLoan;
    }

    public void setExtensionField(ExtensionField extensionField) {
        this.extensionField = extensionField;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
