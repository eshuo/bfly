package info.bfly.p2p.invest.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 投资榜
 *
 *
 */
@Entity
@Table(name="invest_pulished")
public class InvestPulished {
    /**
     * 个人头像
     */
    private String photo;
    private String username;
    /**
     * 投资总额
     */
    private double allInvestMoney;
    /**
     * 投资总收益
     */
    private double allPaidInterest;

    public InvestPulished(){

    }

    public InvestPulished(String photp, String username, double allInvestMoney, double allPaidInterest) {
        this.photo = photp;
        this.username = username;
        this.allInvestMoney = allInvestMoney;
        this.allPaidInterest = allPaidInterest;
    }

    public double getAllInvestMoney() {
        return allInvestMoney;
    }

    public double getAllPaidInterest() {
        return allPaidInterest;
    }

    public String getPhoto() {
        return photo;
    }
    @Id
    public String getUsername() {
        return username;
    }

    public void setAllInvestMoney(double allInvestMoney) {
        this.allInvestMoney = allInvestMoney;
    }

    public void setAllPaidInterest(double allPaidInterest) {
        this.allPaidInterest = allPaidInterest;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
