package info.bfly.app.protocol.model.serializer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.crowd.orders.model.MallStageCache;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


@Service("mallStageCacheSerializer")
public class MallStageCacheSerializer extends JsonSerializer<MallStageCache>  {
    
    @Autowired
    private MallStageService mallStageService;
    
    @Override
    public void serialize(MallStageCache mallStageCache, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", mallStageCache.getMallStage().getId());
        jsonGenerator.writeStringField("mallStageCacheId", mallStageCache.getId());
        jsonGenerator.writeStringField("stageName",mallStageCache.getMallStage().getStageName());
        jsonGenerator.writeStringField("stagePicture", mallStageCache.getMallStage().getStagePicture());
        jsonGenerator.writeNumberField("stageFee", mallStageCache.getMallStage().getStageInventory().getFee()==null?-1:mallStageCache.getMallStage().getStageInventory().getFee());
        jsonGenerator.writeStringField("stageDescription", mallStageCache.getMallStage().getStageDescribe());
        jsonGenerator.writeNumberField("remainNum", mallStageCache.getMallStage().getStageInventory().getNum() == null ? -1:mallStageCache.getMallStage().getStageInventory().getNum());
        //jsonGenerator.writeNumberField("supportNum", mallStage.getStageInventory().getGoodsNum()-mallStage.getStageInventory().getNum());
        //总价
        jsonGenerator.writeNumberField("totalPrice",mallStageService.countMallTotalPrice(mallStageCache.getId(),null));
        
        jsonGenerator.writeEndObject();
        
    }

}
