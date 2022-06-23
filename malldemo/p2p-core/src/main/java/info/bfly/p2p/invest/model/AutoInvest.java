package info.bfly.p2p.invest.model;

import info.bfly.archer.user.model.User;
import info.bfly.core.util.ArithUtil;
import info.bfly.p2p.risk.model.RiskRank;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Description: 自动投标实体
 */
@Entity
@Table(name = "auto_invest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AutoInvest {
    private String   userId;
    private User     user;
    /**
     * 每次投标金额
     */
    private Double   investMoney;
    /**
     * 借款的最小利率
     */
    private Double   minRate;
    private Double   minRatePercent;     // 最小利率，整数。不存入数据库
    /**
     * 借款的最大利率
     */
    private Double   maxRate;
    private Double   maxRatePercent;     // 最小利率，整数。不存入数据库
    /**
     * 借款的最短时间
     */
    private Integer  minDeadline;
    /**
     * 借款的最长时间
     */
    private Integer  maxDeadline;
    /**
     * 借款的最小风险等级
     */
    private RiskRank minRiskRank;
    /**
     * 借款的最大风险等级
     */
    private RiskRank maxRiskRank;
    /**
     * 账户保留余额
     */
    private Double   remainMoney;
    /**
     * 上次自动投标时间
     */
    private Date     lastAutoInvestTime;
    private Integer  seqNum;
    /**
     * 状态（开启 关闭 ）
     */
    private String   status;

    @Column(name = "invest_money")
    public Double getInvestMoney() {
        return investMoney;
    }

    @Column(name = "last_auto_invest_time")
    public Date getLastAutoInvestTime() {
        return lastAutoInvestTime;
    }

    @Column(name = "max_dealline")
    public Integer getMaxDeadline() {
        return maxDeadline;
    }

    @Column(name = "max_rate")
    public Double getMaxRate() {
        return maxRate;
    }

    @Transient
    public Double getMaxRatePercent() {
        if (maxRatePercent == null && getMaxRate() != null) {
            return ArithUtil.round(getMaxRate() * 100, 2);
        }
        return maxRatePercent;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "max_risk_rank")
    public RiskRank getMaxRiskRank() {
        return maxRiskRank;
    }

    @Column(name = "min_deadline")
    public Integer getMinDeadline() {
        return minDeadline;
    }

    @Column(name = "min_rate")
    public Double getMinRate() {
        return minRate;
    }

    @Transient
    public Double getMinRatePercent() {
        if (minRatePercent == null && getMinRate() != null) {
            return ArithUtil.round(getMinRate() * 100, 2);
        }
        return minRatePercent;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "min_risk_rank")
    public RiskRank getMinRiskRank() {
        return minRiskRank;
    }

    @Column(name = "remain_money")
    public Double getRemainMoney() {
        return remainMoney;
    }

    @Column(name = "seq_num")
    public Integer getSeqNum() {
        return seqNum;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    @Id
    @Column(name = "user_id", unique = true, nullable = false, length = 32)
    public String getUserId() {
        return userId;
    }

    public void setInvestMoney(Double investMoney) {
        this.investMoney = investMoney;
    }

    public void setLastAutoInvestTime(Date lastAutoInvestTime) {
        this.lastAutoInvestTime = lastAutoInvestTime;
    }

    public void setMaxDeadline(Integer maxDeadline) {
        this.maxDeadline = maxDeadline;
    }

    public void setMaxRate(Double maxRate) {
        this.maxRate = maxRate;
    }

    public void setMaxRatePercent(Double maxRatePercent) {
        if (maxRatePercent != null) {
            maxRate = ArithUtil.div(maxRatePercent, 100, 4);
        }
        this.maxRatePercent = maxRatePercent;
    }

    public void setMaxRiskRank(RiskRank maxRiskRank) {
        this.maxRiskRank = maxRiskRank;
    }

    public void setMinDeadline(Integer minDeadline) {
        this.minDeadline = minDeadline;
    }

    public void setMinRate(Double minRate) {
        this.minRate = minRate;
    }

    public void setMinRatePercent(Double minRatePercent) {
        if (minRatePercent != null) {
            minRate = ArithUtil.div(minRatePercent, 100, 4);
        }
        this.minRatePercent = minRatePercent;
    }

    public void setMinRiskRank(RiskRank minRiskRank) {
        this.minRiskRank = minRiskRank;
    }

    public void setRemainMoney(Double remainMoney) {
        this.remainMoney = remainMoney;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            userId = user.getId();
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
