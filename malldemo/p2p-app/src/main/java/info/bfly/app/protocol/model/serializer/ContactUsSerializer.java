package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.app.protocol.service.ApiConfigHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/10 0010.
 */
@Service("contactUs")
public class ContactUsSerializer extends JsonSerializer<Object> {

    @Autowired
    private ApiConfigHomeService apiConfigHomeService;
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("zdhtAddress",apiConfigHomeService.getConfigValue("zdhtAddress"));
        jsonGenerator.writeStringField("zdhtEmail",apiConfigHomeService.getConfigValue("zdhtEmail"));
        jsonGenerator.writeStringField("site_phone",apiConfigHomeService.getConfigValue("site_phone"));

        jsonGenerator.writeEndObject();
    }
}
