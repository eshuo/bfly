package info.bfly.archer.key;

public enum ResponseMsg {
    //base

    SUCCESS(200, "处理成功"),

    ERROR(500, "处理失败"),
    //parameter
    PARAMETER_ERROR(300, "参数错误"),
    ILLEGAL_SIGN(301, "%s 验证失败"),

    PARAMETER_INVALID(302, "请求参数%s不合法,请输入正确的格式%s"),
    METHOD_NOT_FOUND(303, "请求方法不存在"),
    ILLEGAL_DECRYPT(304, "解密失败,请检查加密字段"),
    PARAMETER_NOT_NULL(305, "%s 参数不能为空"),
    TOKEN_INVALID(306, "token验证失败"),
    REQUEST_UNIQUE(307, "请求Id重复"),
    PARSE_VALUE_ERROR(308, "%s字段解析错误"),
    HAVE_ERROR(309, "%s不存在"),

    //user
    USER_ERROR(400, "用户操作异常 %s"),
    LOGIN_NOT_FIND(401, "用户或密码错误"),
    REGIST_ERROR(402, "注册异常 %s"),
    PWD_ERROR(403, "密码操作异常"),
    PHONE_UP_ERROR(404, "修改手机号异常"),
    PAY_PWD_ERROR(405, "交易密码操作异常 %s"),
    FILE_UPLOAD_ERROR(406,"文件上传异常 %s"),

    //order
    INVEST_ERROR(601, "投资失败 %s"),
    WITHDRAW_ERROR(602, "提现失败 %s"),
    RECHARGE_ERROR(603, "充值失败 %s"),
    BALANCE_ERROR(604, "余额不足"),
    LOAN_STATUS_ERROR(606, "项目状态错误  %s"),
    EXCEEDDEADLINE_ERROR(607, "优惠券异常  %s"),


    //Sina Pay
    SINA_PAY_ERROR(701, "新浪操作失败  %s"),
    SINA_CERTIFICATION_ERROR(702, "新浪认证失败 %s"),
    CREATESINATRADE_ERROR(703, "创建托管代收交易失败 %s"),
    SHOWMEMBERINFOSSINA_ERROR(704, "新浪展示错误 %s"),

    //bankcard
    ADDBANKCARD_ERROR(801, "绑定银行卡操作异常 %s"),
    REMOVE_BANKCARD_ERROR(802, "解绑银行卡操作异常 %s"),

    //return
    REDIRECTURL(901, "需要跳转至返回参数链接"),
    NEED_CONTINUE_REQUEST(902, "操作成功，需要继续推进操作"),

    //system
    SMS_ERROR(501, "短信异常"),
    LOAN_NOT_FIND(503, "数据库中没找到项目"),
    OBJ_NOT_FIND(504, "对象没找到");


    private final int code;
    private final String description;

    ResponseMsg(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}