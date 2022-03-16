package info.bfly.app.protocol.model.serializer;

import java.io.IOException;

import info.bfly.crowd.traceability.model.TraceColumn;
import info.bfly.crowd.traceability.model.TraceTemplate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TraceRecordSerializer  extends JsonSerializer<TraceTemplate> {

    private StringBuilder detail;
    
    @Override
    public void serialize(TraceTemplate record, JsonGenerator gen,
            SerializerProvider serializers) throws IOException,
            JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", record.getId());
        gen.writeStringField("title", record.getReferrer().getTemplateName());
        detail = new StringBuilder();
        searchTree(record);
        gen.writeStringField("detail", detail.toString());
        System.out.println(detail.toString());
        gen.writeEndObject();
    }
    
    private void searchTree(TraceTemplate template){
        if(template.getTraceColumns().size()>0){
            detail.append("[");
            for(TraceColumn column : template.getTraceColumns()){
                if(column.getReferrer()==null&&column.getStatus()==1){
                    convert(column);
                    detail.append(",");
                }
            }
            detail.deleteCharAt(detail.lastIndexOf(","));
            detail.append("]");
        }
    }
    
    private void convert(TraceColumn column){
        detail.append("{\"name\":\""+column.getColumnName()+"\"");
        if(column.getChildren().size() == 0){
            if(column.getTraceItems().size()!=0&&column.getTraceItems().get(0).getId()!=null){
                detail.append(",\"value\":\""+string2Json(column.getTraceItems().get(0).getItemValue())+"\"");
                String s = column.getTraceItems().get(0).getItemValue();
                System.out.println(s.replace("\"","\\\""));
            }
            else detail.append(",\"value\":\"\"");
        }
        else{
            detail.append(",\"value\":[");
            for(TraceColumn tmpColumn : column.getChildren())
            {
                convert(tmpColumn);
                detail.append(",");
            }
            detail = detail.deleteCharAt(detail.lastIndexOf(","));
            detail.append("]");
        }
        detail.append("}");
    }

    public String string2Json(String s)
    {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
             switch (c){
             case '\"':
                 sb.append("\\\"");
                 break;
             case '\\':
                 sb.append("\\\\");
                 break;
             case '/':
                 sb.append("\\/");
                 break;
             case '\b':
                 sb.append("\\b");
                 break;
             case '\f':
                 sb.append("\\f");
                 break;
             case '\n':
                 sb.append("\\n");
                 break;
             case '\r':
                 sb.append("\\r");
                 break;
             case '\t':
                 sb.append("\\t");
                 break;
             default:
                 sb.append(c);
             }
         }
         return sb.toString();
    }
}
