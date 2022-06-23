package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;

/**
 * 3.19    标的信息查询响应 on 2017/2/20 0020.
 */
public class QueryBidInfoSinaInEntity extends SinaInEntity {

    /**
     * 商户标的号
     */
    @NotNull
    private String out_bid_no;

    /**
     * 新浪支付内部标的号
     */
    private String inner_bid_no;

    /**
     * 网站名称/平台名称
     */
    @NotNull
    private String web_site_name;
    /**
     * 标的名称
     */
    @NotNull
    private String bid_name;
    /**
     * 标的类型
     */
    @NotNull
    private String bid_type;
    /**
     * 发标金额
     */
    @NotNull
    private Double bid_amount;
    /**
     * 年化收益率
     */
    @NotNull
    private Double bid_year_rate;
    /**
     * 借款期限
     */
    @NotNull
    private Integer bid_duration;
    /**
     * 还款方式
     */
    @NotNull
    private String repay_type;
    /**
     * 协议类型
     */
    private String protocol_type;
    /**
     * 标的产品类型
     */
    private String bid_product_type;
    /**
     * 推荐机构
     */
    private String recommend_inst;
    /**
     * 限定最低投标份数
     */
    private Integer limit_min_bid_copys;
    /**
     * 限定每份投标金额
     */
    private Double limit_per_copy_amount;
    /**
     * 限定最多投标金额
     */
    private Double limit_max_bid_amount;
    /**
     * 限定最少投标金额
     */
    private Double limit_min_bid_amount;

    /**
     * 摘要
     */
    private String summary;
    /**
     * 标的url
     */
    private String url;
    /**
     * 标的开始时间
     */
    @NotNull
    private String begin_datebegin_date;
    /**
     * 还款期限
     */
    @NotNull
    private String term;
    /**
     * 担保方式
     */
    @NotNull
    private String guarantee_method;
    /**
     * 借款人信息列表
     */
    @NotNull
    private String borrower_info_list;

    public String getOut_bid_no() {
        return out_bid_no;
    }

    public void setOut_bid_no(String out_bid_no) {
        this.out_bid_no = out_bid_no;
    }

    public String getInner_bid_no() {
        return inner_bid_no;
    }

    public void setInner_bid_no(String inner_bid_no) {
        this.inner_bid_no = inner_bid_no;
    }

    public String getWeb_site_name() {
        return web_site_name;
    }

    public void setWeb_site_name(String web_site_name) {
        this.web_site_name = web_site_name;
    }

    public String getBid_name() {
        return bid_name;
    }

    public void setBid_name(String bid_name) {
        this.bid_name = bid_name;
    }

    public String getBid_type() {
        return bid_type;
    }

    public void setBid_type(String bid_type) {
        this.bid_type = bid_type;
    }

    public Double getBid_amount() {
        return bid_amount;
    }

    public void setBid_amount(Double bid_amount) {
        this.bid_amount = bid_amount;
    }

    public Double getBid_year_rate() {
        return bid_year_rate;
    }

    public void setBid_year_rate(Double bid_year_rate) {
        this.bid_year_rate = bid_year_rate;
    }

    public Integer getBid_duration() {
        return bid_duration;
    }

    public void setBid_duration(Integer bid_duration) {
        this.bid_duration = bid_duration;
    }

    public String getRepay_type() {
        return repay_type;
    }

    public void setRepay_type(String repay_type) {
        this.repay_type = repay_type;
    }

    public String getProtocol_type() {
        return protocol_type;
    }

    public void setProtocol_type(String protocol_type) {
        this.protocol_type = protocol_type;
    }

    public String getBid_product_type() {
        return bid_product_type;
    }

    public void setBid_product_type(String bid_product_type) {
        this.bid_product_type = bid_product_type;
    }

    public String getRecommend_inst() {
        return recommend_inst;
    }

    public void setRecommend_inst(String recommend_inst) {
        this.recommend_inst = recommend_inst;
    }

    public Integer getLimit_min_bid_copys() {
        return limit_min_bid_copys;
    }

    public void setLimit_min_bid_copys(Integer limit_min_bid_copys) {
        this.limit_min_bid_copys = limit_min_bid_copys;
    }

    public Double getLimit_per_copy_amount() {
        return limit_per_copy_amount;
    }

    public void setLimit_per_copy_amount(Double limit_per_copy_amount) {
        this.limit_per_copy_amount = limit_per_copy_amount;
    }

    public Double getLimit_max_bid_amount() {
        return limit_max_bid_amount;
    }

    public void setLimit_max_bid_amount(Double limit_max_bid_amount) {
        this.limit_max_bid_amount = limit_max_bid_amount;
    }

    public Double getLimit_min_bid_amount() {
        return limit_min_bid_amount;
    }

    public void setLimit_min_bid_amount(Double limit_min_bid_amount) {
        this.limit_min_bid_amount = limit_min_bid_amount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBegin_datebegin_date() {
        return begin_datebegin_date;
    }

    public void setBegin_datebegin_date(String begin_datebegin_date) {
        this.begin_datebegin_date = begin_datebegin_date;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGuarantee_method() {
        return guarantee_method;
    }

    public void setGuarantee_method(String guarantee_method) {
        this.guarantee_method = guarantee_method;
    }

    public String getBorrower_info_list() {
        return borrower_info_list;
    }

    public void setBorrower_info_list(String borrower_info_list) {
        this.borrower_info_list = borrower_info_list;
    }
}
