package info.bfly.pay.bean.enums;

public enum IDENT_TYPE {
	IN("IN","申购"),
	OUT("OUT","赎回"),
	BONUS("BONUS","收益");

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
	IDENT_TYPE(String type_name, String type_describe) {
		this.type_name = type_name;
		this.type_describe = type_describe;
	}

}