package info.bfly.pay.p2p.trusteeship.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.common.service.BaseService;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.ArithUtil;
import info.bfly.crowd.orders.OrdersConstants;
import info.bfly.crowd.orders.model.Order;
import info.bfly.crowd.orders.service.OrderService;
import info.bfly.crowd.traceability.service.TraceService;
import info.bfly.p2p.borrower.service.BorrowerService;
import info.bfly.p2p.coupon.CouponConstants;
import info.bfly.p2p.coupon.model.UserCoupon;
import info.bfly.p2p.coupon.service.UserCouponService;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.service.InvestRefundsService;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.exception.BorrowedMoneyTooLittle;
import info.bfly.p2p.loan.exception.ExistWaitAffirmInvests;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.model.WithdrawCash;
import info.bfly.p2p.loan.service.LoanService;
import info.bfly.p2p.repay.exception.NormalRepayException;
import info.bfly.p2p.repay.model.LoanRepay;
import info.bfly.p2p.repay.service.RepayService;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.exception.TrusteeshipFormException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipTicketException;
import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;
import info.bfly.p2p.trusteeship.service.impl.TrusteeshipOperationBO;
import info.bfly.p2p.user.service.RechargeService;
import info.bfly.p2p.user.service.WithdrawCashService;
import info.bfly.pay.bean.callback.AuditCallBackEntity;
import info.bfly.pay.bean.callback.CreateBidInfoSinaInEntity;
import info.bfly.pay.bean.callback.TradeCallBackEntity;
import info.bfly.pay.bean.enums.*;
import info.bfly.pay.controller.SinaOrderController;
import info.bfly.pay.controller.SinaSystemController;
import info.bfly.pay.p2p.trusteeship.service.SinaPayOperationService;
import info.bfly.pay.p2p.user.model.MoneyTransfer;
import info.bfly.pay.p2p.user.service.SinaMoneyTransferService;
import info.bfly.pay.util.SinaUtils;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/25 0025.
 */
@Service("sinaPayOperationService")
public class SinaPayOperationServiceImpl implements SinaPayOperationService {


    @Resource
    HibernateTemplate ht;

    @Log
    private Logger log;

    private final TrusteeshipOperationBO     trusteeshipOperationBO;
    private final InvestService              investService;
    private final LoanService                loanService;
    private final OrderService               orderService;
    private final SinaSystemController       sinaSystemController;
    private final SinaOrderController        sinaOrderController;
    private final UserCouponService          userCouponService;
    private final SinaMoneyTransferService   sinaMoneyTransferService;
    private final BaseService<MoneyTransfer> moneyTransferService;
    private final BorrowerService            borrowerService;
    private final RepayService               repayService;
    private final SinaUtils                  sinaUtils;
    private final TraceService           traceService;


    @Resource
    private RechargeService rechargeService;

    @Resource
    private InvestRefundsService investRefundsService;


    @Resource
    private WithdrawCashService wcsService;

    @Autowired
    private UserService  userService;
    
    @Autowired
    private ObjectMapper om;

    @Autowired
    public SinaPayOperationServiceImpl(TrusteeshipOperationBO trusteeshipOperationBO, InvestService investService, SinaSystemController sinaSystemController, SinaOrderController sinaOrderController, UserCouponService userCouponService, BaseService<MoneyTransfer> moneyTransferService, RepayService repayService, SinaUtils sinaUtils, LoanService loanService, OrderService orderService, SinaMoneyTransferService sinaMoneyTransferService, BorrowerService borrowerService, TraceService traceService) {
        this.trusteeshipOperationBO = trusteeshipOperationBO;
        this.investService = investService;
        this.sinaSystemController = sinaSystemController;
        this.sinaOrderController = sinaOrderController;
        this.userCouponService = userCouponService;
        this.moneyTransferService = moneyTransferService;
        this.repayService = repayService;
        this.sinaUtils = sinaUtils;
        this.loanService = loanService;
        this.orderService = orderService;
        this.sinaMoneyTransferService = sinaMoneyTransferService;
        this.borrowerService = borrowerService;
        this.traceService = traceService;
    }


