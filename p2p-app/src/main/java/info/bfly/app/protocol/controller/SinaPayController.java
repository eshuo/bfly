package info.bfly.app.protocol.controller;

import com.fasterxml.jackson.databind.module.SimpleModule;
import info.bfly.api.exception.ParameterExection;
import info.bfly.api.exception.SinaPayExection;
import info.bfly.api.model.BaseResource;
import info.bfly.api.model.In;
import info.bfly.api.model.Out;
import info.bfly.api.service.ApiService;
import info.bfly.app.protocol.model.request.AuthenticationValue;
import info.bfly.app.protocol.model.request.BankCardValue;
import info.bfly.app.protocol.model.request.InvestValue;
import info.bfly.app.protocol.model.request.MoneyValue;
import info.bfly.app.protocol.model.serializer.BankCardSerializer;
import info.bfly.app.protocol.service.*;
import info.bfly.archer.common.service.AuthService;
import info.bfly.archer.key.ResponseMsg;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.orders.model.Order;
import info.bfly.p2p.bankcard.model.BankCard;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.coupon.model.UserCoupon;
import info.bfly.p2p.invest.exception.ExceedMaxAcceptableRate;
import info.bfly.p2p.invest.exception.ExceedMoneyNeedRaised;
import info.bfly.p2p.invest.exception.IllegalLoanStatusException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.loan.model.WithdrawCash;
import info.bfly.p2p.trusteeship.exception.TrusteeshipFormException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipTicketException;
import info.bfly.pay.controller.SinaUserController;
import info.bfly.pay.p2p.user.service.SinaWithdrawCashService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 支付接口
 */
@Controller
@RequestMapping("/v1.0")
@Scope(ScopeType.REQUEST)
public class SinaPayController extends BaseResource {

    @Autowired
    private ApiService apiService;


    @Log
    private Logger log;


    @Autowired
    private ApiUserService apiUserService;

    @Value("#{refProperties['redirectAppMainUrl']}")
    private String appMainUrl;

    public String getAppMainUrl() {
        return appMainUrl;
    }

    public void setAppMainUrl(String appMainUrl) {
        this.appMainUrl = appMainUrl;
    }

    private String errorMessage = "";

    /**
     * 实名认证(不走后台审核)
     *
     * @param request
     * @return
     */
    @RequestMapping("/addAuthentication")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and !hasRole('INVESTOR')")
    public Out addAuthentication(HttpServletRequest request) {


        In in = apiService.parseIn(request);

        Out out = apiService.parseOut(request);

        AuthenticationValue authenticationValue = in.getFinalValue(AuthenticationValue.class);

        if (StringUtils.isEmpty(authenticationValue.getUserName()))
            throw new ParameterExection("userName");

        if (StringUtils.isEmpty(authenticationValue.getIdCard()))
            throw new ParameterExection("idCard");

        User user = loadUserFromSecurityContext();
        if (apiUserService.addUserParam(user, authenticationValue)) {
            String types = "";
            try {
                types = apiUserService.registerSinaPay(user);

                if ("success".equals(types)) {
                    log.info("{} {} {}", loadUserFromSecurityContext().getId(), "实名认证(不走后台审核)", "认证成功");
                    return out;
                } else {
                    throw new SinaPayExection(ResponseMsg.SINA_CERTIFICATION_ERROR, "新浪认证返回状态错误");
                }

            } catch (TrusteeshipReturnException e) {
                throw new SinaPayExection(ResponseMsg.SINA_CERTIFICATION_ERROR, e.getMessage());
            }
        } else {
            throw new SinaPayExection(ResponseMsg.SINA_CERTIFICATION_ERROR, "保存数据异常");
        }


    }


    @Autowired
    private ApiPassWordService apiPassWordService;


