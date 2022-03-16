package info.bfly.app.protocol.model.serializer;

import java.io.IOException;

import info.bfly.crowd.mall.model.MallStage;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MallStageListSerializer extends JsonSerializer<MallStage>  {
    
    
    private int goodsCacheSize = 5;
    
    @Override
    public void serialize(MallStage mallStage, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", mallStage.getId());
        jsonGenerator.writeStringField("stageName", mallStage.getStageName());
        jsonGenerator.writeStringField("stagePicture", mallStage.getStagePicture());
        jsonGenerator.writeStringField("stageDescribe", mallStage.getStageDescribe());
        jsonGenerator.writeNumberField("stageMinFee", mallStage.getStageInventory().getMinFee() == null ? 0:mallStage.getStageInventory().getMinFee());
        
        int goodsCacheMaxSize = mallStage.getGoodsCache().size();
        jsonGenerator.writeNumberField("goodsCacheSize", getGoodsCacheSize());
        jsonGenerator.writeNumberField("goodsCacheMaxSize", goodsCacheMaxSize);
        jsonGenerator.writeNumberField("goodsCacheMaxPage", (int) Math.ceil((double) goodsCacheMaxSize / (double) getGoodsCacheSize()));
        jsonGenerator.writeObjectField("goodsCache", mallStage.getGoodsCache().subList(0, getGoodsCacheSize()>goodsCacheMaxSize?goodsCacheMaxSize:getGoodsCacheSize()));
        
        jsonGenerator.writeEndObject();
        
    }

    public int getGoodsCacheSize() {
        return goodsCacheSize;
    }

    public void setGoodsCacheSize(int goodsCacheSize) {
        this.goodsCacheSize = goodsCacheSize;
    }

}