    @Override
    public void TradeStatusSyncOperation(String callbackOperationId) {

        TrusteeshipOperation callbackOperation = trusteeshipOperationBO.get(callbackOperationId);

        if (callbackOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.CALLBACK)) {


            //????????????????????????
            TrusteeshipOperation parentOperation = callbackOperation.getOriginalOperation();
            String orderNo = callbackOperation.getMarkId();
            //TODO ?????????????????????
            if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.RECREATE)) {
                parentOperation = parentOperation.getOriginalOperation();
            }
            //??????????????????????????????
            if (parentOperation == null) {
                trusteeshipOperationBO.error(callbackOperation.getId(), "??????????????????????????????");
                return;
            }
            String targetId = parentOperation.getMarkId();
            TradeCallBackEntity tradeCallBackEntity;
            try {
                tradeCallBackEntity = om.readValue(callbackOperation.getResponseData(), TradeCallBackEntity.class);
            } catch (IOException e) {
                trusteeshipOperationBO.error(callbackOperation.getId(), "????????????TradeCallBackEntity??????");
                return;
            }

            if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.PAY_FINISHED)) {
                trusteeshipOperationBO.waiting(callbackOperation.getId());
                trusteeshipOperationBO.waiting(parentOperation.getId());
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.TRADE_CLOSED)) {

                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.INVEST)) {
                    Invest invest = investService.get(targetId);
                    investService.fail(invest);
                    trusteeshipOperationBO.success(parentOperation.getId());
                    trusteeshipOperationBO.success(callbackOperation.getId());
                }
                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.MONEY_TRANSFER_FROM)) {
                    MoneyTransfer moneyTransfer = moneyTransferService.get(MoneyTransfer.class, targetId);
                    try {
                        sinaMoneyTransferService.refuseMoneyTransfer(moneyTransfer);
                    } catch (InsufficientBalance insufficientBalance) {
                        log.error(insufficientBalance.getMessage());
                    }
                    if (UserConstants.MoneyTransferType.USERCOUPON.equals(moneyTransfer.getType())) {
                        userCouponService.disable(moneyTransfer.getTarget());
                    }
                }
                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.MONEY_TRANSFER_TO)) {

                }
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.PRE_AUTH_APPLY_SUCCESS)) {

                //??????????????????
                switch (parentOperation.getType()) {
                    case TrusteeshipConstants.OperationType.INVEST:
                        Invest invest = investService.get(targetId);
                        if (invest == null) {
                            trusteeshipOperationBO.error(parentOperation.getId(), "?????????????????????");
                            trusteeshipOperationBO.error(callbackOperation.getId());
                            //TODO ????????????
                            return;
                        }
                        invest.setOperationOrderNo(orderNo);
                        investService.update(invest);
                        //????????????
                        if (0 == ArithUtil.compareTo(invest.getMoney(), tradeCallBackEntity.getTrade_amount().doubleValue())) {
                            //??????????????????
                            try {
                                invest = investService.frozen(invest);
                                log.info("{} ??????????????????????????????", invest.getId());
                                //??????????????????
                                String loanId = invest.getLoan().getId();
                                //??????????????????
                                Loan loan = loanService.get(loanId);
                                Invest invest1 = investService.get(invest.getId());
                                if ("??????".equals(loan.getBusinessType()) && invest1.getOrder() != null) {
                                    Order order = orderService.get(invest1.getOrder().getId());
                                    order.setOrderStatus(OrdersConstants.OrderStatus.PAYED);
                                    order.setInvest(invest1);
                                    orderService.update(order);
                                    traceService.saveGoodsRecord(order);
                                }
                                if (loanService.isCompletedLoan(loanId)) {
                                    log.info(loan.getId() + "??????????????????!");
                                    sinaSystemController.finishPreAuthTradeSinaPay(loan);
                                    loan.setStatus(LoanConstants.LoanStatus.WAITING_RECHECK_VERIFY);
                                    loanService.update(loan);
                                }
                            } catch (NoMatchingObjectsException e) {
                                investService.fail(invest);
                                //TODO ?????????????????????????????????????????? ?????????????????? ???????????????
                            } catch (TrusteeshipReturnException e) {
                                return;//????????????????????????
                            }
                            trusteeshipOperationBO.waiting(parentOperation.getId());
                            trusteeshipOperationBO.waiting(callbackOperation.getId());
                        } else {
                            //TODO ???????????? ??????????????????????????????
                            trusteeshipOperationBO.error(parentOperation.getId(), "??????????????????");
                            trusteeshipOperationBO.error(callbackOperation.getId());
                        }
                        return;
                    case TrusteeshipConstants.OperationType.REPAY:
                        //????????????
                        LoanRepay lr = repayService.get(targetId);
                        if (lr == null) {
                            trusteeshipOperationBO.error(parentOperation.getId(), "?????????????????????");
                            trusteeshipOperationBO.error(callbackOperation.getId());
                            //TODO ????????????
                            return;
                        }
                        if (lr.getStatus().equals(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY)) {
                            lr.setOperationOrderNo(orderNo);
                            lr.setStatus(LoanConstants.RepayStatus.REPAY_FROZEN);
                            repayService.update(lr);
                        }
                        log.info(lr.getId() + "????????????????????????!");
                        try {
                            sinaSystemController.finishPreAuthTradeSinaPay(lr);
                            trusteeshipOperationBO.waiting(parentOperation.getId());
                            trusteeshipOperationBO.waiting(callbackOperation.getId());
                        } catch (TrusteeshipReturnException e) {
                            log.error("Repay {} ??????????????????", lr.getId());
                        }
                        return;
                }
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.TRADE_FINISHED)) {
                log.info("??????????????????");
                switch (parentOperation.getType()) {
                    case TrusteeshipConstants.OperationType.REPAY:
                        //???????????????
                        try {
                            repayService.finishRepay(targetId);
                            log.info("???????????? {} ????????????!", targetId);
                            trusteeshipOperationBO.success(parentOperation.getId());
                            trusteeshipOperationBO.success(callbackOperation.getId());
                        } catch (InsufficientBalance e) {
                            trusteeshipOperationBO.error(parentOperation.getId(), e.getMessage());
                            trusteeshipOperationBO.error(callbackOperation.getId(), e.getMessage());
                        }
                        return;
                    case TrusteeshipConstants.OperationType.REPAY_BATCH:
                        //????????????????????????
                        try {
                            repayService.finishInvestRepay(targetId);
                            log.info("???????????????????????? {} ????????????!", targetId);
                            trusteeshipOperationBO.success(parentOperation.getId());
                            trusteeshipOperationBO.success(callbackOperation.getId());
                        } catch (InsufficientBalance | NormalRepayException e) {
                            trusteeshipOperationBO.error(parentOperation.getId(), e.getMessage());
                            trusteeshipOperationBO.error(callbackOperation.getId(), e.getMessage());
                        }
                        return;
                    case TrusteeshipConstants.OperationType.INVEST:

                        // ??????
                        Invest invest = investService.get(targetId);//??????ID
                        String pay_method = tradeCallBackEntity.getPay_method();

                        invest.setPayMethod(pay_method);
                        investService.update(invest);
                        if (InvestConstants.InvestStatus.WAIT_LOANING_VERIFY.equals(invest.getStatus()) || InvestConstants.InvestStatus.WAIT_AFFIRM.equals(invest.getStatus())) {
                            try {
                                invest = investService.success(invest);
                                log.info("?????? {} ????????????!", invest.getId());
                                Loan loan = loanService.get(invest.getLoan().getId());
                                if (loan.getStatus().equals(LoanConstants.LoanStatus.WAITING_RECHECK_VERIFY) && loanService.isLoanFullInvest(loan.getId())) {
                                    log.info("?????? {} ????????????!", loan.getId());
                                    loan.setStatus(LoanConstants.LoanStatus.RECHECK);
                                    loanService.update(loan);
                                    log.info(loan.getId() + "??????????????????!");
                                }
                            } catch (NoMatchingObjectsException | InsufficientBalance e) {
                                trusteeshipOperationBO.error(parentOperation.getId());
                                trusteeshipOperationBO.error(callbackOperation.getId());
                            }
                            trusteeshipOperationBO.success(parentOperation.getId());
                            trusteeshipOperationBO.success(callbackOperation.getId());
                        } else {
                            //???????????????????????????????????????
                            trusteeshipOperationBO.refuse(parentOperation.getId());
                            trusteeshipOperationBO.refuse(callbackOperation.getId());
                            //TODO ????????????
                        }
                        return;
                    case TrusteeshipConstants.OperationType.GIVE_MOENY_TO_BORROWER:
                        //??????
                        try {
                            loanService.giveMoneyToBorrower(targetId);
                            log.info(targetId + "????????????");
                            trusteeshipOperationBO.success(callbackOperation.getId());
                            trusteeshipOperationBO.success(parentOperation.getId());
                        } catch (ExistWaitAffirmInvests | BorrowedMoneyTooLittle | RuntimeException e) {
                            //TODO ???????????????????????????
                            trusteeshipOperationBO.error(callbackOperation.getId(), e.getMessage());
                            trusteeshipOperationBO.error(parentOperation.getId(), e.getMessage());
                        }
                        return;
                    //??????????????????
                    case TrusteeshipConstants.OperationType.ONE_GIVE_MOENY_TO_BORROWER:
                        try {
                            loanService.giveMoneyToOneBorrower(targetId);
                            log.info(targetId + "??????????????????");
                            trusteeshipOperationBO.success(callbackOperation.getId());
                            trusteeshipOperationBO.success(parentOperation.getId());
                        } catch (Exception e) {
                            trusteeshipOperationBO.error(callbackOperation.getId(), e.getMessage());
                            trusteeshipOperationBO.error(parentOperation.getId(), e.getMessage());
                        }
                        return;
                    case TrusteeshipConstants.OperationType.MONEY_TRANSFER_FROM:
                        handleMoneyTransferFromSuccess(targetId);
                        trusteeshipOperationBO.success(callbackOperation.getId());
                        trusteeshipOperationBO.success(parentOperation.getId());
                        break;
                    case TrusteeshipConstants.OperationType.MONEY_TRANSFER_TO:
                        handleMoneyTransferToSuccess(targetId);
                        trusteeshipOperationBO.success(callbackOperation.getId());
                        trusteeshipOperationBO.success(parentOperation.getId());
                        break;

                }
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.WAIT_PAY)) {
                trusteeshipOperationBO.waiting(callbackOperation.getId());
                trusteeshipOperationBO.waiting(parentOperation.getId());

            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.PRE_AUTH_CANCELED)) {
                //?????????????????? ??????????????????(????????????)

                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.INVEST)) {
                    investService.cancelOrder(targetId);
                    trusteeshipOperationBO.success(callbackOperation.getId());
                    trusteeshipOperationBO.success(parentOperation.getId());
                    log.info(targetId + "??????????????????!");
                }
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.TRADE_FAILED)) {
                trusteeshipOperationBO.error(callbackOperation.getId(), "????????????????????????");
                trusteeshipOperationBO.error(parentOperation.getId(), "????????????????????????");
                //TODO ?????????????????? ???????????????
                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.INVEST)) {
                    Invest invest = investService.get(targetId);
                    investService.fail(invest);
                }

            }
        } else {
            return;
        }

    }

    //????????????????????? ??????????????????
    @Transactional(noRollbackFor = {TrusteeshipFormException.class, TrusteeshipTicketException.class})
    private void handleMoneyTransferFromSuccess(String transferId) {
        MoneyTransfer moneyTransfer = moneyTransferService.get(MoneyTransfer.class, transferId, LockMode.PESSIMISTIC_WRITE);
        try {
            if (UserConstants.MoneyTransferStatus.FROM_PROCESS.equals(moneyTransfer.getStatus())) {
                moneyTransfer.setStatus(UserConstants.MoneyTransferStatus.WAIT_RECHECK);
                moneyTransferService.update(moneyTransfer);
            }
            if (userService.isSystem(moneyTransfer.getFromUser())) {
                moneyTransfer.setVerifyMessage("???????????????????????????");
                moneyTransfer.setVerifyTime(new Date());
                moneyTransfer.setVerifyUser(moneyTransfer.getFromUser());
                sinaMoneyTransferService.passMoneyTransfer(moneyTransfer);
            }
        } catch (TrusteeshipReturnException e) {
            log.error(e.getMessage());
            //TODO MESSAGE
        }
    }

    ;

    //????????????????????????
    @Transactional
    private void handleMoneyTransferToSuccess(String transferId) {
        MoneyTransfer moneyTransfer = moneyTransferService.get(MoneyTransfer.class, transferId);
        String type = moneyTransfer.getType();
        try {
            sinaMoneyTransferService.finishMoneyTransfer(moneyTransfer);
        } catch (InsufficientBalance insufficientBalance) {
            log.error(insufficientBalance.getMessage());
        }
        if (UserConstants.MoneyTransferType.USERCOUPON.equals(type)) {
            UserCoupon userCoupon = userCouponService.get(moneyTransfer.getTarget(), LockMode.PESSIMISTIC_WRITE);
            if (CouponConstants.UserCouponStatus.SUCCESS.equals(userCoupon.getStatus())) {
                userCoupon.setStatus(CouponConstants.UserCouponStatus.SUCCESS);
                userCouponService.update(userCoupon);
            }
        }
    }

    ;

    @Override
    public void AuditStatusSyncOperation(String callbackOperationId) {

        TrusteeshipOperation callbackOperation = trusteeshipOperationBO.get(callbackOperationId);

        if (callbackOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.CALLBACK)) {
            //????????????????????????
            TrusteeshipOperation parentOperation = callbackOperation.getOriginalOperation();
            String orderNo = callbackOperation.getMarkId();
            if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.audit_status_sync + AUDIT_STATUS.SUCCESS)) {
                borrowerService.verifyBorrowerBusinessInfo(parentOperation.getMarkId(), true, "?????????????????????????????????", TrusteeshipConstants.Trusteeship.SINAPAY);
                trusteeshipOperationBO.success(parentOperation.getId());
                trusteeshipOperationBO.success(callbackOperation.getId());
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.audit_status_sync + AUDIT_STATUS.FAILED)) {
                TrusteeshipOperation parent = callbackOperation.getOriginalOperation();

                try {
                    AuditCallBackEntity auditCallBackEntity = om.readValue(callbackOperation.getResponseData(), AuditCallBackEntity.class);
                    borrowerService.verifyBorrowerBusinessInfo(parent.getMarkId(), false, "????????????????????????????????????" + auditCallBackEntity.getAudit_message(), TrusteeshipConstants.Trusteeship.SINAPAY);
                } catch (IOException e) {
                    callbackOperation.setStatus(TrusteeshipConstants.OperationStatus.ERROR);
                    trusteeshipOperationBO.save(callbackOperation);
                }
                trusteeshipOperationBO.success(parentOperation.getId());
                trusteeshipOperationBO.success(callbackOperation.getId());
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.audit_status_sync + AUDIT_STATUS.PROCESSING)) {
                trusteeshipOperationBO.waiting(parentOperation.getId());
                trusteeshipOperationBO.waiting(callbackOperation.getId());
            }
        } else {
            return;
        }

    }

    @Override
    public void BatchTradeStatusSyncOperation(String callbackOperationId) {

        TrusteeshipOperation callbackOperation = trusteeshipOperationBO.get(callbackOperationId);

        if (callbackOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.CALLBACK)) {

        } else {
            return;
        }
    }

    @Override
    public void BidStatusSyncOperation(String callbackOperationId) {

        TrusteeshipOperation callbackOperation = trusteeshipOperationBO.get(callbackOperationId);

        if (callbackOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.CALLBACK)) {

            //????????????????????????
            TrusteeshipOperation parentOperation = callbackOperation.getOriginalOperation();
            String orderNo = callbackOperation.getMarkId();
            //TODO ?????????????????????
            if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.RECREATE)) {
                parentOperation = parentOperation.getOriginalOperation();
            }
            //??????????????????????????????
            if (parentOperation == null) {
                trusteeshipOperationBO.error(callbackOperation.getId(), "??????????????????????????????");
                return;
            }
            String targetId = parentOperation.getMarkId();


            if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.bid_status_sync + BID_STATUS.VALID)) {

                CreateBidInfoSinaInEntity createBidInfoSinaInEntity = null;
                try {
                    createBidInfoSinaInEntity = om.readValue(callbackOperation.getResponseData(), CreateBidInfoSinaInEntity.class);
                } catch (IOException e) {
                    trusteeshipOperationBO.error(callbackOperation.getId());
                    return;
                }
                if (parentOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.WAITING)) {
                    //???????????? ???????????????
                    loanService.sinaPassApply(createBidInfoSinaInEntity.getOut_bid_no(), createBidInfoSinaInEntity.getOut_bid_no(), createBidInfoSinaInEntity.getInner_bid_no());
                    trusteeshipOperationBO.success(parentOperation.getId());
                    trusteeshipOperationBO.success(callbackOperation.getId());
                }
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.bid_status_sync + BID_STATUS.REJECT) || callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.bid_status_sync + BID_STATUS.INVALID)) {
                CreateBidInfoSinaInEntity createBidInfoSinaInEntity = null;
                try {
                    createBidInfoSinaInEntity = om.readValue(callbackOperation.getResponseData(), CreateBidInfoSinaInEntity.class);
                } catch (IOException e) {
                    callbackOperation.setStatus(TrusteeshipConstants.OperationStatus.ERROR);
                    trusteeshipOperationBO.save(callbackOperation);
                    return;
                }
                //???????????? ???????????????
                TrusteeshipOperation parent = callbackOperation.getOriginalOperation();
                if (parent.getStatus().equals(TrusteeshipConstants.OperationStatus.WAITING)) {
                    loanService.refuseApply(createBidInfoSinaInEntity.getOut_bid_no(), createBidInfoSinaInEntity.getReject_reason(), SecurityContextHolder.getContext().getAuthentication().getName());
                    trusteeshipOperationBO.success(parent.getId());
                    trusteeshipOperationBO.success(callbackOperation.getId());
                }
            }
        }


    }

    @Override
    public void DepositStatusSyncOperation(String callbackOperationId) {

        TrusteeshipOperation callbackOperation = trusteeshipOperationBO.get(callbackOperationId);

        if (callbackOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.CALLBACK)) {
            //????????????????????????
            TrusteeshipOperation parentOperation = callbackOperation.getOriginalOperation();
            String orderNo = callbackOperation.getMarkId();
            //TODO ?????????????????????
            if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.RECREATE)) {
                parentOperation = parentOperation.getOriginalOperation();
            }
            //??????????????????????????????
            if (parentOperation == null) {
                trusteeshipOperationBO.error(callbackOperation.getId(), "??????????????????????????????");
                return;
            }
            String targetId = parentOperation.getMarkId();

            if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.deposit_status_sync + DEPOSIT_STATUS.SUCCESS)) {
                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.RECHARGE)) {
                    rechargeService.rechargePaySuccess(targetId, orderNo);
                    trusteeshipOperationBO.success(callbackOperation.getId());
                    trusteeshipOperationBO.success(parentOperation.getId());
                }
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.deposit_status_sync + DEPOSIT_STATUS.FAILED)) {
                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.RECHARGE)) {
                    rechargeService.rechargePayFail(targetId, orderNo);
                    trusteeshipOperationBO.success(callbackOperation.getId());
                    trusteeshipOperationBO.success(parentOperation.getId());
                }
            }
        } else {
            return;
        }


    }

    @Override
    public void RefundStatusSyncOperation(String callbackOperationId) {

        TrusteeshipOperation callbackOperation = trusteeshipOperationBO.get(callbackOperationId);

        if (callbackOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.CALLBACK)) {


            //????????????????????????
            TrusteeshipOperation parentOperation = callbackOperation.getOriginalOperation();
            String orderNo = callbackOperation.getMarkId();
            //TODO ?????????????????????
            if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.RECREATE)) {
                parentOperation = parentOperation.getOriginalOperation();
            }
            //??????????????????????????????
            if (parentOperation == null) {
                trusteeshipOperationBO.error(callbackOperation.getId(), "??????????????????????????????");
                return;
            }
            String targetId = parentOperation.getMarkId();

            if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.refund_status_sync + REFUND_STATUS.SUCCESS)) {
                // ????????????   loanService;
//                TrusteeshipOperation parentOperation = trusteeshipOperationBO.get(to.getMarkId());
                investRefundsService.refundSuccess(targetId);
                callbackOperation.setStatus(TrusteeshipConstants.OperationStatus.PASSED);
                trusteeshipOperationBO.save(callbackOperation);
            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.refund_status_sync + REFUND_STATUS.FAILED)) {
                trusteeshipOperationBO.waiting(parentOperation.getId());
                trusteeshipOperationBO.waiting(callbackOperation.getId());
            }

        } else {
            return;
        }


    }

    @Override
    public void WithdrawStatusSyncOperation(String callbackOperationId) {

        TrusteeshipOperation callbackOperation = trusteeshipOperationBO.get(callbackOperationId);


        if (callbackOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.CALLBACK)) {
            //????????????????????????
            TrusteeshipOperation parentOperation = callbackOperation.getOriginalOperation();
            String orderNo = callbackOperation.getMarkId();
            //TODO ?????????????????????
            if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.RECREATE)) {
                parentOperation = parentOperation.getOriginalOperation();
            }
            //??????????????????????????????
            if (parentOperation == null) {
                trusteeshipOperationBO.error(callbackOperation.getId(), "??????????????????????????????");
                return;
            }
            String targetId = parentOperation.getMarkId();


            if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.withdraw_status_sync + WITHDRAW_STATUS.SUCCESS)) {

                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.WITHDRAW_CASH)) {
                    WithdrawCash withdrawCash = wcsService.getWithdrawById(targetId);
                    withdrawCash.setOperationOrderNo(orderNo);
                    wcsService.update(withdrawCash);
                    wcsService.confirmWithdraw(withdrawCash);
                    trusteeshipOperationBO.success(callbackOperation.getId());
                    trusteeshipOperationBO.success(parentOperation.getId());
                }

            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.withdraw_status_sync + WITHDRAW_STATUS.PROCESSING)) {
                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.WITHDRAW_CASH)) {
                    WithdrawCash withdrawCash = wcsService.getWithdrawById(targetId);
                    if (withdrawCash.getStatus().equals(UserConstants.WithdrawStatus.UNFREEZE_SUCCESS)) {
                        withdrawCash.setOperationOrderNo(orderNo);
                        wcsService.update(withdrawCash);
                        wcsService.processWithdraw(withdrawCash);
                        trusteeshipOperationBO.waiting(callbackOperation.getId());
                        trusteeshipOperationBO.waiting(parentOperation.getId());
                    }
                    if (withdrawCash.getStatus().equals(UserConstants.WithdrawStatus.SUCCESS)) {
                        trusteeshipOperationBO.success(callbackOperation.getId());
                        trusteeshipOperationBO.success(parentOperation.getId());
                    }
                }

            } else if (callbackOperation.getOperator().equals(TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.withdraw_status_sync + WITHDRAW_STATUS.FAILED)) {
                if (parentOperation.getType().equals(TrusteeshipConstants.OperationType.WITHDRAW_CASH)) {
                    WithdrawCash withdrawCash = wcsService.getWithdrawById(targetId);
                    withdrawCash.setOperationOrderNo(orderNo);
                    wcsService.update(withdrawCash);
                    wcsService.failWithdraw(withdrawCash);
                    trusteeshipOperationBO.success(callbackOperation.getId());
                    trusteeshipOperationBO.success(parentOperation.getId());

                }
            }
        } else {
            return;
        }


    }
}
