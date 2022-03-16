package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QueryBankCardListEntity extends SinaInEntity{
    /**
     * 卡列表
     * 详见“卡条目参数”条目按时间倒序排列，每个条目中的参数用“^”分隔，条目与条目之间用“|”分隔。
     */
    @Size(max = 4000)
    public String card_list;

    /**
     * 卡信息ID
     */
    @NotNull
    @Size(max = 32)
    public String string1;

    /**
     * 银行编号
     */
    @NotNull
    @Size(max = 10)
    public String string2;

    /**
     * 银行卡号
     */
    @NotNull
    public String string3;

    /**
     * 户名
     */
    @NotNull
    public String string4;

    /**
     * 卡类型
     */
    @NotNull
    @Size(max = 10)
    public String string5;

    /**
     * 卡属性
     */
    @NotNull
    @Size(max = 10)
    public String string6;

    /**
     * VerifyMode是否是Sign
     */
    @NotNull
    @Size(max = 1)
    public String string7;

    /**
     * 创建时间
     */
    @NotNull
    @Size(max = 14)
    public String string8;

    /**
     * 安全卡标识
     */
    @NotNull
    @Size(max = 1)
    public String string9;

    public String getCard_list() {
        return card_list;
    }

    public void setCard_list(String card_list) {
        this.card_list = card_list;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }

    public String getString4() {
        return string4;
    }

    public void setString4(String string4) {
        this.string4 = string4;
    }

    public String getString5() {
        return string5;
    }

    public void setString5(String string5) {
        this.string5 = string5;
    }

    public String getString6() {
        return string6;
    }

    public void setString6(String string6) {
        this.string6 = string6;
    }

    public String getString7() {
        return string7;
    }

    public void setString7(String string7) {
        this.string7 = string7;
    }

    public String getString8() {
        return string8;
    }

    public void setString8(String string8) {
        this.string8 = string8;
    }

    public String getString9() {
        return string9;
    }

    public void setString9(String string9) {
        this.string9 = string9;
    }

}
