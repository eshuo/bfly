package info.bfly.pay.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.exception.AuthInfoAlreadyInColdException;
import info.bfly.archer.common.service.AuthService;
import info.bfly.archer.common.service.BaseService;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.UserBillConstants;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.service.impl.UserBillBO;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.DateUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.model.InvestRefunds;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.loan.model.WithdrawCash;
import info.bfly.p2p.loan.service.LoanService;
import info.bfly.p2p.repay.model.InvestRepay;
import info.bfly.p2p.repay.model.LoanRepay;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.exception.TrusteeshipFormException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipTicketException;
import info.bfly.p2p.trusteeship.model.TrusteeshipAccount;
import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;
import info.bfly.p2p.trusteeship.service.TrusteeshipAccountService;
import info.bfly.p2p.user.service.WithdrawCashService;
import info.bfly.pay.bean.SinaAPI;
import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.callback.*;
import info.bfly.pay.bean.enums.*;
import info.bfly.pay.bean.order.*;
import info.bfly.pay.bean.order.param.*;
import info.bfly.pay.p2p.user.model.MoneyTransfer;
import info.bfly.pay.service.impl.OrderOperationService;
import info.bfly.pay.util.SinaUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ??????????????? 2017/2/16 0016.
 */
@Controller
@Scope(ScopeType.PROTOTYPE)
public class SinaOrderController implements Serializable {

    private static final long serialVersionUID = -8983410846453142549L;
    @Autowired
    private LoginUserInfo userInfo;
    @Autowired
    private SinaUtils     sinaUtils;
    @Log
    Logger            log;
    @Resource
    HibernateTemplate ht;
    @Autowired
    private AuthService authService;

    @Value("#{refProperties['redirectMainUrl']}")
    private String mainUrl;

