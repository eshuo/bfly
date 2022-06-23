package info.bfly.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bfly.api.exception.ParameterExection;
import info.bfly.api.security.AppConstants;
import info.bfly.archer.key.ResponseMsg;
import info.bfly.core.util.RSAEncryptUtils;
import info.bfly.core.util.ThreeDES;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import static info.bfly.api.validate.Validate.*;


/**
 * 从app端收到的一次内容
 * <p>
 */

public class In {
    /**
     * 设备编号（设备唯一标识）
     */

    private String deviceId;
    /**
     * 请求编号（同一设备编号下，请求编号不能重复）
     */
    private String requestId;

    /**
     * 请求值（需3des加密，每次在客户端生成用于本次请求的解密）
     */
    private String value;

    /**
     * 3des密钥 (需要rsa加密，加密方式DESede/CBC/PKCS5Padding)
     */
    private String key;
    /**
     * 3des IV(和key在同一个请求参数里面)
     */
    private String iv;

    /**
     * 备注（原封不动返回）
     */
    private String remark;
    /**
     * MD5(deviceId+requestId+key+method+value  所有的值加密后md5，再RSA加密)
     */
    private String sign;
    /**
     * 请求时间
     */
    private long   time;

    private boolean inited = false;


    public In() {
    }

    public In(String deviceId, String requestId, String value, String key, String remark, String sign) {
        this.deviceId = deviceId;
        this.requestId = requestId;
        this.value = value;
        this.key = key;
        this.remark = remark;
        this.sign = sign;
        init();
    }

    public In(Map<String, String> param) {
        this(param.get("deviceId"), param.get("requestId"), param.get("value"), param.get("key"), param.get("remark"), param.get("sign"));

    }
    public In(HttpServletRequest req){
        this( req.getParameter("deviceId"), req.getParameter("requestId"),req.getParameter("value"),req.getParameter("key"), req.getParameter("remark"),req.getParameter("sign"));
    }

    public void init() {
        if (!inited) {
            notEmpty(this.deviceId, ResponseMsg.PARAMETER_NOT_NULL, "deviceId");
            matchesPattern(this.deviceId,".{16}",ResponseMsg.PARAMETER_INVALID,"deviceId","【长度为16位】");
            notEmpty(this.requestId, ResponseMsg.PARAMETER_NOT_NULL, "requestId");

            notEmpty(this.key, ResponseMsg.PARAMETER_NOT_NULL, "key");
            notEmpty(this.sign, ResponseMsg.PARAMETER_NOT_NULL, "sign");
            //解密签名
            this.sign = decryptSign();
            //验证签名
            isTrue(verify(), ResponseMsg.ILLEGAL_SIGN,"sign");
            //解密3DES密钥
            this.key = RSAEncryptUtils.getInstance().decryptByPrivateKey(this.key, AppConstants.Config.RSA_BASE64_PRIVATE_KEY);
            //key是由8位iv和密钥组合起来的
            this.iv = this.key.substring(0, 8);
            this.key = this.key.substring(8);
            this.value = decryptValue();
            this.inited = true;
        }
    }


    /**
     * 验证签名是否一致
     *
     * @return
     */
    public Boolean verify() {
        return DigestUtils.md5Hex(deviceId + requestId + key + value)
                .equals(sign);
    }

    public String decryptSign() {
        return RSAEncryptUtils.getInstance().decryptByPrivateKey(this.sign, AppConstants.Config.RSA_BASE64_PRIVATE_KEY);
    }

    public String decryptValue() {
        return new ThreeDES(this.key, this.iv).decode(value);
    }

    private String decryptydeskey() {
        return RSAEncryptUtils.getInstance().decryptByPrivateKey(this.key, AppConstants.Config.RSA_BASE64_PRIVATE_KEY);
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

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

    public String getKey() {
        return key;
    }

    public String getIv() {
        return iv;
    }

    public void getFinalValue() {
        init();

    }

    public <T> T getFinalValue(Class<T> entityClass) {
        init();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(getValue(), entityClass);
        } catch (IOException e) {
            throw new ParameterExection(ResponseMsg.PARSE_VALUE_ERROR,entityClass.getSimpleName());
        }
    }
}
