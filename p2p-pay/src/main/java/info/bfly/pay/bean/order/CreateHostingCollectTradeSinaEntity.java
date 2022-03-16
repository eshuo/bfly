package info.bfly.pay.bean.order;

import info.bfly.pay.bean.enums.OUT_TRADE_CODE;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 3.1    创建托管代收交易 on 2017/2/16 0016.
 */

public class CreateHostingCollectTradeSinaEntity extends OrderSinaEntity implements Serializable {


    private static final long serialVersionUID = -4716193263753417339L;


    /**
     * 交易订单号
     */
    @NotNull
    @Size(min = 1, max = 32)
    private String out_trade_no;

    /**
     * 外部业务码
     */
    @NotNull
    private OUT_TRADE_CODE out_trade_code;

    /**
     * 摘要
     */

    @NotNull
    @Size(min = 1, max = 64)
    private String summary;

    /**
     * 交易关闭时间
     */
    @Size(max = 8)
    private String trade_close_time;

    /**
     * 支付失败是否可以再次支付
     */
    @Pattern(regexp = "Y|N")
    private String can_repay_on_failed = "N";

    /**
     * 商户标的号
     */
    @Size(max = 64)
    private String goods_id;


    /**
     * 付款用户ID
     */
    @NotNull
    @Size(min = 1, max = 32)
    private String payer_id;

    /**
     * 付款用户ID标识类型
     */
    @NotNull
    @Size(min = 1, max = 16)
    private String payer_identity_type="UID";

    /**
     * 付款用户IP地址
     */
    @NotNull
    @Size(min = 1, max = 32)
    @Value("#{refProperties['sinapay_client_ip']}")
    private String payer_ip;

    /**
     * 支付方式
     */
    @NotNull
    @Size(min = 1, max = 1000)
    private String pay_method;

    /**
     * 代收交易类型
     */
    @Size(max = 32)
    private String collect_trade_type;


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public OUT_TRADE_CODE getOut_trade_code() {
        return out_trade_code;
    }

    public void setOut_trade_code(OUT_TRADE_CODE out_trade_code) {
        this.out_trade_code = out_trade_code;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTrade_close_time() {
        return trade_close_time;
    }

    public void setTrade_close_time(String trade_close_time) {
        this.trade_close_time = trade_close_time;
    }

    public String getCan_repay_on_failed() {
        return can_repay_on_failed;
    }

    public void setCan_repay_on_failed(String can_repay_on_failed) {
        this.can_repay_on_failed = can_repay_on_failed;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

    public String getPayer_identity_type() {
        return payer_identity_type;
    }

    public void setPayer_identity_type(String payer_identity_type) {
        this.payer_identity_type = payer_identity_type;
    }

    public String getPayer_ip() {
        return payer_ip;
    }

    public void setPayer_ip(String payer_ip) {
        this.payer_ip = payer_ip;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getCollect_trade_type() {
        return collect_trade_type;
    }

    public void setCollect_trade_type(String collect_trade_type) {
        this.collect_trade_type = collect_trade_type;
    }

    @Override
    public String toString() {
        return "CreateHostingCollectTradeSinaEntity{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", out_trade_code='" + out_trade_code + '\'' +
                ", summary='" + summary + '\'' +
                ", trade_close_time='" + trade_close_time + '\'' +
                ", can_repay_on_failed='" + can_repay_on_failed + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", payer_id='" + payer_id + '\'' +
                ", payer_identity_type='" + payer_identity_type + '\'' +
                ", payer_ip='" + payer_ip + '\'' +
                ", pay_method='" + pay_method + '\'' +
                ", collect_trade_type='" + collect_trade_type + '\'' +
                "} " + super.toString();
    }
}
