package info.bfly.pay.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.RSAEncryptUtils;
import info.bfly.pay.bean.BaseSinaEntity;
import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.SinaSerializable;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by XXSun on 2017/1/10.
 */
@Singleton
@Service
public class SinaUtils {
    @Log
    static Logger log;
    public final static String EQ                  = "=";
    public final static String AND                 = "&";
    public final static String SIGN_KEY_PARAM      = "sign";
    public final static String SIGN_TYPE_KEY_PARAM = "sign_type";
    public final static String SIGN_TYPE_MD5       = "MD5";
    public final static String SIGN_TYPE_RSA       = "RSA";

    @Value("#{refProperties['sinapay_rsa_sign_public_key']}")
    private Resource RSA_Sina_PublicKey_path;
    @Value("#{refProperties['sinapay_rsa_public_key']}")
    private Resource RSA_Sina_ParamPublicKey_path;
    @Value("#{refProperties['sinapay_rsa_sign_private_key']}")
    private Resource RSA_Customer_PrivateKey_path;
    @Value("#{refProperties['sinapay_md5_sign_key']}")
    private String   MD5_Sina_Key;
    @Value("#{refProperties['sinapay_mas_url']}")
    private String masUrl = "";

    @Value("#{refProperties['sinapay_mgs_url']}")
    private String msgUrl = "";

    @Value("#{refProperties['sinapay_sign_type']}")
    private String signType;

    public <T extends BaseSinaEntity> Set<BasicNameValuePair> sign(T entity) throws UnsupportedEncodingException {
        if ("MD5".equals(signType)) {
            return signWithMD5(entity);
        }
        if ("RSA".equals(signType)) {
            return signWithRSA(entity);
        }
        return null;
    }

    /**
     * Sign ??????
     *
     * @return
     */
    public boolean checkSign(SinaInEntity sinaIn) throws UnsupportedEncodingException {
        if ("RSA".equals(sinaIn.getSign_type())) {
            return RSAEncryptUtils.getInstance(1024).checkSign(StringUtils.join(loopEntity(sinaIn), AND), sinaIn.getSign(), getKey(RSA_Sina_PublicKey_path));
        }
        return false;
    }


    public Set<BasicNameValuePair> signWithMD5(BaseSinaEntity entity) throws UnsupportedEncodingException {
        Set<BasicNameValuePair> basicNameValuePairs = loopEntity(entity);
        if (!basicNameValuePairs.isEmpty()) {
            String md5 = DigestUtils.md5Hex(StringUtils.join(basicNameValuePairs, AND) + MD5_Sina_Key);
            basicNameValuePairs.add(new BasicNameValuePair(SIGN_KEY_PARAM, md5));
            basicNameValuePairs.add(new BasicNameValuePair(SIGN_TYPE_KEY_PARAM, SIGN_TYPE_MD5));
        }
        return formatEntity(basicNameValuePairs, entity.get_input_charset());
    }

    public Set<BasicNameValuePair> signWithRSA(BaseSinaEntity entity) throws UnsupportedEncodingException {
        Set<BasicNameValuePair> basicNameValuePairs = loopEntity(entity);
        if (!basicNameValuePairs.isEmpty()) {
            String rsa = RSAEncryptUtils.getInstance(1024).sign(StringUtils.join(basicNameValuePairs, AND), getKey(RSA_Customer_PrivateKey_path));
            basicNameValuePairs.add(new BasicNameValuePair(SIGN_KEY_PARAM, rsa));
            basicNameValuePairs.add(new BasicNameValuePair(SIGN_TYPE_KEY_PARAM, SIGN_TYPE_RSA));
        }
        return formatEntity(basicNameValuePairs, entity.get_input_charset());
    }

