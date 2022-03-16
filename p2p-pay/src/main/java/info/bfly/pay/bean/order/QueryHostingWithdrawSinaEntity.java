package info.bfly.pay.bean.order;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 3.13托管提现查询 on 2017/2/17 0017.
 */

public class QueryHostingWithdrawSinaEntity extends OrderSinaEntity implements Serializable {
    private static final long serialVersionUID = 6977594176312281520L;
    /**
     * 用户标识信息
     */
    @NotNull
    private String identity_id;
    /**
     * 用户标识类型
     */
    @NotNull
    private String identity_type;
    /**
     * 账户类型
     */
    private String account_type;
    /**
     * 交易订单号
     */
    private String out_trade_no;
    /**
     * 开始时间
     */
    private String start_time;
    /**
     * 结束时间
     */
    private String end_time;
    /**
     * 页号
     */
    private Integer page_no = 1;
    /**
     * 每页大小
     */
    private Integer page_size = 20;

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public String getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(String identity_type) {
        this.identity_type = identity_type;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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

    @Override
    public String toString() {
        return "QueryHostingWithdrawSinaEntity{" +
                "identity_id='" + identity_id + '\'' +
                ", identity_type='" + identity_type + '\'' +
                ", account_type='" + account_type + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", page_no=" + page_no +
                ", page_size=" + page_size +
                "} " + super.toString();
    }
}
