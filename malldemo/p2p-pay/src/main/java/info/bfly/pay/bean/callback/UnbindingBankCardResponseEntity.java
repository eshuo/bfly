package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.Size;

public class UnbindingBankCardResponseEntity extends SinaInEntity {
    private static final long serialVersionUID = -2662148231475265954L;
    /**
     * 后续推进需要的参数
     * 如果需要推进则会返回此参数，解绑推进时需要带上此参数，ticket有效期为15分钟
     */
    @Size(max=100)
    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

}
