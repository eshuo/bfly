package info.bfly.pay.bean.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by XXSun on 3/1/2017.
 */
public enum OUT_TRADE_CODE {
    TO_LOAN_1(1001, "投资业务,收款"),
    TO_LOAN_2(2001, "投资业务,放款"),
    OUT_LOAN_1(1002, "还款业务,收款"),
    OUT_LOAN_2(2002, "还款业务,放款"),
    OTHER_1(1000, "其他业务,收款"),
    OTHER_2(2000, "其他业务,放款");
    private final Integer     code_name;
    private final String type_describe;

    @JsonValue
    public Integer  getCode_name() {
        return code_name;
    }

    public String getType_describe() {
        return type_describe;
    }


    /**
     * @param code_name
     * @param type_describe
     */
    OUT_TRADE_CODE(Integer  code_name, String type_describe) {
        this.code_name = code_name;
        this.type_describe = type_describe;
    }
}
