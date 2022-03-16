package info.bfly.pay.bean.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class BalanceFreezeEntity extends QueryBalanceEntity {
    private static final long serialVersionUID = -3847512946488590691L;
    /**
     * 冻结订单号
     * 商户网站冻结订单号，商户内部保证唯一
     */
    @NotNull
    @Size(max = 32)
    private String out_freeze_no;
    /**
     * 金额
     * 单位为：RMB Yuan。精确到小数点后两位
     */
    @NotNull
    private Number amount;
    /**
     * 请求摘要
     */
    @NotNull
    @Size(max = 64)
    private String summary;

    public String getOut_freeze_no() {
        return out_freeze_no;
    }

    public void setOut_freeze_no(String out_freeze_no) {
        this.out_freeze_no = out_freeze_no;
    }

    public Number getAmount() {
        return amount;
    }

    public void setAmount(Number amount) {
        this.amount = amount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "BalanceFreezeEntity{" +
                "out_freeze_no='" + out_freeze_no + '\'' +
                ", amount=" + amount +
                ", summary='" + summary + '\'' +
                "} " + super.toString();
    }
}
