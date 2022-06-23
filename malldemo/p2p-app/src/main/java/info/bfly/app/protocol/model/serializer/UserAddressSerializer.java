package info.bfly.app.protocol.model.serializer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.bfly.crowd.user.model.UserAddress;
import info.bfly.p2p.statistics.controller.LoanStatistics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Service("userAddressSerializer")
public class UserAddressSerializer extends JsonSerializer<UserAddress>  {
    
    @Autowired
    private LoanStatistics loanStatistics;
    
    @Override
    public void serialize(UserAddress userAddress, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", userAddress.getId());
        jsonGenerator.writeStringField("area",loanStatistics.getMobileAreaId(userAddress.getAreas()[0]));
        jsonGenerator.writeStringField("detailAddress", userAddress.getDetailAddress());
        jsonGenerator.writeStringField("consigneeName",userAddress.getConsigneeName());
        jsonGenerator.writeStringField("consigneePhone",userAddress.getConsigneePhone());
        jsonGenerator.writeBooleanField("isDefault", userAddress.getIsDefault());

        jsonGenerator.writeEndObject();
        
    }

}
