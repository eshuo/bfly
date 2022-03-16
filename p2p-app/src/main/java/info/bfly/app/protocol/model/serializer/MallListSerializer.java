package info.bfly.app.protocol.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import info.bfly.app.protocol.util.LoanUtil;
import info.bfly.archer.system.controller.DictUtil;
import info.bfly.core.util.ArithUtil;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.p2p.loan.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("mallListData")
public class MallListSerializer extends JsonSerializer<Loan> {
    
    @Autowired
    private  MallStageService   mallStageService;

    @Autowired
    private DictUtil dictUtil;
    
    @Override
    public void serialize(Loan loan, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException,
            JsonProcessingException {
        
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", loan.getId());
        jsonGenerator.writeStringField("name", loan.getName());
        jsonGenerator.writeStringField("picture", loan.getCustomPicture());
        jsonGenerator.writeStringField("deadline", loan.getDeadline() * loan.getType().getRepayTimePeriod() + dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
        jsonGenerator.writeStringField("status", loan.getStatus());
        jsonGenerator.writeStringField("statusName", dictUtil.getValue("loan", loan.getStatus()));
        jsonGenerator.writeStringField("residualTime", LoanUtil.getRemainTime(loan.getExpectTime(), loan.getStatus()));
        
        StringBuilder sb = new StringBuilder();
        if(loan.getLoanAttrs().size()>0){
            for(int i = 0; i < loan.getLoanAttrs().size(); i++){
                String tmp = loan.getLoanAttrs().get(i).getId();
                if(!tmp.equals("credit") && !tmp.equals("Field") && !tmp.equals("Guarantee") && !tmp.equals("index") && !tmp.equals("mortgage") && !tmp.equals("recommend")){
                    sb.append(loan.getLoanAttrs().get(i).getDescription());
                    sb.append(",");
                }
            }
        }
        jsonGenerator.writeStringField("mallType", sb.length()>1?sb.substring(0, sb.length()-1).toString():"");
        
        int supportnum = 0;
        if(loan.getMallstages().size()>0){
            jsonGenerator.writeStringField("recommandStage", loan.getMallstages().get(0).getStageInventory().getFee()==null?loan.getMallstages().get(0).getStageInventory().getMinFee()+"元起":"￥"+loan.getMallstages().get(0).getStageInventory().getFee());
            for(int i = 0; i<loan.getMallstages().size(); i++){
                String mallStageId = loan.getMallstages().get(i).getId();
                supportnum += mallStageService.supportNum(mallStageId);
            }
        }else jsonGenerator.writeStringField("recommandStage", "暂无");
        jsonGenerator.writeNumberField("supportNum", supportnum);
        jsonGenerator.writeNumberField("schedule", ArithUtil.round(mallStageService.getTotalOrderCountByLoanId(loan.getId()) / loan.getLoanMoney() * 100, 2));
        jsonGenerator.writeStringField("businessType", loan.getBusinessType());
        
        jsonGenerator.writeEndObject();
        
    }

}
