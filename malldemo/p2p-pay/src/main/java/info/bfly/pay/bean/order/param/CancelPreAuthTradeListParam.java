package info.bfly.pay.bean.order.param;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import info.bfly.pay.bean.order.OrderSinaEntity;
import info.bfly.pay.json.CancelPreAuthTradeListParamDeSerialize;
import info.bfly.pay.json.CancelPreAuthTradeListParamSerialize;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 3.17代收撤销交易参数 on 2017/2/20 0020.
 */
@JsonSerialize(as = CancelPreAuthTradeListParamSerialize.class)
@JsonDeserialize(as = CancelPreAuthTradeListParamDeSerialize.class)
public class CancelPreAuthTradeListParam extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 7865741420535339015L;

    /**
     * 代收撤销单笔请求号
     */
    @NotNull
    private String revoke_trade_no;

    /**
     * 代收冻结订单号
     */
    @NotNull
    private String freezes_trade_no;


    /**
     * 摘要
     */
    @NotNull
    private String summary;

    /**
     * 扩展信息
     */
    private String extend_param;


    public String getRevoke_trade_no() {
        return revoke_trade_no;
    }

    public void setRevoke_trade_no(String revoke_trade_no) {
        this.revoke_trade_no = revoke_trade_no;
    }

    public String getFreezes_trade_no() {
        return freezes_trade_no;
    }

    public void setFreezes_trade_no(String freezes_trade_no) {
        this.freezes_trade_no = freezes_trade_no;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }
}
