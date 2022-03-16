package info.bfly.pay.controller;

import info.bfly.core.annotations.Log;
import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.repay.exception.NormalRepayException;
import info.bfly.p2p.repay.model.InvestRepay;
import info.bfly.p2p.repay.model.LoanRepay;
import info.bfly.p2p.repay.service.RepayService;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;
import info.bfly.pay.bean.SinaAPI;
import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.callback.CreateSingleHostingPayTradeSinaInEntity;
import info.bfly.pay.bean.enums.ACCOUNT_TYPE;
import info.bfly.pay.bean.enums.IDENTITY_TYPE;
import info.bfly.pay.bean.enums.OUT_TRADE_CODE;
import info.bfly.pay.bean.enums.RESPONSE_CODE;
import info.bfly.pay.bean.order.CreateBatchHostingPayTradeSinaEntity;
import info.bfly.pay.bean.order.CreateSingleHostingPayTradeSinaEntity;
import info.bfly.pay.bean.order.FinishPreAuthTradeSinaEntity;
import info.bfly.pay.bean.order.OrderSinaEntity;
import info.bfly.pay.bean.order.param.FinishPreAuthTradeListParam;
import info.bfly.pay.bean.order.param.SplitParam;
import info.bfly.pay.bean.order.param.TradeListParam;
import info.bfly.pay.service.impl.OrderOperationService;
import info.bfly.pay.util.SinaUtils;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XXSun on 3/13/2017.
 */
@Controller
public class SinaSystemController implements Serializable {
    private static final long serialVersionUID = 3810387801359552697L;

    @Log
    Logger log;

    @Value("#{refProperties['sinapay_client_ip']}")
    private String sinaClientIp = "127.0.0.1";

    @Value("#{refProperties['sinapay_partner_account_type']}")
    private String partner_account_type;
    @Value("#{refProperties['sinapay_partner_identity_id']}")
    private String partner_identity_id;
    @Value("#{refProperties['sinapay_partner_identity_type']}")
    private String partner_identity_type;

    private final SinaUtils sinaUtils;

    @Resource
    HibernateTemplate ht;

    @Autowired
    ApplicationContext applicationContext;
    //--------------3.3    创建批量托管代付交易Strat


    final         RepayService                                                              repayService;
    private final OrderOperationService<SinaInEntity, CreateBatchHostingPayTradeSinaEntity> CBHPTService;
    private final InvestService                                                             investService;
    private final OrderOperationService<SinaInEntity, OrderSinaEntity>                      baseService;

    @Autowired
    public SinaSystemController(SinaUtils sinaUtils, OrderOperationService<SinaInEntity, CreateBatchHostingPayTradeSinaEntity> CBHPTService, OrderOperationService<SinaInEntity, OrderSinaEntity> baseService, RepayService repayService, OrderOperationService<SinaInEntity, FinishPreAuthTradeSinaEntity> FPATService, OrderOperationService<CreateSingleHostingPayTradeSinaInEntity, CreateSingleHostingPayTradeSinaEntity> CSHPTService, InvestService investService) {
        this.sinaUtils = sinaUtils;
        this.CBHPTService = CBHPTService;
        this.baseService = baseService;
        this.repayService = repayService;
        this.FPATService = FPATService;
        this.CSHPTService = CSHPTService;
        this.investService = investService;
    }

    //--------------3.2    创建托管代付交易Start
    private final OrderOperationService<CreateSingleHostingPayTradeSinaInEntity, CreateSingleHostingPayTradeSinaEntity> CSHPTService;


    /**
     * 创建托管代付交易
     *
     * @return
     */
    @Transactional
    public String createSingleHostingPayTradeSinaPay(Loan loan, ACCOUNT_TYPE accountType) {
        CreateSingleHostingPayTradeSinaEntity cshpt = CSHPTService.getRequestEntity(CreateSingleHostingPayTradeSinaEntity.class);
        cshpt.setService(SinaAPI.CREATE_SINGLE_HOSTING_PAY_TRADE.getService_name());
        cshpt.setOut_trade_no(IdGenerator.randomUUID());
        cshpt.setOut_trade_code(OUT_TRADE_CODE.TO_LOAN_2);
        cshpt.setPayee_identity_id(loan.getUser().getId());
        cshpt.setAmount(BigDecimal.valueOf(loan.getMoney()));
        cshpt.setSummary("满标" + loan.getName());
        cshpt.setGoods_id(loan.getSina_bid_no());
        if (loan.getLoanGuranteeFee() > 0) {
            SplitParam feeAccount = new SplitParam(loan.getUser().getId(), "UID", accountType.getType_name(), partner_identity_id, partner_identity_type, partner_account_type, BigDecimal.valueOf(loan.getLoanGuranteeFee()), "借款管理费");
            cshpt.setSplit_list(sinaUtils.castBeanToSinaString(feeAccount, "^", "payer_identity", "payer_type", "payer_account_type", "payee_identity", "payee_type", "payee_account_type", "amount", "remarks"));
        }
        TrusteeshipOperation operation = CSHPTService.createOperation(cshpt, TrusteeshipConstants.OperationType.GIVE_MOENY_TO_BORROWER, loan.getId());
        cshpt.setOut_trade_no(operation.getId());
        operation = CSHPTService.updateOperation(cshpt, operation);
        CreateSingleHostingPayTradeSinaInEntity sinaIn = CSHPTService.sendHttpClientOperation(cshpt, operation, CreateSingleHostingPayTradeSinaInEntity.class);
        log.info("{} do {} return {} with {}", "system", SinaAPI.CREATE_SINGLE_HOSTING_PAY_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());

        if (sinaIn == null)
            return null;
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            CSHPTService.success(operation.getId());
        } else {
            CSHPTService.refuse(operation.getId());
        }

