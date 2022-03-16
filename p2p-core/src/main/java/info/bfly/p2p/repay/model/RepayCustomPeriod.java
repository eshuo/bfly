package info.bfly.p2p.repay.model;

/**
 * Description: 自定义还款阶段
 */
public class RepayCustomPeriod {
    /**
     * 第几期
     */
    private int    period;
    /**
     * 时间长度
     */
    private int    length;
    /**
     * 本金所占总金额的比率
     */
    private double corpusRatio;

    public double getCorpusRatio() {
        return corpusRatio;
    }

    public int getLength() {
        return length;
    }

    public int getPeriod() {
        return period;
    }

    public void setCorpusRatio(double corpusRatio) {
        this.corpusRatio = corpusRatio;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
