package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.app.protocol.util.LoanUtil;
import info.bfly.archer.banner.model.BannerPicture;
import info.bfly.archer.system.controller.DictUtil;
import info.bfly.core.util.ArithUtil;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.service.LoanCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("mallDetailData")
public class MallDetailSerializer extends JsonSerializer<Loan> {

    @Autowired
    private LoanCalculator loanCalculator;
    
    @Autowired
    private  MallStageService   mallStageService;

    @Autowired
    private DictUtil dictUtil;
    
    private int commentSize = 5;

    private int investSize = 5;

    @Override
    public void serialize(Loan loan, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", loan.getId());
        jsonGenerator.writeStringField("name", loan.getName());
//        jsonGenerator.writeNumberField("loanMoney", loan.getLoanMoney());
//        jsonGenerator.writeStringField("repayTimeUnit", loan.getDeadline() * loan.getType().getRepayTimePeriod() + DictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
        jsonGenerator.writeNumberField("ratePercent", loan.getRatePercent());
//        jsonGenerator.writeStringField("commitTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(loan.getCommitTime()));
        jsonGenerator.writeStringField("status", loan.getStatus());
        jsonGenerator.writeStringField("statusName", dictUtil.getValue("loan", loan.getStatus()));
//        jsonGenerator.writeStringField("repayType", DictUtil.getValue("repay_type", loan.getType().getRepayType()));
//        jsonGenerator.writeNumberField("minInvestMoney", loan.getMinInvestMoney());
//        jsonGenerator.writeNumberField("cardinalNumber", loan.getCardinalNumber());
        jsonGenerator.writeStringField("residualTime", LoanUtil.getRemainTime(loan.getExpectTime(), loan.getStatus()));
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if(loan.getLoanInfoPics().size()>0){
            for(int i = 0; i<loan.getLoanInfoPics().size(); i++){
                BannerPicture bannerPicture = new BannerPicture();
                bannerPicture = loan.getLoanInfoPics().get(i);
                sb.append("{\"src\":\"");
                sb.append(bannerPicture.getPicture());
                sb.append("\"}");
                sb.append(",");
            }
        }
        jsonGenerator.writeStringField("pic", (sb.substring(0,sb.length()-1)+"]").length()>2?sb.substring(0,sb.length()-1)+"]":"");
        
        jsonGenerator.writeStringField("description", loan.getDescription());
        int commentsMaxSize = loan.getConmments().size();
        jsonGenerator.writeNumberField("commentSize", getCommentSize());
        jsonGenerator.writeNumberField("commentsMaxSize", commentsMaxSize);
        jsonGenerator.writeNumberField("commentsMaxPage", (int) Math.ceil((double) commentsMaxSize / (double) getCommentSize()));
        jsonGenerator.writeObjectField("comments", loan.getConmments().subList(0, getCommentSize()>commentsMaxSize?commentsMaxSize:getCommentSize()));
        int investMaxSize = loan.getInvests().size();
        jsonGenerator.writeNumberField("investSize", getInvestSize());
        jsonGenerator.writeNumberField("investMaxSize", investMaxSize);
        jsonGenerator.writeNumberField("investMaxPage", (int) Math.ceil((double) investMaxSize / (double) getInvestSize()));
        jsonGenerator.writeObjectField("invest", loan.getInvests().subList(0, getInvestSize()>investMaxSize?investMaxSize:getInvestSize()));
                
        int supportnum = 0;
        if(loan.getMallstages().size()>0){
            jsonGenerator.writeStringField("recommandStage", loan.getMallstages().get(0).getStageInventory().getFee()==null?loan.getMallstages().get(0).getStageInventory().getMinFee()+"元起":"￥"+loan.getMallstages().get(0).getStageInventory().getFee());
            for(int i = 0; i<loan.getMallstages().size(); i++){
                String mallStageId = loan.getMallstages().get(i).getId();
                supportnum += mallStageService.supportNum(mallStageId);
            }
        }else  jsonGenerator.writeStringField("recommandStage", "暂无");
        jsonGenerator.writeNumberField("supportNum", supportnum);
        jsonGenerator.writeNumberField("schedule", ArithUtil.round(mallStageService.getTotalOrderCountByLoanId(loan.getId()) / loan.getLoanMoney() * 100, 2));
        
        
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
