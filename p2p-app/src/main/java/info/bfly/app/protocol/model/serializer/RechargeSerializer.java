package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.p2p.loan.model.Recharge;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public class RechargeSerializer extends JsonSerializer<Recharge> {
    @Override
    public void serialize(Recharge value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {



        gen.writeStartObject();

        gen.writeStringField("id", value.getId());//充值ID



        gen.writeStringField("time", replaceNullToEmpty(value.getTime()));//申请时间
        gen.writeStringField("actualMoney", replaceNullToEmpty(value.getActualMoney()));//到账金额
        gen.writeStringField("realMoney", replaceNullToEmpty(value.getRealMoney()));//到账金额
        gen.writeStringField("rechargeWay", replaceNullToEmpty(value.getRechargeWay()));//充值方式
        gen.writeStringField("callbackTime", replaceNullToEmpty(value.getCallbackTime()));//申请时间
        gen.writeStringField("status", replaceNullToEmpty(value.getStatus()));//状态
        gen.writeStringField("operationOrderNo", replaceNullToEmpty(value.getOperationOrderNo()));//订单编号


        gen.writeEndObject();



    }



    private String replaceNullToEmpty(Object o) {
        return o == null ? "" : o + "";
    }
}
