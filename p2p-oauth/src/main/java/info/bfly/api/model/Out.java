package info.bfly.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import info.bfly.api.security.AppConstants;
import info.bfly.archer.key.ResponseMsg;
import info.bfly.core.util.RSAEncryptUtils;
import info.bfly.core.util.ThreeDES;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * 从app端收到的一次内容
 *
 * @author Administrator
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Out {
    private static final Logger log               = LoggerFactory.getLogger(Out.class);
    public final static  String DEFAULT_RESULT    = "{}";
    public final static  String DEFAULT_DEVICEID  = "0000-0000-0000-000000";
    public final static  String DEFAULT_REQUESTID = "0000";
    /**
     * 返回号码 （请求状态）
     */

    private int resultCode;

    /**
     * 返回信息 （请求处理信息）
     */
    private String resultMsg;

    private ResponseMsg message;

    /**
     * 3des密钥 (不返回)
     */
    @JsonIgnore
    private String key;

    /**
     * 3des IV (不返回)
     */
    @JsonIgnore
    private String iv;

    /**
     * 设备编号 （请求设备编号）
     */

    private String deviceId;

    /**
     * 请求编号
     */
    private String requestId;


    /**
     * 返回结果（需3des加密）
     */
    private String result;

    /**
     * 请求时候的备注
     */
    private String remark;

    /**
     * MD5(deviceId+requestId+method+valueBean) RSA 私钥加密
     */
    private String sign;

    /**
     * 返回时间
     */
    private long time;

    public Out() {
        this.deviceId = DEFAULT_DEVICEID;
        this.requestId = DEFAULT_REQUESTID;
        this.time = System.currentTimeMillis();
    }

    public Out(In in) {
        this.deviceId = in.getDeviceId();
        this.requestId = in.getRequestId();
        this.remark = in.getRemark();
        this.key = in.getKey();
        this.iv = in.getIv();
        this.result = DEFAULT_RESULT;
        Assert.notNull(this.key, "key could not be null");
        Assert.notNull(this.iv, "iv could not be null");
    }

    public Out(ResponseMsg message) {
        this();
        setResultCode(message);
    }

    public Out(ResponseMsg message, String key, String iv, String deviceId, String requestId, String result, String remark) {
        setResultCode(message);
        this.key = key;
        this.iv = iv;
        this.deviceId = deviceId;
        this.requestId = requestId;
        this.result = result;
        this.remark = remark;
        this.time = System.currentTimeMillis();
    }


    public Out(ResponseMsg message, String deviceId, String requestId, String result, String remark) {
        setResultCode(message);

        this.deviceId = deviceId;
        this.requestId = requestId;
        this.result = result;
        this.remark = remark;
        this.time = System.currentTimeMillis();
    }

    public Out(ResponseMsg message, String deviceId, String requestId, String remark) {
        this(message, deviceId, requestId, DEFAULT_RESULT, remark);
    }

    public Out(int resultCode, String resultMsg, String deviceId,
               String requestId, String result, String remark, String key, String iv) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.deviceId = deviceId;
        this.requestId = requestId;
        this.remark = remark;
        this.key = key;
        this.iv = iv;
        this.setResult(result);
        this.time = System.currentTimeMillis();

    }
    public Out(HttpServletRequest req){
        this(ResponseMsg.SUCCESS,req);
    }
    public Out(ResponseMsg message, HttpServletRequest req) {
        this(message, req.getParameter("deviceId"), req.getParameter("requestId"), req.getParameter("remark"));
    }


    public void encryptResult(String result) {
        if (result == null) this.result = DEFAULT_RESULT;
        if (StringUtils.isEmpty(this.key)) {
            //TODO 开发的时候先不base64加密
            //this.result = Base64.getEncoder().encodeToString(result.getBytes(StandardCharsets.UTF_8));

        } else
            this.result = new ThreeDES(this.key, this.iv).encode(this.result);
    }

    public void sign() {
        this.sign = RSAEncryptUtils.getInstance().sign(DigestUtils.md5Hex(deviceId + requestId + result), AppConstants.Config.RSA_BASE64_PRIVATE_KEY);
        this.time = System.currentTimeMillis();
    }

    public void encrypt() {
        encryptResult(this.result);
        if (this.resultCode == 0) {
            setResultCode(ResponseMsg.SUCCESS);
        }
        sign();
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode){
        this.resultCode = resultCode;
    }
    public void setResultCode(ResponseMsg responseMsg) {
        this.resultCode = responseMsg.getCode();
        if (AppConstants.rMsgProps.contains(ResponseMsg.SUCCESS.name())) {
            this.resultMsg = String.valueOf((AppConstants.rMsgProps.get(ResponseMsg.SUCCESS.name())));
        } else
            this.resultMsg = responseMsg.getDescription();

        this.message = responseMsg;
    }


    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public String getResult() {
        return result;
    }

    public void setResult() {
        this.setResult(DEFAULT_RESULT);
    }

    public void setResult(String result) {
        this.result = result;
        encrypt();
    }
/*
    public void setResult(Object object) {
        setResult(toJson(object));

    }*/

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setThreeDes(String key, String iv) {
        this.key = key;
        this.iv = iv;
    }


    public String toJson(Object object, SimpleModule... modules) {
        try {
            return new ObjectMapper().registerModules(modules).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    public void setResult(Object object, SimpleModule... modules) {
        setResult(toJson(object,modules));
    }
}
