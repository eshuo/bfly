package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BindingBankCardAdvanceResponseEntity extends SinaInEntity{
    /**
     * 钱包系统卡ID
     */
    @NotNull
    @Size(max = 32)
    public String card_id;
    /**
     * 银行卡是否已认证，Y：已认证；N：未认证
     */
    @NotNull
    @Size(max = 1)
    public String is_verified;
    public String getCard_id() {
        return card_id;
    }
    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }
    public String getIs_verified() {
        return is_verified;
    }
    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

}
