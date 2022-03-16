package info.bfly.pay.bean.enums;

/**
 * 异步通知类型
 * Created by XXSun on 2017/2/17.
 */
public enum DIGEST_TYPE {
	MD5("MD5","MD5");
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
    DIGEST_TYPE(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
