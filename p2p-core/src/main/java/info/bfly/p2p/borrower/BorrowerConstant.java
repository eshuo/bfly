package info.bfly.p2p.borrower;

public class BorrowerConstant {
    public static class Url {
        public final static String LOANERADDITION       = "/user/loanerAdditionInfo";
        public final static String LOANERAUTHENTICATION = "/user/loanerAuthentication";
    }

    public static class Verify {
        public final static String unverified = "未审核";  // 未审核
        public final static String passed     = "审核通过";  // 审核通过
        public final static String refuse     = "审核未通过"; // 审核未通过
    }

    public final static String NO  = "0";
    public final static String YES = "1";
}
