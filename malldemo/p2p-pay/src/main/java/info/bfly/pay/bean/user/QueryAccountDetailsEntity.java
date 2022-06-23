package info.bfly.pay.bean.user;

import info.bfly.pay.bean.enums.ACCOUNT_TYPE;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class QueryAccountDetailsEntity extends UserSinaEntity{
    private static final long serialVersionUID = -6178602689815426915L;
    /**
     * 账户类型（基本户、保证金户、存钱罐、银行账户）。默认基本户，见附录
     */
    private ACCOUNT_TYPE account_type;
    /**
     * 开始时间
     * 数字串，一共14位格式为：年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
     */
    @NotNull
    @Size(max = 14)
    private String start_time;
    /**
     * 结束时间
     * 数字串，一共14位格式为：年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]
     */
    @NotNull
    @Size(max = 14)
    private String end_time;
    /**
     * 页号
     * 页号，从1开始，默认为1
     */
    private Number page_no;
    /**
     * 每页大小
     * 每页记录数，不超过30，默认20，
     */
    private Number page_size;
 
    public ACCOUNT_TYPE getAccount_type() {
		return account_type;
	}
	public void setAccount_type(ACCOUNT_TYPE account_type) {
		this.account_type = account_type;
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
    public Number getPage_no() {
        return page_no;
    }
    public void setPage_no(Number page_no) {
        this.page_no = page_no;
    }
    public Number getPage_size() {
        return page_size;
    }
    public void setPage_size(Number page_size) {
        this.page_size = page_size;
    }


}
