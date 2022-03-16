package info.bfly.core.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Map;


/**
 * Description: html 元素工具，例如打包form表单之类
 */
public class HtmlElementUtil {
    /**
     * 构造一个在页面加载完成自动提交的表单，可用于post表单数据（请求支付）等等。
     *
     * @param params    提交参数
     * @param actionUrl form action url
     * @param charset   mete content charset
     */
    public static String createAutoSubmitForm(Map<String, String> params, String actionUrl, String charset) {
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>跳转......</title>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charset + "\">");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<form action=\"" + actionUrl + "\" method=\"post\" id=\"frm1\" style=\"display:none;\">");
        for (String key : params.keySet()) {
            sb.append("<input type=\"hidden\" name=" + key + " value=\"" + StringEscapeUtils.escapeHtml4(params.get(key)) + "\">");
        }
        sb.append("</form>");
        sb.append("<script type=\"text/javascript\">document.getElementById(\"frm1\").submit()</script>");
        sb.append("</body>");
        return sb.toString();
    }

    /**
     * 构造一个在页面加载完成自动提交的表单，可用于post表单数据（请求支付）等等。
     *
     * @param charset mete content charset
     * @param form    html形式的表单字符串
     */
    public static String createAutoSubmitForm(String charset, String form) {
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>跳转......</title>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charset + "\">");
        sb.append("</head>");
        sb.append("<body>");
        sb.append(form);
        sb.append("<script type=\"text/javascript\">document.getElementsByTagName(\"form\")[0].submit()</script>");
        sb.append("</body>");
        return sb.toString();
    }
}
