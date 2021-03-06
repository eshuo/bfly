package info.bfly.app.protocol.controller;

import com.fasterxml.jackson.databind.module.SimpleModule;
import info.bfly.api.exception.ParameterExection;
import info.bfly.api.model.BaseResource;
import info.bfly.api.model.In;
import info.bfly.api.model.Out;
import info.bfly.api.service.ApiService;
import info.bfly.app.protocol.model.request.CommentValue;
import info.bfly.app.protocol.model.request.LoanValue;
import info.bfly.app.protocol.model.request.MallStageValue;
import info.bfly.app.protocol.model.request.UserAddressValue;
import info.bfly.app.protocol.model.response.ResponsePage;
import info.bfly.app.protocol.model.serializer.*;
import info.bfly.app.protocol.service.*;
import info.bfly.archer.comment.CommentConstants;
import info.bfly.archer.comment.model.Comment;
import info.bfly.archer.comment.service.CommentService;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.user.model.Area;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.IdGenerator;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.mall.model.MallStageItem;
import info.bfly.crowd.mall.service.MallService;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.model.MallStageCache;
import info.bfly.crowd.orders.model.Order;
import info.bfly.crowd.orders.service.OrderService;
import info.bfly.crowd.user.model.UserAddress;
import info.bfly.crowd.user.service.UserAddressService;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.loan.model.Loan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
@Controller
@RequestMapping("/v1.0")
@Scope(ScopeType.REQUEST)
public class LoanController extends BaseResource {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ApiLoanService loanService;

    @Autowired
    private LoanDetailSerializer loanDetailSerializer;
    
    @Autowired
    private MallDetailSerializer mallDetailSerializer;

    @Autowired
    private ApiCommentService apiCommentService;

    @Autowired
    private ApiInvestListService apiInvestListService;

    @Autowired
    private CommentService commentService;

    
    @Autowired
    private ApiMallStageListService apiStageService;
    
    @Autowired
    private ApiUserAddressService apiUserAddressService;
    
    @Autowired
    private MallStageService mallStageService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserAddressService userAddressService;
    
    @Autowired
    private ApiMallStageService apiMallStageService;
    
    @Autowired
    private MallListSerializer mallListSerializer;
    
    @Autowired
    private MallStageSerializer mallStageSerializer;
    
    @Autowired
    private MallStageCacheSerializer mallStageCacheSerializer;
    
    @Autowired
    private UserAddressSerializer userAddressSerializer;
    
    @Autowired
    private ApiOrderService apiOrderService;
    
    @Autowired
    private OrderDetailSerializer orderDetailSerializer;
    
    @Autowired
    private MallService mallService;
    
    /**
     * @param @param  request
     * @param @return ????????????
     * @return LoanValue ????????????
     * @throws
     * @Title: initLoanValue
     * @Description: TODO(?????????????????????????????????????????????)
     */
    private LoanValue initLoanValue(HttpServletRequest request) {
        LoanValue loanValue = null;
        In in = apiService.parseIn(request);
        loanValue = in.getFinalValue(LoanValue.class);
        if (loanValue.getId() == null || loanValue.getId().length() == 0)
            throw new ParameterExection("??????ID");

        return loanValue;
    }
    
