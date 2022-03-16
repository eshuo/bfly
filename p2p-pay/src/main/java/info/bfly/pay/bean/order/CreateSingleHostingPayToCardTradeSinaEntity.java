package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 3.14    创建单笔代付到提现卡交易 on 2017/2/17 0017.
 */

public class CreateSingleHostingPayToCardTradeSinaEntity extends OrderSinaEntity implements Serializable {

    private static final long serialVersionUID = -8695711283352191979L;

    /**
     * 交易订单号
     */
    @NotNull
    private String out_trade_no;
    /**
     * 交易码
     */
    @NotNull
    private String out_trade_code;
    /**
     * 收款方式
     */
    @NotNull
    private String collect_method;
    /**
     * 金额
     */
    @NotNull
    private BigDecimal amount;
    /**
     * 摘要
     */
    @NotNull
    private String summary;
    /**
     * 到账类型
     */
    private String payto_type;
    /**
     * 标的号
     */
    @NotNull
    private String goods_id;
    /**
     * 债权变动明细列表
     */
    private String creditor_info_list;
    /**
     * 用户IP地址
     */
    @NotNull
    private String user_ip;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_trade_code() {
        return out_trade_code;
    }

    public void setOut_trade_code(String out_trade_code) {
        this.out_trade_code = out_trade_code;
    }

    public String getCollect_method() {
        return collect_method;
    }

    public void setCollect_method(String collect_method) {
        this.collect_method = collect_method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPayto_type() {
        return payto_type;
    }

    public void setPayto_type(String payto_type) {
        this.payto_type = payto_type;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getCreditor_info_list() {
        return creditor_info_list;
    }

    public void setCreditor_info_list(String creditor_info_list) {
        this.creditor_info_list = creditor_info_list;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    @Override
    public String toString() {
        return "CreateSingleHostingPayToCardTradeSinaEntity{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", out_trade_code='" + out_trade_code + '\'' +
                ", collect_method='" + collect_method + '\'' +
                ", amount='" + amount + '\'' +
                ", summary='" + summary + '\'' +
                ", payto_type='" + payto_type + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", creditor_info_list='" + creditor_info_list + '\'' +
                ", user_ip='" + user_ip + '\'' +
                "} " + super.toString();
    }
}
