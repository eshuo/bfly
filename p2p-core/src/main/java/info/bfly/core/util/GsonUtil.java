package info.bfly.core.util;

import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <p>
 * GSON工具类
 * </p>
 */
public class GsonUtil {
    private static Gson gson = null;

    static {
        GsonUtil.gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    /**
     * JSON字符串转为Map<String,String>
     *
     * @param json
     * @return
     */
    @SuppressWarnings("all")
    public static Map<String, String> fromJson2Map(String json) {
        return GsonUtil.gson.fromJson(json, new TypeToken<Map<String, String>>() {
            private static final long serialVersionUID = -3006593022069606890L;
        }.getType());
    }

    /**
     * 小写下划线的格式解析JSON字符串到对象
     * <p>
     * 例如 is_success->isSuccess
     * </p>
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T fromJsonUnderScoreStyle(String json, Class<T> classOfT) {
        return GsonUtil.gson.fromJson(json, classOfT);
    }

    /**
     * Map<String,String>转为JSON字符串
     *
     * @param json
     * @return
     */
    @SuppressWarnings("all")
    public static String fromMap2Json(Map<String, String> map) {
        return GsonUtil.gson.toJson(map, new TypeToken<Map<String, String>>() {
            private static final long serialVersionUID = -1512805424754755816L;
        }.getType());
    }

    /**
     * 小写下划线的格式将对象转换成JSON字符串
     *
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        return GsonUtil.gson.toJson(src);
    }
}
