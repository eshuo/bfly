package info.bfly.core.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 */
public class Dom4jUtil {
    private static final byte[] UTF_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    private static final Logger log     = LoggerFactory.getLogger(Dom4jUtil.class);
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pMerBillNo", "1000000");
        map.put("pIdentNo", "10320");
        map.put("pRealName", "义军路");
    }

    /**
     * 移除UTF-8的BOM
     */
    public static String removeBom(String xml) {
        try {
            byte[] bs = xml.getBytes("UTF-8");
            if (bs[0] == Dom4jUtil.UTF_BOM[0] && bs[1] == Dom4jUtil.UTF_BOM[1] && bs[2] == Dom4jUtil.UTF_BOM[2]) {
                xml = new String(bs, 3, bs.length - 3, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            // FIXME:throw exception
            log.debug(e.getMessage());
        }
        return xml;
    }

    public static void submitForm(Map<String, Object> argMap, HttpServletResponse response, String form_url) {
        PrintWriter writer;
        response.setCharacterEncoding("utf-8");
        try {
            writer = response.getWriter();
            writer.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<title>跳转......</title>");
            writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<form action=\"" + form_url + "\" method=\"post\" id=\"frm1\">");
            for (String key : argMap.keySet()) {
                writer.println("<input type=\"hidden\" name=" + key + " value=\"" + argMap.get(key) + "\">");
            }
            writer.println("</form>");
            writer.println("<script type=\"text/javascript\">document.getElementById(\"frm1\").submit()</script>");
            writer.println("</body>");
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    // /**
    // * 递归遍历方法
    // *
    // * @param element
    // */
    // public static void getElementList(Element element) {
    // List elements = element.elements();
    // if (elements.size() == 0) {
    // //没有子元素
    // String xpath = element.getPath();
    // String value = element.getTextTrim();
    // System.out.println(value);
    // } else {
    // //有子元素
    // for (Iterator it = elements.iterator(); it.hasNext();) {
    // Element elem = (Element) it.next();
    // //递归遍历
    // getElementList(elem);
    // }
    // }
    // }

    /**
     * 将xml转成map
     *
     * @param xml
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map xmltoMap(String xml) {
        // FIXME:只有能遍历xml第一层
        Map<String, String> xmlMap = new HashMap<String, String>();
        Document doc = null;
        try {
            xml = Dom4jUtil.removeBom(xml);
            doc = DocumentHelper.parseText(xml);
            Element element = doc.getRootElement();
            Iterator iter = element.elementIterator();
            while (iter.hasNext()) {
                Element childElement = (Element) iter.next();
                xmlMap.put(childElement.getName(), childElement.getStringValue());
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return xmlMap;
    }
}
