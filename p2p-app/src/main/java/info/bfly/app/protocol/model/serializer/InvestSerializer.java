package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.repay.model.RepayRoadmap;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
public class InvestSerializer extends JsonSerializer<Invest> {

    private boolean userSwith = false;

    public InvestSerializer(boolean b) {
        userSwith = b;
    }

    public InvestSerializer() {
        userSwith = false;
    }

    @Override
    public void serialize(Invest invest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("investId", invest.getId());
        jsonGenerator.writeStringField("userName", invest.getUser().getUsername());
        jsonGenerator.writeNumberField("investMoney", invest.getInvestMoney());
        jsonGenerator.writeStringField("time", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(invest.getTime()));
        jsonGenerator.writeStringField("status", invest.getStatus());

        if (userSwith) {
            /**
             * 还款路标值
             */
            jsonGenerator.writeStringField("loanName", invest.getLoan().getName());
            jsonGenerator.writeNumberField("ratePercent", getParamData(invest.getRatePercent()));
            jsonGenerator.writeStringField("interestBeginTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(invest.getLoan().getInterestBeginTime()));
            jsonGenerator.writeStringField("completeTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(invest.getLoan().getCommitTime()));
            RepayRoadmap repayRoadmap = invest.getRepayRoadmap();
            jsonGenerator.writeNumberField("unPaidMoney", getParamData(repayRoadmap.getUnPaidMoney()));
            jsonGenerator.writeNumberField("unPaidCorpus", getParamData(repayRoadmap.getUnPaidCorpus()));
            jsonGenerator.writeNumberField("unPaidInterest", getParamData(repayRoadmap.getUnPaidInterest()));
            jsonGenerator.writeNumberField("unPaidFee", getParamData(repayRoadmap.getUnPaidFee()));
            jsonGenerator.writeNumberField("unPaidPeriod", getParamData(repayRoadmap.getUnPaidPeriod()));
            jsonGenerator.writeNumberField("unPaidDefaultInterest", getParamData(repayRoadmap.getUnPaidDefaultInterest()));
            jsonGenerator.writeNumberField("paidMoney", getParamData(repayRoadmap.getPaidMoney()));
            jsonGenerator.writeNumberField("paidCorpus", getParamData(repayRoadmap.getPaidCorpus()));
            jsonGenerator.writeNumberField("paidInterest", getParamData(repayRoadmap.getPaidInterest()));
            jsonGenerator.writeNumberField("paidFee", getParamData(repayRoadmap.getPaidFee()));
            jsonGenerator.writeNumberField("paidDefaultInterest", getParamData(repayRoadmap.getPaidDefaultInterest()));
            jsonGenerator.writeNumberField("paidPeriod", getParamData(repayRoadmap.getPaidPeriod()));
            jsonGenerator.writeNumberField("repayMoney", getParamData(repayRoadmap.getRepayMoney()));
            jsonGenerator.writeNumberField("repayCorpus", getParamData(repayRoadmap.getRepayCorpus()));
            jsonGenerator.writeNumberField("repayInterest", getParamData(repayRoadmap.getRepayInterest()));
            jsonGenerator.writeNumberField("repayFee", getParamData(repayRoadmap.getRepayFee()));
            jsonGenerator.writeNumberField("repayDefaultInterest", getParamData(repayRoadmap.getRepayDefaultInterest()));
            jsonGenerator.writeNumberField("nextRepayMoney", getParamData(repayRoadmap.getNextRepayMoney()));
            jsonGenerator.writeNumberField("nextRepayCorpus", getParamData(repayRoadmap.getNextRepayCorpus()));
            jsonGenerator.writeNumberField("nextRepayInterest", getParamData(repayRoadmap.getNextRepayInterest()));
            jsonGenerator.writeNumberField("nextRepayFee", getParamData(repayRoadmap.getNextRepayFee()));
            jsonGenerator.writeNumberField("nextRepayDefaultInterest", getParamData(repayRoadmap.getNextRepayDefaultInterest()));
            jsonGenerator.writeStringField("nextRepayDate", repayRoadmap.getNextRepayDate() == null ? "" : new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(repayRoadmap.getNextRepayDate()));
            jsonGenerator.writeStringField("businessType", invest.getLoan().getBusinessType());
        }


        jsonGenerator.writeEndObject();


    }

    private Double getParamData(Double param) {
        return param == null ? 0.0 : param;
    }

    private int getParamData(Integer param) {
        return param == null ? 0 : param;
    }
}
