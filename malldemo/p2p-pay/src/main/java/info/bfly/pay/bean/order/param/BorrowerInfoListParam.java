package info.bfly.pay.bean.order.param;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 借款人信息 2017/2/20 0020.
 */
public class BorrowerInfoListParam implements Serializable {
    private static final long serialVersionUID = 3732556902632700453L;

    /**
     * 借款人ID标识
     */
    @NotNull
    private String     borrower_id;
    /**
     * 借款人ID标识类型
     */
    @NotNull
    private String     borrower_id_type;
    /**
     * 借款金额
     */
    @NotNull
    private BigDecimal amount;
    /**
     * 借款用途
     */
    @NotNull
    private String     purpose;
    /**
     * 借款人手机号码
     */
    @NotNull
    private String     mobile_phone;
    /**
     * 借款人固定电话
     */
    private String     telephone;

    /**
     * 借款人工作单位
     */
    private String work_company;

    /**
     * 借款人工作年限
     */
    private String work_years;
    /**
     * 借款人税后月收入
     */
    private String income;

    /**
     * 借款人学历
     */
    private String education;
    /**
     * 借款人婚否
     */
    private String marriage;
    /**
     * 借款人地址
     */
    private String address;
    /**
     * 借款人邮箱
     */
    private String email;
    /**
     * 摘要
     */
    @NotNull
    private String summary;

    /**
     * 扩展信息
     */
    private String extend_param;

    public String getBorrower_id() {
        return borrower_id;
    }

    public void setBorrower_id(String borrower_id) {
        this.borrower_id = borrower_id;
    }

    public String getBorrower_id_type() {
        return borrower_id_type;
    }

    public void setBorrower_id_type(String borrower_id_type) {
        this.borrower_id_type = borrower_id_type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWork_company() {
        return work_company;
    }

    public void setWork_company(String work_company) {
        this.work_company = work_company;
    }

    public String getWork_years() {
        return work_years;
    }

    public void setWork_years(String work_years) {
        this.work_years = work_years;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }

    public BorrowerInfoListParam(String borrower_id, String borrower_id_type, BigDecimal amount, String purpose, String mobile_phone, String telephone, String work_company,  String work_years,String income, String education, String marriage, String address, String email, String summary, String extend_param) {
        this.borrower_id = borrower_id == null ? "" : borrower_id;
        this.borrower_id_type = borrower_id_type == null ? "" : borrower_id_type;
        this.amount = amount == null ? BigDecimal.valueOf(0) : amount;
        this.purpose = purpose == null ? "" : purpose;
        this.mobile_phone = mobile_phone == null ? "" : mobile_phone;
        this.telephone = telephone == null ? "" : telephone;
        this.work_company = work_company == null ? "" : work_company;
        this.work_years = work_years == null ? "" : work_years;
        this.income = income == null ? "" : income;
        this.education = education == null ? "" : education;
        this.marriage = marriage == null ? "" : marriage;
        this.address = address == null ? "" : address;
        this.email = email == null ? "" : email;
        this.summary = summary == null ? "" : summary;
        this.extend_param = extend_param == null ? "" : extend_param;
    }


    public String getOrderStrToData(String classseparatorChars) {
        Cleaner cleaner = new Cleaner(Whitelist.none());
        Document parse = Jsoup.parse(summary);
        Document clean = cleaner.clean(parse);
        if (StringUtils.isEmpty(classseparatorChars))
            classseparatorChars = "~";
        return borrower_id + classseparatorChars + borrower_id_type + classseparatorChars + amount + classseparatorChars + purpose + classseparatorChars + mobile_phone + classseparatorChars + telephone +
                classseparatorChars + work_company + classseparatorChars + work_years + classseparatorChars + income + classseparatorChars + education + classseparatorChars + marriage +
                classseparatorChars + address + classseparatorChars + email + classseparatorChars + StringUtils.remove(clean.text(), " ") + classseparatorChars + extend_param;
    }
}
