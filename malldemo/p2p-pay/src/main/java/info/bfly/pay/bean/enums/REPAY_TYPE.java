package info.bfly.pay.bean.enums;

/**
 * 异步通知类型
 * Created by XXSun on 2017/2/17.
 */
public enum REPAY_TYPE {
	REPAY_CAPITAL_WITH_INTEREST("REPAY_CAPITAL_WITH_INTEREST","一次还本付息"),
	AVERAGE_CAPITAL("AVERAGE_CAPITAL","等额本金"),
	AVERAGE_CAPITAL_PLUS_INTERES("AVERAGE_CAPITAL_PLUS_INTERES","等额本息"),
	SCHEDULED_INTEREST_PAYMENTS_DUE("SCHEDULED_INTEREST_PAYMENTS_DUE","按期付息到期还本"),
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
    REPAY_TYPE(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
