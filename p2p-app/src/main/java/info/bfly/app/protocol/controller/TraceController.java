package info.bfly.app.protocol.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import info.bfly.api.exception.ParameterExection;
import info.bfly.api.model.BaseResource;
import info.bfly.api.model.In;
import info.bfly.api.model.Out;
import info.bfly.api.service.ApiService;
import info.bfly.app.protocol.model.request.TraceValue;
import info.bfly.app.protocol.model.response.ResponsePage;
import info.bfly.app.protocol.model.serializer.TraceRecordListSerializer;
import info.bfly.app.protocol.model.serializer.TraceRecordSerializer;
import info.bfly.app.protocol.service.ApiTraceService;
import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.traceability.model.TraceTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.module.SimpleModule;

@Controller
@RequestMapping("/v1.0")
@Scope(ScopeType.REQUEST)
public class TraceController extends BaseResource {
    
    @Autowired
    private ApiService apiService;
    
    @Autowired
    private ApiTraceService traceService;
    
    /**
     * @param @param  request
     * @param @return 设定文件
     * @return TraceValue 返回类型
     * @throws
     * @Title: initTraceValue
     * @Description: TODO(公共方法用来转换页面传入的参数)
     */
    private TraceValue initTraceValue(HttpServletRequest request) {
        TraceValue traceValue = null;
        In in = apiService.parseIn(request);
        traceValue = in.getFinalValue(TraceValue.class);
        if (traceValue.getId() == null || traceValue.getId().length() == 0)
            throw new ParameterExection("可追溯档案ID");

        return traceValue;
    }
    
    @RequestMapping("/traceDetail")
    @ResponseBody
    public Out getTraceDetail(HttpServletRequest request) {
        // 注入
        TraceValue traceValue = this.initTraceValue(request);
        // 参数
        traceService.addRestriction("traceTemplate.id=" + traceValue.getId());
        // 查询
        ResponsePage page = new ResponsePage(traceService);
        // 设置解析模式
        SimpleModule module = new SimpleModule();
        module.addSerializer(TraceTemplate.class,new TraceRecordSerializer());
        Out out = apiService.parseOut(request);
        out.setResult(page.getData(), module);
        return out;
    }
    
    @RequestMapping("/traceList")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out getTraceList(HttpServletRequest request) {
        
        List<TraceTemplate> records = traceService.getUserTraceRecords(loadUserFromSecurityContext());
        
        SimpleModule module = new SimpleModule();
        // 设置解析模式
        module.addSerializer(TraceTemplate.class,new TraceRecordListSerializer());
        // 返回数据
        Out out = apiService.parseOut(request);
        out.setResult(records, module);
        return out;
    }
}
