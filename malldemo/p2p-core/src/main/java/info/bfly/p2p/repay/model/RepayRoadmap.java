package info.bfly.p2p.repay.model;

import info.bfly.core.util.ArithUtil;

import java.util.Date;

/**
 * 还款路标
 *
 * @author Administrator
 *
 */
public class RepayRoadmap {
    /**
     * Y:手续费使总金额增加。 N:手续费使总金额减少 plus or minus
     */
    private Boolean feePlus;
    /**
     * 待还金额
     */
    private Double  unPaidMoney;
    /**
     * 待还本金
     */
    private Double  unPaidCorpus;
    /**
     * 待还利息
     */
    private Double  unPaidInterest;
    /**
     * 待还费用
     */
    private Double  unPaidFee;
    /**
     * 待还期数
     */
    private int     unPaidPeriod;
    /**
     * 待还罚息
     */
    private Double  unPaidDefaultInterest;
    /**
     * 已还金额
     */
    private Double  paidMoney;
    /**
     * 已还本金
     */
    private Double  paidCorpus;
    /**
     * 已还利息
     */
    private Double  paidInterest;
    /**
     * 已还费用
     */
    private Double  paidFee;
    /**
     * 已还罚息
     */
    private Double  paidDefaultInterest;
    /**
     * 已还期数
     */
    private int     paidPeriod;
    /**
     * 还款总额
     */
    private Double  repayMoney;
    /**
     * 还款总的本金
     */
    private Double  repayCorpus;
    /**
     * 还款总的利息
     */
    private Double  repayInterest;
    /**
     * 还款总的费用
     */
    private Double  repayFee;
    /**
     * 还款总的罚息
     */
    private Double  repayDefaultInterest;
    /**
     * 下个还款日
     */
    private Date    nextRepayDate;
    /**
     * 下个还款总金额
     */
    private Double  nextRepayMoney;
    /**
     * 下个还款的本金
     */
    private Double  nextRepayCorpus;
    /**
     * 下个还款的利息
     */
    private Double  nextRepayInterest;
    /**
     * 下个还款费用
     */
    private Double  nextRepayFee;
    /**
     * 下个还款的罚息
     */
    private Double  nextRepayDefaultInterest;

    public RepayRoadmap() {
        super();
    }

    public RepayRoadmap(Double unPaidCorpus, Double unPaidInterest, Double unPaidFee, Double unPaidDefaultInterest, Double paidCorpus, Double paidInterest, Double paidFee, Double paidDefaultInterest,
                        Date nextRepayDate, Double nextRepayCorpus, Double nextRepayInterest, Double nextRepayFee, Double nextRepayDefaultInterest, int paidPeriod, int unPaidPeriod, Boolean feePlus) {
        super();
        this.unPaidCorpus = unPaidCorpus;
        this.unPaidInterest = unPaidInterest;
        this.unPaidFee = unPaidFee;
        this.unPaidDefaultInterest = unPaidDefaultInterest;
        this.paidCorpus = paidCorpus;
        this.paidInterest = paidInterest;
        this.paidFee = paidFee;
        this.paidDefaultInterest = paidDefaultInterest;
        this.nextRepayDate = nextRepayDate;
        this.nextRepayCorpus = nextRepayCorpus;
        this.nextRepayInterest = nextRepayInterest;
        this.nextRepayFee = nextRepayFee;
        this.nextRepayDefaultInterest = nextRepayDefaultInterest;
        this.paidPeriod = paidPeriod;
        this.unPaidPeriod = unPaidPeriod;
        this.feePlus = feePlus;
    }

