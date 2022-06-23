package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.api.exception.AnalyticParamExection;
import info.bfly.app.protocol.util.LoanUtil;
import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.system.controller.DictUtil;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.service.LoanCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
@Service("loanDetailData")
public class LoanDetailSerializer extends JsonSerializer<Loan> {

    @Autowired
    private LoanCalculator loanCalculator;

    @Autowired
    private DictUtil dictUtil;

    private int commentSize = 5;

    private int investSize = 5;

    @Override
    public void serialize(Loan loan, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", loan.getId());
        jsonGenerator.writeStringField("name", loan.getName());
        jsonGenerator.writeNumberField("loanMoney", loan.getLoanMoney());
        jsonGenerator.writeStringField("repayTimeUnit", loan.getDeadline() * loan.getType().getRepayTimePeriod() + dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
        jsonGenerator.writeNumberField("ratePercent", loan.getRatePercent());
        jsonGenerator.writeStringField("commitTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(loan.getCommitTime()));
        jsonGenerator.writeStringField("status", loan.getStatus());
        jsonGenerator.writeStringField("statusName", dictUtil.getValue("loan", loan.getStatus()));
        jsonGenerator.writeStringField("repayType", dictUtil.getValue("repay_type", loan.getType().getRepayType()));
        jsonGenerator.writeNumberField("minInvestMoney", loan.getMinInvestMoney());
        jsonGenerator.writeNumberField("cardinalNumber", loan.getCardinalNumber());
        jsonGenerator.writeStringField("residualTime", LoanUtil.getRemainTime(loan.getExpectTime(), loan.getStatus()));
        try {
            jsonGenerator.writeNumberField("schedule", loanCalculator.calculateRaiseCompletedRate(loan.getId()));
        } catch (NoMatchingObjectsException e) {
            throw new AnalyticParamExection("融资进度");
        }

        try {
            jsonGenerator.writeNumberField("calculateMoney", loanCalculator.calculateMoneyNeedRaised(loan.getId()));
        } catch (NoMatchingObjectsException e) {
            throw new AnalyticParamExection("剩余金额");
        }
        jsonGenerator.writeNumberField("successNumber", loanCalculator.countSuccessInvest(loan.getId()));
        jsonGenerator.writeStringField("description", loan.getDescription());
        int commentsMaxSize = loan.getConmments().size();
        jsonGenerator.writeNumberField("commentSize", commentSize);
        jsonGenerator.writeNumberField("commentsMaxSize", commentsMaxSize);
        jsonGenerator.writeNumberField("commentsMaxPage", (int) Math.ceil((double) commentsMaxSize / (double) commentSize));
        jsonGenerator.writeObjectField("comments", loan.getConmments().subList(0, commentSize>commentsMaxSize?commentsMaxSize:commentSize));
        int investMaxSize = loan.getInvests().size();
        jsonGenerator.writeNumberField("investSize", investSize);
        jsonGenerator.writeNumberField("investMaxSize", investMaxSize);
        jsonGenerator.writeNumberField("investMaxPage", (int) Math.ceil((double) investMaxSize / (double) investSize));
        jsonGenerator.writeObjectField("invest", loan.getInvests().subList(0, investSize>investMaxSize?investMaxSize:investSize));

        jsonGenerator.writeEndObject();
    }


    public int getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(int commentSize) {
        this.commentSize = commentSize;
    }

    public int getInvestSize() {
        return investSize;
    }

    public void setInvestSize(int investSize) {
        this.investSize = investSize;
    }
}
