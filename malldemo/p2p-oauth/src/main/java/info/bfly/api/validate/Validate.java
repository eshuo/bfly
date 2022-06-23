package info.bfly.api.validate;

import info.bfly.api.exception.ParameterExection;
import info.bfly.archer.key.ResponseMsg;

import java.util.regex.Pattern;

/**
 * Created by XXSun on 2016/12/21.
 */
public class Validate {

    public static <T> T notNull(final T object, final ResponseMsg message, final String... values) {
        if (object == null) {
            throw new ParameterExection(message, values);
        }
        return object;
    }


    public static <T extends CharSequence> T notEmpty(final T chars, final ResponseMsg message, final String... values) {
        if (chars == null||chars.length() == 0) {
            throw new ParameterExection(message,values);
        }
        return chars;
    }

    public static void isTrue(final boolean expression, final ResponseMsg message, final String... values) {
        if (!expression) {
            throw new ParameterExection(message,values);
        }
    }
    public static void matchesPattern(final CharSequence input, final String pattern,final ResponseMsg message,final String... values) {
        // TODO when breaking BC, consider returning input
        if (!Pattern.matches(pattern, input)) {
            throw new ParameterExection(message, values);
        }
    }

}
