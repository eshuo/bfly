package info.bfly.pay.bean.user;

import javax.validation.constraints.Size;


public class QueryBankCardEntity extends UserSinaEntity {
    private static final long serialVersionUID = -5138724296757285364L;
    /**
     * 卡ID钱包系统卡ID，绑卡返回的ID
     */
    @Size(max= 32)
    private String card_id;


    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }



}
