package info.bfly.p2p.loan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 项目类型费率
 *
 * @author yinjl
 *
 */
@Entity
@Table(name = "loan_type_rates")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class LoanTypeRates {
    private String   id;
    private Double   rate;
    private LoanType loanType;
    private String   description;

    public LoanTypeRates() {
    }

    public LoanTypeRates(String id, Double rate, LoanType loanType, String description) {
        this.id = id;
        this.rate = rate;
        this.loanType = loanType;
        this.description = description;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type")
    public LoanType getLoanType() {
        return loanType;
    }

    @Column(name = "rate")
    public Double getRate() {
        return rate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
