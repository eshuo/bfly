package info.bfly.pay.bean.enums;

public enum TRADE_TYPE {
	MOBILE("MOBILE", "手机"), EMAIL("EMAIL", "邮箱");
	private final String type_name;
	private final String type_describe;

	public String getType_name() {
		return type_name;
	}

	public String getType_describe() {
		return type_describe;
	}

	/**
	 * @param type_name
	 * @param type_describe
	 */
	TRADE_TYPE(String type_name, String type_describe) {
		this.type_name = type_name;
		this.type_describe = type_describe;
	}

}