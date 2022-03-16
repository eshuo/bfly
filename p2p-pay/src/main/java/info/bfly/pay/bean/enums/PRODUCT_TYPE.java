package info.bfly.pay.bean.enums;

/**
 * 异步通知类型
 * Created by XXSun on 2017/2/17.
 */
public enum PRODUCT_TYPE {
	HOUSING_LOAN("HOUSING_LOAN","房贷类"),
	CAR_LOAN("CAR_LOAN","车贷类"),
	ASSIGNMENT_DEBT("ASSIGNMENT_DEBT","收益权转让类"),
	CREDIT_LOAN("CREDIT_LOAN","信用贷款类"),
	STOCK_ALLOCATION("STOCK_ALLOCATION","股票配资类"),
	BANK_ACCEPTANCE("BANK_ACCEPTANCE","银行承兑汇票"),
	COMMERCIAL_ACCEPTANCE("COMMERCIAL_ACCEPTANCE","商业承兑汇票"),
	CONSUMER_LOANS("CONSUMER_LOANS","消费贷款类"),
	SUPPLY_CHAIN_LOAN("SUPPLY_CHAIN_LOAN","供应链类"),
	BRIDGE_LOAN("BRIDGE_LOAN","过桥贷类"),
	FINANCE_LEASE("FINANCE_LEASE","融资租赁类"),
	OTHER("OTHER","其他");
	private final String type_name;
    private final String type_describe;

    public String getType_name() {
		return type_name;
	}


	public String getType_describe() {
		return type_describe;
	}




    /**
     * @param status_name
     * @param status_describe
     */
    PRODUCT_TYPE(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
