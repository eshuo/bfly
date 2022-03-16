package info.bfly.p2p.message.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import info.bfly.core.annotations.Log;
import info.bfly.p2p.message.exception.SmsSendErrorException;
import info.bfly.p2p.message.service.SmsService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service("SmsService")
public class SmsServiceImpl implements SmsService {
    @Log
    Logger log;
    final private Gson    gson          = new Gson();
    @Value("#{refProperties['sms_apikey']}")
    private       String  APIKEY        = "0f84c45f37c774c44a81f97262061564";
    @Value("#{refProperties['sms_user_info_api']}")
    private       String  USER_INFO_API = "https://yunpian.com/v1/user/get.json";
    @Value("#{refProperties['sms_send_sms_api']}")
    private       String  SEND_SMS_API  = "https://yunpian.com/v1/sms/send.json";
    @Value("#{refProperties['sms_encoding']}")
    private       String  ENCODING      = "UTF-8";
    @Value("#{refProperties['sms_method']}")
    private       String  METHOD        = "POST";
    @Value("#{refProperties['sms_debug']}")
    private       boolean SMSDEBUG      = true;


    public <T extends ApiResultBase> T performHttpClient(String url, String param, String method, Class<T> type) {
        T toBean = null;
        if (SMSDEBUG) {
            log.debug("send to [{}] with [{}] ", url, param);
            if (type != null) toBean = gson.fromJson("{\"code\":\"0\"}", type);
            return toBean;
        }

        HttpURLConnection httpConn = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        BufferedReader br = null;
        InputStream in = null;
        try {
            // 创建URL对象
            URL myURL = new URL(url);
            httpConn = (HttpURLConnection) myURL.openConnection();
            // 设置参数
            httpConn.setReadTimeout(10000);
            httpConn.setConnectTimeout(15000);
            httpConn.setRequestMethod(method);
            httpConn.setDoOutput(true);
            os = httpConn.getOutputStream();
            // 加入数据
            writer = new BufferedWriter(new OutputStreamWriter(os, ENCODING));
            writer.write(param);
            writer.flush();
            writer.close();
            // 获取输入流
            in = httpConn.getInputStream();
            int code = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == code) {
                br = new BufferedReader(new InputStreamReader(in));
                if (type != null) toBean = gson.fromJson(br, type);
                log.debug("accept json from url:{" + url + "} param:{" + param + "} toBean:{" + type + "} \r\n value is :{" + in + "}");
                br.close();
            }
            httpConn.disconnect();
        } catch (IOException e) {
            log.debug("connect to url:{" + url + "} failed see \r\n {" + e + "}");
            throw new SmsSendErrorException("failed to connect ", e);
        } catch (JsonParseException e) {
            log.debug("ParseJson for in:{" + in + "} error see \r\b {" + e + "}");
        } finally {
            // 关闭所有的输入输出流
            if (httpConn != null) httpConn.disconnect();
        }
        return toBean;
    }

    @Override
    public void send(String content, String mobileNumber) throws SmsSendErrorException {
        SendSmsResult result = send(content, mobileNumber, SendSmsResult.class);
        if (!result.isSuccess()) throw new SmsSendErrorException(result.getMsg());
    }

    @Override
    public <T extends ApiResultBase> T send(String content, String mobileNumber, Class<T> type) throws SmsSendErrorException {
        return performHttpClient(SEND_SMS_API, toParam(content, mobileNumber), METHOD, type);
    }

    private String toParam(String content, String mobileNumber) throws SmsSendErrorException {
        String string;
        try {
            string = "apikey=" + URLEncoder.encode(APIKEY, ENCODING) + "&mobile=" + URLEncoder.encode(mobileNumber, ENCODING) + "&text="
                    + URLEncoder.encode(content, ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new SmsSendErrorException("failed to encode param", e);
        }
        return string;
    }
}
