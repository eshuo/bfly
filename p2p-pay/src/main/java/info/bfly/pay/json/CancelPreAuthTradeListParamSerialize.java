package info.bfly.pay.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.pay.bean.order.param.CancelPreAuthTradeListParam;

import java.io.IOException;

/**
 * Created by XXSun on 2017/2/21.
 */
public class CancelPreAuthTradeListParamSerialize extends JsonSerializer<CancelPreAuthTradeListParam>{

    @Override
    public void serialize(CancelPreAuthTradeListParam value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {

    }
}
