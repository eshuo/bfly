package info.bfly.p2p.repay.model;

// default package

import info.bfly.p2p.loan.model.Loan;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 借款的还款信息
 * 
 * @author wangzhi
 */
@Entity
@Table(name = "loan_repay")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class LoanRepay implements Serializable {
    // Fields
    /**
     *
     */
    private static final long serialVersionUID = -4334452634255325208L;
    private String  id;
    private Loan    loan;
    /**
     * 还款途径
     */
    private String  repayWay;
    /**
     * 当前还款为第几期
     */
    private Integer period;
    /**
     * 实际还款时间
     */
    private Date    time;
    /**
     * 还款日（账单日）
     */
    private Date    repayDay;
    /**
     * 本金
     */
    private Double  corpus;
    /**
     * 利息
     */
    private Double  interest;
    /**
     * 罚息（逾期利息+网站逾期罚息）
     */
    private Double  defaultInterest;
    /**
     * 手续费（给系统的）
     */
    private Double  fee;
    /**
     * 状态
     */
    private String  status;
    /**
     * 本期长度
     */
    private Integer length;
    /**
     * 备注
     */
    private String  remark;

    /**
     * 交易订单号
     */
    private String operationOrderNo;

    // Constructors

    /**
     * default constructor
     */
    public LoanRepay() {
    }

    /**
     * 本金
     */
    @Column(name = "corpus", nullable = false, precision = 22, scale = 0)
    public Double getCorpus() {
        return corpus;
    }

    /**
     * 罚息
     */
    @Column(name = "default_interest", precision = 22, scale = 0)
    public Double getDefaultInterest() {
        if (defaultInterest == null) {
            return 0D;
        }
        return defaultInterest;
    }

    @Column(name = "fee")
    public Double getFee() {
        return fee;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    /**
     * 利息
     */
    @Column(name = "interest", nullable = false, precision = 22, scale = 0)
    public Double getInterest() {
        return interest;
    }

    @Column(name = "length")
    public Integer getLength() {
        return length;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    public Loan getLoan() {
        return loan;
    }

    @Column(name = "period", nullable = false)
    public Integer getPeriod() {
        return period;
    }

    @Lob
    @Column(name = "remark", columnDefinition = "CLOB")
    public String getRemark() {
        return remark;
    }

    /**
     * 还款日
     *
     * @return
     */
    @Column(name = "repay_day", nullable = false)
    public Date getRepayDay() {
        return repayDay;
    }

    @Column(name = "repay_way", length = 200)
    public String getRepayWay() {
        return repayWay;
    }

    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return status;
    }

    /**
     * 还款时间
     */
    @Column(name = "time", length = 19)
    public Date getTime() {
        return time;
    }

    @Column(name = "order_no",unique = true,length = 64)
    public String getOperationOrderNo() {
        return operationOrderNo;
    }

    /**
     * 本金
     *
     * @param corpus
     */
    public void setCorpus(Double corpus) {
        this.corpus = corpus;
    }

    /**
     * 罚息
     */
    public void setDefaultInterest(Double defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 利息
     *
     * @param data .corpus
     */
    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 还款日
     */
    public void setRepayDay(Date repayDay) {
        this.repayDay = repayDay;
    }

    public void setRepayWay(String repayWay) {
        this.repayWay = repayWay;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 还款时间
     *
     * @param time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    public void setOperationOrderNo(String operationOrderNo) {
        this.operationOrderNo = operationOrderNo;
    }
}
