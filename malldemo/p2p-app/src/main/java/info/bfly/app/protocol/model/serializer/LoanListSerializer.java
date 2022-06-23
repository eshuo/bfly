package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.app.protocol.util.LoanUtil;
import info.bfly.archer.system.controller.DictUtil;
import info.bfly.p2p.loan.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class LoanListSerializer extends JsonSerializer<Loan> {

    @Autowired
    private DictUtil dictUtil;


    @Override
    public void serialize(Loan loan, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", loan.getId());
        jsonGenerator.writeStringField("name", loan.getName());
        jsonGenerator.writeStringField("riskLevel", loan.getRiskLevel());
        jsonGenerator.writeStringField("deadline", loan.getDeadline() * loan.getType().getRepayTimePeriod() + dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
        jsonGenerator.writeNumberField("ratePercent", loan.getRatePercent());
        jsonGenerator.writeStringField("status", loan.getStatus());
        jsonGenerator.writeStringField("statusName", dictUtil.getValue("loan", loan.getStatus()));
        jsonGenerator.writeStringField("residualTime", LoanUtil.getRemainTime(loan.getExpectTime(), loan.getStatus()));
        jsonGenerator.writeStringField("customPicture", loan.getCustomPicture());
        jsonGenerator.writeStringField("businessType", loan.getBusinessType());





        jsonGenerator.writeEndObject();
    }
}
