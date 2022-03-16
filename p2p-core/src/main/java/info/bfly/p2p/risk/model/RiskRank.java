package info.bfly.p2p.risk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 风险等级
 *
 */
@Entity
@Table(name = "risk_rank")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class RiskRank {
    private String  id;
    private String  rank;
    /**
     * 风险系数(越小，风险等级越低，越没有风险)
     */
    private Integer score;
    private String  description;

    public RiskRank() {
    }

    public RiskRank(String id, String rank, String description) {
        this.id = id;
        this.rank = rank;
        this.description = description;
    }

    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "rank", length = 20)
    public String getRank() {
        return rank;
    }

    @Column(name = "score")
    public Integer getScore() {
        return score;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
