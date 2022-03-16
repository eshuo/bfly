package info.bfly.app.protocol.model.request;

public class TraceValue {
    private String id;
    public String DEFALT_STATUS = "trace_template.status like 1 and trace_template.type like 'record'";
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}
