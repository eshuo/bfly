package info.bfly.pay.bean.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class UnbindingBankCardAdvanceEntity extends UserSinaEntity {
    private static final long serialVersionUID = 3411882540734594953L;
    /**
     * 绑卡时返回的ticket
     * ticket有效期为15分钟，可以多次使用（最多5次）
     */
    @NotNull
    @Size(max = 100)
    private String ticket;
    /**
     * 短信验证码
     * 用户绑卡手机收到的验证码
     */
    @NotNull
    @Size(max = 32)
    private String valid_code;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getValid_code() {
        return valid_code;
    }

    public void setValid_code(String valid_code) {
        this.valid_code = valid_code;
    }
}
