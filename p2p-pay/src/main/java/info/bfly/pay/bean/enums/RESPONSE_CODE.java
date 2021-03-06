package info.bfly.pay.bean.enums;

/**
 * Created by XXSun on 2017/2/21.
 */
public enum RESPONSE_CODE {
    HTML_RESPONSE("HTML_RESPONSE", "html格式的返回数据"),
    APPLY_SUCCESS("APPLY_SUCCESS", "提交成功，存在业务响应的以业务响应状态为准"),
    AUTHORIZE_FAIL("AUTHORIZE_FAIL", "授权失败"),
    AUTH_INVALID_DATE("AUTH_INVALID_DATE", "商户该接口授权已过期"),
    ADD_VERIFY_FAIL("ADD_VERIFY_FAIL", "添加认证信息失败"),
    ADVANCE_FAILED("ADVANCE_FAILED", "支付或绑卡推进失败"),
    BANK_ACCOUNT_NOT_EXISTS("BANK_ACCOUNT_NOT_EXISTS", "银行卡信息不存在"),
    BANK_ACCOUNT_TOO_MANY("BANK_ACCOUNT_TOO_MANY", "绑定银行卡数量超限"),
    BANK_CARD_NOT_VERIFIED("BANK_CARD_NOT_VERIFIED", "银行卡未认证"),
    BANK_CODE_NOT_SUPPORT("BANK_CODE_NOT_SUPPORT", "暂不支持该银行"),
    BANK_CARD_NOT_EFFECT("BANK_CARD_NOT_EFFECT", "银行卡未生效"),
    BANK_CARD_NOT_SIGN("BANK_CARD_NOT_SIGN", "银行卡未签约"),
    BANK_INFO_VERIFY_FAILED("BANK_INFO_VERIFY_FAILED", "银行卡信息校验失败"),
    BIND_CARD_FAILED("BIND_CARD_FAILED", "绑卡失败"),
    BIZ_PENDING("BIZ_PENDING", "业务处理中，等待通知或查询"),
    BLANCE_NOT_ENOUGH("BLANCE_NOT_ENOUGH", "余额不足"),
    CONNECT_TIME_OUT("CONNECT_TIME_OUT", "连接超时"),
    CARD_TYPE_NOT_SUPPORT("CARD_TYPE_NOT_SUPPORT", "卡类型暂不支持"),
    CERT_NOT_EXIST("CERT_NOT_EXIST", "证件号不存在，请提前实名认证"),
    CERTNO_NOT_MATCHING("CERTNO_NOT_MATCHING", "证件号不匹配"),
    DUPLICATE_IDENTITY_ID("DUPLICATE_IDENTITY_ID", "用户标识信息重复"),
    DUPLICATE_OUT_FREEZE_NO("DUPLICATE_OUT_FREEZE_NO", "冻结订单号重复"),
    DUPLICATE_OUT_UNFREEZE_NO("DUPLICATE_OUT_UNFREEZE_NO", "解冻订单号重复"),
    DUPLICATE_REQUEST_NO("DUPLICATE_REQUEST_NO", "重复的请求号"),
    DUPLICATE_VERIFY("DUPLICATE_VERIFY", "会员认证信息重复"),
    FREEZE_FUND_FAIL("FREEZE_FUND_FAIL", "冻结余额失败"),
    FREEZE_FUND_PROCESSING("FREEZE_FUND_PROCESSING", "冻结余额处理中，请联系管理员"),
    GET_VERIFY_FAIL("GET_VERIFY_FAIL", "查询认证信息失败"),
    HOST_PAY_NOT_SUPPORT_REFUND("HOST_PAY_NOT_SUPPORT_REFUND", "代付交易不允许退款"),
    ILLEGAL_ACCESS_SWITCH_SYSTEM("ILLEGAL_ACCESS_SWITCH_SYSTEM", "不允许访问该类型的接口"),
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "参数校验未通过"),
    ILLEGAL_DECRYPT("ILLEGAL_DECRYPT", "解密失败，请检查加密字段"),
    ILLEGAL_INDETITY_PALTFORMTYPE("ILLEGAL_INDETITY_PALTFORMTYPE", "用户标识信息中不存在该平台标志"),
    ILLEGAL_IP_OR_DOMAIN("ILLEGAL_IP_OR_DOMAIN", "非法的商户IP或域名"),
    ILLEGAL_OUTER_TRADE_NO("ILLEGAL_OUTER_TRADE_NO", "交易订单号不存在"),
    ILLEGAL_SERVICE("ILLEGAL_SERVICE", "服务接口不存在"),
    ILLEGAL_SIGN("ILLEGAL_SIGN", "验签未通过"),
    ILLEGAL_SIGN_TYPE("ILLEGAL_SIGN_TYPE", "签名类型不正确"),
    INCORRECT_CARD_INFORMATION("INCORRECT_CARD_INFORMATION", "用户卡信息有误"),
    INSUFFICIENT_FREEZE_BALANCE("INSUFFICIENT_FREEZE_BALANCE", "超过可冻结金额"),
    INSUFFICIENT_UNFREEZE_BALANCE("INSUFFICIENT_UNFREEZE_BALANCE", "超过可解冻金额"),
    MEMBER_ID_NOT_EXIST("MEMBER_ID_NOT_EXIST", "用户不存在"),
    MEMBER_NOT_EXIST("MEMBER_NOT_EXIST", "用户标识信息不存在"),
    NO_BANK_CARD_INFO("NO_BANK_CARD_INFO", "无相关银行卡信息"),
    NO_BASIC_ACCOUNT("NO_BASIC_ACCOUNT", "用户无基本账户信息或没有激活"),
    NO_FUND_ORIG_FREEEZE_TRADE("NO_FUND_ORIG_FREEEZE_TRADE", "原冻结交易不存在"),
    NO_SUCH_MERCHANT("NO_SUCH_MERCHANT", "该商户信息不存在"),
    ORDER_NOT_EXIST("ORDER_NOT_EXIST", "订单不存在"),
    OTHER_ERROR("OTHER_ERROR", "其它错误"),
    PARAMETER_INVALID("PARAMETER_INVALID", "请求参数不合法"),
    PARTNER_ID_NOT_EXIST("PARTNER_ID_NOT_EXIST", "合作方Id不存在"),
    PAY_METHOD_NOT_SUPPORT("PAY_METHOD_NOT_SUPPORT", "支付方式不支持"),
    PAYER_INCONSISTENT("PAYER_INCONSISTENT", "订单批量支付付款人信息不一致"),
    PAYMENT_DUPLIDATE("PAYMENT_DUPLIDATE", "重复支付"),
    PAY_FAILED("PAY_FAILED", "支付失败"),
    REALNAME_CONFIRM_FAILED("REALNAME_CONFIRM_FAILED", "实名认证不通过"),
    REAL_NAME_NOT_MATCHING("REAL_NAME_NOT_MATCHING", "实名不匹配"),
    REQUEST_METHOD_NOT_VALIDATE("REQUEST_METHOD_NOT_VALIDATE", "请求方式不合法"),
    SAVING_POT_ACCOUNT_OPEN_FAILED("SAVING_POT_ACCOUNT_OPEN_FAILED", "存钱罐账户开户失败"),
    SYSTEM_ERROR("SYSTEM_ERROR", "系统内部错误"),
    TRADE_AMOUNT_MODIFIED("TRADE_AMOUNT_MODIFIED", "交易金额修改不合法"),
    TRADE_CLOSED("TRADE_CLOSED", "交易关闭"),
    TRADE_FAILED("TRADE_FAILED", "交易调用失败"),
    TRADE_NO_MATCH_ERROR("TRADE_NO_MATCH_ERROR", "交易号信息有误"),
    USER_BANK_ACCOUNT_NOT_MATCH("USER_BANK_ACCOUNT_NOT_MATCH", "用户银行卡信息不匹配"),
    VERIFY_NOT_EXIST("VERIFY_NOT_EXIST", "认证信息不存在"),
    VERIFY_BINDED_OVERRUN("VERIFY_BINDED_OVERRUN", "认证信息绑定超限"),
    BIZ_CHECK_EXCEPTION("BIZ_CHECK_EXCEPTION", "业务校验异常"),
    UPLOAD_FILE_FAIL("UPLOAD_FILE_FAIL", "上传文件失败"),
    CHECK_FILE_DIGEST_FAIL("CHECK_FILE_DIGEST_FAIL", "文件摘要验证失败"),
    ACCOUNT_NOT_EXIST("ACCOUNT_NOT_EXIST", "账户信息不存在"),
    MERCHANT_BUILD_FAIL("MERCHANT_BUILD_FAIL", "构建商户基本信息失败"),
    MERCHANT_SUBMIT_AUDIT_FAIL("MERCHANT_SUBMIT_AUDIT_FAIL", "商户审核请求失败"),
    MERCHANT_AUDIT_REQ_IS_EMPTY("MERCHANT_AUDIT_REQ_IS_EMPTY", "商户审核请求信息不合法"),
    FORBIDDEN_REPEAT_REQUEST("FORBIDDEN_REPEAT_REQUEST", "短时间内禁止重复请求"),
    AUDIT_PROCESSING("AUDIT_PROCESSING", "审核处理中，请不要重复提交"),
    AUDIT_SUCCESS("AUDIT_SUCCESS", "审核已通过，请不要重复提交"),
    AUDIT_REFUSED("AUDIT_REFUSED", "审核驳回，请重新提交信息"),
    AUDIT_NOTHING("AUDIT_NOTHING", "无任何审核信息"),
    BINDING_BANK_CARD_FAILD("BINDING_BANK_CARD_FAILD", "绑银行卡失败"),
    FILE_NOT_FOUND("FILE_NOT_FOUND", "文件不存在"),
    MERCHANT_OPEN_ACCOUNT_FAIL("MERCHANT_OPEN_ACCOUNT_FAIL", "开户失败"),
    MERCHANT_OPEN_REQ_ERROR("MERCHANT_OPEN_REQ_ERROR", "开户参数不合法"),
    ILLEGAL_PARTY_INFO("ILLEGAL_PARTY_INFO", "参与方信息不合法"),
    INSPECT_FILTER_FAIL("INSPECT_FILTER_FAIL", "鉴权未通过"),
    INSPECT_FILTER_SAFECARD_FAIL("INSPECT_FILTER_SAFECARD_FAIL", "鉴权安全卡未通过"),
    QUERY_IS_SAFECARD_SUPPORT_FAIL("QUERY_IS_SAFECARD_SUPPORT_FAIL", "查询是否安全卡支持错误"),
    VALIDATE_UNBIND_VERIFYMOBILE_FAIL("VALIDATE_UNBIND_VERIFYMOBILE_FAIL", "无权限解绑手机"),
    VERIFY_CODE_WRONG("VERIFY_CODE_WRONG", "验证码错误"),
    REQUEST_EXPIRED("REQUEST_EXPIRED", "请求过期"),
    AUTH_EXPIRED("AUTH_EXPIRED", "授权已过期"),
    INTERFACE_AUTH_EXPIRED("INTERFACE_AUTH_EXPIRED", "接口授权已过期"),
    ILLEGAL_SIGN_FILE("ILLEGAL_SIGN_FILE", "文件摘要验证未通过"),
    HOST_INSTANT_NOT_SUPPORT_REFUND("HOST_INSTANT_NOT_SUPPORT_REFUND", "即时到帐交易不允许退款"),
    BANK_ACCOUNT_OPEN_FAILED("BANK_ACCOUNT_OPEN_FAILED", "银行托管账户开户失败"),
    ADVANCE_TICKET_VALIDATE_FAIL("ADVANCE_TICKET_VALIDATE_FAIL", "ticket校验失败"),
    DELAY_TRADE_CLOSE_TIME_FAIL("DELAY_TRADE_CLOSE_TIME_FAIL", "延长交易关闭时间失败"),
    TRADE_CLOSE_FAIL("TRADE_CLOSE_FAIL", "订单关闭失败"),
    BATCH_PAY_TO_CARD_TYPE_ERROR("BATCH_PAY_TO_CARD_TYPE_ERROR", "批量代付到卡类型不一致"),
    VALIDATE_REAL_NAME_FAIL("VALIDATE_REAL_NAME_FAIL", "验证实名信息失败"),
    VALIDATE_MERCHANT_AUDIT_STATUS_FAIL("VALIDATE_MERCHANT_AUDIT_STATUS_FAIL", "验证企业用户审核状态失败"),
    DIRECT_PAYMENT_NOT_SUPPORT("DIRECT_PAYMENT_NOT_SUPPORT", "直连方式不支持"),
    ROLLBACK_WITHHOLD_FLOW_FAIL("ROLLBACK_WITHHOLD_FLOW_FAIL", "回滚委托扣款流量失败"),
    WITHHOLD_PAYMENT_FAIL("WITHHOLD_PAYMENT_FAIL", "交易支付方式不支持"),
    AUTH_EMPTY("AUTH_EMPTY", "鉴权失败，未找到对应的授权信息"),
    AUTH_QUOTA_FAIL("AUTH_QUOTA_FAIL", "鉴权失败，单笔额度超限"),
    AUTH_DAY_QUOTA_FAIL("AUTH_DAY_QUOTA_FAIL", "鉴权失败，日累计额度超限"),
    AUTH_FAIL("AUTH_FAIL", "鉴权失败"),
    RISK_REJECT("RISK_REJECT", "风控拒绝"),
    RATE_LIMIT_REJECT("RATE_LIMIT_REJECT", "请求过于频繁请重新再试"),
    OVERALL_BANK_INSPECT_FAIL("OVERALL_BANK_INSPECT_FAIL", "银行鉴权失败"),
    APPLY_FAILED("APPLY_FAILED", "文件处理提交申请失败"),
    FILE_DEAL_THREAD_ERROR("FILE_DEAL_THREAD_ERROR", "文件异步处理失败"),
    GET_TMP_FILE_PATH_ERROR("GET_TMP_FILE_PATH_ERROR", "未获取到服务器基础目录"),
    DOWN_SFTP_FILE_ERROR("DOWN_SFTP_FILE_ERROR", "下载sftp文件失败"),
    INVALID_BID_STATUS("INVALID_BID_STATUS", "标的状态无效"),
    SYNCHRONIZATION_SECURITY_INFO_FAIL("SYNCHRONIZATION_SECURITY_INFO_FAIL", "同步安全认证信息失败"),
    GET_PAYER_PARTNER_ACCOUNT_FAIL("GET_PAYER_PARTNER_ACCOUNT_FAIL", "获取付款方中间账户失败"),
    QUERY_MEMBER_IDENTITY_FAIL("QUERY_MEMBER_IDENTITY_FAIL", "查询会员认证信息失败"),
    ILLEGAL_SERVICE_MAINTAIN("ILLEGAL_SERVICE_MAINTAIN", "服务接口维护中,请留意新浪支付公告!"),
    REALNAME_CONFIRM_OPE_ERROR("REALNAME_CONFIRM_OPE_ERROR", "实名认证系统繁忙，请稍后再试"),
    UNBINDING_SECURITY_CARD_FORBIDDEN("UNBINDING_SECURITY_CARD_FORBIDDEN", "禁止解绑安全卡"),
    ILLEGAL_OUTER_REQUEST_NO("ILLEGAL_OUTER_REQUEST_NO", "原请求号不存在"),
    IDENTITY_ID_NOT_EXIST("IDENTITY_ID_NOT_EXIST", "用户标识信息不存在"),
    KEY_INVALID_DATE("KEY_INVALID_DATE", "密钥授权已过期"),
    ACCOUNT_EXIST("ACCOUNT_EXIST", "账户信息已经存在"),
    MERCHANT_SUBMIT_AGAIN("MERCHANT_SUBMIT_AGAIN", "审核中,不能重复提交"),
    UNKNOWN_AUDIT_STATUS("UNKNOWN_AUDIT_STATUS", "未知的审核状态"),
    DOWNLOAD_FILE_ERROR("DOWNLOAD_FILE_ERROR", "下载文件失败"),
    UPLOAD_FILE_ERROR("UPLOAD_FILE_ERROR", "上传文件失败"),
    REALNAME_CHECK_FAIL("REALNAME_CHECK_FAIL", "您尚未设置实名信息"),
    PAYPWD_HAS_SET("PAYPWD_HAS_SET", "您已成功设置过支付密码"),
    PAYPWD_SET_NULL("PAYPWD_SET_NULL", "您尚未设置支付密码"),
    PAYPWD_ISSET_CHECK_FAIL("PAYPWD_ISSET_CHECK_FAIL", "支付密码是否设置校验失败"),
    QUERY_OPERATORS_FAIL("QUERY_OPERATORS_FAIL", "查询操作员失败"),
    ENTERISE_AUDIT_CHECK_FAIL("ENTERISE_AUDIT_CHECK_FAIL", "未通过企业商户资质审核"),
    BASIC_ACCOUNT_NULL("BASIC_ACCOUNT_NULL", "基本户账户为空"),
    OPERATOR_NULL("OPERATOR_NULL", "未获取到操作员"),
    BANK_ACCOUNT_UPDATE_FAIL("BANK_ACCOUNT_UPDATE_FAIL", "银行卡状态更新失败"),
    BANK_ACCOUNT_OVER_LIMIT("BANK_ACCOUNT_OVER_LIMIT", "同平台银行卡可绑定身份证数超限"),
    MERCHANT_CORPORTAION_BUILD_FAIL("MERCHANT_CORPORTAION_BUILD_FAIL", "构建商户法人信息失败"),
    MERCHANT_CORPORTAION_REQ_EMPTY("MERCHANT_CORPORTAION_REQ_EMPTY", "商户法人信息不合法"),
    MERCHANT_SUBMIT_LICENSE_FAIL("MERCHANT_SUBMIT_LICENSE_FAIL", "构建企业资质信息失败"),
    MERCHANT_LICENSE_REQ_EMPTY("MERCHANT_LICENSE_REQ_EMPTY", "企业资质信息不合法"),
    CONVERT_DATA_ERROR("CONVERT_DATA_ERROR", "数据转换出现异常"),
    MERCHANT_VERIFY_FAILD("MERCHANT_VERIFY_FAILD", "邮箱认证失败"),
    MERCHANT_EMAIL_IS_VERIFIED("MERCHANT_EMAIL_IS_VERIFIED", "认证信息绑定次数超额,只能绑定一个"),
    MERCHANT_VOUCHERNO_IS_NULL("MERCHANT_VOUCHERNO_IS_NULL", "统一凭证不能为空"),
    MERCHANT_BIND_CARD_FAILD("MERCHANT_BIND_CARD_FAILD", "绑银行卡失败"),
    MERCHANT_BANK_CARD_EXITED("MERCHANT_BANK_CARD_EXITED", "银行卡已经存在"),
    MERCHANT_BIND_CARD_REQ_ERROR("MERCHANT_BIND_CARD_REQ_ERROR", "绑卡参数不合法"),
    ID_CARD_AUTH_FALSE("ID_CARD_AUTH_FALSE", "实名验证失败"),
    VALIDATE_SMS_CODE_FAIL("VALIDATE_SMS_CODE_FAIL", "验证短信验证码失败，请重新获取短信验证码"),
    APPLY_SMS_VALIDATECODE_FAIL("APPLY_SMS_VALIDATECODE_FAIL", "短信验证码发送失败"),
    VOUCHER_INFO_EMPTY("VOUCHER_INFO_EMPTY", "凭证信息不存在"),
    VOUCHER_INFO_ERROR("VOUCHER_INFO_ERROR", "凭证信息不完整"),
    UNBIND_WITHDRAW_CARD_ADV_FAIL("UNBIND_WITHDRAW_CARD_ADV_FAIL", "提现卡解绑不支持推进方式"),
    MEMBER_BANK_ACCOUNT_NOT_MATCH("MEMBER_BANK_ACCOUNT_NOT_MATCH", "银行卡会员不匹配"),
    MEMBER_LOCK_STATUS_ERROR("MEMBER_LOCK_STATUS_ERROR", "用户已被锁定,无法操作"),
    AGENT_BUY_IS_NULL("AGENT_BUY_IS_NULL", "未查到经办人信息"),
    ADD_MEMBER_IDENTITY_FAIL("ADD_MEMBER_IDENTITY_FAIL", "添加会员认证信息失败"),
    MEMBER_IDENTITY_BINDED_OVERRUN_FAIL("MEMBER_IDENTITY_BINDED_OVERRUN_FAIL", "认证信息已被其他用户绑定"),
    REMOVE_MEMBER_IDENTITY_FAIL("REMOVE_MEMBER_IDENTITY_FAIL", "删除会员认证信息失败"),
    MEMBER_IDENTITY_NOT_EXIST("MEMBER_IDENTITY_NOT_EXIST", "认证信息不存在"),
    ACCOUNT_TYPE_NOT_SUPPORT("ACCOUNT_TYPE_NOT_SUPPORT", "账户类型暂不支持");

    private final String status_name;
    private final String status_describe;

    public String getStatus_name() {
        return status_name;
    }


    public String getStatus_describe() {
        return status_describe;
    }


    /**
     * @param status_name
     * @param status_describe
     */
    RESPONSE_CODE(String status_name, String status_describe) {
        this.status_name = status_name;
        this.status_describe = status_describe;
    }
}
