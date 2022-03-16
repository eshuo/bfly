package info.bfly.pay.bean.user;

import info.bfly.pay.bean.BaseSinaEntity;
import info.bfly.pay.bean.enums.OUT_TRADE_CODE;

import javax.validation.constraints.Size;


public class QueryMiddleAccountEntity extends BaseSinaEntity {
    private static final long serialVersionUID = 4858342086791458568L;
    /**
     * 外部业务码
     */
    @Size(max = 16)
    private OUT_TRADE_CODE out_trade_code;

    public OUT_TRADE_CODE getOut_trade_code() {
        return out_trade_code;
    }

    public void setOut_trade_code(OUT_TRADE_CODE out_trade_code) {
        this.out_trade_code = out_trade_code;
    }

}
