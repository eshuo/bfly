package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserBillService;
import info.bfly.core.util.DateUtil;
import info.bfly.p2p.statistics.controller.InvestStatistics;
import info.bfly.p2p.user.service.impl.RechargeStatistics;
import info.bfly.p2p.user.service.impl.WithdrawStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

@Service("userInfo")
public class UserInfoSerializer extends JsonSerializer<User> {

    @Autowired
    private UserBillService ubs;

    @Autowired
    private RechargeStatistics rechargeStatistics;

    @Autowired
    private WithdrawStatistics withdrawStatistics;

    @Autowired
    private InvestStatistics investStatistics;

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("userName", user.getUsername());
        jsonGenerator.writeStringField("photo", user.getPhoto() == null ? "" : user.getPhoto());
        jsonGenerator.writeStringField("mobileNumber", user.getMobileNumber() == null ? "" : user.getMobileNumber());
        double balance = ubs.getBalance(user.getId());
        jsonGenerator.writeNumberField("balances", balance);
        double frozenMoney = ubs.getFrozenMoney(user.getId());
        jsonGenerator.writeNumberField("frozenMoney", frozenMoney);

        jsonGenerator.writeNumberField("rechargeMoney", rechargeStatistics.getPaidRechargeMoney(user.getId()));
        jsonGenerator.writeNumberField("withdrawMoney", withdrawStatistics.getSuccessWithdrawMoney(user.getId()));
        jsonGenerator.writeNumberField("receivedInterest", investStatistics.getReceivedInterest(user.getId()));
        jsonGenerator.writeNumberField("receivableInterest", investStatistics.getReceivableInterest(user.getId(), DateUtil.getPerFirstDayOfMonth()));
        jsonGenerator.writeNumberField("receivableInterestAll", investStatistics.getReceivableInterest(user.getId()));
        jsonGenerator.writeNumberField("receivedCorpus", investStatistics.getReceivedCorpus(user.getId()));
        jsonGenerator.writeNumberField("receivableCorpus", investStatistics.getReceivableCorpus(user.getId(), DateUtil.getPerFirstDayOfMonth()));
        jsonGenerator.writeNumberField("receivableCorpusAll", investStatistics.getReceivableCorpus(user.getId()));
        jsonGenerator.writeNumberField("totalBalances", balance+frozenMoney);//总资产
        jsonGenerator.writeEndObject();
    }
}
