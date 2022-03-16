package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.archer.system.controller.DictUtil;
import info.bfly.core.util.DateStyle;
import info.bfly.core.util.DateUtil;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.model.Order;
import info.bfly.p2p.statistics.controller.LoanStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service("orderDetailSerializer")
public class OrderDetailSerializer extends JsonSerializer<Order>  {
    
    @Resource
    private LoanStatistics loanStatistics;

    @Autowired
    private DictUtil dictUtil;
    
    @Override
    public void serialize(Order order, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException,
            JsonProcessingException {
        jsonGenerator.writeStartObject();
        
        jsonGenerator.writeStringField("id", order.getId());
        jsonGenerator.writeStringField("createTime", DateUtil.DateToString(order.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
        jsonGenerator.writeStringField("loanName", order.getOrderCaches().get(0).getMallStageCache().getMallStage().getLoan().getName());
        jsonGenerator.writeStringField("stageDescribe", order.getOrderCaches().get(0).getMallStageCache().getMallStage().getStageDescribe());
        jsonGenerator.writeStringField("goodsCache", this.returnGoodsCacheDescribe(order.getOrderCaches().get(0).getMallStageCache().getMallStage().getGoodsCache()));
        jsonGenerator.writeStringField("consigneeName", order.getUserAddress().getConsigneeName());
        jsonGenerator.writeStringField("consigneePhone", order.getUserAddress().getConsigneePhone());
        jsonGenerator.writeStringField("detailAddress", loanStatistics.getALLAreaName(order.getUserAddress().getAreas()[0]) + " " +order.getUserAddress().getDetailAddress());
        jsonGenerator.writeStringField("orderStatus",dictUtil.getValue("LOAN",order.getOrderStatus()));
        jsonGenerator.writeNumberField("money", null == order.getInvest().getMoney() ? 0:order.getInvest().getMoney());
        jsonGenerator.writeEndObject();
        
    }
    
    
    private String returnGoodsCacheDescribe(List<GoodsCache> list) {
        String str = "";
        for (GoodsCache goodsCache : list) {
            str = str + goodsCache.getGoods().getGoodsName() + "*"
                    + goodsCache.getNum() + " ";
        }
        return str;

    }
}
