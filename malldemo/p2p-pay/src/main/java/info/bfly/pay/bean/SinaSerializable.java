package info.bfly.pay.bean;

import java.util.Map;

/**
 * Created by XXSun on 5/3/2017.
 */
public interface SinaSerializable {
    Map<String, Object> toRequestParameters();
}
