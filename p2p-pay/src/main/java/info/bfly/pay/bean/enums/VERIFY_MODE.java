package info.bfly.pay.bean.enums;

/**
 * Created by XXSun on 2/23/2017.
 */
public enum VERIFY_MODE {
    SIGN("SIGN","签约认证");
    private final String mode_name;
    private final String mode_describe;

    public String getMode_name() {
        return mode_name;
    }

    public String getMode_describe() {
        return mode_describe;
    }

    /**
     * @param mode_name
     * @param mode_describe
     */
    VERIFY_MODE(String mode_name, String mode_describe) {
        this.mode_name = mode_name;
        this.mode_describe = mode_describe;
    }
}
