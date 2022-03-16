package info.bfly.pay.bean.user;

import info.bfly.pay.bean.BaseSinaEntity;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BindingBankCardAdvanceEntity extends BaseSinaEntity {
    /**
     * 绑卡时返回的ticket
     * ticket有效期为15分钟，可以多次使用（最多5次）
     */
    @NotNull
    @Size(max = 100)
    private String ticket;
    /**
     * 用户绑卡手机收到的验证码
     */
    @NotNull
    @Size(max = 32)
    private String valid_code;
    /**
     * 用户在商户平台操作时候的IP地址，公网IP，不是内网IP用于风控校验，请填写用户真实IP，否则容易风控拦截
     */
    @NotNull
    @Size(max = 50)
    @Value("#{refProperties['sinapay_client_ip']}")
    private String client_ip;
    /**
     * 业务扩展信息，参数格式：参数名1^参数值1|参数名2^参数值2|……
     */
    @Size(max = 200)
    public String extend_param;
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
    public String getClient_ip() {
        return client_ip;
    }
    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }
    public String getExtend_param() {
        return extend_param;
    }
    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }



}