        return "";
    }
    //--------------3.2    创建托管代付交易End

    /**
     * 创建批量托管代付交易
     * 还款的时候转到投资人账户
     *
     * @return
     */
    @Transactional
    public String createBatchHostingPayTradeSinaPay(LoanRepay lr, ACCOUNT_TYPE accountType) throws NormalRepayException, TrusteeshipReturnException {
        LoanRepay loanRepay = ht.get(LoanRepay.class, lr.getId(), LockMode.PESSIMISTIC_WRITE);
        CreateBatchHostingPayTradeSinaEntity cbhpt = CBHPTService.getRequestEntity(CreateBatchHostingPayTradeSinaEntity.class);
        if (!(loanRepay.getStatus().equals(LoanConstants.RepayStatus.FINISH_FUNDS))) {
            // 该还款不处于正常还款状态。
            throw new NormalRepayException("还款：" + loanRepay.getId() + "不处于正常还款状态。");
        }
        loanRepay.setStatus(LoanConstants.RepayStatus.WAIT_REPAY_COMPLETE);
        cbhpt.setService(SinaAPI.CREATE_BATCH_HOSTING_PAY_TRADE.getService_name());
        List<InvestRepay> irs = (List<InvestRepay>) ht.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?", loanRepay.getLoan().getId(), loanRepay.getPeriod());
        List<TradeListParam> trade_list = new ArrayList<>();
        for (int i = 0; i < irs.size(); i++) {
            InvestRepay ir = irs.get(i);
            ir.setStatus(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY);
            ir.setTime(new Date());
            ht.update(ir);
            //转账到用户账户
            TradeListParam param = new TradeListParam();
            param.setAmount(BigDecimal.valueOf(ArithUtil.add(ir.getCorpus(), ir.getInterest())));
            param.setOut_trade_no(IdGenerator.randomUUID());
            param.setPayee_identity(ir.getInvest().getUser().getId());
            param.setPayee_identity_type(IDENTITY_TYPE.UID.getType_name());
            param.setAccount_type(accountType.getType_name());
            param.setSummary("还款收益");
            param.setGoods_id(ir.getInvest().getLoan().getSina_bid_no());

            //分账
            if (ir.getFee() > 0) {
                SplitParam feeAccount = new SplitParam(ir.getInvest().getUser().getId(), "UID", accountType.getType_name(), partner_identity_id, partner_identity_type, partner_account_type, BigDecimal.valueOf(ir.getFee()), "手续费");
                param.setSplit_list(sinaUtils.castBeanToSinaString(feeAccount, "^", "payer_identity", "payer_type", "payer_account_type", "payee_identity", "payee_type", "payee_account_type", "amount", "remarks"));
            }
            param.setExtend_param("notify_type^sync");
            TrusteeshipOperation operation = new TrusteeshipOperation();
            operation.setId(param.getOut_trade_no());
            operation.setMarkId(ir.getId());
            operation.setStatus(TrusteeshipConstants.OperationStatus.WAITING);
            operation.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
            operation.setType(TrusteeshipConstants.OperationType.REPAY_BATCH);
            operation.setOperator("system");
            operation.setRequestTime(new Date());
            baseService.updateOperation(param, operation);
            trade_list.add(param);
        }
        cbhpt.setNotify_method("single_notify");
        cbhpt.setTrade_list(sinaUtils.castListtoSinaString(trade_list, "$", "~"
                , "out_trade_no", "payee_identity", "payee_identity_type", "account_type", "amount", "split_list", "summary", "extend_param"
                , "payer_fee", "goods_id", "creditor_info_list"));
        cbhpt.setUser_ip(sinaClientIp);
        cbhpt.setOut_trade_code(OUT_TRADE_CODE.OUT_LOAN_2);
        TrusteeshipOperation operation = CBHPTService.createOperation(cbhpt, TrusteeshipConstants.OperationType.REPAY_BATCH, loanRepay.getId());
        cbhpt.setOut_pay_no(operation.getId());
        operation = CBHPTService.updateOperation(cbhpt, operation);
        SinaInEntity sinaIn = CBHPTService.sendHttpClientOperation(cbhpt, operation, SinaInEntity.class);
        log.info("{} do {} return {} with {}", "system", SinaAPI.CREATE_BATCH_HOSTING_PAY_TRADE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        if (sinaIn == null)
            return null;
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            ht.update(loanRepay);
        } else {
            CBHPTService.refuse(operation.getId());
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());

        }
        return "";
    }

    //--------------3.3    创建批量托管代付交易End

    private final OrderOperationService<SinaInEntity, FinishPreAuthTradeSinaEntity> FPATService;


    @Transactional(rollbackFor = Exception.class)
    public void finishPreAuthTradeSinaPay(LoanRepay lr) throws TrusteeshipReturnException {
        FinishPreAuthTradeSinaEntity fpat = FPATService.getRequestEntity(FinishPreAuthTradeSinaEntity.class);
        fpat.setService(SinaAPI.FINISH_PRE_AUTH_TRADE.getService_name());
        if (lr.getStatus().equals(LoanConstants.RepayStatus.REPAY_FROZEN)) {
            fpat.setOut_request_no(IdGenerator.randomUUID());
            Double allRepayMoney = ArithUtil.add(lr.getCorpus(),
                    lr.getDefaultInterest(), lr.getFee(), lr.getInterest());
            FinishPreAuthTradeListParam param = new FinishPreAuthTradeListParam();
            param.setOut_trade_no(IdGenerator.randomUUID());
            param.setSummary("确认收到还款！");
            param.setFreezes_trade_no(lr.getOperationOrderNo());
            param.setAmount(BigDecimal.valueOf(allRepayMoney));
            param.setExtend_param("notify_type^sync");
            TrusteeshipOperation paramto = new TrusteeshipOperation();
            paramto.setId(param.getOut_trade_no());
            paramto.setMarkId(lr.getId());
            paramto.setStatus(TrusteeshipConstants.OperationStatus.PREPARE);
            paramto.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
            paramto.setType(TrusteeshipConstants.OperationType.FINISH_PRE_AUTH_TRADE);
            paramto.setOperator("system");
            paramto.setRequestTime(new Date());
            baseService.updateOperation(param, paramto);

            //tradeList 必须大于0否则怎么出现呢？

            fpat.setTrade_list(sinaUtils.castBeanToSinaString(param, "~", "out_trade_no", "freezes_trade_no", "amount", "summary", "extend_param"));
            //TODO 服务器IP
            fpat.setUser_ip(sinaClientIp);
            TrusteeshipOperation operation = FPATService.createOperation(fpat, TrusteeshipConstants.OperationType.FINISH_PRE_AUTH_TRADE_BATCH, fpat.toString().hashCode() + "");
            SinaInEntity inEntity = FPATService.sendHttpClientOperation(fpat, operation, SinaInEntity.class);
            if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
                lr.setStatus(LoanConstants.RepayStatus.WAIT_FINISH_FUNDS);
                repayService.update(lr);
            } else {
                FPATService.refuse(operation.getId());
                throw new TrusteeshipReturnException(inEntity.getResponse_message());

            }

        }
    }

