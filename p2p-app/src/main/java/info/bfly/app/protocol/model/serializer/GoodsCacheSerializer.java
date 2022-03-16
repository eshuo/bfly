package info.bfly.app.protocol.model.serializer;

import java.io.IOException;

import info.bfly.crowd.orders.model.GoodsCache;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GoodsCacheSerializer extends JsonSerializer<GoodsCache>  {
    
    
    
    @Override
    public void serialize(GoodsCache goodsCache, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", goodsCache.getId());
        jsonGenerator.writeStringField("goodsPicture",goodsCache.getGoods().getGoodsPicture());
        jsonGenerator.writeStringField("description", goodsCache.getDescription());
        jsonGenerator.writeNumberField("num", goodsCache.getNum());
        jsonGenerator.writeNumberField("money",goodsCache.getMoney());

        jsonGenerator.writeEndObject();
        
    }

  

}
