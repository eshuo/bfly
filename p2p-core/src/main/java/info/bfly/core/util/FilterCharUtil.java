package info.bfly.core.util;

import info.bfly.archer.lucene.LuceneConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;



public class FilterCharUtil {
    private final static Logger        log = LoggerFactory.getLogger(FilterCharUtil.class);
    private final static StringManager sm  = StringManager.getManager(LuceneConstants.Package);

    public static String Html2Text(String inputString) {
        String htmlStr = inputString;
        String textStr = "";
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            String regEx_html = "</?[^<>]*>";
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll("");
            textStr = htmlStr;
        } catch (Exception e) {
            log.debug(e.getMessage());
            FilterCharUtil.log.error(FilterCharUtil.sm.getString("log.changeHtml.error", e));
        }
        return textStr;
    }
}
