package info.bfly.app.protocol.model.serializer;

import java.io.IOException;

import info.bfly.archer.user.model.AreaItem;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class AreaItemSerializer extends JsonSerializer<AreaItem>  {

    
    @Override
    public void serialize(AreaItem areaItem, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("id", areaItem.getId());
        jsonGenerator.writeObjectField("value", areaItem.getValue());
        jsonGenerator.writeObjectField("parentId",areaItem.getParentId());
        jsonGenerator.writeEndObject();
        
    }

}
