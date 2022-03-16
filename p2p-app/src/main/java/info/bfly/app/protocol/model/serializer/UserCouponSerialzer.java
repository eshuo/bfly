package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.core.util.DateStyle;
import info.bfly.core.util.DateUtil;
import info.bfly.p2p.coupon.model.Coupon;
import info.bfly.p2p.coupon.model.UserCoupon;

import java.util.Date;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/18 0018.
 */
public class UserCouponSerialzer extends JsonSerializer<UserCoupon> {


    @Override
    public void serialize(UserCoupon value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {

        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("description", value.getDescription());
        gen.writeStringField("status", value.getStatus());
        gen.writeStringField("usedTime", DateToString(value.getUsedTime()));
        gen.writeStringField("deadline", DateToString(value.getDeadline()));
        Coupon coupon = value.getCoupon();
        gen.writeStringField("name", coupon.getName());
        gen.writeStringField("type", coupon.getType());
        gen.writeNumberField("money", coupon.getMoney());
        gen.writeNumberField("lowerLimitMoney", coupon.getLowerLimitMoney());
        gen.writeEndObject();
    }

    private String DateToString(Date date) {
        if (date == null)
            return "";
        return DateUtil.DateToString(date, DateStyle.YYYY_MM_DD_HH_MM_SS);
    }
}
