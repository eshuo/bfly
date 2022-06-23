package info.bfly.pay.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bfly.pay.bean.enums.RESPONSE_CODE;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by XXSun on 2017/1/11.
 */
public class SinaInEntity  implements SinaSerializable, Serializable {

    private static final long serialVersionUID = -3676084897526647739L;
    @NotNull
    @JsonFormat(pattern = "yyyyMMddHHmmss" ,timezone = "GMT+8")
    private Date          response_time;
    @NotNull
    private String        partner_id;
    @NotNull
    private String        _input_charset;
    @NotNull
    private String        sign;
    @NotNull
    private String        sign_type;

    private String        sign_version;
    @NotNull
    private RESPONSE_CODE response_code;
    @NotNull
    private String        response_message;
    @NotNull
    private String        memo;

    private String version;
    private String error_url;

    public Date getResponse_time() {
        return response_time;
    }

    public void setResponse_time(Date response_time) {
        this.response_time = response_time;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String get_input_charset() {
        return _input_charset;
    }

    public void set_input_charset(String _input_charset) {
        this._input_charset = _input_charset;
    }


    @JsonIgnore()
    public String getSign() {
        return sign;
    }

    @JsonProperty
    public void setSign(String sign) {
        this.sign = sign;
    }

    @JsonIgnore
    public String getSign_type() {
        return sign_type;
    }

    @JsonProperty
    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    @JsonIgnore
    public String getSign_version() {
        return sign_version;
    }

    @JsonProperty
    public void setSign_version(String sign_version) {
        this.sign_version = sign_version;
    }

    public RESPONSE_CODE getResponse_code() {
        return response_code;
    }

    public void setResponse_code(RESPONSE_CODE response_code) {
        this.response_code = response_code;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public String getError_url() {
        return error_url;
    }

    public void setError_url(String error_url) {
        this.error_url = error_url;
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
        return "SinaInEntity{" +
                "response_time=" + response_time +
                ", partner_id='" + partner_id + '\'' +
                ", _input_charset='" + _input_charset + '\'' +
                ", sign='" + sign + '\'' +
                ", sign_type='" + sign_type + '\'' +
                ", sign_version='" + sign_version + '\'' +
                ", response_code=" + response_code +
                ", response_message='" + response_message + '\'' +
                ", memo='" + memo + '\'' +
                ", version='" + version + '\'' +
                ", error_url='" + error_url + '\'' +
                '}';
    }
}
