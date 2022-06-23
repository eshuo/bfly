package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;

public class QueryFundAgentBuyEntity extends SinaInEntity {
    /**
     * 经办人编号
     */
    @NotNull
    public String agent_id;
    /**
     * 用户会员编号
     */
    @NotNull
    public String member_id;
    /**
     * 经办人电子邮箱
     */
    public String email;
    /**
     * 经办人姓名
     */
    public String agent_name;
    /**
     * 经办人手机号
     */
    public String agent_mobile;
    /**
     * 证件号码
     */
    public String license_no;
    /**
     * 证件类型
     */
    public String license_type_code;
    /**
     * 平台
     */
    public String platform;
    /**
     * 商户IP
     */
    public String cip;
    /**
     * 状态
     */
    public String status;
    /**
     * 创建日期
     */
    public String create_date;
    public String getAgent_id() {
        return agent_id;
    }
    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }
    public String getMember_id() {
        return member_id;
    }
    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAgent_name() {
        return agent_name;
    }
    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }
    public String getAgent_mobile() {
        return agent_mobile;
    }
    public void setAgent_mobile(String agent_mobile) {
        this.agent_mobile = agent_mobile;
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
    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public String getCip() {
        return cip;
    }
    public void setCip(String cip) {
        this.cip = cip;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCreate_date() {
        return create_date;
    }
    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

}
