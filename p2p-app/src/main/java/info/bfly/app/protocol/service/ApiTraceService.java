package info.bfly.app.protocol.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.traceability.controller.TraceTemplateList;
import info.bfly.crowd.traceability.model.TraceTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope(ScopeType.REQUEST)
@Service
public class ApiTraceService extends TraceTemplateList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8757583567090533597L;
    public ApiTraceService() {
        final String[] RESTRICTIONS = {};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
    
    public List<TraceTemplate> getUserTraceRecords(User user){
        if (user == null)
            return null;
        return (List<TraceTemplate>) getHt().find("select tt from TraceTemplate tt left join tt.orderCaches as oc left join oc.mallOrder as mo where mo.user.id like ?", user.getId());
    }
}