    private Set<BasicNameValuePair> loopEntity(SinaSerializable entity) throws UnsupportedEncodingException {
        Map<String, Object> parameters = entity.toRequestParameters();
        Set<BasicNameValuePair> parameters_sort = new TreeSet<>(Comparator.comparing(o -> (o.getName())));
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            String value = getRSAValue(parameter.getKey(), parameter.getValue());
            if (value != null) {
                if (parameter.getKey().endsWith("_noSign"))

                    parameters_sort.add(new BasicNameValuePair(parameter.getKey().substring(0, parameter.getKey().length() - 7), value));
                else
                    parameters_sort.add(new BasicNameValuePair(parameter.getKey(), value));
            }
        }
        return parameters_sort;
    }

    private Set<BasicNameValuePair> formatEntity(Set<BasicNameValuePair> parameters, String charset) throws UnsupportedEncodingException {
        Set<BasicNameValuePair> reParameters = new TreeSet<BasicNameValuePair>(Comparator.comparing(o -> (o.getName())));
        for (BasicNameValuePair param : parameters) {
            reParameters.add(new BasicNameValuePair(param.getName(), URLEncoder.encode(URLEncoder.encode(param.getValue(), charset), charset)));
        }
        return reParameters;

    }

    private String getRSAValue(String key, Object value) {
        //TODO ???????????????
        value = String.valueOf(value);
        if (value != null) {
            if (key.endsWith("_noSign")) {
                return (String) value;
            }
            for (String rsa : get_need_RSA()) {
                if (rsa.equals(key))
                    return RSAEncryptUtils.getInstance(1024).encryptByPublicKey((String) value, getKey(RSA_Sina_ParamPublicKey_path));
            }
            return (String) value;
        }
        return null;
    }


    public String getKey(Resource file) {
        String str = null;
        try {
            str = FileUtils.readFileToString(file.getFile());
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        String a[] = str.split("-----");
        String str2 = a[2];
        str2 = str2.replaceAll("(\\n|\\r)", "");

        return str2;
    }

    /*
     * ??????????????????????????????????????????????????????
     */
    public String[][] get_trade_Interface_service() {
        String[][] trade_Interface_service =
                {
                        {"create_hosting_collect_trade", "????????????????????????"},
                        {"create_single_hosting_pay_trade", "????????????????????????"},
                        {"create_batch_hosting_pay_trade", "??????????????????????????????"},
                        {"pay_hosting_trade", "??????????????????"},
                        {"query_pay_result", "??????????????????"},
                        {"query_hosting_trade", "??????????????????"},
                        {"query_hosting_batch_trade", "????????????????????????"},
                        {"create_hosting_refund", "????????????"},
                        {"query_hosting_refund", "??????????????????"},
                        {"create_hosting_deposit", "????????????"},
                        {"query_hosting_deposit", "??????????????????"},
                        {"create_hosting_withdraw", "????????????"},
                        {"query_hosting_withdraw", "??????????????????"},
                        {"create_hosting_transfer", "????????????"},
                        {"advance_hosting_pay", "????????????"},
                        {"create_bid_info", "????????????"}, {"query_bid_info", "??????????????????"},
                        {"create_single_hosting_pay_to_card_trade", "????????????????????????????????????"},
                        {"create_batch_hosting_pay_to_card_trade", "????????????????????????????????????"},
                        {"finish_pre_auth_trade", "????????????"},
                        {"cancel_pre_auth_trade", "????????????"},
                        {"query_fund_yield", "5.1??????????????????????????????"}
                };
        return trade_Interface_service;
    }

    public static String[] get_need_RSA() {
        String[] array = {"real_name", "cert_no", "verify_entity", "bank_account_no", "account_name",
                "phone_no", "validity_period", "verification_value", "telephone", "email", "organization_no",
                "legal_person", "legal_person_phone", "agent_name", "license_no", "agent_mobile"};

        return array;
    }

    public <E extends BaseSinaEntity> String getRequestUrl(E e) {

        for (String[] services : get_trade_Interface_service()) {
            for (String str : services) {
                if (str.equals(e.getService())) {
                    return masUrl;// ???????????? ??????????????????????????????????????????????????????????????????
                }
            }
        }
        return msgUrl;// ???????????? ??????????????????????????????????????????????????????????????????

    }

    @Autowired
    private ObjectMapper om;

    /**
     * ????????????????????????
     *
     * @param liststr             ???????????????
     * @param separatorChars      ???????????????
     * @param classseparatorChars ???????????????
     * @param clazz               ????????????
     * @param orders              ????????????????????????
     * @param <U>
     * @return
     */
    public <U> List<U> castSinaStringtoList(final String liststr, final String separatorChars, final String classseparatorChars, Class<U> clazz, String... orders) {
        String[] classValues = StringUtils.split(liststr, separatorChars);
        List<U> returnList = new ArrayList<U>(classValues.length);
        for (String classValue : classValues) {
            U u = castSinaStringToBean(classValue, classseparatorChars, clazz, orders);
            returnList.add(u);
        }
        return returnList;
    }

    /**
     * ?????????????????????Bean??????
     *
     * @param classValue          ????????????
     * @param classseparatorChars ?????????
     * @param clazz               Bean??????
     * @param orders              ??????
     * @param <U>
     * @return
     */
    public <U> U castSinaStringToBean(String classValue, String classseparatorChars, Class<U> clazz, String... orders) {
        String[] values = StringUtils.split(classValue, classseparatorChars);
        int loopsize = values.length > orders.length ? orders.length : values.length;
        Map<String, String> properties = new HashMap<String, String>();
        for (int i = 0; i < loopsize; i++) {
            properties.put(orders[i], values[i]);
        }
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return om.convertValue(properties, clazz);
    }

    /**
     * ???????????????????????????
     *
     * @param list                ?????????????????????
     * @param separatorChars      ???????????????
     * @param classseparatorChars ???????????????
     * @param orders              ????????????
     * @param <U>
     * @return
     */
    public <U> String castListtoSinaString(final List<U> list, final String separatorChars, final String classseparatorChars, String... orders) {
        log.debug("castListtoSinaString list size {}", list.size());
        List<String> returnStr = new ArrayList<String>(list.size());
        for (U u : list) {
            String str = castBeanToSinaString(u, classseparatorChars, orders);
            returnStr.add(str);
        }
        return StringUtils.join(returnStr, separatorChars);
    }

    /**
     * ???????????????????????????
     *
     * @param u                   ?????????????????????
     * @param classseparatorChars ???????????????
     * @param orders              ????????????
     * @param <U>
     * @return
     */
    public <U> String castBeanToSinaString(final U u, final String classseparatorChars, final String... orders) {
        List<String> returnStr = new ArrayList<String>(orders.length);
        for (String order : orders) {
            try {
                String str = BeanUtils.getProperty(u, order);
                returnStr.add(str);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
                returnStr.add(StringUtils.EMPTY);
            }

        }
        return StringUtils.join(returnStr, classseparatorChars);
    }

    /**
     * ?????????Map???Json?????????????????????????????????sizeData????????????????????????
     *
     * @param json     map???Json?????????
     * @param sizeData ????????????
     * @return
     * @throws IOException
     */
    public String removeMapSizeToStr(String json, String... sizeData) throws IOException {

        Map<String, String> map = om.readValue(json, Map.class);

        for (String str : sizeData) {
            map.remove(str);
        }
        return createLinkString(map);
    }

    /**
     * ???Map?????????POST?????????URL ??????&?????????
     *
     * @param params
     * @return
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        String charset = params.get("_input_charset");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * ???????????????
     *
     * @param separator ?????????
     * @param strings
     * @return
     */
    public String concatString(final String separator, final String... strings) {

        return StringUtils.join(strings, separator);
    }

}
