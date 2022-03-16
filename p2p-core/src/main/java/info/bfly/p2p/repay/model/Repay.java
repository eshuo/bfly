package info.bfly.p2p.repay.model;

// default package
import java.util.Date;

/**
 * 还款
 */
public class Repay {
    /**
     * 当前还款为第几期
     */
    private Integer period;
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
     * 罚息（逾期利息+网站逾期罚息）
     */
    private Double  defaultInterest;
    /**
     * 本期长度
     */
    private Integer length;

    public Double getCorpus() {
        return corpus;
    }

    public Double getDefaultInterest() {
        return defaultInterest;
    }

    public Double getInterest() {
        return interest;
    }

    public Integer getLength() {
        return length;
    }

    public Integer getPeriod() {
        return period;
    }

    public Date getRepayDay() {
        return repayDay;
    }

    public void setCorpus(Double corpus) {
        this.corpus = corpus;
    }

    public void setDefaultInterest(Double defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setRepayDay(Date repayDay) {
        this.repayDay = repayDay;
    }
}