    /**
     * ??????loan??????
     *
     * @return
     */
    @RequestMapping("/loanList")
    @ResponseBody
    public Out getLoanList(HttpServletRequest request) {
        // ??????
        In in = apiService.parseIn(request);
        LoanValue loanValue = in.getFinalValue(LoanValue.class);

        // ????????????
        loanService.setCurrentPage(loanValue.getPage().getCurrentPage());
        loanService.setPageSize(loanValue.getPage().getSize());
        loanService.addOrder(loanValue.getPage().getOrder());


        if ( StringUtils.isNotEmpty(loanValue.getType()) ) {
            if(loanValue.getType().equals("index")){
                loanService.addRestriction("attr.id='index'");
            }else if(loanValue.getType().equals("recommend")){
                loanService.addRestriction("attr.id='recommend'");
            }
        }

        Loan example = loanService.getExample();
        if (loanValue.getStatus() != null) {
            example.setStatus(loanValue.getStatus());
        }else{
            loanService.addRestriction(loanValue.DEFALT_STATUS);
        }
        if (loanValue.getRiskLevel() != null) {
            example.setRiskLevel(loanValue.getRiskLevel());
        }
        if (loanValue.getMinRate() != null) {
            loanService.setMinRate(Double.parseDouble(loanValue.getMinRate()));
        }
        if (loanValue.getMaxRate() != null) {
            loanService.setMaxRate(Double.parseDouble(loanValue.getMaxRate()));
        }
        if (loanValue.getMaxDeadline() != null) {
            loanService.setMaxDeadline(Integer.parseInt(loanValue.getMaxDeadline()));
        }
        if (loanValue.getMinDeadline() != null) {
            loanService.setMinDeadline(Integer.parseInt(loanValue.getMinDeadline()));
        }
        // ??????
        ResponsePage page = new ResponsePage(loanService);
        // ??????????????????
        SimpleModule module = new SimpleModule();
        module.addSerializer(Loan.class, new LoanListSerializer());
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page, module);
        return out;
    }
    
    /**
     * ????????????????????????
     */
    @RequestMapping("/mallList")
    @ResponseBody
    public Out getMallList(HttpServletRequest request) {
        // ??????
        In in = apiService.parseIn(request);
        LoanValue loanValue = in.getFinalValue(LoanValue.class);

        // ????????????
        loanService.setCurrentPage(loanValue.getPage().getCurrentPage());
        loanService.setPageSize(loanValue.getPage().getSize());
        loanService.addRestriction(loanValue.DEFALT_STATUS2);
        Loan example = loanService.getExample();
        
        if (loanValue.getStatus() != null) {
            example.setStatus(loanValue.getStatus());
        }else{
            loanService.addRestriction(loanValue.DEFALT_STATUS1);
        }
        // ??????
        ResponsePage page = new ResponsePage(loanService);
        // ??????????????????
        SimpleModule module = new SimpleModule();
        module.addSerializer(Loan.class, mallListSerializer);
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page, module);
        return out;
    }

    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/loan")
    @ResponseBody
    public Out getLoan(HttpServletRequest request) {
        // ??????
        LoanValue loanValue = this.initLoanValue(request);
        // ??????
        loanService.addRestriction(" loan.id=" + loanValue.getId());
        // ??????
        ResponsePage page = new ResponsePage(loanService);
        // ??????????????????
        SimpleModule module = new SimpleModule();
        module.addSerializer(Loan.class, loanDetailSerializer).addSerializer(Comment.class, new CommentSerializer()).addSerializer(Invest.class, new InvestSerializer());
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page.getData(), module);
        return out;
    }

    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/mall")
    @ResponseBody
    public Out getMall(HttpServletRequest request) {
        // ??????
        LoanValue loanValue = this.initLoanValue(request);
        // ??????
        loanService.addRestriction(" loan.id=" + loanValue.getId());
        // ??????
        ResponsePage page = new ResponsePage(loanService);
        // ??????????????????
        SimpleModule module = new SimpleModule();
        module.addSerializer(Loan.class, mallDetailSerializer).addSerializer(Comment.class, new CommentSerializer()).addSerializer(Invest.class, new InvestSerializer());
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page.getData(), module);
        return out;
    }
    
    /**
     * 
    * @Title: getUserAddressList 
    * @Description:  ???????????????????????????
    * @param @param request
    * @param @return    ???????????? 
    * @return Out    ???????????? 
    * @throws
     */
    @RequestMapping("/userAddressList")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out getUserAddressList(HttpServletRequest request){
        
        
        User user = loadUserFromSecurityContext();
        apiUserAddressService.addRestriction("userAddress.user.id = '" + user.getId() + "' and userAddress.status = 1");
        
        // ??????
        apiUserAddressService.setCurrentPage(0);
        apiUserAddressService.setPageSize(20);
        
        ResponsePage page = new ResponsePage(apiUserAddressService);
        
        SimpleModule module = new SimpleModule();
        // ??????????????????
        module.addSerializer(UserAddress.class,userAddressSerializer);
        
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page.getData(), module);
        
        return out;
    }
    
    /**
     * ????????????????????????
     */
    @RequestMapping("/mallRecommendList")
    @ResponseBody
    public Out getMallRecommendList(HttpServletRequest request) {
        // ??????????????????
        SimpleModule module = new SimpleModule();
        module.addSerializer(Loan.class, mallListSerializer);
        // ????????????
        Out out = apiService.parseOut(request);

        List<Loan> recommendList = mallService.getRecommendCrowFundingList();
        if (null == recommendList || recommendList.size() == 0) {
            return null;
        }
        out.setResult(recommendList, module);
        return out;
    }

   
    @RequestMapping("/saveUserAddress")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out saveUserAddress(HttpServletRequest request){
        
        
        In in = apiService.parseIn(request);
        UserAddressValue userAddressValue = in.getFinalValue(UserAddressValue.class);
        
        UserAddress userAddress = new UserAddress();
        userAddress.setConsigneeName(userAddressValue.getConsigneeName());
        userAddress.setDetailAddress(userAddressValue.getDetailAddress());
        userAddress.setAreas(new String[] { userAddressValue.getArea() });
        userAddress.setConsigneePhone(userAddressValue.getConsigneePhone());
        userAddress.setIsDefault("true".equals(userAddressValue.getIsDefault()) ? true : false);
        userAddress.setStatus(1);
        userAddress.setUser(loadUserFromSecurityContext());
        
        userAddressService.createUserAddress(userAddress);
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult("true");
        return out;
    }
    
    
    @RequestMapping("/orderList")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out getOrderList(HttpServletRequest request){
        
        User user = loadUserFromSecurityContext();
        apiOrderService.addRestriction("order1.user.id = '" + user.getId() + "'");
        // ??????
        apiOrderService.setCurrentPage(0);
        apiOrderService.setPageSize(20);
        
        ResponsePage page = new ResponsePage(apiOrderService);
        SimpleModule module = new SimpleModule();
        // ??????????????????
        module.addSerializer(Order.class,orderDetailSerializer);
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page.getData(), module);
        return out;
        
    }
    
    /**
     * 
    * @Title: getPrivateMallStage 
    * @Description:  
    * @param @param request
    * @param @return    ???????????? 
    * @return Out    ???????????? 
    * @throws
     */
    @RequestMapping("/privateMallStage")
    @ResponseBody
    public Out getPrivateMallStage(HttpServletRequest request){
        
        In in = apiService.parseIn(request);
        MallStageValue mallStageValue = in.getFinalValue(MallStageValue.class);
        if (mallStageValue.getMallStageId() == null || mallStageValue.getMallStageId().length() == 0)
            throw new ParameterExection("??????ID");
        // ??????
        apiStageService.addRestriction(" mallstage.id=" + mallStageValue.getMallStageId());
        // ??????
        ResponsePage page = new ResponsePage(apiStageService);
        // ??????????????????
        SimpleModule module = new SimpleModule();
        module.addSerializer(MallStage.class, new MallStageListSerializer()).addSerializer(GoodsCache.class, new GoodsCacheSerializer());
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page.getData(), module);
        return out;
        
    }
    
    
    @RequestMapping("/privateMallStageSubmit")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out submitPrivateMallStage(HttpServletRequest request){
        
        In in = apiService.parseIn(request);
        MallStageValue mallStageValue = in.getFinalValue(MallStageValue.class);
        if (mallStageValue.getMallStageId() == null || mallStageValue.getMallStageId().length() == 0)
            throw new ParameterExection("??????ID");
        
        Map<String,String> goodsCacheMap = new HashMap<String, String>();
        for(MallStageItem item:mallStageValue.getStageItems()){
            goodsCacheMap.put(item.getGoodsCacheId(), item.getNum());
        }
        
        String mallStageCacheId=  mallStageService.setPrivateMallStage(mallStageValue.getMallStageId(), goodsCacheMap);
        // ????????????
        Out out = apiService.parseOut(request);
        
        if(null == mallStageCacheId){
            out.setResult("false");
        }else{
            out.setResult(mallStageCacheId);

        }
        
        return out;
    }
    
    
    @RequestMapping("/orderSubmit")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out submitOrder(HttpServletRequest request){
        In in = apiService.parseIn(request);
        MallStageValue mallStageValue = in.getFinalValue(MallStageValue.class);
        String mallStageId = mallStageValue.getMallStageId();
        String mallStageCacheId = mallStageValue.getMallStageCacheId();
        String userAddressId = mallStageValue.getUserAddressId();
        User user = loadUserFromSecurityContext();
        String orderId="";
        try {
            orderId = orderService.createOrder(mallStageCacheId, mallStageId, user.getId(), userAddressId);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult("{\"orderId\":"+"\""+orderId+"\"}");
        return out;
        
    }
    
    /**
     * 
    * @Title: fillInOrder 
    * @Description:  
    * @param @param request
    * @param @return    ???????????? 
    * @return Out    ???????????? 
    * @throws
     */
    @RequestMapping("/fillInOrder")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out fillInOrder(HttpServletRequest request){
        
        In in = apiService.parseIn(request);
        MallStageValue mallStageValue = in.getFinalValue(MallStageValue.class);
        
        Out out = apiService.parseOut(request);
        
        String mallStageJson = "";
        
        MallStage mallStage = null;
        MallStageCache mallStageCache = null;
        
        List<Area> loanAreaList = null; 
        // ??????
        SimpleModule module = new SimpleModule();
        
        if(StringUtils.isEmpty(mallStageValue.getMallStageCacheId())){
            // ??????????????????
             mallStage = mallStageService.getMallStage(mallStageValue.getMallStageId());
             
             loanAreaList =  mallStage.getLoan().getAreas();
             
             // ??????????????????
             module.addSerializer(MallStage.class, mallStageSerializer);
             // ????????????            
             mallStageJson = out.toJson(mallStage, module);
        }else{
            //??????????????????
            mallStageCache = mallStageService.getMallStageCache(mallStageValue.getMallStageCacheId());
            
            loanAreaList =  mallStageCache.getMallStage().getLoan().getAreas();
            // ??????????????????
            module.addSerializer(MallStageCache.class, mallStageCacheSerializer);
            // ????????????            
            mallStageJson = out.toJson(mallStageCache, module);
        }
        
        User user = loadUserFromSecurityContext();
        apiUserAddressService.addRestriction("userAddress.user.id = '" + user.getId() + "' and userAddress.isDefault = true");
        // ??????
        apiUserAddressService.setCurrentPage(mallStageValue.getPage().getCurrentPage());
        apiUserAddressService.setPageSize(mallStageValue.getPage().getSize());
        
        ResponsePage page2 = new ResponsePage(apiUserAddressService);
        // ??????????????????
        module.addSerializer(UserAddress.class,userAddressSerializer);
        
        String userAddress = out.toJson(page2.getData(), module);
        
        String loanArea = userAddressService.changeToMobileLoanAddress(loanAreaList);
        
        String temp = "{ \"mallStage\":" +  mallStageJson + "," +loanArea +",\"userAddress\":"+ (StringUtils.isEmpty(userAddress)?"\"\"":userAddress) + "}";
        // ????????????
        out.setResult(temp);
        return out;
        
    }
    
    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/stageList")
    @ResponseBody
    public Out getStageList(HttpServletRequest request) {
        // ??????
        LoanValue loanValue = this.initLoanValue(request);
        // ??????
        apiMallStageService.setPageSize(loanValue.getPage().getSize());
        apiMallStageService.addRestriction(" mallstage.loan.id=" + loanValue.getId());
        // ??????
        ResponsePage page = new ResponsePage(apiMallStageService);
        // ??????????????????
        SimpleModule module = new SimpleModule();
        module.addSerializer(MallStage.class, mallStageSerializer);
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page, module);
        return out;
    }
    
    /**
     * ???????????????????????????
     *
     * @param
     * @return
     */
    @RequestMapping("/comment")
    @ResponseBody
    public Out getComment(HttpServletRequest request) {

        // ??????
        LoanValue loanValue = this.initLoanValue(request);
        // ??????
        apiCommentService.setLoanId(loanValue.getId());
        apiCommentService.setCurrentPage(loanValue.getPage().getCurrentPage());
        apiCommentService.setPageSize(loanValue.getPage().getSize());
        apiCommentService.addOrder(loanValue.getPage().getOrder());
        // ??????
        ResponsePage page = new ResponsePage(apiCommentService);
        // ??????????????????
        SimpleModule module = new SimpleModule();
        module.addSerializer(Comment.class, new CommentSerializer());
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page, module);
        return out;
    }

    /**
     * ?????????????????????User
     *
     * @param @param  request
     * @param @return ????????????
     * @return Out ????????????
     * @throws
     * @Title: saveComment
     * @Description: TODO(????????????)
     */
    @RequestMapping("/comment/save")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public Out saveComment(HttpServletRequest request) {
        // ??????
        In in = apiService.parseIn(request);
        CommentValue commentValue = in.getFinalValue(CommentValue.class);
        Comment comment = new Comment();
        if (commentValue.getId() == null || commentValue.getId().length() == 0)
            throw new ParameterExection("??????ID");

        comment.setId(IdGenerator.randomUUID());

        // TODO ?????? ?????????Id ???????????????loan,node loan ???????????? node ????????????
        if (StringUtils.isEmpty(commentValue.getType()))
            throw new ParameterExection("type not exist");

        switch (commentValue.getType()) {
            case "loan":
                comment.setLoan(new Loan(commentValue.getId()));
                break;
            case "node":
                comment.setNode(new Node(commentValue.getId()));
                break;
            default:
                throw new ParameterExection("type not exist");
        }
        //????????????????????????????????????
        if (StringUtils.isNotEmpty(commentValue.getPid())) {
            comment.setParentComment(new Comment(commentValue.getPid()));
        }
        comment.setBody(commentValue.getCommentBody());
        comment.setUserByCreator(new User(loadUserFromSecurityContext().getId()));
        comment.setIp(loadUserIpSecurityContext());
        //?????????????????????
        comment.setStatus(CommentConstants.COMMENT_STATUS_ENABLE);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());

        commentService.addComment(comment);
        // ????????????
        Out out = apiService.parseOut(request);
        return out;
    }

    /**
     * ?????????????????????????????????
     *
     * @param request
     * @return
     */
    @RequestMapping("/invest")
    @ResponseBody
    public Out getInvest(HttpServletRequest request) {

        // ??????
        LoanValue loanValue = this.initLoanValue(request);
        // ??????
        apiInvestListService.getExample().getLoan().setId(loanValue.getId());
        apiInvestListService.setCurrentPage(loanValue.getPage().getCurrentPage());
        apiInvestListService.setPageSize(loanValue.getPage().getSize());
        apiInvestListService.addOrder(loanValue.getPage().getOrder());
        // ??????
        ResponsePage page = new ResponsePage(apiInvestListService);

        // ??????????????????
        SimpleModule module = new SimpleModule();
        module.addSerializer(Invest.class, new InvestSerializer());
        // ????????????
        Out out = apiService.parseOut(request);
        out.setResult(page, module);
        return out;

    }

}
