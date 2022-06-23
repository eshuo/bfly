package info.bfly.p2p.repay.model;

// default package

import info.bfly.p2p.invest.model.Invest;

import javax.persistence.*;
import java.util.Date;

/**
 * 投资的还款信息
 *
 * @author wangzhi
 */
@Entity
@Table(name = "invest_repay")
public class InvestRepay implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 6306713152448243765L;
    private String  id;
    private Invest  invest;
    /**
     * 当前还款为第几期
     */
    private Integer period;
    /**
     * 还款时间
     */
    private Date    time;
    /**
     * 还款日
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
     * 罚息（逾期利息）
     */
    private Double  defaultInterest;
    /**
     * 手续费（从利息中扣除，给系统）
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

    // Constructors

    /**
     * default constructor
     */
    public InvestRepay() {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invest_id", nullable = false)
    public Invest getInvest() {
        return invest;
    }

    @Column(name = "length")
    public Integer getLength() {
        return length;
    }

    @Column(name = "period", nullable = false)
    public Integer getPeriod() {
        return period;
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
     * @param
     */
    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public void setInvest(Invest invest) {
        this.invest = invest;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * 还款日
     */
    public void setRepayDay(Date repayDay) {
        this.repayDay = repayDay;
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
}
