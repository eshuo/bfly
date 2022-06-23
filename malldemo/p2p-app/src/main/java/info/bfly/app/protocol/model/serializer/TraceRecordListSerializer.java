package info.bfly.app.protocol.model.serializer;

import java.io.IOException;

import info.bfly.crowd.traceability.model.TraceTemplate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TraceRecordListSerializer  extends JsonSerializer<TraceTemplate>{

    @Override
    public void serialize(TraceTemplate template, JsonGenerator gen,
            SerializerProvider serializers) throws IOException,
            JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", template.getId());
        gen.writeStringField("goodsId", template.getGoods().getId());
        gen.writeStringField("goodsPicture", template.getGoods().getGoodsPicture());
        gen.writeStringField("createTime", template.getCommitTime().toString());
        gen.writeStringField("goodsName", template.getGoods().getGoodsName());
        gen.writeEndObject();
    }

}
