package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.archer.system.controller.DictUtil;
import info.bfly.p2p.loan.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by XXSun on 2016/12/27.
 */
public class LoanSerializer extends JsonSerializer<Loan> {

    @Autowired
    private DictUtil dictUtil;

    @Override
    public void serialize(Loan loan, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("id", loan.getId());
        jgen.writeStringField("name", loan.getName());
        jgen.writeStringField("typeName", loan.getType().getName());
        jgen.writeNumberField("loanMoney", loan.getLoanMoney());
        jgen.writeNumberField("rate", loan.getRatePercent());
        jgen.writeStringField("repayTimePeriod", loan.getDeadline() * loan.getType().getRepayTimePeriod() + dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
        jgen.writeStringField("commitTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(loan.getCommitTime()));
//        jgen.writeObjectField("comments", loan.getConmments());

        jgen.writeEndObject();

    }
}
