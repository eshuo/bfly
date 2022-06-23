package info.bfly.app.protocol.controller;


import com.fasterxml.jackson.databind.module.SimpleModule;
import info.bfly.api.model.BaseResource;
import info.bfly.api.model.In;
import info.bfly.api.model.Out;
import info.bfly.api.service.ApiService;
import info.bfly.app.protocol.model.request.DemoPageValue;
import info.bfly.app.protocol.model.request.DemoValue;
import info.bfly.app.protocol.model.response.ApiUser;
import info.bfly.app.protocol.model.response.ResponsePage;
import info.bfly.app.protocol.model.serializer.CommentSerializer;
import info.bfly.app.protocol.model.serializer.LoanSerializer;
import info.bfly.app.protocol.service.ApiLoanService;
import info.bfly.archer.comment.model.Comment;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 31/05/2013
 */
@Controller
@RequestMapping("/v1.0")
@Scope(ScopeType.REQUEST)
public class DemoController extends BaseResource {
    @Autowired
    private ApiService apiService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApiLoanService loanService;

    /**
     * demo without param without security
     */
    @RequestMapping("/demo_without_param_without_sec")
    @ResponseBody
    public Out demoUser_noSec() {
        Out out = new Out();
        out.setResult(new ApiUser().getId());
        return out;
    }

    /**
     * demo with param without security
     *
     * @param request
     * @return
     */
    @RequestMapping("/demo_with_param_without_sec")
    @ResponseBody
    public Out demoUser_noSec(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        Out out = new Out(in);
        out.setResult(new ApiUser().getId());
        return out;
    }

    /**
     * demo without param with security
     */
    @RequestMapping("/demo_without_param_with_sec")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @ResponseBody
    public Out demoUser_Sec() {
        Out out = new Out();
        out.setResult(new ApiUser(loadUserFromSecurityContext()).getId());
        return out;
    }

    /**
     * demo with param with security
     *
     * @return
     */
    @RequestMapping("/demo_with_param_with_sec")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @ResponseBody
    public Out demoUser_Sec(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        DemoValue d = in.getFinalValue(DemoValue.class);
        Out out = new Out(in);
        out.setResult(new ApiUser(userService.getUserById(d.getUserid())));
        return out;
    }

    /**
     * demo page
     * @param request
     * @return
     */
    @RequestMapping("/demo_page")
    @ResponseBody
    public Out demoPage(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        DemoPageValue d = in.getFinalValue(DemoPageValue.class);
        //设置参数
        Loan loan = loanService.getExample();
        loan.getUser().setId(d.getUserId());
        loanService.setCurrentPage(d.getPage().getCurrentPage());
        loanService.setPageSize(d.getPage().getSize());
        loanService.addOrder(d.getPage().getOrder());
        //处理数据
        ResponsePage page = new ResponsePage(loanService);

        //设置解析模式
        SimpleModule module = new SimpleModule();
        module.addSerializer(Loan.class, new LoanSerializer()).addSerializer(Comment.class,new CommentSerializer());
        //返回数据
        Out out = apiService.parseOut(request);
        out.setResult(page,module);
        return out;
    }
}