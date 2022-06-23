package info.bfly.app.protocol.model.serializer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.mall.service.MallStageService;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Service("mallStageData")
public class MallStageSerializer extends JsonSerializer<MallStage>  {
    
    @Autowired
    private  MallStageService   mallStageService;
    
    @Override
    public void serialize(MallStage mallStage, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", mallStage.getId());
        jsonGenerator.writeStringField("stageName", mallStage.getStageName());
        jsonGenerator.writeStringField("stagePicture", mallStage.getStagePicture());
        jsonGenerator.writeNumberField("stageFee", mallStage.getStageInventory().getFee()==null?-1:mallStage.getStageInventory().getFee());
        jsonGenerator.writeStringField("stageDescription", mallStage.getStageDescribe());
        jsonGenerator.writeNumberField("supportNum", mallStageService.supportNum(mallStage.getId()));
        jsonGenerator.writeNumberField("remainNum", mallStageService.beBoughtNum(mallStage.getId()));
        jsonGenerator.writeNumberField("totalPrice",null == mallStage.getStageInventory().getFee() ? 0: mallStage.getStageInventory().getFee());
        jsonGenerator.writeEndObject();
        
    }

}
