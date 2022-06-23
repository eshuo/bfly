package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.archer.user.model.Role;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/25 0025.
 */
public class roleSerializer extends JsonSerializer<Role> {
    @Override
    public void serialize(Role value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {


        gen.writeStartObject();

        gen.writeStringField("id",value.getId());
        gen.writeStringField("name",value.getName());
        gen.writeStringField("description",value.getDescription());

        gen.writeEndObject();

    }
}
