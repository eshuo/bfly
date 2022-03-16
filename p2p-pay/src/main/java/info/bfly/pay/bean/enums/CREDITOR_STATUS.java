package info.bfly.pay.bean.enums;

/**
 * 审核结果
 * Created by XXSun on 2017/2/17.
 */
public enum CREDITOR_STATUS {
	investor_id("investor_id", "投资人标识ID"),
	investor_id_type("investor_id_type", "投资人标识类型"),
	borrower_id("borrower_id", "借款人标识ID"),
	borrower_id_type("borrower_id_type", "借款人标识类型"),
	amount("amount", "金额"),
	fund_type("fund_type", "资金类型"),
	remark("remark", "备注"),;
    private final String status_name;
    private final String status_describe;

    public String getStatus_name() {
        return status_name;
    }


    public String getStatus_describe() {
        return status_describe;
    }


    /**
     * @param status_name
     * @param status_describe
     */
    CREDITOR_STATUS(String status_name, String status_describe) {
        this.status_name = status_name;
        this.status_describe = status_describe;
    }
}
