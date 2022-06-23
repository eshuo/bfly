package info.bfly.crowd.traceability.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.crowd.traceability.model.TraceTemplate;

@Component
@Scope(ScopeType.VIEW)
public class TraceList extends EntityQuery<TraceTemplate> implements Serializable {

    /**
     * 可追溯系统档案管理
     */
    private static final long serialVersionUID = -4564640512992220885L;
    
    private Date   searchtraceMinTime;
    private Date   searchtraceMaxTime;
    
    List<TraceTemplate> recordList=null;
    List<TraceTemplate> templateList=null;

    @Resource
    private HibernateTemplate ht;
    
    private static final String lazyModelCountHql = "select count(distinct trace) from TraceTemplate trace ";
    private static final String lazyModelHql      = "select distinct trace from TraceTemplate trace";
    
    public TraceList(){
        setCountHql(TraceList.lazyModelCountHql);
        setHql(TraceList.lazyModelHql);
        final String[] RESTRICTIONS = {"trace.id like #{traceList.example.id}", "trace.parent.user.username like #{traceList.example.parent.user.id}",
                "trace.parent.id like #{traceList.example.parent.name}","trace.type like 'template'",
                "trace.createTime >= #{traceList.searchtraceMinTime}", "trace.createTime <= #{traceList.searchtraceMaxTime}","trace.status like 1" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public Date getSearchtraceMinTime() {
        return searchtraceMinTime;
    }

    public void setSearchtraceMinTime(Date searchtraceMinTime) {
        this.searchtraceMinTime = searchtraceMinTime;
    }

    public Date getSearchtraceMaxTime() {
        return searchtraceMaxTime;
    }

    public void setSearchtraceMaxTime(Date searchtraceMaxTime) {
        this.searchtraceMaxTime = searchtraceMaxTime;
    }
    
    public List<TraceTemplate> getRecordList() {
        recordList = (List<TraceTemplate>) ht.findByNamedQuery("TraceTemplate.getTraceTemplateByType", "record");
        return recordList;
    }

    public void setRecordList(List<TraceTemplate> recordList) {
        this.recordList = recordList;
    }

    public List<TraceTemplate> getTemplateList() {
        templateList = (List<TraceTemplate>) ht.findByNamedQuery("TraceTemplate.getTraceTemplateByType", "template");
        return templateList;
    }

    public void setTemplateList(List<TraceTemplate> templateList) {
        this.templateList = templateList;
    }
    
    @Override
    protected void initExample() {
        TraceTemplate template = new TraceTemplate();
        Loan loan = new Loan();
        loan.setUser(new User());
        template.setParent(loan);
        setExample(template);
    }

}
