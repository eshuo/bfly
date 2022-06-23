package info.bfly.pay.bean.enums;

public enum CARD_TYPE {
	DEBIT("DEBIT", "借记"), CREDIT("CREDIT", "贷记（信用卡）");
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
	CARD_TYPE(String type_name, String type_describe) {
		this.type_name = type_name;
		this.type_describe = type_describe;
	}

}