    public Double getNextRepayCorpus() {
        return nextRepayCorpus;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public Double getNextRepayDefaultInterest() {
        return nextRepayDefaultInterest;
    }

    public Double getNextRepayInterest() {
        return nextRepayInterest;
    }

    public Double getNextRepayMoney() {
        if (nextRepayMoney == null) {
            if (nextRepayCorpus == null) {
                return null;
            }
            if (feePlus) {
                return ArithUtil.add(ArithUtil.add(ArithUtil.add(nextRepayCorpus, nextRepayInterest), nextRepayDefaultInterest), nextRepayFee);
            } else {
                return ArithUtil.sub(ArithUtil.add(ArithUtil.add(nextRepayCorpus, nextRepayInterest), nextRepayDefaultInterest), nextRepayFee);
            }
        }
        return nextRepayMoney;
    }

    public Double getPaidCorpus() {
        return paidCorpus;
    }

    public Double getPaidDefaultInterest() {
        return paidDefaultInterest;
    }

    public Double getPaidFee() {
        return paidFee;
    }

    public Double getPaidInterest() {
        return paidInterest;
    }

    public Double getPaidMoney() {
        if (paidMoney == null) {
            if (feePlus) {
                return ArithUtil.add(ArithUtil.add(ArithUtil.add(paidCorpus, paidInterest), paidDefaultInterest), paidFee);
            } else {
                return ArithUtil.sub(ArithUtil.add(ArithUtil.add(paidCorpus, paidInterest), paidDefaultInterest), paidFee);
            }
        }
        return paidMoney;
    }

    public Integer getPaidPeriod() {
        return paidPeriod;
    }

    public Double getRepayCorpus() {
        if (repayCorpus == null) {
            return ArithUtil.add(getUnPaidCorpus(), getPaidCorpus());
        }
        return repayCorpus;
    }

    public Double getRepayDefaultInterest() {
        if (repayDefaultInterest == null) {
            return ArithUtil.add(getUnPaidDefaultInterest(), getPaidDefaultInterest());
        }
        return repayDefaultInterest;
    }

    public Double getRepayFee() {
        if (repayFee == null) {
            return ArithUtil.add(getUnPaidFee(), getPaidFee());
        }
        return repayFee;
    }

    public Double getRepayInterest() {
        if (repayInterest == null) {
            return ArithUtil.add(getUnPaidInterest(), getPaidInterest());
        }
        return repayInterest;
    }

    public Double getRepayMoney() {
        if (repayMoney == null) {
            return ArithUtil.add(getUnPaidMoney(), getPaidMoney());
        }
        return repayMoney;
    }

    /**
     * 还款总期数
     */
    public int getRepayPeriod() {
        return paidPeriod + unPaidPeriod;
    }

    public Double getUnPaidCorpus() {
        return unPaidCorpus;
    }

    public Double getUnPaidDefaultInterest() {
        return unPaidDefaultInterest;
    }

    public Double getUnPaidFee() {
        return unPaidFee;
    }

    public Double getUnPaidInterest() {
        return unPaidInterest;
    }

    public Double getUnPaidMoney() {
        if (unPaidMoney == null) {
            if (feePlus) {
                return ArithUtil.add(ArithUtil.add(ArithUtil.add(unPaidCorpus, unPaidInterest), unPaidDefaultInterest), unPaidFee);
            } else {
                return ArithUtil.sub(ArithUtil.add(ArithUtil.add(unPaidCorpus, unPaidInterest), unPaidDefaultInterest), unPaidFee);
            }
        }
        return unPaidMoney;
    }

    public Integer getUnPaidPeriod() {
        return unPaidPeriod;
    }

    public void setNextRepayCorpus(Double nextRepayCorpus) {
        this.nextRepayCorpus = nextRepayCorpus;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public void setNextRepayDefaultInterest(Double nextRepayDefaultInterest) {
        this.nextRepayDefaultInterest = nextRepayDefaultInterest;
    }

    public void setNextRepayInterest(Double nextRepayInterest) {
        this.nextRepayInterest = nextRepayInterest;
    }

    public void setNextRepayMoney(Double nextRepayMoney) {
        this.nextRepayMoney = nextRepayMoney;
    }

    public void setPaidCorpus(Double paidCorpus) {
        this.paidCorpus = paidCorpus;
    }

    public void setPaidDefaultInterest(Double paidDefaultInterest) {
        this.paidDefaultInterest = paidDefaultInterest;
    }

    public void setPaidFee(Double paidFee) {
        this.paidFee = paidFee;
    }

    public void setPaidInterest(Double paidInterest) {
        this.paidInterest = paidInterest;
    }

    public void setPaidMoney(Double paidMoney) {
        this.paidMoney = paidMoney;
    }

    public void setPaidPeriod(Integer paidPeriod) {
        this.paidPeriod = paidPeriod;
    }

    public void setRepayCorpus(Double repayCorpus) {
        this.repayCorpus = repayCorpus;
    }

    public void setRepayDefaultInterest(Double repayDefaultInterest) {
        this.repayDefaultInterest = repayDefaultInterest;
    }

    public void setRepayFee(Double repayFee) {
        this.repayFee = repayFee;
    }

    public void setRepayInterest(Double repayInterest) {
        this.repayInterest = repayInterest;
    }

    public void setRepayMoney(Double repayMoney) {
        this.repayMoney = repayMoney;
    }

    public void setUnPaidCorpus(Double unPaidCorpus) {
        this.unPaidCorpus = unPaidCorpus;
    }

    public void setUnPaidDefaultInterest(Double unPaidDefaultInterest) {
        this.unPaidDefaultInterest = unPaidDefaultInterest;
    }

    public void setUnPaidFee(Double unPaidFee) {
        this.unPaidFee = unPaidFee;
    }

    public void setUnPaidInterest(Double unPaidInterest) {
        this.unPaidInterest = unPaidInterest;
    }

    public void setUnPaidMoney(Double unPaidMoney) {
        this.unPaidMoney = unPaidMoney;
    }

    public void setUnPaidPeriod(Integer unPaidPeriod) {
        this.unPaidPeriod = unPaidPeriod;
    }

    public Double getNextRepayFee() {
        return nextRepayFee;
    }

    public void setNextRepayFee(Double nextRepayFee) {
        this.nextRepayFee = nextRepayFee;
    }
}
