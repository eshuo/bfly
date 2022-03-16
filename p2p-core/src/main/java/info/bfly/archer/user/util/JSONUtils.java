package info.bfly.archer.user.util;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class JSONUtils {
    static Logger log = LoggerFactory.getLogger(JSONUtils.class);

    public static String getMessage(FieldError fieldError) {
        StringBuffer sb = new StringBuffer();
        String type = "";
        if (null != fieldError) {
            sb.append(fieldError.getField());
            sb.append("类型异常,");
            sb.append(fieldError.getDefaultMessage());
        }
        return sb.toString();
    }

    public static String getResponseStringJSON(Object obj) {
        String result = "";
        if (null != obj) {
            JSONObject jo = JSONObject.fromObject(obj);
            result = jo.toString();
            JSONUtils.log.debug(result);
        }
        return JSONUtils.transcoding(result);
    }

    public static String getResponseStringJSON1(Object obj) {
        String result = "";
        if (null != obj) {
            JSONObject jo = JSONObject.fromObject(obj);
            result = jo.toString();
        }
        return result;
    }

    public static void responseString(HttpServletResponse response, Object obj) {
        if (null != response) {
            JSONObject jo = JSONObject.fromObject(obj);
            response.setCharacterEncoding("utf-8");
            PrintWriter pw = null;
            try {
                pw = response.getWriter();
                pw.write(jo.toString());
            } catch (IOException e) {
                JSONUtils.log.error(e.getMessage(), e.fillInStackTrace());
            }
            pw.flush();
            pw.close();
        }
    }

    public static Map saveMap(Map dataMap, String status, Object message, Object object) {
        if (null == dataMap) {
            dataMap = new HashMap();
        }
        if (null != dataMap) {
            // dataMap.clear();
            dataMap.put("status", status);
            if (message instanceof FieldError) {
                dataMap.put("message", JSONUtils.getMessage((FieldError) message));
            } else if (message instanceof Throwable) {
                dataMap.put("message", ((Throwable) message).getMessage());
            } else {
                dataMap.put("message", message);
            }
            dataMap.put("data", object);
            // 如果操作失败，返回的object 可能为{},或者[]
            // if("1".equals(status)){
            //
            // dataMap.remove("data");
            // }
        }
        return dataMap;
    }

    public static String transcoding(String str) {
        try {
            JSONUtils.log.debug("=====aaaaaa========" + str);
            str = URLEncoder.encode(str.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.debug(e.getMessage());
        }
        return str;
    }

    public static String transcoding1(String str) {
        try {
            str = URLEncoder.encode(str.toString(), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            log.debug(e.getMessage());
        }
        return str;
    }
}
