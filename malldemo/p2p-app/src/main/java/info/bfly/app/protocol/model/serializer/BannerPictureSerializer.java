package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.archer.banner.model.BannerPicture;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public class BannerPictureSerializer extends JsonSerializer<BannerPicture> {
    @Override
    public void serialize(BannerPicture bannerPicture, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", bannerPicture.getId());
        jsonGenerator.writeStringField("url", bannerPicture.getUrl());
        jsonGenerator.writeStringField("title", bannerPicture.getTitle());
        jsonGenerator.writeBooleanField("isOutSite", bannerPicture.getIsOutSite() == null ? false : bannerPicture.getIsOutSite());
        jsonGenerator.writeStringField("prcture", bannerPicture.getPicture());
        jsonGenerator.writeEndObject();
    }
}
