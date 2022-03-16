package info.bfly.pay.bean.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SmtFundAgentBuyEntity extends UserSinaEntity{
    private static final long serialVersionUID = 5719727703034020873L;
    /**
     * 用户标识信息
     */
    @NotNull
    @Size(max = 50)
    private String agent_name;
    /**
     * 经办人身份证
     */
    @NotNull
    @Size(max = 18)
    private String license_no;
    /**
     * 证件类型
     */
    @NotNull
    @Size(max = 16)
    private String license_type_code;
    /**
     * 经办人手机号
     */
    @NotNull
    @Size(max = 32)
    private String agent_mobile;
    /**
     * 经办人邮箱
     */
    @Size(max = 32)
    @JsonProperty("email_noSign")
    private String email;

    public String getAgent_name() {
        return agent_name;
    }
    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }
    public String getLicense_no() {
        return license_no;
    }
    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }
    public String getLicense_type_code() {
        return license_type_code;
    }
    public void setLicense_type_code(String license_type_code) {
        this.license_type_code = license_type_code;
    }
    public String getAgent_mobile() {
        return agent_mobile;
    }
    public void setAgent_mobile(String agent_mobile) {
        this.agent_mobile = agent_mobile;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
