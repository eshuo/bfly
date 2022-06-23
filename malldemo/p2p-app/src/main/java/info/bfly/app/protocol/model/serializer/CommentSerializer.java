package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.archer.comment.model.Comment;
import info.bfly.core.util.DateStyle;
import info.bfly.core.util.DateUtil;

import java.io.IOException;

/**
 * Created by XXSun on 2016/12/27.
 */
public class CommentSerializer extends JsonSerializer<Comment> {
    @Override
    public void serialize(Comment comment, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", comment.getId());
        jsonGenerator.writeStringField("body", comment.getBody());
        jsonGenerator.writeStringField("createTime", DateUtil.DateToString(comment.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
        jsonGenerator.writeStringField("userName", comment.getUserByCreator().getUsername());


        jsonGenerator.writeEndObject();

    }
}