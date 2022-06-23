package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author hdy
 */
public class BindBankCardResponseEntity extends SinaInEntity {
    private static final long serialVersionUID = 7657625957992274510L;
    /**
     * 钱包系统卡ID
     */
    @NotNull
    @Size(max = 32)
    public String card_id;

    /**
     * 是否采用卡认证的方式
     * 如果之前请求中此卡采用的是卡认证的方式（verify_mode不为空），则返回Y，注意此参数和卡是否通过银行认证无关
     */
    @NotNull
    @Size(max = 1)
    public String is_verified;

    /**
     * 后续推进需要的参数
     * 如果需要推进则会返回此参数，支付推进时需要带上此参数，ticket有效期为15分钟，可以多次使用（最多5次）
     */
    @Size(max = 100)
    public String ticket;


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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

}
 