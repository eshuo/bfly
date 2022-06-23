package info.bfly.archer.user.exception;

/**
 * 该异常表示规则验证过程中出现不匹配情况
 * 
 * @author wanghm
 *
 */
public class NotConformRuleException extends Exception {
    private static final long serialVersionUID = 4025663490526678515L;

    public NotConformRuleException(String message) {
        super(message);
    }
}
