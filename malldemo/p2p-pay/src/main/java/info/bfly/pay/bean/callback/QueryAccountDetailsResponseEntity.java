package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class QueryAccountDetailsResponseEntity extends SinaInEntity {
    /**
     * 收支明细列表
     */
    @Size(max = 4000)
    public String detail_list;
    /**
     * 页号
     */
    @Size(max = 5)
    public String page_no;
    /**
     * 每页大小
     */
    @Size(max = 5)
    public String page_size;
    /**
     * 总计录数
     *  本次查询返回的总记录数
     */
    public Number total_item;
    /**
     * 总收入
     * 单位为：RMB Yuan。精确到小数点后两位
     */
    public Number total_income;
    /**
     * 总支出
     * 单位为：RMB Yuan。精确到小数点后两位
     */
    public Number total_payout;
    /**
     * 总收益
     * 单位为：RMB Yuan。精确到小数点后两位。该参数属于1.1版本，即“version”中设置为1.1时，才会返回该参数。
     */
    public Number total_bonus;

    /**
     * 摘要
     */
    @NotNull
    @Size(max = 64)
    public String string1;

    /**
     * 入账时间
     */
    @NotNull
    @Size(max = 14)
    public String string2;

    /**
     * 加减方向
     */
    @NotNull
    @Size(max = 1)
    public String string3;

    /**
     * 发生额
     */
    @NotNull
    public Number string4;

    /**
     * 交易后余额
     */
    @NotNull
    public Number string5;

    /**
     * 业务类型
     */
    @Size(max = 14)
    public String string6;
    public String getDetail_list() {
        return detail_list;
    }
    public void setDetail_list(String detail_list) {
        this.detail_list = detail_list;
    }
    public String getPage_no() {
        return page_no;
    }
    public void setPage_no(String page_no) {
        this.page_no = page_no;
    }
    public String getPage_size() {
        return page_size;
    }
    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }
    public Number getTotal_item() {
        return total_item;
    }
    public void setTotal_item(Number total_item) {
        this.total_item = total_item;
    }
    public Number getTotal_income() {
        return total_income;
    }
    public void setTotal_income(Number total_income) {
        this.total_income = total_income;
    }
    public Number getTotal_payout() {
        return total_payout;
    }
    public void setTotal_payout(Number total_payout) {
        this.total_payout = total_payout;
    }
    public Number getTotal_bonus() {
        return total_bonus;
    }
    public void setTotal_bonus(Number total_bonus) {
        this.total_bonus = total_bonus;
    }



}
