package info.bfly.core.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HttpClientUtil {
    public final static  int    SUCCESS = 1;
    public final static  int    FAIL    = 0;
    private static final Logger log     = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String getResponseBodyAsString(String url) {
        GetMethod get = new GetMethod(url);
        HttpClient client = new HttpClient();
        try {
            client.executeMethod(get);
            // System.out.println(url);
            // System.out.println( get.getResponseCharSet() );
            return get.getResponseBodyAsString();
        } catch (HttpException e) {
            log.debug(e.getMessage());
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public static String requestParametersToString(ServletRequest request) {
        StringBuffer sb = new StringBuffer();
        Map map = request.getParameterMap();
        for (Object str : map.keySet()) {
            sb.append(str);
            sb.append(":");
            sb.append(request.getParameter(str.toString()));
            sb.append("  ");
        }
        return sb.toString();
    }

    public static int saveHtmlFromRemoteSite(String url, File file) {
        if (!file.exists()) {
            // file.mkdirs();
            try {
                File temp = file.getParentFile();
                if (!temp.exists()) {
                    temp.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                log.debug(e.getMessage());
                return HttpClientUtil.FAIL;
            }
        }
        try {
            final String response = HttpClientUtil.getResponseBodyAsString(url);
            // System.out.println(response);
            // FileUtils.writeStringToFile(file, response , EncodingUtil.UTF8);
            FileUtils.writeByteArrayToFile(file, response.getBytes("utf-8"));
        } catch (IOException e) {
            log.debug(e.getMessage());
            return HttpClientUtil.FAIL;
        }
        return HttpClientUtil.SUCCESS;
    }

    public static int saveHtmlFromRemoteSite(String url, String filePath) {
        File file = new File(filePath);
        return HttpClientUtil.saveHtmlFromRemoteSite(url, file);
    }
}
