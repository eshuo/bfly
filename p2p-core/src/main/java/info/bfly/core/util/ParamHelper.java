
package info.bfly.core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @author yinjunlu 参数编码和解码
 */
public class ParamHelper {
    private static final String TEXT_ENCODING = "utf-8";

    /**
     * 对参数内容解码
     *
     * @param source 编码后的字符串
     * @return 解码内容
     * @throws IOException
     */
    public static String decodeParameter(final String source) throws IOException {
        if (source == null || source.equals("")) {
            return "";
        }
        return new String(Base64.getDecoder().decode(URLDecoder.decode(source, ParamHelper.TEXT_ENCODING)), ParamHelper.TEXT_ENCODING);
    }

    /**
     * 对参数内容进行编码
     *
     * @param source 原始字符串
     * @return 编码结果
     * @throws UnsupportedEncodingException
     */
    public static String encodeParameter(final String source) throws UnsupportedEncodingException {
        if (source == null || source.equals("")) {
            return "";
        }
        return URLEncoder.encode(Base64.getEncoder().encode(source.getBytes(ParamHelper.TEXT_ENCODING)).toString(), ParamHelper.TEXT_ENCODING);
    }
}
