package info.bfly.pay.bean.user;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class UnbindingBankCardEntity extends UserSinaEntity {
    private static final long serialVersionUID = -1256809199272071554L;
    /**
     * 卡ID钱包系统卡ID，绑卡返回的ID
     */
    @NotNull
    @Size(max= 32)
    private String card_id;

    /**
     * 是否推进方式（必须为Y）
     */
    @NotNull
    @Value("Y")
    private String advance_flag;

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getAdvance_flag() {
        return advance_flag;
    }

    public void setAdvance_flag(String advance_flag) {
        this.advance_flag = advance_flag;
    }


}
