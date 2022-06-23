package info.bfly.pay.bean.enums;

/**
 * 到账类型
 * Created by XXSun on 2017/2/17.
 */
public enum PAYTO_TYPE {
	GENERAL("GENERAL","普通"),
	FAST("FAST","快速");
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
    PAYTO_TYPE(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
