package info.bfly.pay.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import info.bfly.pay.bean.order.param.CancelPreAuthTradeListParam;

import java.io.IOException;

/**
 * Created by XXSun on 2017/2/21.
 */
public class CancelPreAuthTradeListParamDeSerialize  extends JsonDeserializer<CancelPreAuthTradeListParam>{
    @Override
    public CancelPreAuthTradeListParam deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }
}
