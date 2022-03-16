package info.bfly.pay.bean;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by XXSun on 2017/1/11.
 */
public class BaseSinaEntity implements SinaSerializable, Serializable {
    private static final long serialVersionUID = -2139846711602889961L;
    /**
     * 参数
     */
    @NotNull
    @Value("#{refProperties['sinapay_input_charset']}")
    private String _input_charset = "UTF-8";
    /**
     * 接口名称
     */
    @NotNull
    private String service;
    /**
     * 接口版本
     */
    @NotNull
    @Value("#{refProperties['sinapay_version']}")
    private String version;
    /**
     * 请求时间
     */
    @NotNull
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "GMT+8")
    private Date request_time = new Date();
    /**
     * 合作者身份ID
     */
    @NotNull
    @Value("#{refProperties['sinapay_partner_id']}")
    private String partner_id;
    /**
     * 签名版本号
     */
    @JsonIgnore
    private String sign_version = "1.0";
    /**
     * 加密版本号
     */
    private String encrypt_version;
    /**
     * 系统异步回调通知地址
     */
    @Value("#{refProperties['sinapay_notify_url']}")
    private String notify_url;
    /**
     * 页面跳转同步返回页面路径
     */
    @Value("#{refProperties['sinapay_return_url']}")
    private String return_url;
    /**
     * 备注
     */
    private String memo;

    public String get_input_charset() {
        return _input_charset;
    }

    public void set_input_charset(String _input_charset) {
        this._input_charset = _input_charset;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Date getRequest_time() {
        return request_time;
    }

    public void setRequest_time(Date request_time) {
        this.request_time = request_time;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getSign_version() {
        return sign_version;
    }

    public void setSign_version(String sign_version) {
        this.sign_version = sign_version;
    }

    public String getEncrypt_version() {
        return encrypt_version;
    }

    public void setEncrypt_version(String encrypt_version) {
        this.encrypt_version = encrypt_version;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Map<String, Object> toRequestParameters() {
        ObjectMapper m = new ObjectMapper();
        //清除空元素
        m.setSerializationInclusion(JsonInclude.Include.NON_EMPTY).enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS).enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS
        );
        return m.convertValue(this, Map.class);

    }

    @Override
    public String toString() {

        request_time = new Date();

        return "BaseSinaEntity{" +
                "_input_charset='" + _input_charset + '\'' +
                ", service='" + service + '\'' +
                ", version='" + version + '\'' +
                ", request_time='" + request_time + '\'' +
                ", partner_id='" + partner_id + '\'' +
                ", sign_version='" + sign_version + '\'' +
                ", encrypt_version='" + encrypt_version + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", return_url='" + return_url + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }


}
