package info.bfly.archer.user.controller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.io.*;
import java.util.Map;
import java.util.regex.Pattern;

public class SimpleHttpClient {
    static Logger log = LoggerFactory.getLogger(SimpleHttpClient.class);

    private static HttpMethod getGetMethod() {
        return new GetMethod("/archer/league/getleagueList");
    }

    private static HttpMethod getPostMethod() {
        PostMethod post = new PostMethod("/archer/league/getleagueList");
        NameValuePair simcard = new NameValuePair("simcard", "1330227");
        post.setRequestBody(new NameValuePair[]{simcard});
        return post;
    }

    public static String Html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            SimpleHttpClient.log.error(e.getMessage());
        }
        return textStr;// 返回文本字符串
    }

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost("localhost", 8080, "http");
        HttpMethod method = SimpleHttpClient.getGetMethod();// 使用POST方式提交数据
        client.executeMethod(method);
        InputStream stream = null;
        try {
            stream = method.getResponseBodyAsStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.debug(e.getMessage());
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            log.debug(e.getMessage());
        }
        StringBuffer buf = new StringBuffer();
        String line;
        try {
            while (null != (line = br.readLine())) {
                buf.append(line).append("\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.debug(e.getMessage());
        }
        // System.out.println(buf.toString());
//        System.out.println(SimpleHttpClient.Html2Text(URLDecoder.decode(buf.toString(), "utf-8")));
        method.releaseConnection();
    }

    public static void post(final String url, final String paramString) throws IOException {
        Runnable nable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                HttpClient client = new HttpClient();
                PostMethod method = new PostMethod(url);
                method.addParameter("url", url);
                method.addParameter("param1", paramString);
                HttpMethodParams param = method.getParams();
                param.setContentCharset("UTF-8");
                try {
                    client.executeMethod(method);
                } catch (HttpException e) {
                    // TODO Auto-generated catch block
                    log.debug(e.getMessage());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.debug(e.getMessage());
                }
                log.info(method.getStatusLine().toString());
                InputStream stream = null;
                try {
                    stream = method.getResponseBodyAsStream();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.debug(e.getMessage());
                }
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    log.debug(e.getMessage());
                }
                StringBuffer buf = new StringBuffer();
                String line;
                try {
                    while (null != (line = br.readLine())) {
                        buf.append(line).append("\n");
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.debug(e.getMessage());
                }
                // System.out.println(buf.toString());
                // 释放连接
                method.releaseConnection();
            }
        };
        Thread t1 = new Thread(nable);
        t1.start();
    }

    public void test() throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>TestBackMethod.test()");
        Map<String, String> request = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String urlString = request.get("url");
        String paramString = request.get("param1");
        log.info("url======>>" + urlString);
        log.info("paramString=======>>>>" + paramString);
        SimpleHttpClient.post(urlString, paramString);
    }
}
