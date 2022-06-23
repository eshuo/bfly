package info.bfly.app.protocol.model.serializer;

import java.io.IOException;

import info.bfly.archer.user.model.AreaToJson;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class AreaToJsonSerializer extends JsonSerializer<AreaToJson>  {
    

    
    @Override
    public void serialize(AreaToJson areaToJson, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("Provinces", areaToJson.getProvinces());
        jsonGenerator.writeObjectField("sCitys", areaToJson.getsCitys());
        jsonGenerator.writeObjectField("district", areaToJson.getDistrict());
        jsonGenerator.writeEndObject();
        
    }

}
