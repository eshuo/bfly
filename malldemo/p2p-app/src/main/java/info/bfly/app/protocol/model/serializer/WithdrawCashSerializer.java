package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.archer.user.model.User;
import info.bfly.p2p.loan.model.WithdrawCash;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public class WithdrawCashSerializer extends JsonSerializer<WithdrawCash> {
    @Override
    public void serialize(WithdrawCash value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeStringField("withdrawId", value.getId());//提现ID
        gen.writeStringField("type", replaceNullToEmpty(value.getType()));//类型
        gen.writeStringField("money", value.getMoney() + "");//金额
        gen.writeStringField("fee", value.getFee() + "");//手续费
        gen.writeStringField("callbackTime", replaceNullToEmpty(value.getCallbackTime()));//通过时间
        gen.writeStringField("time", replaceNullToEmpty(value.getTime()));//申请时间

        User verifyUser = value.getVerifyUser();

        gen.writeStringField("verifyUser", verifyUser == null ? "" : verifyUser.getUsername());//初审人
        gen.writeStringField("verifyMessage", replaceNullToEmpty(value.getVerifyMessage()));//初审信息
        gen.writeStringField("verifyTime", replaceNullToEmpty(value.getVerifyTime()));//初审时间
        User recheckUser = value.getRecheckUser();
        gen.writeStringField("recheckUser", recheckUser == null ? "" : recheckUser.getUsername());//复审人
        gen.writeStringField("recheckMessage", replaceNullToEmpty(value.getRecheckMessage()));//复审信息
        gen.writeStringField("recheckTime", replaceNullToEmpty(value.getRecheckTime()));//复审时间

        gen.writeStringField("status", value.getStatus());//状态


        gen.writeEndObject();


    }

    private String replaceNullToEmpty(Object o) {
        return o == null ? "" : o + "";
    }

}
