package info.bfly.core.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

/**
 * Description: java.util.Map 与之相关的工具类
 */
public class MapUtil {
    /**
     * hashMap字符串变为json
     *
     * @param str
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static String hashMapToJsonString(Map<String, String> map) {
        return new JSONObject(map).toString();
    }

    /**
     * json字符串变为hashMap
     *
     * @param str
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static Map<String, String> jsonStringToHashMap(String str) throws IOException {
        return new ObjectMapper().readValue(str, HashMap.class);
    }

    /**
     * HashMap 转换为 String
     *
     * @param map
     * @return
     * @see
     * @deprecated
     */
    @Deprecated
    public static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * 字符串变为map，字符串格式 key1=value1, key2=value2...
     *
     * @param str HashMap<String,String> toString()以后产生的字符串
     * @return
     * @see jsonStringToHashMap
     * @deprecated
     */
    @Deprecated
    public static Map<String, String> stringToHashMap(String str) {
        Map<String, String> map = new HashMap<String, String>();
        for (final String entry : str.split(",")) {
            final String key = entry.substring(0, entry.indexOf('=')).trim();
            final String value = entry.substring(entry.indexOf('=') + 1, entry.length()).trim();
            map.put(key, value);
        }
        return map;
    }
}
