package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.archer.system.controller.DictUtil;
import info.bfly.archer.user.model.UserBill;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/1/10 0010.
 */
public class UserBillListSerializer extends JsonSerializer<UserBill> {
    @Autowired
    private DictUtil dictUtil;
    @Override
    public void serialize(UserBill user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("time", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(user.getTime()));
        jsonGenerator.writeStringField("type", user.getType());
        jsonGenerator.writeStringField("typeInfo", user.getTypeInfo());
        jsonGenerator.writeStringField("typeInfoName", dictUtil.getValue("bill_operator", user.getTypeInfo()));
        jsonGenerator.writeNumberField("money", user.getMoney());
        jsonGenerator.writeNumberField("balance", user.getBalance());
        jsonGenerator.writeNumberField("frozenMoney", user.getFrozenMoney());
        jsonGenerator.writeStringField("detail", user.getDetail());
        jsonGenerator.writeEndObject();
    }
}
