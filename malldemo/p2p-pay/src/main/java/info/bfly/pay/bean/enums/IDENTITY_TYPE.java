package info.bfly.pay.bean.enums;

public enum IDENTITY_TYPE {
        UID("UID", "商户用户ID"),
        MOBILE("MOBILE", "钱包绑定手机号"),
        EMAIL("EMAIL", "钱包绑定邮箱"),
        MEMBER_ID("MEMBER_ID", "用户在SINA支付的会员编号");
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
        IDENTITY_TYPE(String type_name, String type_describe) {
                this.type_name = type_name;
                this.type_describe = type_describe;
        }

}