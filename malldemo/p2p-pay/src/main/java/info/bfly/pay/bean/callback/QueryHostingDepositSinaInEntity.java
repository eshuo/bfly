package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

/**
 * 3.11    托管充值查询响应 on 2017/2/17 0017.
 */
public class QueryHostingDepositSinaInEntity extends SinaInEntity {

    /**
     * 充值明细列表
     */
    private String deposit_list;//String

    /**
     * 页号
     */
    private Integer page_no;

    /**
     * 每页大小
     */
    private Integer page_size;

    /**
     * 总记录数
     */
    private Integer total_item;

    public String getDeposit_list() {
        return deposit_list;
    }

    public void setDeposit_list(String deposit_list) {
        this.deposit_list = deposit_list;
    }

    public Integer getPage_no() {
        return page_no;
    }

    public void setPage_no(Integer page_no) {
        this.page_no = page_no;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Integer getTotal_item() {
        return total_item;
    }

    public void setTotal_item(Integer total_item) {
        this.total_item = total_item;
    }
}