    @Value("#{refProperties['sinapay_partner_account_type']}")
    private String partner_account_type;
    @Value("#{refProperties['sinapay_partner_identity_id']}")
    private String partner_identity_id;
    @Value("#{refProperties['sinapay_partner_identity_type']}")
    private String partner_identity_type;


    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }


    /**
     * ??????RUL????????????
     *
     * @param source
     * @param target
     * @param deadline
     * @param authType
     * @param code
     * @return
     */
    private String returnAuthCode(String source, String target, Date deadline, String authType, String code) {

        try {
            authService.createAuthInfo(source, target, deadline, authType, code);
        } catch (AuthInfoAlreadyInColdException e) {
            log.error(e.getMessage());
        }
        return target + "/" + authType;
    }


    //--------------3.1    ????????????????????????Start
    @Autowired
    private OrderOperationService<CreateHostingCollectTradeSinaInEntity, CreateHostingCollectTradeSinaEntity> CHCTService;


    @Resource
    InvestService investService;


    /**
     * ????????????????????????
     * ??????
     *
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String createHostingCollectTradeSinaPay(Invest invest) throws TrusteeshipReturnException {
        CreateHostingCollectTradeSinaEntity chct = CHCTService.getRequestEntity(CreateHostingCollectTradeSinaEntity.class);
        invest.setStatus(InvestConstants.InvestStatus.WAIT_AFFIRM);
        ht.saveOrUpdate(invest);
        chct.setService(SinaAPI.CREATE_HOSTING_COLLECT_TRADE.getService_name());
        chct.setOut_trade_no(IdGenerator.randomUUID());
        chct.setOut_trade_code(OUT_TRADE_CODE.TO_LOAN_1);
        chct.setGoods_id(invest.getLoan().getSina_bid_no());
        chct.setSummary("??????" + invest.getLoan().getName());
        TrusteeshipAccount trusteeshipAccount = trusteeshipAccountService.getTrusteeshipAccount(invest.getUser().getId(), TrusteeshipConstants.Trusteeship.SINAPAY);
        Assert.notNull(trusteeshipAccount, "?????????????????????????????????");
        chct.setPayer_id(trusteeshipAccount.getAccountId());
        chct.setPayer_identity_type(trusteeshipAccount.getType());
        chct.setCan_repay_on_failed("N");
        chct.setTrade_close_time("10m");
        chct.setNotify_url(chct.getNotify_url() + "?sinaPnsVersion=v0.1");
       // chct.setCollect_trade_type("pre_auth");
//        chct.setReturn_url(url);

        PayMethodParam onlineBank = PayMethodParam.ONLINE_BANK;
        onlineBank.setExtendParam("SINAPAY,DEBIT,C");
        onlineBank.setPayMoney(BigDecimal.valueOf(invest.getInvestMoney()));
        chct.setPay_method(sinaUtils.castBeanToSinaString(onlineBank, "^", "payType", "payMoney", "extendParam"));
        TrusteeshipOperation operation = CHCTService.createOperation(chct, TrusteeshipConstants.OperationType.INVEST, invest.getId(), chct.getTrade_close_time());
        //????????????????????????????????????
        chct.setOut_trade_no(operation.getId());
        CHCTService.updateOperation(chct, operation);
        //????????????
        CreateHostingCollectTradeSinaInEntity sinaIn = CHCTService.sendHttpClientOperation(chct, operation, CreateHostingCollectTradeSinaInEntity.class);
        //?????????????????????
        log.info("{} do {} return {} with {}", userInfo.getLoginUserId(), SinaAPI.CREATE_HOSTING_COLLECT_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            if (StringUtils.isNotEmpty(sinaIn.getRedirect_url())) {
                return returnAuthCode(invest.getUser().getId(), operation.getId(), DateUtil.addMinute(operation.getRequestTime(), 10), chct.getService(), sinaIn.getRedirect_url());
            } else {
                throw new TrusteeshipReturnException(sinaIn.getResponse_message());
            }
        } else
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
    }

    @Autowired
    UserBillBO userBillBO;

    /**
     * ????????????????????????
     * ??????
     *
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String createHostingCollectTradeSinaPay(LoanRepay loanRepay) throws TrusteeshipReturnException, InsufficientBalance {
        CreateHostingCollectTradeSinaEntity chct = CHCTService.getRequestEntity(CreateHostingCollectTradeSinaEntity.class);
        Double allRepayMoney = ArithUtil.add(loanRepay.getCorpus(),
                loanRepay.getDefaultInterest(), loanRepay.getFee(), loanRepay.getInterest());
        if (loanRepay.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
            // ??????????????????
            userBillBO.freezeMoney(loanRepay.getLoan().getUser().getId(), allRepayMoney, UserBillConstants.OperatorInfo.NORMAL_REPAY, "????????????????????????????????????"
                    + loanRepay.getId());
            loanRepay.setStatus(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY);
            ht.update(loanRepay);
        }

        if (loanRepay.getStatus().equals(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY)) {
            chct.setService(SinaAPI.CREATE_HOSTING_COLLECT_TRADE.getService_name());
            chct.setOut_trade_no(IdGenerator.randomUUID());
            chct.setOut_trade_code(OUT_TRADE_CODE.OUT_LOAN_1);
            chct.setGoods_id(loanRepay.getLoan().getSina_bid_no());
            chct.setSummary("??????" + loanRepay.getLoan().getName());
            TrusteeshipAccount trusteeshipAccount = trusteeshipAccountService.getTrusteeshipAccount(loanRepay.getLoan().getUser().getId(), TrusteeshipConstants.Trusteeship.SINAPAY);
            Assert.notNull(trusteeshipAccount, "?????????????????????????????????");
            chct.setPayer_id(trusteeshipAccount.getAccountId());
            chct.setPayer_identity_type(trusteeshipAccount.getType());
            chct.setCan_repay_on_failed("N");
            chct.setTrade_close_time("10m");
            chct.setNotify_url(chct.getNotify_url() + "?sinaPnsVersion=v0.1");
           //chct.setCollect_trade_type("pre_auth");
            PayMethodParam onlineBank = PayMethodParam.ONLINE_BANK;
            onlineBank.setExtendParam("SINAPAY,DEBIT,C");
            onlineBank.setPayMoney(BigDecimal.valueOf(allRepayMoney));
            chct.setPay_method(sinaUtils.castBeanToSinaString(onlineBank, "^", "payType", "payMoney", "extendParam"));
            TrusteeshipOperation operation = CHCTService.createOperation(chct, TrusteeshipConstants.OperationType.REPAY, loanRepay.getId(), chct.getTrade_close_time());
            //????????????????????????????????????
            chct.setOut_trade_no(operation.getId());
            CHCTService.updateOperation(chct, operation);
            //????????????
            CreateHostingCollectTradeSinaInEntity sinaIn = CHCTService.sendHttpClientOperation(chct, operation, CreateHostingCollectTradeSinaInEntity.class);
            //?????????????????????
            log.info("{} do {} return {} with {}", userInfo.getLoginUserId(), SinaAPI.CREATE_HOSTING_COLLECT_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
            if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
                if (StringUtils.isNotEmpty(sinaIn.getRedirect_url())) {
                    //FacesUtil.sendRedirect(sinaIn.getRedirect_url());
                    return returnAuthCode(loanRepay.getLoan().getUser().getId(), operation.getId(), DateUtil.addMinute(operation.getRequestTime(), 10), chct.getService(), sinaIn.getRedirect_url());

                }
            } else if (sinaIn.getResponse_code().equals(RESPONSE_CODE.HTML_RESPONSE)) {
                return sinaIn.getResponse_message();
            } else {
                CHCTService.refuse(operation.getId());
                throw new TrusteeshipReturnException(sinaIn.getResponse_message());
            }
        }

        return "";
    }

    /**
     * ????????????????????????
     * ??????
     *
     * @param transfer ????????????
     * @return ????????????
     * @throws TrusteeshipReturnException ?????????????????????????????????
     * @throws TrusteeshipFormException   ????????????????????????form??????
     * @throws TrusteeshipTicketException ?????????????????????????????????
     */
    public String createHostingCollectTradeSinaPay(MoneyTransfer transfer) throws TrusteeshipReturnException, TrusteeshipFormException, TrusteeshipTicketException {

        CreateHostingCollectTradeSinaEntity chct = CHCTService.getRequestEntity(CreateHostingCollectTradeSinaEntity.class);
        chct.setService(SinaAPI.CREATE_HOSTING_COLLECT_TRADE.getService_name());
        chct.setOut_trade_code(OUT_TRADE_CODE.TO_LOAN_1);
        chct.setSummary("????????????" + transfer.getId());
        TrusteeshipAccount trusteeshipAccount = trusteeshipAccountService.getTrusteeshipAccount(transfer.getFromUser().getId(), TrusteeshipConstants.Trusteeship.SINAPAY);
        Assert.notNull(trusteeshipAccount, "?????????????????????????????????");
        chct.setPayer_id(trusteeshipAccount.getAccountId());
        chct.setPayer_identity_type(trusteeshipAccount.getType());
        chct.setPayer_ip(sinaClientIp);
        chct.setCan_repay_on_failed("N");
        chct.setTrade_close_time("10m");
        chct.setNotify_url(chct.getNotify_url() + "?sinaPnsVersion=v0.1");
        PayMethodParam payMethod = getPayMethod(transfer.getFormAccountType());
        payMethod.setPayMoney(BigDecimal.valueOf(transfer.getMoney()));
        chct.setPay_method(sinaUtils.castBeanToSinaString(payMethod, "^", "payType", "payMoney", "extendParam"));
        TrusteeshipOperation operation = CHCTService.createOperation(chct, TrusteeshipConstants.OperationType.MONEY_TRANSFER_FROM, transfer.getId(), chct.getTrade_close_time());
        //????????????????????????????????????
        chct.setOut_trade_no(operation.getId());
        CHCTService.updateOperation(chct, operation);
        //????????????
        CreateHostingCollectTradeSinaInEntity sinaIn = CHCTService.sendHttpClientOperation(chct, operation, CreateHostingCollectTradeSinaInEntity.class);
        //?????????????????????
        log.info("{} do {} return {} with {}", userInfo.getLoginUserId(), SinaAPI.CREATE_HOSTING_COLLECT_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        //TODO ????????????????????????
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            transfer.setStatus(UserConstants.MoneyTransferStatus.FROM_PROCESS);
            transfer.setOperationOrderNo(operation.getId());
            moneyTransferService.update(transfer);
            if (StringUtils.isNotEmpty(sinaIn.getRedirect_url()))
                return returnAuthCode(transfer.getFromUser().getId(), operation.getId(), DateUtil.addMinute(operation.getRequestTime(), 10), chct.getService(), sinaIn.getRedirect_url());
            else if (StringUtils.isNotEmpty(sinaIn.getTicket())) {
                String deadtime = StringUtils.defaultString(configService.getConfigValue("money_transfer_ticket_deadtime"), "10m");
                try {
                    authService.createAuthInfo(transfer.getFromUser().getId(), transfer.getId(), getDeadTime(deadtime), CommonConstants.AuthInfoType.RECHARGE, sinaIn.getTicket());
                    throw new TrusteeshipTicketException("?????????????????????");
                } catch (AuthInfoAlreadyInColdException e) {
                    log.error(e.getMessage());
                    throw new TrusteeshipReturnException("??????ticket??????");
                }
            }
        } else if (sinaIn.getResponse_code().equals(RESPONSE_CODE.HTML_RESPONSE)) {
            throw new TrusteeshipFormException(sinaIn.getResponse_message());
        }
        throw new TrusteeshipReturnException("??????????????????????????????");
    }

    //--------------3.1    ????????????????????????End

    private PayMethodParam getPayMethod(String payWay) throws TrusteeshipReturnException {

        PayMethodParam payMethod;
        switch (payWay) {
            case "BASIC":
            case "SAVING_POT":
                payMethod = PayMethodParam.BALANCE;
                payMethod.setExtendParam(payWay);
                break;
            case "online_bank":
                payMethod = PayMethodParam.ONLINE_BANK;
                break;
            case "balance":
                payMethod = PayMethodParam.BALANCE;
                break;
            default:
                if (payWay.matches("^\\d{0,10}$")) {
                    payMethod = PayMethodParam.BINDING_PAY;
                    payMethod.setExtendParam(payWay);
                } else
                    throw new TrusteeshipReturnException("????????????????????????????????????");
        }
        Assert.notNull(payMethod, "????????????????????????");
        return payMethod;
    }

    //--------------3.2    ????????????????????????Start
    @Autowired
    private OrderOperationService<CreateSingleHostingPayTradeSinaInEntity, CreateSingleHostingPayTradeSinaEntity> CSHPTService;


    /**
     * ????????????????????????
     * ??????
     *
     * @return
     */
    @Transactional
    public String createSingleHostingPayTradeSinaPay(Loan loan, Double money, ACCOUNT_TYPE accountType) {
        CreateSingleHostingPayTradeSinaEntity cshpt = CSHPTService.getRequestEntity(CreateSingleHostingPayTradeSinaEntity.class);
        cshpt.setService(SinaAPI.CREATE_SINGLE_HOSTING_PAY_TRADE.getService_name());
        cshpt.setOut_trade_no(IdGenerator.randomUUID());
        cshpt.setOut_trade_code(OUT_TRADE_CODE.TO_LOAN_2);
        cshpt.setPayee_identity_id(loan.getUser().getId());
        cshpt.setAccount_type(ACCOUNT_TYPE.valueOf(StringUtils.defaultIfEmpty(loan.getAccountType(), "BASIC")));
        //TODO ????????????
        cshpt.setAmount(BigDecimal.valueOf(money));
        cshpt.setSummary("??????" + loan.getName());
        cshpt.setGoods_id(loan.getSina_bid_no());
        cshpt.setUser_ip(sinaClientIp);

        if (loan.getLoanGuranteeFee() > 0) {
            SplitParam feeAccount = new SplitParam(loan.getUser().getId(), "UID", accountType.getType_name(), partner_identity_id, partner_identity_type, partner_account_type, BigDecimal.valueOf(loan.getLoanGuranteeFee()), "???????????????");
            cshpt.setSplit_list(sinaUtils.castBeanToSinaString(feeAccount, "^", "payer_identity", "payer_type", "payer_account_type", "payee_identity", "payee_type", "payee_account_type", "amount", "remarks"));
        }
        TrusteeshipOperation operation = CSHPTService.createOperation(cshpt, TrusteeshipConstants.OperationType.GIVE_MOENY_TO_BORROWER, loan.getId());
        cshpt.setOut_trade_no(operation.getId());
        operation = CSHPTService.updateOperation(cshpt, operation);
        CreateSingleHostingPayTradeSinaInEntity sinaIn = CSHPTService.sendHttpClientOperation(cshpt, operation, CreateSingleHostingPayTradeSinaInEntity.class);
        log.info("{} do {} return {} with {}", userInfo.getLoginUserId(), SinaAPI.CREATE_SINGLE_HOSTING_PAY_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            loan.setStatus(LoanConstants.LoanStatus.SINA_RECHECK_COMPLETE);
            loanService.update(loan);
        } else {
            CSHPTService.refuse(operation.getId());
        }

        return "";
    }


    /**
     * ?????????????????????
     */
    @Value("#{refProperties['sinapay_client_ip']}")
    private String sinaClientIp = "127.0.0.1";

    @Autowired
    private BaseService<MoneyTransfer> moneyTransferService;

    @Transactional
    public void createSingleHostingPayTradeSinaPay(MoneyTransfer transfer) throws TrusteeshipReturnException {
        CreateSingleHostingPayTradeSinaEntity cshpt = CSHPTService.getRequestEntity(CreateSingleHostingPayTradeSinaEntity.class);
        cshpt.setService(SinaAPI.CREATE_SINGLE_HOSTING_PAY_TRADE.getService_name());
        cshpt.setOut_trade_no(IdGenerator.randomUUID());
        cshpt.setOut_trade_code(OUT_TRADE_CODE.TO_LOAN_2);
        cshpt.setPayee_identity_id(transfer.getToUser().getId());
        //TODO ????????????
        cshpt.setAmount(BigDecimal.valueOf(transfer.getMoney()));
        cshpt.setAccount_type(ACCOUNT_TYPE.valueOf(transfer.getToAccountType()));
        cshpt.setSummary("??????" + transfer.getId());
        cshpt.setUser_ip(sinaClientIp);
        TrusteeshipAccount account = trusteeshipAccountService.getTrusteeshipAccount(transfer.getToUser().getId(), TrusteeshipConstants.Trusteeship.SINAPAY);
        Assert.notNull(account);
        if (transfer.getFee() > 0) {
            TrusteeshipAccount system = trusteeshipAccountService.getSystemTrusteeshipAccount(TrusteeshipConstants.Trusteeship.SINAPAY);
            Assert.notNull(system);
            SplitParam feeAccount = new SplitParam(account.getAccountId(), account.getType(), transfer.getToAccountType(), system.getAccountId(), system.getType(), system.getDefaultAccountType(), BigDecimal.valueOf(transfer.getFee()), "???????????????");
            cshpt.setSplit_list(sinaUtils.castBeanToSinaString(feeAccount, "^", "payer_identity", "payer_type", "payer_account_type", "payee_identity", "payee_type", "payee_account_type", "amount", "remarks"));
        }
        TrusteeshipOperation operation = CSHPTService.createOperation(cshpt, TrusteeshipConstants.OperationType.MONEY_TRANSFER_TO, transfer.getId());
        cshpt.setOut_trade_no(operation.getId());
        operation = CSHPTService.updateOperation(cshpt, operation);
        CreateSingleHostingPayTradeSinaInEntity sinaIn = CSHPTService.sendHttpClientOperation(cshpt, operation, CreateSingleHostingPayTradeSinaInEntity.class);
        log.info("{} do {} return {} with {}", userInfo.getLoginUserId(), SinaAPI.CREATE_SINGLE_HOSTING_PAY_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            transfer.setStatus(UserConstants.MoneyTransferStatus.TO_PROCESS);
            moneyTransferService.update(transfer);
            //????????????
        } else {
            transfer.setStatus(UserConstants.MoneyTransferStatus.FAIL);
            moneyTransferService.update(transfer);
            CSHPTService.refuse(operation.getId());
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
        }

    }


    /**
     * ????????????????????????
     * ????????????
     *
     * @return
     */
    @Transactional
    public String createOneSingleHostingPayTradeSinaPay(Invest invest, ACCOUNT_TYPE accountType) throws TrusteeshipReturnException {
        CreateSingleHostingPayTradeSinaEntity cshpt = CSHPTService.getRequestEntity(CreateSingleHostingPayTradeSinaEntity.class);
        if (invest.getStatus().equals(InvestConstants.InvestStatus.BID_SUCCESS)) {
            Loan loan = invest.getLoan();
            cshpt.setService(SinaAPI.CREATE_SINGLE_HOSTING_PAY_TRADE.getService_name());
            cshpt.setOut_trade_no(IdGenerator.randomUUID());
            cshpt.setOut_trade_code(OUT_TRADE_CODE.TO_LOAN_2);
            cshpt.setPayee_identity_id(loan.getUser().getId());
            //TODO ????????????
            cshpt.setAmount(BigDecimal.valueOf(invest.getInvestMoney()));
            cshpt.setSummary("????????????" + loan.getName());
            cshpt.setGoods_id(loan.getSina_bid_no());
            cshpt.setUser_ip(userInfo.getRemoteAddr());

            if (loan.getLoanGuranteeFee() > 0) {
                SplitParam feeAccount = new SplitParam(loan.getUser().getId(), "UID", accountType.getType_name(), partner_identity_id, partner_identity_type, partner_account_type, BigDecimal.valueOf(0), "?????????????????????");
                cshpt.setSplit_list(sinaUtils.castBeanToSinaString(feeAccount, "^", "payer_identity", "payer_type", "payer_account_type", "payee_identity", "payee_type", "payee_account_type", "amount", "remarks"));
            }
            TrusteeshipOperation operation = CSHPTService.createOperation(cshpt, TrusteeshipConstants.OperationType.ONE_GIVE_MOENY_TO_BORROWER, invest.getId());
            cshpt.setOut_trade_no(operation.getId());
            operation = CSHPTService.updateOperation(cshpt, operation);
            CreateSingleHostingPayTradeSinaInEntity sinaIn = CSHPTService.sendHttpClientOperation(cshpt, operation, CreateSingleHostingPayTradeSinaInEntity.class);
            log.info("{} do {} return {} with {}", userInfo.getLoginUserId(), SinaAPI.CREATE_SINGLE_HOSTING_PAY_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
            if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
                invest.setStatus(InvestConstants.InvestStatus.ONE_SINAPAY_WAIT);
                investService.update(invest);
            } else {
                CSHPTService.refuse(operation.getId());
            }
        } else {
            throw new TrusteeshipReturnException("?????????????????????");
        }

        return "";
    }


    //--------------3.2    ????????????????????????End

    //--------------3.3    ??????????????????????????????Strat


    @Autowired
    private OrderOperationService<SinaInEntity, CreateBatchHostingPayTradeSinaEntity> CBHPTService;


    /**
     * ??????????????????????????????
     * ????????????????????????????????????
     *
     * @return
     */
    @Transactional
    public String createBatchHostingPayTradeSinaPay(LoanRepay lr, ACCOUNT_TYPE accountType) {
        CreateBatchHostingPayTradeSinaEntity cbhpt = CBHPTService.getRequestEntity(CreateBatchHostingPayTradeSinaEntity.class);
        cbhpt.setService(SinaAPI.CREATE_BATCH_HOSTING_PAY_TRADE.getService_name());
        List<InvestRepay> irs = (List<InvestRepay>) ht.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?", lr.getLoan().getId(), lr.getPeriod());
        List<TradeListParam> trade_list = new ArrayList<>();
        for (int i = 0; i < irs.size(); i++) {
            InvestRepay ir = irs.get(i);
            TradeListParam param = new TradeListParam();
            param.setAmount(BigDecimal.valueOf(ArithUtil.add(ir.getCorpus(), ir.getInterest())));
            param.setOut_trade_no(IdGenerator.randomUUID());
            param.setPayee_identity(ir.getInvest().getUser().getId());
            param.setPayee_identity_type(IDENTITY_TYPE.UID.getType_name());
            param.setAccount_type(accountType.getType_name());
            param.setSummary("????????????");
            param.setGoods_id(ir.getInvest().getLoan().getSina_bid_no());
            if (ir.getFee() > 0) {
                SplitParam feeAccount = new SplitParam(ir.getInvest().getUser().getId(), "UID", accountType.getType_name(), partner_identity_id, partner_identity_type, partner_account_type, BigDecimal.valueOf(ir.getFee()), "?????????");
                param.setSplit_list(sinaUtils.castBeanToSinaString(feeAccount, "^", "payer_identity", "payer_type", "payer_account_type", "payee_identity", "payee_type", "payee_account_type", "amount", "remarks"));
            }
            param.setExtend_param("notify_type^sync");
            TrusteeshipOperation operation = new TrusteeshipOperation();
            operation.setId(param.getOut_trade_no());
            operation.setMarkId(ir.getId());
            operation.setStatus(TrusteeshipConstants.OperationStatus.PREPARE);
            operation.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
            operation.setType(TrusteeshipConstants.OperationType.FINISH_PRE_AUTH_TRADE);
            operation.setOperator(userInfo.getLoginUserId());
            operation.setRequestTime(new Date());
            baseOderderService.updateOperation(param, operation);

        }
        cbhpt.setTrade_list(sinaUtils.castListtoSinaString(trade_list, "$", "~"
                , "out_trade_no", "payee_identity", "payee_identity_type", "account_type", "amount", "split_list", "summary", "extend_param"
                , "payer_fee", "goods_id", "creditor_info_list"));
        cbhpt.setUser_ip(userInfo.getRemoteAddr());
        cbhpt.setOut_trade_code(OUT_TRADE_CODE.OUT_LOAN_2);
        TrusteeshipOperation operation = CBHPTService.createOperation(cbhpt, TrusteeshipConstants.OperationType.REPAY_BATCH, lr.getId());
        cbhpt.setOut_pay_no(operation.getId());
        operation = CBHPTService.updateOperation(cbhpt, operation);
        SinaInEntity sinaIn = CBHPTService.sendHttpClientOperation(cbhpt, operation, SinaInEntity.class);
        log.info("{} do {} return {} with {}", userInfo.getLoginUserId(), SinaAPI.CREATE_BATCH_HOSTING_PAY_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            CBHPTService.success(operation.getId());
        } else {
            CBHPTService.refuse(operation.getId());
        }
        return "";
    }


    @Transactional
    public String createBatchHostingPayTradeSinaPay(Loan loan, ACCOUNT_TYPE accountType) {

        CreateBatchHostingPayTradeSinaEntity cbhpt = CBHPTService.getRequestEntity(CreateBatchHostingPayTradeSinaEntity.class);
        cbhpt.setService(SinaAPI.CREATE_BATCH_HOSTING_PAY_TRADE.getService_name());

        cbhpt.setOut_pay_no(IdGenerator.randomUUID());
        cbhpt.setOut_trade_code(OUT_TRADE_CODE.TO_LOAN_2);
        cbhpt.setNotify_method("single_notify");
        cbhpt.setUser_ip(userInfo.getRemoteAddr());
        List<TradeListParam> trade_list = new ArrayList<>();
        for (Invest invest : loan.getInvests()) {
            if (invest.getStatus().equals(InvestConstants.InvestStatus.BID_SUCCESS)) {
                TradeListParam param = new TradeListParam();
                param.setAmount(BigDecimal.valueOf(invest.getMoney()));//?????????????????????
                param.setOut_trade_no(IdGenerator.randomUUID());
                param.setPayee_identity(loan.getUser().getId());
                param.setPayee_identity_type(IDENTITY_TYPE.UID.getType_name());
                param.setAccount_type(accountType.getType_name());
                param.setSummary("??????????????????");
                param.setGoods_id(invest.getLoan().getId());
                TrusteeshipOperation operation = new TrusteeshipOperation();
                operation.setId(param.getOut_trade_no());
                operation.setMarkId(invest.getId());
                operation.setStatus(TrusteeshipConstants.OperationStatus.PREPARE);
                operation.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
                operation.setType(TrusteeshipConstants.OperationType.FINISH_PRE_AUTH_TRADE);
                operation.setOperator(SecurityContextHolder.getContext().getAuthentication().getName());
                operation.setRequestTime(new Date());
                baseOderderService.updateOperation(param, operation);
                trade_list.add(param);
            }

        }
        if (trade_list.size() > 0)
            cbhpt.setTrade_list(sinaUtils.castListtoSinaString(trade_list, "$", "~"
                    , "out_trade_no", "payee_identity", "payee_identity_type", "account_type", "amount", "split_list", "summary", "extend_param"
                    , "payer_fee", "goods_id", "creditor_info_list"));


        TrusteeshipOperation operation = CBHPTService.createOperation(cbhpt, TrusteeshipConstants.OperationType.GIVE_MOENY_TO_BORROWER, loan.getId());

        cbhpt.setOut_pay_no(operation.getId());

        operation = CBHPTService.updateOperation(cbhpt, operation);

        SinaInEntity sinaIn = CBHPTService.sendHttpClientOperation(cbhpt, operation, SinaInEntity.class);

        log.info("{} do {} return {} with {}", SecurityContextHolder.getContext().getAuthentication().getName(), SinaAPI.CREATE_BATCH_HOSTING_PAY_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
//            CBHPTService.success(operation.getId());
        } else {
            CBHPTService.refuse(operation.getId());
        }
        return "";
    }


    //--------------3.3    ??????????????????????????????End


    //--------------3.4    ??????????????????Start

    @Autowired
    private OrderOperationService<PayHostingTradeSinaInEntity, PayHostingTradeSinaEntity> PHTService;


    public String payHostingTradeSinaPay() {

        PayHostingTradeSinaEntity pht = PHTService.getRequestEntity(PayHostingTradeSinaEntity.class);
        pht.setService(SinaAPI.PAY_HOSTING_TRADE.getService_name());


        TrusteeshipOperation operation = PHTService.createOperation(pht);
        PayHostingTradeSinaInEntity inEntity = PHTService.sendHttpClientOperation(pht, operation, PayHostingTradeSinaInEntity.class);


        return "";
    }


    //--------------3.4    ??????????????????End


    //--------------3.5    ??????????????????Start

    @Autowired
    private OrderOperationService<QueryPayRequestSinaInEntity, QueryPayRequestSinaEntity> QPRService;


    /**
     * ??????????????????
     *
     * @return
     */
    public String queryPayRequestSinaPay() {


        QueryPayRequestSinaEntity qpr = QPRService.getRequestEntity(QueryPayRequestSinaEntity.class);
        qpr.setService(SinaAPI.QUERY_PAY_RESULT.getService_name());
        TrusteeshipOperation operation = QPRService.createOperation(qpr);
        QueryPayRequestSinaInEntity inEntity = QPRService.sendHttpClientOperation(qpr, operation, QueryPayRequestSinaInEntity.class);
        return "";
    }


    //--------------3.5    ??????????????????End


    //--------------3.6    ??????????????????Start

    @Autowired
    private OrderOperationService<QueryHostingTradeSinaInEntity, QueryHostingTradeSinaEntity> QHTService;


    /**
     * ??????????????????
     *
     * @return
     */
    public String queryHostingTradeSinaPay() {
        QueryHostingTradeSinaEntity qht = QHTService.getRequestEntity(QueryHostingTradeSinaEntity.class);
        qht.setService(SinaAPI.QUERY_HOSTING_TRADE.getService_name());
        TrusteeshipOperation operation = QHTService.createOperation(qht);
        QueryHostingTradeSinaInEntity inEntity = QHTService.sendHttpClientOperation(qht, operation, QueryHostingTradeSinaInEntity.class);
        return "";
    }


    //--------------3.6    ??????????????????End
    //--------------3.7    ????????????????????????Start


    @Autowired
    private OrderOperationService<QueryHostingBatchTradeSinaInEntity, QueryHostingBatchTradeSinaEntity> QHBTService;


    /**
     * ????????????????????????
     *
     * @return
     */
    public String queryHostingBatchTradeSinaPay() {

        QueryHostingBatchTradeSinaEntity qhbt = QHBTService.getRequestEntity(QueryHostingBatchTradeSinaEntity.class);
        qhbt.setService(SinaAPI.QUERY_HOSTING_BATCH_TRADE.getService_name());
        TrusteeshipOperation operation = QHBTService.createOperation(qhbt);
        QueryHostingBatchTradeSinaInEntity inEntity = QHBTService.sendHttpClientOperation(qhbt, operation, QueryHostingBatchTradeSinaInEntity.class);
        return "";
    }


    //--------------3.7    ????????????????????????End
    //--------------3.8    ????????????Start

    @Autowired
    private OrderOperationService<CreateHostingRefundSinaInEntity, CreateHostingRefundSinaEntity> CHRService;


    /**
     * ????????????
     *
     * @return
     */
    public String createHostingRefundSinaPay(InvestRefunds investRefunds) {
        CreateHostingRefundSinaEntity chr = CHRService.getRequestEntity(CreateHostingRefundSinaEntity.class);
        chr.setService(SinaAPI.CREATE_HOSTING_REFUND.getService_name());
        chr.setOrig_outer_trade_no(investRefunds.getOperationOrderNo());
        chr.setRefund_amount(investRefunds.getRefundMoney());
        chr.setSummary(investRefunds.getInvest().getId() + "????????????");
        chr.setUser_ip(userInfo.getRemoteAddr());
        TrusteeshipOperation operation = CHRService.createOperation(chr, TrusteeshipConstants.OperationType.INVEST_REFUND, investRefunds.getId());
        chr.setOut_trade_no(operation.getId());
        CreateHostingRefundSinaInEntity inEntity = CHRService.sendHttpClientOperation(chr, operation, CreateHostingRefundSinaInEntity.class);
        //??????????????????
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            if (inEntity.getRefund_status().equals(REFUND_STATUS.SUCCESS.getStatus_name())) {
                return "SUCCESS";
            }
            return inEntity.getResponse_message();

        } else {
            return inEntity.getResponse_message();
        }

    }

    /**
     * ????????????
     *
     * @return
     */
    public String createHostingRefundSinaPay(MoneyTransfer transfer) {

        CreateHostingRefundSinaEntity chr = CHRService.getRequestEntity(CreateHostingRefundSinaEntity.class);
        chr.setService(SinaAPI.CREATE_HOSTING_REFUND.getService_name());
        chr.setOrig_outer_trade_no(transfer.getOperationOrderNo());
        chr.setRefund_amount(BigDecimal.valueOf(transfer.getMoney()));
        chr.setSummary("????????????");
        chr.setUser_ip(userInfo.getRemoteAddr());
        TrusteeshipOperation operation = CHRService.createOperation(chr);

        chr.setOut_trade_no(operation.getId());
        operation.setMarkId(transfer.getId());
        CHRService.updateOperation(chr, operation);

        CreateHostingRefundSinaInEntity inEntity = CHRService.sendHttpClientOperation(chr, operation, CreateHostingRefundSinaInEntity.class);

        if (inEntity == null) {
            //????????????
            return "inEntity is Null ";
        }
        //??????????????????
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {

            if (inEntity.getRefund_status().equals(REFUND_STATUS.SUCCESS.getStatus_name())) {
                return "SUCCESS";
            }
            return inEntity.getResponse_message();

        } else {
            return inEntity.getResponse_message();
        }

    }

    //--------------3.8    ????????????End
    //--------------3.9    ??????????????????Start

    @Autowired
    private OrderOperationService<QueryHostingRefundSinaInEntity, QueryHostingRefundSinaEntity> QHRService;


    /**
     * ??????????????????
     *
     * @return
     */
    public String queryHostingRefundSinaPay(Date start, Date end, Integer page_no, Integer page_size) {
        QueryHostingRefundSinaEntity qhr = QHRService.getRequestEntity(QueryHostingRefundSinaEntity.class);
        qhr.setService(SinaAPI.QUERY_HOSTING_REFUND.getService_name());

        qhr.setIdentity_id(userInfo.getLoginUserId());
        qhr.setIdentity_type(IDENTITY_TYPE.UID.getType_name());

        qhr.setStart_time(DateUtil.DateToString(start, "yyyyMMddHHmmss"));

        qhr.setEnd_time(DateUtil.DateToString(end, "yyyyMMddHHmmss"));

        qhr.setPage_no(page_no == null ? 1 : page_no);

        qhr.setPage_size(page_size == null ? 20 : page_size);

        TrusteeshipOperation operation = QHRService.createOperation(qhr);

        QHRService.updateOperation(qhr, operation);

        QueryHostingRefundSinaInEntity inEntity = QHRService.sendHttpClientOperation(qhr, operation, QueryHostingRefundSinaInEntity.class);

        //??????????????????
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {

        }

        return "";
    }

    //--------------3.9    ??????????????????End
    //--------------3.10    ????????????Start
    @Autowired
    private OrderOperationService<CreateHostingDepositSinaInEntity, CreateHostingDepositSinaEntity> CHDService;


    @Autowired
    private ConfigService configService;

    /**
     * ????????????
     *
     * @return
     */
    public String createHostingDepositSinaPay(Recharge recharge) throws TrusteeshipReturnException, TrusteeshipTicketException, TrusteeshipFormException {
        CreateHostingDepositSinaEntity chd = CHDService.getRequestEntity(CreateHostingDepositSinaEntity.class);
        chd.setService(SinaAPI.CREATE_HOSTING_DEPOSIT.getService_name());
        chd.setIdentity_id(recharge.getUser().getId());
        chd.setSummary(recharge.getUser().getId() + "????????????");
        chd.setIdentity_type(IDENTITY_TYPE.UID);
        chd.setAccount_type(recharge.getAccountType());
        //????????????
        chd.setAmount(BigDecimal.valueOf(recharge.getRealMoney()));
        chd.setUser_fee(BigDecimal.valueOf(recharge.getFee()));
        chd.setPayer_ip(userInfo.getRemoteAddr());
        chd.setDeposit_close_time("10m");
        PayMethodParam payMethod = getPayMethod(recharge.getRechargeWay());
        Assert.isTrue(!payMethod.getPayType().equals(PAYMETHOD_TYPE.BALANCE.getType_name()), "?????????????????????");
        payMethod.setPayMoney(BigDecimal.valueOf(recharge.getRealMoney()));
        chd.setPay_method(sinaUtils.castBeanToSinaString(payMethod, "^", "payType", "payMoney", "extendParam"));
        //????????????
        TrusteeshipOperation operation = CHDService.createOperation(chd, TrusteeshipConstants.OperationType.RECHARGE, recharge.getId(), chd.getDeposit_close_time());
        //???????????????????????????????????????Id
        chd.setOut_trade_no(operation.getId());
        CHDService.updateOperation(chd, operation);
        //????????????
        CreateHostingDepositSinaInEntity sinaIn = CHDService.sendHttpClientOperation(chd, operation, CreateHostingDepositSinaInEntity.class);

        //TODO ????????????????????????
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            if (StringUtils.isNotEmpty(sinaIn.getRedirect_url()))
                return returnAuthCode(recharge.getUser().getId(), operation.getId(), DateUtil.addMinute(operation.getRequestTime(), 10), chd.getService(), sinaIn.getRedirect_url());
            else if (StringUtils.isNotEmpty(sinaIn.getTicket())) {
                String deadtime = StringUtils.defaultString(configService.getConfigValue("recharge_ticket_deadtime"), "10m");
                try {
                    authService.createAuthInfo(recharge.getUser().getId(), recharge.getId(), getDeadTime(deadtime), CommonConstants.AuthInfoType.RECHARGE, sinaIn.getTicket());
                    throw new TrusteeshipTicketException("?????????????????????");
                } catch (AuthInfoAlreadyInColdException e) {
                    log.error(e.getMessage());
                    throw new TrusteeshipReturnException("??????ticket??????");
                }
            } else if (DEPOSIT_STATUS.SUCCESS.equals(sinaIn.getDeposit_status())) {
                throw new TrusteeshipReturnException("????????????");
            } else if (DEPOSIT_STATUS.FAILED.equals(sinaIn.getDeposit_status())) {
                throw new TrusteeshipReturnException("????????????????????????");
            } else if (DEPOSIT_STATUS.PROCESSING.equals(sinaIn.getDeposit_status())) {
                throw new TrusteeshipReturnException("???????????????");
            }
        } else if (sinaIn.getResponse_code().equals(RESPONSE_CODE.HTML_RESPONSE)) {
            throw new TrusteeshipFormException(sinaIn.getResponse_message());
        }
        throw new TrusteeshipReturnException("??????????????????????????????");
    }

    private Date getDeadTime(String minute) {
        return DateUtil.add(new Date(), minute);

    }


    //--------------3.10    ????????????End
    //--------------3.11    ??????????????????Start
    @Autowired
    private OrderOperationService<QueryHostingDepositSinaInEntity, QueryHostingDepositSinaEntity> QHDService;


    /**
     * ??????????????????
     *
     * @return
     */

    public String queryHostingDepositSinaPay(Date start, Date end) {
        QueryHostingDepositSinaEntity qhd = QHDService.getRequestEntity(QueryHostingDepositSinaEntity.class);
        qhd.setService(SinaAPI.QUERY_HOSTING_DEPOSIT.getService_name());

        qhd.setIdentity_id(userInfo.getLoginUserId());
        qhd.setIdentity_type(IDENTITY_TYPE.UID.getType_name());

        qhd.setStart_time(DateUtil.DateToString(start, "yyyyMMddHHmmss"));
        qhd.setEnd_time(DateUtil.DateToString(end, "yyyyMMddHHmmss"));


        TrusteeshipOperation operation = QHDService.createOperation(qhd);
        QueryHostingDepositSinaInEntity inEntity = QHDService.sendHttpClientOperation(qhd, operation, QueryHostingDepositSinaInEntity.class);


        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))
            return inEntity.getDeposit_list();


        return "";
    }


    //--------------3.11    ??????????????????End
    //--------------3.12    ????????????Start
    @Autowired
    private OrderOperationService<CreateHostingWithdrawSinaInEntity, CreateHostingWithdrawSinaEntity> CHWService;


    @Autowired
    private WithdrawCashService withdrawCashService;

    /**
     * ????????????
     *
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String createHostingWithdrawSinaPay(WithdrawCash withdrawCash) throws TrusteeshipReturnException {
        CreateHostingWithdrawSinaEntity chw = CHWService.getRequestEntity(CreateHostingWithdrawSinaEntity.class);
        withdrawCash = withdrawCashService.getWithdrawById(withdrawCash.getId());
        chw.setService(SinaAPI.CREATE_HOSTING_WITHDRAW.getService_name());
        chw.setIdentity_id(withdrawCash.getUser().getId());
        chw.setIdentity_type(IDENTITY_TYPE.UID);
        chw.setAmount(BigDecimal.valueOf(withdrawCash.getMoney()));
        chw.setUser_fee(BigDecimal.valueOf(withdrawCash.getFee()));
        chw.setUser_ip(userInfo.getRemoteAddr());
        chw.setExtend_param("customNotify^Y");
        TrusteeshipOperation operation = CHWService.createOperation(chw, TrusteeshipConstants.OperationType.WITHDRAW_CASH, withdrawCash.getId(), chw.getWithdraw_close_time());
        chw.setOut_trade_no(operation.getId());
        CHWService.updateOperation(chw, operation);
        CreateHostingWithdrawSinaInEntity inEntity = CHWService.sendHttpClientOperation(chw, operation, CreateHostingWithdrawSinaInEntity.class);
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            if (StringUtils.isNotEmpty(inEntity.getRedirect_url())) {
                return returnAuthCode(withdrawCash.getUser().getId(), operation.getId(), DateUtil.addMinute(operation.getRequestTime(), 10), chw.getService(), inEntity.getRedirect_url());
            }
        }
        throw new TrusteeshipReturnException(inEntity.getResponse_message());
    }

    //--------------3.12    ????????????End
    //--------------3.13    ??????????????????Start
    @Autowired
    private OrderOperationService<QueryHostingWithdrawSinaInEntity, QueryHostingWithdrawSinaEntity> QHWService;


    /**
     * ??????????????????
     * ???????????????
     *
     * @return
     */
    public String queryHostingWithdrawSinaPay(String userId, Date start, Date end) {
        QueryHostingWithdrawSinaEntity qhw = QHWService.getRequestEntity(QueryHostingWithdrawSinaEntity.class);
        qhw.setService(SinaAPI.QUERY_HOSTING_WITHDRAW.getService_name());
        qhw.setIdentity_id(userId);
        qhw.setIdentity_type(IDENTITY_TYPE.UID.getType_name());
        qhw.setStart_time(DateUtil.DateToString(start, "yyyyMMddHHmmss"));
        qhw.setEnd_time(DateUtil.DateToString(end, "yyyyMMddHHmmss"));
        TrusteeshipOperation operation = QHWService.createOperation(qhw);
        QueryHostingWithdrawSinaInEntity inEntity = QHWService.sendHttpClientOperation(qhw, operation, QueryHostingWithdrawSinaInEntity.class);
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))
            return inEntity.getWithdraw_list();
        return "";
    }

    /**
     * ??????????????????
     * ???????????????
     *
     * @return
     */
    public List<WithdrawCashParam> queryHostingWithdrawSinaPay(String userId, String orderId) {

        QueryHostingWithdrawSinaEntity qhw = QHWService.getRequestEntity(QueryHostingWithdrawSinaEntity.class);
        qhw.setService(SinaAPI.QUERY_HOSTING_WITHDRAW.getService_name());
        qhw.setIdentity_id(userId);
        qhw.setIdentity_type(IDENTITY_TYPE.UID.getType_name());
        qhw.setOut_trade_no(orderId);
        TrusteeshipOperation operation = QHWService.createOperation(qhw);
        QueryHostingWithdrawSinaInEntity inEntity = QHWService.sendHttpClientOperation(qhw, operation, QueryHostingWithdrawSinaInEntity.class);
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            List<WithdrawCashParam> withdrawCashParamList = sinaUtils.castSinaStringtoList(inEntity.getWithdraw_list(), "|", "^", WithdrawCashParam.class, "out_trade_no", "amount", "status", "start_time", "end_time");
            return withdrawCashParamList;
        }
        return null;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public List<WithdrawCashParam> queryHostingWithdrawSinaPay(WithdrawCash withdrawCash) {
        withdrawCash = withdrawCashService.getWithdrawById(withdrawCash.getId());

        TrusteeshipOperation operation = QHWService.getOperation(TrusteeshipConstants.OperationType.WITHDRAW_CASH, withdrawCash.getId(), withdrawCash.getUser().getId(), TrusteeshipConstants.Trusteeship.SINAPAY);
        if (operation == null)
            return null;
        return queryHostingWithdrawSinaPay(withdrawCash.getId(), operation.getId());
    }
    //--------------3.13    ??????????????????End
    //--------------3.14    ????????????????????????????????????Start


    @Autowired
    private OrderOperationService<CreateSingleHostingPayToCardTradeSinaInEntity, CreateSingleHostingPayToCardTradeSinaEntity> CSHPTCTService;


    private String out_trade_code;//?????????


    public String getOut_trade_code() {
        return out_trade_code;
    }

    public void setOut_trade_code(String out_trade_code) {
        this.out_trade_code = out_trade_code;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    public String createSingleHostingPayToCardTradeSinaPay(Invest invest) {

        CreateSingleHostingPayToCardTradeSinaEntity cshptct = CSHPTCTService.getRequestEntity(CreateSingleHostingPayToCardTradeSinaEntity.class);
        cshptct.setService(SinaAPI.CREATE_SINGLE_HOSTING_PAY_TO_CARD_TRADE.getService_name());

        cshptct.setOut_trade_no(IdGenerator.randomUUID());

        cshptct.setOut_trade_code(getOut_trade_code() == null ? "2001" : getOut_trade_code());


        cshptct.setCollect_method("binding_card^" + sinaUtils.concatString(",", userInfo.getLoginUserId(), "UID"));//???????????????ID


        cshptct.setAmount(BigDecimal.valueOf(invest.getInvestMoney()));
        cshptct.setSummary(userInfo.getLoginUserId() + "??????");
        cshptct.setPayto_type(PAYTO_TYPE.FAST.getType_name());

        cshptct.setGoods_id(invest.getLoan().getSina_bid_no());


        cshptct.setUser_ip(userInfo.getRemoteAddr());//??????IP


        //172197


        TrusteeshipOperation operation = CSHPTCTService.createOperation(cshptct);
        CreateSingleHostingPayToCardTradeSinaInEntity inEntity = CSHPTCTService.sendHttpClientOperation(cshptct, operation, CreateSingleHostingPayToCardTradeSinaInEntity.class);
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))
            return inEntity.getResponse_message();
        return "";
    }


    //--------------3.14    ????????????????????????????????????End
    //--------------3.15    ????????????????????????????????????Start

    @Autowired
    private OrderOperationService<SinaInEntity, CreateBatchHostingPayToCardTradeSinaEntity> CBHPTCTService;


    /**
     * ????????????????????????????????????
     *
     * @return
     */
    public String createBatchHostingPayToCardTradeSinapay() {
        CreateBatchHostingPayToCardTradeSinaEntity cbhptct = CBHPTCTService.getRequestEntity(CreateBatchHostingPayToCardTradeSinaEntity.class);
        cbhptct.setService(SinaAPI.CREATE_BATCH_HOSTING_PAY_TO_CARD_TRADE.getService_name());


        TrusteeshipOperation operation = CBHPTCTService.createOperation(cbhptct);
        SinaInEntity inEntity = CBHPTCTService.sendHttpClientOperation(cbhptct, operation, SinaInEntity.class);
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))
            return inEntity.getResponse_message();
        return "";
    }


    //--------------3.15    ????????????????????????????????????End
    //--------------3.16    ????????????Start

    @Autowired
    private OrderOperationService<SinaInEntity, FinishPreAuthTradeSinaEntity> FPATService;

    @Autowired
    private OrderOperationService<SinaInEntity, OrderSinaEntity> baseOderderService;


    @Transactional(rollbackFor = Exception.class)
    public String finishPreAuthTradeSinaPay(Loan loan) throws TrusteeshipReturnException {
        FinishPreAuthTradeSinaEntity fpat = FPATService.getRequestEntity(FinishPreAuthTradeSinaEntity.class);
        loan = ht.merge(loan);
        fpat.setService(SinaAPI.FINISH_PRE_AUTH_TRADE.getService_name());
        fpat.setOut_request_no(IdGenerator.randomUUID());
        List<FinishPreAuthTradeListParam> tradeList = new ArrayList<FinishPreAuthTradeListParam>();
        for (Invest invest : loan.getInvests()) {
            if (invest.getStatus().equals(InvestConstants.InvestStatus.BID_FROZEN)) {
                invest.setStatus(InvestConstants.InvestStatus.WAIT_LOANING_VERIFY);
                investService.update(invest);
                FinishPreAuthTradeListParam param = new FinishPreAuthTradeListParam();
                param.setOut_trade_no(IdGenerator.randomUUID());
                param.setAmount(BigDecimal.valueOf(invest.getMoney()));
                param.setSummary(loan.getId() + "????????????");
                param.setFreezes_trade_no(invest.getOperationOrderNo());
                param.setExtend_param("notify_type^sync");
                TrusteeshipOperation operation = new TrusteeshipOperation();
                operation.setId(param.getOut_trade_no());
                operation.setMarkId(invest.getId());
                operation.setStatus(TrusteeshipConstants.OperationStatus.PREPARE);
                operation.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
                operation.setType(TrusteeshipConstants.OperationType.FINISH_PRE_AUTH_TRADE);
                operation.setOperator(userInfo.getLoginUserId());
                operation.setRequestTime(new Date());
                baseOderderService.updateOperation(param, operation);
                tradeList.add(param);
            }
        }
        //tradeList ????????????0????????????????????????
        if (tradeList.size() > 0)
            fpat.setTrade_list(sinaUtils.castListtoSinaString(tradeList, "$", "~", "out_trade_no", "freezes_trade_no", "amount", "summary", "extend_param"));
        fpat.setUser_ip(userInfo.getRemoteAddr());
        fpat.setExtend_param("notify_type^sync");
        TrusteeshipOperation operation = FPATService.createOperation(fpat, TrusteeshipConstants.OperationType.FINISH_PRE_AUTH_TRADE_BATCH, loan.getId());
        SinaInEntity inEntity = FPATService.sendHttpClientOperation(fpat, operation, SinaInEntity.class);
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
//            FPATService.success(operation.getId());
            log.info(loan.getId() + "??????????????????");
            return inEntity.getResponse_message();
        } else {
            throw new TrusteeshipReturnException("????????????");

        }
    }

    /**
     * ????????????
     *
     * @return
     */
    public String finishPreAuthTradeSinaPay(String finish_id, String amount, String summary) {

        FinishPreAuthTradeSinaEntity fpat = FPATService.getRequestEntity(FinishPreAuthTradeSinaEntity.class);
        fpat.setService(SinaAPI.FINISH_PRE_AUTH_TRADE.getService_name());

        String uuid = IdGenerator.randomUUID();

        fpat.setOut_request_no(uuid);//????????????????????????????????????????????????

        fpat.setTrade_list(uuid + "~" + finish_id + "~" + amount + "~" + summary + "~");

        fpat.setUser_ip(userInfo.getRemoteAddr());//IP


        TrusteeshipOperation operation = FPATService.createOperation(fpat);
        SinaInEntity inEntity = FPATService.sendHttpClientOperation(fpat, operation, SinaInEntity.class);

        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))
            return inEntity.getResponse_message();


        return "";
    }

    //--------------3.16    ????????????End
    //--------------3.17    ????????????Start

    @Autowired
    private OrderOperationService<SinaInEntity, CancelPreAuthTradeSinaEntity> CPATService;


    /**
     * ????????????
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String cancelPreAuthTradeSinaPay(Loan loan) throws TrusteeshipReturnException {
        CancelPreAuthTradeSinaEntity cpat = CPATService.getRequestEntity(CancelPreAuthTradeSinaEntity.class);
        cpat.setService(SinaAPI.CANCEL_PRE_AUTH_TRADE.getService_name());
        cpat.setOut_request_no(IdGenerator.randomUUID());
        List<CancelPreAuthTradeListParam> tradeList = new ArrayList<CancelPreAuthTradeListParam>();
        for (Invest invest : loan.getInvests()) {
            //???????????????
            if (invest.getStatus().equals(InvestConstants.InvestStatus.BID_FROZEN)) {
                invest.setStatus(InvestConstants.InvestStatus.WAIT_CANCEL_AFFIRM);
                investService.update(invest);
                CancelPreAuthTradeListParam param = new CancelPreAuthTradeListParam();
                param.setRevoke_trade_no(IdGenerator.randomUUID());
                param.setSummary("??????");
                param.setFreezes_trade_no(invest.getOperationOrderNo());
                param.setExtend_param("notify_type^sync");
                TrusteeshipOperation operation = new TrusteeshipOperation();
                operation.setId(param.getRevoke_trade_no());
                operation.setMarkId(invest.getId());
                operation.setStatus(TrusteeshipConstants.OperationStatus.PREPARE);
                operation.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
                operation.setType(TrusteeshipConstants.OperationType.CANCEL_LOAN);
                operation.setOperator(userInfo.getLoginUserId());
                operation.setRequestTime(new Date());
                baseOderderService.updateOperation(param, operation);
                tradeList.add(param);
            }
        }

        if (tradeList.size() > 0)
            cpat.setTrade_list(sinaUtils.castListtoSinaString(tradeList, "$", "~", "revoke_trade_no", "freezes_trade_no", "summary", "extend_param"));

        cpat.setExtend_param("notify_type^sync");
        TrusteeshipOperation operation = CPATService.createOperation(cpat, TrusteeshipConstants.OperationType.CANCEL_LOAN, cpat.toString().hashCode() + "");
        SinaInEntity inEntity = CPATService.sendHttpClientOperation(cpat, operation, SinaInEntity.class);

        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            log.info(loan.getId() + "??????????????????");
            return inEntity.getResponse_message();
        } else {
            throw new TrusteeshipReturnException(inEntity.getResponse_message());
        }
    }


    /**
     * ????????????
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String oneCancelPreAuthTradeSinaPay(Invest invest) throws TrusteeshipReturnException {

        CancelPreAuthTradeSinaEntity cpat = CPATService.getRequestEntity(CancelPreAuthTradeSinaEntity.class);
        if (invest.getStatus().equals(InvestConstants.InvestStatus.BID_FROZEN)) {

            cpat.setService(SinaAPI.CANCEL_PRE_AUTH_TRADE.getService_name());

            TrusteeshipOperation operation = CPATService.createOperation(cpat, TrusteeshipConstants.OperationType.CANCEL_INVEST, invest.getId());

            cpat.setOut_request_no(operation.getId());
            cpat.setTrade_list(sinaUtils.concatString("~", operation.getId(), invest.getOperationOrderNo(), "??????"));

            SinaInEntity inEntity = CPATService.sendHttpClientOperation(cpat, operation, SinaInEntity.class);

            if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
                return "SUCCESS";
            } else {
                return inEntity.getResponse_message();
            }
        } else {
            throw new TrusteeshipReturnException("???????????????????????????");
        }
    }


    //--------------3.17    ????????????End
    //--------------3.18    ????????????Start
    @Autowired
    private OrderOperationService<CreateBidInfoSinaInEntity, CreateBidInfoSinaEntity> CBIService;


    @Autowired
    private LoanService loanService;

    @Autowired
    private TrusteeshipAccountService trusteeshipAccountService;

    /**
     * ????????????
     *
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class, noRollbackFor = TrusteeshipReturnException.class)
    public String createBidInfoSinaPay(Loan loan) {
        CreateBidInfoSinaEntity cbi = CBIService.getRequestEntity(CreateBidInfoSinaEntity.class);
        cbi.setService(SinaAPI.CREATE_BID_INFO.getService_name());
        if (loan != null) {
            cbi.setOut_bid_no(IdGenerator.randomUUID());//???????????????
            cbi.setWeb_site_name("demo");//????????????
            cbi.setBid_name(loan.getName());//????????????
            cbi.setBid_type(loan.getTarget_type());//????????????
            cbi.setBid_amount(BigDecimal.valueOf(loan.getLoanMoney()));//????????????
            cbi.setBid_year_rate(BigDecimal.valueOf(loan.getRatePercent()));//????????????
            cbi.setBid_duration(loan.getDeadline());//?????????????????????
            cbi.setRepay_type(OrderSinaAPI.RepaymentType.OTHER);//????????????
            cbi.setProtocol_type(OrderSinaAPI.ProtocolType.OTHER);//????????????
            cbi.setBid_product_type(OrderSinaAPI.BidProductType.OTHER);//??????????????????
            cbi.setBid_product_type("");//????????????
            cbi.setLimit_min_bid_amount(BigDecimal.valueOf(loan.getMinInvestMoney()));
            cbi.setLimit_max_bid_amount(BigDecimal.valueOf(loan.getMaxInvestMoney()));
            cbi.setSummary("???????????????" + loan.getType().getName() + "|???????????????" + loan.getRiskLevel());//??????
            cbi.setBegin_date(DateUtil.DateToString(loan.getInterestBeginTime() == null ? new Date() : loan.getInterestBeginTime(), "yyyyMMddHHmmss"));//??????????????????
            cbi.setTerm(DateUtil.DateToString(loan.getExpectTime(), "yyyyMMddHHmmss"));//????????????
            cbi.setGuarantee_method(loan.getContractType());//????????????
            cbi.setExtend_param("notify_type^sync");

            final TrusteeshipAccount trusteeshipAccount = trusteeshipAccountService.getTrusteeshipAccount(loan.getUser().getId(), TrusteeshipConstants.Trusteeship.SINAPAY);
            BorrowerInfoListParam borrowerInfoListParam = new BorrowerInfoListParam(
                    trusteeshipAccount.getUser().getId(),
                    trusteeshipAccount.getType(),
                    BigDecimal.valueOf(loan.getLoanMoney()),
                    loan.getLoanPurpose()
                    , loan.getUser().getMobileNumber()
                    , "", "", "", "", "", "", loan.getUser().getCurrentAddress(), loan.getUser().getEmail(), loan.getName(), "");
            cbi.setBorrower_info_list(borrowerInfoListParam.getOrderStrToData("~"));//?????????

            TrusteeshipOperation operation = CBIService.createOperation(cbi, TrusteeshipConstants.OperationType.CREATE_LOAN, loan.getId());
            cbi.setOut_bid_no(operation.getId());
            CBIService.updateOperation(cbi, operation);
            CreateBidInfoSinaInEntity inEntity = CBIService.sendHttpClientOperation(cbi, operation, CreateBidInfoSinaInEntity.class);

            if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
                if (inEntity.getBid_status().equals(BID_STATUS.VALID)) {
                    loan.setInner_bid_no(inEntity.getInner_bid_no());
                    loanService.update(loan);
                    CBIService.success(operation.getId());
                    return "SUCCESS";
                } else if (inEntity.getBid_status().equals(BID_STATUS.INVALID) || inEntity.getBid_status().equals(BID_STATUS.REJECT)) {
                    loanService.update(loan);
                    CBIService.refuse(operation.getId());
                    return inEntity.getReject_reason();
                } else {
                    //?????????????????????
                    loan.setStatus(LoanConstants.LoanStatus.WAITING_VERIFY_AFFIRM);
                    loanService.update(loan);
                    CBIService.waiting(operation.getId());
                    return "SINA_VERIFY";
                }
            } else {
                return inEntity.getResponse_message();
            }
        }
        return "";
    }


    //--------------3.18    ????????????End
    //--------------3.19    ??????????????????Start

    @Autowired
    private OrderOperationService<QueryBidInfoSinaInEntity, QueryBidInfoSinaEntity> QBIService;


    /**
     * ??????????????????
     *
     * @return
     */
    public String queryBidInfoSinaPay(String out_bid_no) {
        QueryBidInfoSinaEntity qbi = QBIService.getRequestEntity(QueryBidInfoSinaEntity.class);
        qbi.setService(SinaAPI.QUERY_BID_INFO.getService_name());
        qbi.setOut_bid_no(out_bid_no);
        TrusteeshipOperation operation = QBIService.createOperation(qbi);
        QueryBidInfoSinaInEntity inEntity = QBIService.sendHttpClientOperation(qbi, operation, QueryBidInfoSinaInEntity.class);

        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {

        }

        return "";
    }


    //--------------3.19    ??????????????????End


}