    /**
     * 查询是否设置交易密码
     *
     * @param request
     * @return
     */
    @RequestMapping("/queryIsPayPassword")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out queryIsPassWord(HttpServletRequest request) {

        boolean bool = apiPassWordService.queryUserSetPassWord(loadUserFromSecurityContext().getId());

        Out out = apiService.parseOut(request);

        out.setResult(bool);

        log.info("{} {} {}", loadUserFromSecurityContext().getId(), "查询是否设置交易密码", "操作成功");
        return out;
    }


    /**
     * 设置交易密码
     *
     * @param request
     * @return
     */
    @RequestMapping("/setPayPassword")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out setPayPassword(HttpServletRequest request) {

        String url = null;
        Out out = apiService.parseOut(request);

        try {
            url = apiPassWordService.payPassword(loadUserFromSecurityContext().getId());

            if (StringUtils.isNotEmpty(url)) {
                out.setResultCode(ResponseMsg.REDIRECTURL);
                out.setResult(getAppMainUrl() + url);
                log.info("{} {} {}", loadUserFromSecurityContext().getId(), "设置交易密码", "操作成功");
                return out;
            } else {
                log.error("{} {} {}", loadUserFromSecurityContext().getId(), "设置交易密码", "交易密码操作错误,返回参数空");
                throw new SinaPayExection(ResponseMsg.PAY_PWD_ERROR, "返回参数空");
            }

        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.PAY_PWD_ERROR, e.getMessage());
        }


    }

    /**
     * 修改交易密码
     *
     * @param request
     * @return
     */
    @RequestMapping("/modifyPayPassword")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out modifyPayPassword(HttpServletRequest request) {

        String url = null;
        Out out = apiService.parseOut(request);

        try {
            url = apiPassWordService.payPassword(loadUserFromSecurityContext().getId());

            if (StringUtils.isNotEmpty(url)) {
                out.setResultCode(ResponseMsg.REDIRECTURL);
                out.setResult(getAppMainUrl() + url);
                log.info("{} {} {}", loadUserFromSecurityContext().getId(), "修改交易密码", "操作成功");
                return out;
            } else {
                log.error("{} {} {}", loadUserFromSecurityContext().getId(), "修改交易密码", "交易密码操作错误,返回参数空");
                throw new SinaPayExection(ResponseMsg.PAY_PWD_ERROR, "返回参数空");
            }

        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.PAY_PWD_ERROR, e.getMessage());
        }


    }

    /**
     * 找回修改密码
     *
     * @param request
     * @return
     */
    @RequestMapping("/findPayPassword")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out findPayPassword(HttpServletRequest request) {

        String url = null;
        Out out = apiService.parseOut(request);

        try {
            url = apiPassWordService.findPayPassword(loadUserFromSecurityContext().getId());

            if (StringUtils.isNotEmpty(url)) {
                out.setResultCode(ResponseMsg.REDIRECTURL);
                out.setResult(getAppMainUrl() + url);
                log.info("{} {} {}", loadUserFromSecurityContext().getId(), "找回交易密码", "操作成功");
                return out;
            } else {
                log.error("{} {} {}", loadUserFromSecurityContext().getId(), "找回交易密码", "交易密码操作错误,返回参数空");
                throw new SinaPayExection(ResponseMsg.PAY_PWD_ERROR, "返回参数空");
            }

        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.PAY_PWD_ERROR, e.getMessage());
        }
    }


    @Autowired
    private ApiBankCardListService apiBankCardListService;


    /**
     * 查询绑定银行卡
     *
     * @return
     */
    @RequestMapping("/queryBankCard")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out queryBankCard(HttpServletRequest request) {

        List<BankCard> bankCards = apiBankCardListService.getBankCardByUser(loadUserFromSecurityContext());
        Out out = apiService.parseOut(request);
        out.setResult(bankCards, new SimpleModule().addSerializer(BankCard.class, new BankCardSerializer()));
        return out;

    }


    @Resource
    SinaUserController sinaUserController;


    /**
     * 绑定银行卡
     *
     * @return
     */
    @RequestMapping("/addBankCard")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out addBankCard(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        BankCardValue bankCardValue = in.getFinalValue(BankCardValue.class);

        if (StringUtils.isEmpty(bankCardValue.getBankCity()))
            throw new ParameterExection("bankCity");

        if (StringUtils.isEmpty(bankCardValue.getBankNo()))
            throw new ParameterExection("bankNo");

        if (StringUtils.isEmpty(bankCardValue.getBankProvince()))
            throw new ParameterExection("bankProvince");

        if (StringUtils.isEmpty(bankCardValue.getCardNo()))
            throw new ParameterExection("cardNo");
        if (bankCardValue.getCardNo().length() < 16)
            throw new ParameterExection(ResponseMsg.PARSE_VALUE_ERROR, "cardNo");

        if (StringUtils.isEmpty(bankCardValue.getBindPhone()))
            throw new ParameterExection("bindPhone");


        BankCard bankCard = new BankCard();

        bankCard.setUser(loadUserFromSecurityContext());
        bankCard.setBankCity(bankCardValue.getBankCity());
        bankCard.setBankNo(bankCardValue.getBankNo());
        bankCard.setBankProvince(bankCardValue.getBankProvince());
        bankCard.setCardNo(bankCardValue.getCardNo());
        bankCard.setBindPhone(bankCardValue.getBindPhone());

        String cardId = null;
        Out out = apiService.parseOut(request);

        try {

            cardId = sinaUserController.bindBankCardSinaPay(bankCard);

            if (StringUtils.isNotEmpty(cardId)) {
                out.setResultCode(ResponseMsg.NEED_CONTINUE_REQUEST);
                out.setResult(cardId);
                log.info("{} {} {}", loadUserFromSecurityContext().getId(), "绑定银行卡", "操作成功，请继续完成绑卡推进操作");
                return out;
            } else {
                throw new SinaPayExection(ResponseMsg.ADDBANKCARD_ERROR, "返回cardId为空");
            }

        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.ADDBANKCARD_ERROR, e.getMessage());
        }

    }

    /**
     * 推进绑定银行卡
     *
     * @return
     */
    @RequestMapping("/addingBankCard")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out addingBankCard(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        BankCardValue bankCardValue = in.getFinalValue(BankCardValue.class);

        if (StringUtils.isEmpty(bankCardValue.getCardId()))
            throw new ParameterExection("cardId");

        if (StringUtils.isEmpty(bankCardValue.getCode()))
            throw new ParameterExection("code");

        String types = null;
        Out out = apiService.parseOut(request);

        log.info("{} {} {}", loadUserFromSecurityContext().getId(), "推进绑定银行卡", errorMessage);
        try {
            types = sinaUserController.bindingBankCardAdvanceSinaPay(bankCardValue.getCardId(), bankCardValue.getCode());

            if ("success".equals(types)) {
                return out;
            } else {
                throw new SinaPayExection(ResponseMsg.ADDBANKCARD_ERROR, "推进绑卡返回状态错误");
            }

        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.ADDBANKCARD_ERROR, e.getMessage());
        }

    }

    /**
     * 解绑银行卡
     *
     * @param request
     * @return
     */
    @RequestMapping("/removeBankCard")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out removeBankCard(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        BankCardValue bankCardValue = in.getFinalValue(BankCardValue.class);

        if (StringUtils.isEmpty(bankCardValue.getCardId()))
            throw new ParameterExection("cardId");

        Out out = apiService.parseOut(request);

        String types = "";

        try {
            types = sinaUserController.unbindingBankCardSinaPay(bankCardValue.getCardId());

            if ("success".equals(types)) {
                out.setResultCode(ResponseMsg.NEED_CONTINUE_REQUEST);
                log.info("{} {} {}", loadUserFromSecurityContext().getId(), "解绑银行卡", "操作成功，请继续完成解绑银行卡推进操作");
                return out;
            } else {
                throw new SinaPayExection(ResponseMsg.REMOVE_BANKCARD_ERROR, "返回状态错误");
            }
        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.REMOVE_BANKCARD_ERROR, e.getMessage());
        }

    }

    /**
     * 解绑银行卡推进
     *
     * @param request
     * @return
     */
    @RequestMapping("/removeingBankCard")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out removeingBankCard(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        BankCardValue bankCardValue = in.getFinalValue(BankCardValue.class);

        if (StringUtils.isEmpty(bankCardValue.getCardId()))
            throw new ParameterExection("cardId");

        if (StringUtils.isEmpty(bankCardValue.getCode()))
            throw new ParameterExection("code");

        String types = null;
        Out out = apiService.parseOut(request);

        try {
            types = sinaUserController.unbindingBankCardAdvanceSinaPay(bankCardValue.getCardId(), bankCardValue.getCode());

            if ("success".equals(types)) {
                log.info("{} {} {}", loadUserFromSecurityContext().getId(), "解绑银行卡推进", "操作成功");
                return out;

            } else {
                throw new SinaPayExection(ResponseMsg.REMOVE_BANKCARD_ERROR, "推进绑卡返回状态错误");
            }
        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.REMOVE_BANKCARD_ERROR, e.getMessage());
        }

    }


    @Autowired
    private ApiSinaInvestHomeService apiSinaInvestHomeService;

    /**
     * 创建托管代收交易
     * 投资
     *
     * @return
     */
    @RequestMapping("/createSinaTrade")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out createHostingCollectTradeSinaPay(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        Out out = apiService.parseOut(request);

        InvestValue investValue = in.getFinalValue(InvestValue.class);
        //如果订单investID不为空，继续支付订单
        if (StringUtils.isNotEmpty(investValue.getInvestId())) {
            try {
                out.setResultCode(ResponseMsg.REDIRECTURL);
                out.setResult(getAppMainUrl() + apiSinaInvestHomeService.addRepay(investValue.getInvestId()));
                log.info("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资继续支付", "操作成功");
                return out;
            } catch (TrusteeshipReturnException e) {
                throw new SinaPayExection(ResponseMsg.SINA_PAY_ERROR, "第三方资金托管时候,返回的错误信息 " + e.getMessage());
            }
        }

        if (StringUtils.isEmpty(investValue.getLoanId().trim())) {
            throw new ParameterExection("LoanId");
        }
        if (investValue.getInvestMoney() <= 0.0) {
            throw new ParameterExection("investMoney");
        }
        Loan loan = apiSinaInvestHomeService.getBaseService().get(Loan.class, investValue.getLoanId());

        if (loan == null) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "投资项目不存在");
            throw new SinaPayExection(ResponseMsg.CREATESINATRADE_ERROR, "投资项目不存在");
        }

        Invest investInstance = new Invest();

        investInstance.setInvestMoney(investValue.getInvestMoney());
        investInstance.setMoney(investValue.getInvestMoney());

        investInstance.setLoan(loan);

        User user = loadUserFromSecurityContext();

        investInstance.setUser(user);

        if (investValue.getOrderId() != null) {
            investInstance.setOrder(apiSinaInvestHomeService.getBaseService().get(Order.class, investValue.getOrderId()));
        }

        if (StringUtils.isNotEmpty(investValue.getCouponId())) {
            UserCoupon userCoupon = apiSinaInvestHomeService.getBaseService().get(UserCoupon.class, investValue.getCouponId());
            if (userCoupon != null)
                investInstance.setUserCoupon(userCoupon);
        }

        String url = "";
        try {
            url = apiSinaInvestHomeService.appCreateTransactionInvest(investInstance);
        } catch (IllegalLoanStatusException e) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "项目为非购买状态");
            throw new SinaPayExection(ResponseMsg.LOAN_STATUS_ERROR, "项目为非购买状态 ");
        } catch (ExceedDeadlineException e) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "优惠券超过有效期");
            throw new SinaPayExection(ResponseMsg.EXCEEDDEADLINE_ERROR, "优惠券超过有效期 ");
        } catch (ExceedMoneyNeedRaised exceedMoneyNeedRaised) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "投资金额大于尚未募集的金额");
            throw new SinaPayExection(ResponseMsg.CREATESINATRADE_ERROR, "投资金额大于尚未募集的金额 ");
        } catch (ExceedMaxAcceptableRate exceedMaxAcceptableRate) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "投资竞标利率大于借款可接受的最大利率");
            throw new SinaPayExection(ResponseMsg.CREATESINATRADE_ERROR, "投资竞标利率大于借款可接受的最大利率 ");
        } catch (InsufficientBalance insufficientBalance) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "余额不足");
            throw new SinaPayExection(ResponseMsg.BALANCE_ERROR);
        } catch (UnreachedMoneyLimitException e) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "金额未达到优惠券使用条件");
            throw new SinaPayExection(ResponseMsg.EXCEEDDEADLINE_ERROR, "金额未达到优惠券使用条件 ");
        } catch (TrusteeshipReturnException e) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "第三方资金托管时候,返回的错误信息" + e.getMessage());
            throw new SinaPayExection(ResponseMsg.SINA_PAY_ERROR, "第三方资金托管时候,返回的错误信息 " + e.getMessage());
        } catch (Exception e) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "内部错误" + e.getMessage());
            throw new SinaPayExection(ResponseMsg.CREATESINATRADE_ERROR, e.getMessage());
        }


        if (StringUtils.isNotEmpty(url)) {
            out.setResultCode(ResponseMsg.REDIRECTURL);
            out.setResult(getAppMainUrl() + url);
            log.info("{} {} {}", loadUserFromSecurityContext().getId(), "创建托管代收交易投资", "操作成功");
            return out;
        } else {
            throw new SinaPayExection(ResponseMsg.CREATESINATRADE_ERROR, "返回参数空");
        }

    }


    @Autowired
    private ApiSinaRechargeHomeService apiSinaRechargeHomeService;


    /**
     * 托管充值
     *
     * @param request
     * @return
     */
    @RequestMapping("/createHostingDeposit")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out createHostingDeposit(HttpServletRequest request) {

        In in = apiService.parseIn(request);

        MoneyValue moneyValue = in.getFinalValue(MoneyValue.class);

        if (Double.valueOf(moneyValue.getMoney()) <= 0.0)
            throw new ParameterExection("moeny");

        Recharge recharge = new Recharge();
        recharge.setActualMoney(Double.valueOf(moneyValue.getMoney()));
        recharge.setUser(loadUserFromSecurityContext());

        String url = "";
        try {
            url = apiSinaRechargeHomeService.createHostingDeposit(recharge, moneyValue.getRechargeId());
        } catch (ExceedDeadlineException e) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "托管充值", "优惠券超过有效期");
            throw new SinaPayExection(ResponseMsg.EXCEEDDEADLINE_ERROR, "优惠券超过有效期");
        } catch (UnreachedMoneyLimitException e) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "托管充值", "金额未达到优惠券使用条件");
            throw new SinaPayExection(ResponseMsg.EXCEEDDEADLINE_ERROR, "金额未达到优惠券使用条件");
        } catch (TrusteeshipFormException e) {
            //TODO 返回表单数据
        } catch (TrusteeshipTicketException e) {
            //TODO 需要推进
        } catch (TrusteeshipReturnException e) {
           //TODO 请求错误
        }

        Out out = apiService.parseOut(request);

        if (StringUtils.isNotEmpty(url)) {
            out.setResultCode(ResponseMsg.REDIRECTURL);
            out.setResult(getAppMainUrl() + url);
            log.info("{} {} {}", loadUserFromSecurityContext().getId(), "托管充值", "操作成功");
            return out;
        } else {
            throw new SinaPayExection(ResponseMsg.RECHARGE_ERROR, "返回参数空");
        }
    }


    @Autowired
    private SinaWithdrawCashService withdrawCashService;

    /**
     * 申请提现
     *
     * @param request
     * @return
     */
    @RequestMapping("/applyWithdraw")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out applyWithdraw(HttpServletRequest request) {
        In in = apiService.parseIn(request);
        MoneyValue moneyValue = in.getFinalValue(MoneyValue.class);
        if (Double.valueOf(moneyValue.getMoney()) <= 0.0) {
            throw new ParameterExection("moeny");
        }
        Out out = apiService.parseOut(request);
        try {
            WithdrawCash withdrawCash = withdrawCashService.generateWithdraw(Double.valueOf(moneyValue.getMoney()), loadUserFromSecurityContext());
            withdrawCashService.applyWithdrawCash(withdrawCash);
        } catch (InsufficientBalance insufficientBalance) {
            log.error("{} {} {}", loadUserFromSecurityContext().getId(), "申请提现", "余额不足");
            throw new SinaPayExection(ResponseMsg.WITHDRAW_ERROR, "余额不足");
        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.WITHDRAW_ERROR, "返回参数异常");
        }
        log.info("{} {} {}", loadUserFromSecurityContext().getId(), "申请提现", "操作成功，等待后台审核通过");
        return out;
    }

    /**
     * 确认提现
     *
     * @param request
     * @return
     */
    @RequestMapping("/commitWithdraw")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out commitWithdraw(HttpServletRequest request) {

        In in = apiService.parseIn(request);
        MoneyValue moneyValue = in.getFinalValue(MoneyValue.class);
        if (StringUtils.isEmpty(moneyValue.getWithdrawId()))
            throw new ParameterExection("withdrawId");
        Out out = apiService.parseOut(request);
        String url = null;
        try {
            url = withdrawCashService.getWithdrawLink(moneyValue.getWithdrawId());
            if (StringUtils.isNotEmpty(url)) {
                out.setResultCode(ResponseMsg.REDIRECTURL);
                out.setResult(getAppMainUrl() + url);
                log.info("{} {} {}", loadUserFromSecurityContext().getId(), "确认提现", "操作成功");
                return out;
            } else {
                throw new SinaPayExection(ResponseMsg.WITHDRAW_ERROR, "返回参数空");
            }
        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.WITHDRAW_ERROR, e.getMessage());
        }
    }

    /**
     * Sina页面用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/showMemberInfosSina")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public Out showMemberInfosSina(HttpServletRequest request) {
        Out out = apiService.parseOut(request);

        String url = "";

        try {
            url = apiUserService.showMemberInfosSina(loadUserFromSecurityContext().getId());

            if (StringUtils.isNotEmpty(url)) {
                out.setResultCode(ResponseMsg.REDIRECTURL);
                out.setResult(getAppMainUrl() + url);
                log.info("{} {} {}", loadUserFromSecurityContext().getId(), "Sina页面用户信息", "操作成功");
                return out;
            } else {
                throw new SinaPayExection(ResponseMsg.SHOWMEMBERINFOSSINA_ERROR, "返回参数空");
            }

        } catch (TrusteeshipReturnException e) {
            throw new SinaPayExection(ResponseMsg.SHOWMEMBERINFOSSINA_ERROR, e.getMessage());
        }
    }


    @Autowired
    private AuthService authService;


    /**
     * @param target
     * @param authType
     * @Date 2017/6/8 0008 14:44
     * @Description：根据参数获取Sina页面
     */
    @RequestMapping("/redirect/{target}/{authType}")
    @PreAuthorize("hasRole('USER')")
    public String getRedirect(@PathVariable String target, @PathVariable String authType) {

        User user = loadUserFromSecurityContext();
        String authCode = authService.getAuthCode(user.getId(), target, authType);

        try {
            authService.verifyAuthInfo(user.getId(), target, "", authType);
        } catch (Exception e) {
            throw new SinaPayExection(e.getMessage());
        }
        return "redirect:" + authCode;
    }

    /**
     * @param param
     * @Date 2017/6/8 0008 14:44
     * @Description：
     */
    public String returnFrom(String param) {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /></head><body>"
                + "<form id=\"form1\" action='" + param + "' method=\"POST\"></form>"
                + "</body><script language=\"javascript\"> form1.submit();</script></html>";
        return html;
    }


}
