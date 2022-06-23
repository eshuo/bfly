package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QueryBalanceResponseEntity extends SinaInEntity{
    /**
     * 余额/基金份额
     * 单位为：RMB Yuan。精确到小数点后两位。
     */
    @NotNull
    public Double balance;
    /**
     * 可用余额/基金份额
     * 单位为：RMB Yuan。精确到小数点后两位。
     */
    @NotNull
    public Double available_balance;
    /**
     * 存钱罐收益
     * 参数格式：昨日收益^最近一月收益^总收益。
     */
    @Size(max = 100)
    public String bonus;

    public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getAvailable_balance() {
		return available_balance;
	}
	public void setAvailable_balance(Double available_balance) {
		this.available_balance = available_balance;
	}
	public String getBonus() {
        return bonus;
    }
    public void setBonus(String bonus) {
        this.bonus = bonus;
    }


}
