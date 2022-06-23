package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Service
public class QueryMemberInfosEntity extends SinaInEntity{
    /**
     * 会员类型
     */
    @Size(max = 1)
    public String member_type;
    /**
     * 公司名称
     */
    @NotNull
    @Size(max = 90)
    public String company_name;
    /**
     * 企业网址
     */
    @Size(max = 90)
    public String website;
    /**
     * 企业地址
     */
    @NotNull
    @Size(max = 256)
    public String address;
    /**
     * 执照号
     */
    @NotNull
    @Size(max = 50)
    public String license_no;
    /**
     * 营业执照所在地
     */
    @NotNull
    @Size(max = 256)
    public String license_address;
    /**
     * 执照过期日（营业期限）
     */
    @NotNull
    @Size(max = 32 )
    public String license_expire_date;
    /**
     * 营业范围
     */
    @NotNull
    @Size(max = 50 )
    public String business_scope;
    /**
     * 联系电话
     */
    @NotNull
    @Size(max = 20 )
    public String telephone;
    /**
     * 联系Email
     */
    @Size(max = 50 )
    public String email;
    /**
     * 组织机构代码
     */
    @Size(max = 32)
    @NotNull
    public String organization_no;
    /**
     * 企业简介
     */
    @NotNull
    @Size(max = 512)
    public String summary;
    /**
     * 企业法人
     */
    @NotNull
    @Size(max = 50)
    public String legal_person;
    /**
     * 证件号码
     */
    @NotNull
    @Size(max = 18)
    public String cert_no;
    /**
     * 证件类型
     */
    @NotNull
    @Size(max = 18)
    public String cert_type;
    /**
     * 法人手机号码
     */
    @NotNull
    @Size(max = 20)
    public String legal_person_phone;
    /**
     * 扩展信息
     * @return
     */
    @Size(max = 200)
    public String extend_param;
    public String getMember_type() {
        return member_type;
    }
    public void setMember_type(String member_type) {
        this.member_type = member_type;
    }
    public String getExtend_param() {
        return extend_param;
    }
    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }
    public String getCompany_name() {
        return company_name;
    }
    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getLicense_no() {
        return license_no;
    }
    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }
    public String getLicense_address() {
        return license_address;
    }
    public void setLicense_address(String license_address) {
        this.license_address = license_address;
    }
    public String getLicense_expire_date() {
        return license_expire_date;
    }
    public void setLicense_expire_date(String license_expire_date) {
        this.license_expire_date = license_expire_date;
    }
    public String getBusiness_scope() {
        return business_scope;
    }
    public void setBusiness_scope(String business_scope) {
        this.business_scope = business_scope;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getOrganization_no() {
        return organization_no;
    }
    public void setOrganization_no(String organization_no) {
        this.organization_no = organization_no;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getLegal_person() {
        return legal_person;
    }
    public void setLegal_person(String legal_person) {
        this.legal_person = legal_person;
    }
    public String getCert_no() {
        return cert_no;
    }
    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }
    public String getCert_type() {
        return cert_type;
    }
    public void setCert_type(String cert_type) {
        this.cert_type = cert_type;
    }
    public String getLegal_person_phone() {
        return legal_person_phone;
    }
    public void setLegal_person_phone(String legal_person_phone) {
        this.legal_person_phone = legal_person_phone;
    }


}
