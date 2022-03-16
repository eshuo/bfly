package info.bfly.pay.bean.enums;

/**
 * 异步通知类型
 * Created by XXSun on 2017/2/17.
 */
public enum BANK_CODE {
    ABC("ABC", "农业银行"), GNXS("GNXS", "广州市农信社"),
    BCCB("BCCB", "北京银行"), GZCB("GZCB", "广州市商业银行"),
    BJRCB("BJRCB", "北京农商行"), HCCB("HCCB", "杭州银行.."),
    BOC("BOC", "中国银行"), HKBCHINA("HKBCHINA", "汉口银行"),
    BOS("BOS", "上海银行"), HSBANK("HSBANK", "徽商银行"),
    CBHB("CBHB", "渤海银行"), HXB("HXB", "华夏银行"),
    CCB("CCB", "建设银行"), ICBC("ICBC", "工商银行"),
    CCQTGB("CCQTGB", "重庆三峡银行"), NBCB("NBCB", "宁波银行"),
    CEB("CEB", "光大银行"), NJCB("NJCB", "南京银行"),
    CIB("CIB", "兴业银行"), PSBC("PSBC", "中国邮储银行"),
    CITIC("CITIC", "中信银行"), SHRCB("SHRCB", "上海农村商业银行"),
    CMB("CMB", "招商银行"), SNXS("SNXS", "深圳农村商业银行"),
    CMBC("CMBC", "民生银行"), SPDB("SPDB", "浦东发展银行"),
    COMM("COMM", "交通银行"), SXJS("SXJS", "晋城市商业银行"),
    CSCB("CSCB", "长沙银行"), SZPAB("SZPAB", "平安银行"),
    CZB("CZB", "浙商银行"), UPOP("UPOP", "银联在线支付"),
    CZCB("CZCB", "浙江稠州商业银行"), WZCB("WZCB", "温州市商业银行"),
    GDB("GDB", "广东发展银行");

    private final String code_name;
    private final String code_describe;

    public String getCode_name() {
        return code_name;
    }

    public String getCode_describe() {
        return code_describe;
    }


    /**
     * @param code_name
     * @param code_describe
     */
    BANK_CODE(String code_name, String code_describe) {
        this.code_name = code_name;
        this.code_describe = code_describe;
    }
}
