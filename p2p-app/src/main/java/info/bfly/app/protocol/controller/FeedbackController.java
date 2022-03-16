package info.bfly.app.protocol.controller;

import info.bfly.api.model.BaseResource;
import info.bfly.api.model.In;
import info.bfly.api.model.Out;
import info.bfly.api.service.ApiService;
import info.bfly.app.protocol.model.request.FeedbackValue;
import info.bfly.app.protocol.service.ApiFeedbackService;
import info.bfly.core.annotations.ScopeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@RequestMapping("/v1.0")
@Controller
@Scope(ScopeType.REQUEST)
public class FeedbackController extends BaseResource {

    @Autowired
    private ApiFeedbackService apiFeedbackService;

    @Autowired
    private ApiService apiService;

    @RequestMapping("/saveFeedback")
    @ResponseBody
    public Out saveFeedback(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        Out out = apiService.parseOut(request);
        FeedbackValue feedbackValue = in.getFinalValue(FeedbackValue.class);
        String content = feedbackValue.getContent();
        String mobileNumber = feedbackValue.getMobileNumber();
        if (content == null || content.length() == 0 || mobileNumber == null || mobileNumber.length() == 0) {
            out.setResult("false");
            return out;
        }
        apiFeedbackService.getInstance().setContent(content);
        apiFeedbackService.getInstance().setMobileNumber(mobileNumber);
        apiFeedbackService.save();
        out.setResult("true");
        return out;
    }

}
