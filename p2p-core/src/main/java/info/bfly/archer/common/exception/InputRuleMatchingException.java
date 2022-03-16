package info.bfly.archer.common.exception;

/**
 * Description:输入规则匹配验证，如果验证出错，则抛此异常。
 */
public class InputRuleMatchingException extends Exception {
    private static final long serialVersionUID = 4428769689153956015L;

    public InputRuleMatchingException(String msg) {
        super(msg);
    }

    public InputRuleMatchingException(String msg, Throwable e) {
        super(msg, e);
    }
}