//--------------代收完成


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
                param.setSummary(loan.getId() + "代收完成");
                param.setFreezes_trade_no(invest.getOperationOrderNo());
                param.setExtend_param("notify_type^sync");
                TrusteeshipOperation operation = new TrusteeshipOperation();
                operation.setId(param.getOut_trade_no());
                operation.setMarkId(invest.getId());
                operation.setStatus(TrusteeshipConstants.OperationStatus.PREPARE);
                operation.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
                operation.setType(TrusteeshipConstants.OperationType.FINISH_PRE_AUTH_TRADE);
                operation.setOperator("system");//默认系统执行
                operation.setRequestTime(new Date());
                baseService.updateOperation(param, operation);
                tradeList.add(param);
            }
        }
        //tradeList 必须大于0否则怎么出现呢？
        if (tradeList.size() > 0)
            fpat.setTrade_list(sinaUtils.castListtoSinaString(tradeList, "$", "~", "out_trade_no", "freezes_trade_no", "amount", "summary", "extend_param"));
        //TODO 服务器IP
        fpat.setUser_ip(sinaClientIp);
        fpat.setExtend_param("notify_type^sync");
        TrusteeshipOperation operation = FPATService.createOperation(fpat, TrusteeshipConstants.OperationType.FINISH_PRE_AUTH_TRADE_BATCH, loan.getId());
        SinaInEntity inEntity = FPATService.sendHttpClientOperation(fpat, operation, SinaInEntity.class);
        if (inEntity.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
//            FPATService.success(operation.getId());
//            log.info(loan.getId() + "代收放款完成");
            return inEntity.getResponse_message();
        } else {
            throw new TrusteeshipReturnException("我是异常");

        }
    }


}