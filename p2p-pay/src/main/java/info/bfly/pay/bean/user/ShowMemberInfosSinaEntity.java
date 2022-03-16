package info.bfly.pay.bean.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class ShowMemberInfosSinaEntity extends UserSinaEntity {
    private static final long serialVersionUID = 9223208981100057732L;
    /**
     * 响应方式
     */
    @NotNull
    @Size(max = 1)
    private String resp_method;
    /**
     * 默认展示页面
     */
    @Size(max = 50)
    private String default_page;
    /**
     * 隐藏页面
     */
    @Size(max = 200)
    private String hide_pages;
    /**
     * 定制化模板配置
     */
    @Size(max = 50)
    private String templet_custom;
    /**
     * 单项定制化配置
     */
    @Size(max = 1500)
    private String single_custom;
    public String getResp_method() {
        return resp_method;
    }
    public void setResp_method(String resp_method) {
        this.resp_method = resp_method;
    }
    public String getDefault_page() {
        return default_page;
    }
    public void setDefault_page(String default_page) {
        this.default_page = default_page;
    }
    public String getHide_pages() {
        return hide_pages;
    }
    public void setHide_pages(String hide_pages) {
        this.hide_pages = hide_pages;
    }
    public String getTemplet_custom() {
        return templet_custom;
    }
    public void setTemplet_custom(String templet_custom) {
        this.templet_custom = templet_custom;
    }
    public String getSingle_custom() {
        return single_custom;
    }
    public void setSingle_custom(String single_custom) {
        this.single_custom = single_custom;
    }

}
