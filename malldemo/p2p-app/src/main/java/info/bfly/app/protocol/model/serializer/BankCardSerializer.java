package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.p2p.bankcard.model.BankCard;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
public class BankCardSerializer extends JsonSerializer<BankCard> {

    @Override
    public void serialize(BankCard value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();

        gen.writeStringField("id", value.getId());
        gen.writeStringField("cardNo", value.getCardNo());
        gen.writeStringField("accountName", value.getAccountName());
        gen.writeStringField("bankCardType", value.getBankCardType());
        gen.writeStringField("bankNo", value.getBankNo());
        gen.writeStringField("bankProvince", value.getBankProvince());
        gen.writeStringField("bankServiceType", value.getBankServiceType());
        gen.writeStringField("bindPhone", value.getBindPhone());
        gen.writeStringField("thirdNo", value.getThirdNo());
        gen.writeStringField("safeCard", value.getSafeCard());


        gen.writeEndObject();
    }
}
