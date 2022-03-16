package info.bfly.app.protocol.model.request;

/**
 * Created by Administrator on 2017/4/10 0010.
 */
public class MoneyValue extends ReturnUrlValue {


    private String withdrawId;

    private String rechargeId;

    private String money;

    public String getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(String rechargeId) {
        this.rechargeId = rechargeId;
    }
}
