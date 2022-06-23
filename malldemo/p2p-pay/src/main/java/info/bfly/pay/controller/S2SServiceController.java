package info.bfly.pay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;
import info.bfly.p2p.trusteeship.service.impl.TrusteeshipOperationBO;
import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.callback.*;
import info.bfly.pay.p2p.trusteeship.publisher.SinaPayPublisher;
import info.bfly.pay.util.SinaUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * 新浪回调异步通知接口
 * Created by XXSun on 2017/1/12.
 */

@Controller
@Scope(ScopeType.REQUEST)
@RequestMapping("/sina")
public class S2SServiceController {
    private final String SUCCESS = "success";
    private final String FAIL = "failed";
    @Log
    Logger log;

    @Autowired
    SinaUtils sinaUtils;

    @Autowired
    TrusteeshipOperationBO trusteeshipOperationBO;

    @Autowired
    private SinaPayPublisher sinaPayPublisher;

    ObjectMapper om = new ObjectMapper();

    @InitBinder()
    public void initBinder(WebDataBinder binder) {
        // binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        binder.setFieldMarkerPrefix(null);
    }

    /**
     * 4.0默认callBack;
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/callback")
    @ResponseBody
    public String callBack(String notify_type, HttpServletRequest request) {
        log.error("get notify type={},value={}", notify_type, request.getParameterMap());
        return SUCCESS;
    }

    /**
     * 4.3 交易结果通知
     *
     * @return
     */
    @RequestMapping(value = "/callback", params = "notify_type=trade_status_sync")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public String tradeCallback(TradeCallBackEntity tradeCallBackEntity) {
        // tradeCallBackEntity.set_input_charset(charset);
        log.debug("交易结果通知接收参数：" + tradeCallBackEntity.toString());
        //获取原始记录
        TrusteeshipOperation trusteeshipOperation = trusteeshipOperationBO.get(tradeCallBackEntity.getOuter_trade_no());
        //数据已经处理了
        if (trusteeshipOperation == null || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.PASSED) || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.REFUSED)) {
            return SUCCESS;
        }
        //记录请求数据
        TrusteeshipOperation saveOp = new TrusteeshipOperation();
        saveOp.setId(IdGenerator.randomUUID());
        saveOp.setType(TrusteeshipConstants.OperationType.CALLBACK);
        saveOp.setStatus(TrusteeshipConstants.OperationStatus.CALLBACK);
        saveOp.setMarkId(tradeCallBackEntity.getOuter_trade_no());
        saveOp.setOperator(TrusteeshipConstants.Trusteeship.SINAPAY + tradeCallBackEntity.getNotify_type().getType_name() + tradeCallBackEntity.getTrade_status());
        log.error("记录交易交过通知：" + TrusteeshipConstants.Trusteeship.SINAPAY + tradeCallBackEntity.getNotify_type().getType_name() + tradeCallBackEntity.getTrade_status());
        saveOp.setResponseTime(new Date());
        try {
            saveOp.setResponseData(om.writeValueAsString(tradeCallBackEntity));
        } catch (JsonProcessingException e) {
            saveOp.setResponseData(tradeCallBackEntity.toString());
        }
        saveOp.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);

        saveOp.setOriginalOperation(trusteeshipOperation);

        checkSign(tradeCallBackEntity, saveOp);
        if (trusteeshipOperationBO.get(saveOp.getType(), saveOp.getMarkId(), saveOp.getOperator(), saveOp.getTrusteeship()) != null) {
            //记录已经存在
            return SUCCESS;
        }
        trusteeshipOperationBO.save(saveOp);

        //TODO 改定时任务
        sinaPayPublisher.TradeStatusSyncEventPublisher(saveOp.getId());

        return SUCCESS;
    }

    /**
     * 4.4 批处理交易结果通知
     *
     * @param batchTradeCallBackEntity
     * @return
     */
    @RequestMapping(value = "/callback", params = "notify_type=batch_trade_status_sync")
    @ResponseBody
    public String batchTradeCallBack(BatchTradeCallBackEntity batchTradeCallBackEntity) {
        log.debug("批处理交易结果通知接收参数：" + batchTradeCallBackEntity.toString());

        //获取原始记录
        TrusteeshipOperation trusteeshipOperation = trusteeshipOperationBO.get(batchTradeCallBackEntity.getOuter_batch_no());
        //数据已经处理了
        if (trusteeshipOperation == null || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.PASSED) || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.REFUSED)) {
            return SUCCESS;
        }
        //记录请求数据
        TrusteeshipOperation saveOp = new TrusteeshipOperation();
        saveOp.setId(IdGenerator.randomUUID());
        saveOp.setType(TrusteeshipConstants.OperationType.CALLBACK);
        saveOp.setStatus(TrusteeshipConstants.OperationStatus.CALLBACK);
        saveOp.setMarkId(batchTradeCallBackEntity.getOuter_batch_no());
        saveOp.setOperator(TrusteeshipConstants.Trusteeship.SINAPAY + batchTradeCallBackEntity.getNotify_type().getType_name() + batchTradeCallBackEntity.getBatch_status());
        log.error("批处理交易结果通知：" + TrusteeshipConstants.Trusteeship.SINAPAY + batchTradeCallBackEntity.getNotify_type().getType_name() + batchTradeCallBackEntity.getBatch_status());
        saveOp.setRequestTime(new Date());
        checkSign(batchTradeCallBackEntity, saveOp);
        saveOp.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);

        saveOp.setOriginalOperation(trusteeshipOperation);
        checkSign(batchTradeCallBackEntity, saveOp);
        if (trusteeshipOperationBO.get(saveOp.getType(), saveOp.getMarkId(), saveOp.getOperator(), saveOp.getTrusteeship()) != null) {
            //记录已经存在
            return SUCCESS;
        }
        trusteeshipOperationBO.save(saveOp);

        //TODO 改定时任务
        sinaPayPublisher.BatchTradeStatusSyncEventPublisher(saveOp.getId());

        return SUCCESS;
    }


    /**
     * 4.5 退款结果通知
     *
     * @param refundCallBackEntity
     * @return
     */
    @RequestMapping(value = "/callback", params = "notify_type=refund_status_sync")
    @ResponseBody
    public String RefundCallBack(RefundCallBackEntity refundCallBackEntity) {
        log.debug("退款结果通知接收参数：" + refundCallBackEntity.toString());

        //获取原始记录
        TrusteeshipOperation trusteeshipOperation = trusteeshipOperationBO.get(refundCallBackEntity.getOuter_trade_no());
        //数据已经处理了
        if (trusteeshipOperation == null || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.PASSED) || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.REFUSED)) {
            return SUCCESS;
        }
        //记录请求数据
        TrusteeshipOperation saveOp = new TrusteeshipOperation();
        saveOp.setId(IdGenerator.randomUUID());
        saveOp.setType(TrusteeshipConstants.OperationType.CALLBACK);
        saveOp.setStatus(TrusteeshipConstants.OperationStatus.CALLBACK);
        saveOp.setMarkId(refundCallBackEntity.getOuter_trade_no());
        saveOp.setOperator(TrusteeshipConstants.Trusteeship.SINAPAY + refundCallBackEntity.getNotify_type().getType_name() + refundCallBackEntity.getRefund_status());

        log.error("退款结果通知：" + TrusteeshipConstants.Trusteeship.SINAPAY + refundCallBackEntity.getNotify_type().getType_name() + refundCallBackEntity.getRefund_status());
        saveOp.setRequestTime(new Date());
        try {
            saveOp.setResponseData(om.writeValueAsString(refundCallBackEntity));
        } catch (JsonProcessingException e) {
            saveOp.setResponseData(refundCallBackEntity.toString());
        }
        saveOp.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);

        saveOp.setOriginalOperation(trusteeshipOperation);
        checkSign(refundCallBackEntity, saveOp);
        if (trusteeshipOperationBO.get(saveOp.getType(), saveOp.getMarkId(), saveOp.getOperator(), saveOp.getTrusteeship()) != null) {
            //记录已经存在
            return SUCCESS;
        }
        trusteeshipOperationBO.save(saveOp);

        //TODO 改定时任务
        sinaPayPublisher.RefundStatusSyncEventPublisher(saveOp.getId());

        return SUCCESS;
    }


    /**
     * 4.6 充值结果通知
     *
     * @param depositCallBackEntity
     * @return
     */
    @RequestMapping(value = "/callback", params = "notify_type=deposit_status_sync")
    @ResponseBody
    public String DepositCallBack(DepositCallBackEntity depositCallBackEntity, @RequestParam Map<String, String> requestMap, @RequestParam("_input_charset") String chareset) {
        log.debug("充值结果通知接收参数depositCallBackEntity：" + depositCallBackEntity.toString());
        log.debug("充值结果通知接收参数requestMap：" + requestMap.toString());
        //获取原始记录
        TrusteeshipOperation trusteeshipOperation = trusteeshipOperationBO.get(depositCallBackEntity.getOuter_trade_no());
        //数据已经处理了
        if (trusteeshipOperation == null || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.PASSED) || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.REFUSED)) {
            return SUCCESS;
        }

        //记录请求数据
        TrusteeshipOperation saveOp = new TrusteeshipOperation();
        saveOp.setId(IdGenerator.randomUUID());
        saveOp.setType(TrusteeshipConstants.OperationType.CALLBACK);
        saveOp.setStatus(TrusteeshipConstants.OperationStatus.CALLBACK);
        saveOp.setMarkId(depositCallBackEntity.getOuter_trade_no());
        saveOp.setOperator(TrusteeshipConstants.Trusteeship.SINAPAY + depositCallBackEntity.getNotify_type().getType_name() + depositCallBackEntity.getDeposit_status());

        log.error("充值结果通知：" + TrusteeshipConstants.Trusteeship.SINAPAY + depositCallBackEntity.getNotify_type().getType_name() + depositCallBackEntity.getDeposit_status());
        saveOp.setRequestTime(new Date());
        try {
            saveOp.setResponseData(om.writeValueAsString(depositCallBackEntity));
        } catch (JsonProcessingException e) {
            saveOp.setResponseData(depositCallBackEntity.toString());
        }
        saveOp.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);

        saveOp.setOriginalOperation(trusteeshipOperation);

        checkSign(depositCallBackEntity, saveOp);
        if (trusteeshipOperationBO.get(saveOp.getType(), saveOp.getMarkId(), saveOp.getOperator(), saveOp.getTrusteeship()) != null) {
            //记录已经存在
            return SUCCESS;
        }
        trusteeshipOperationBO.save(saveOp);

        //TODO 改定时任务
        sinaPayPublisher.DepositStatusSyncEventPublisher(saveOp.getId());

        return SUCCESS;
    }


    /**
     * 4.7 提现结果通知
     *
     * @param withdrawCallBackEntity
     * @return
     */
    @RequestMapping(value = "/callback", params = "notify_type=withdraw_status_sync")
    @ResponseBody
    public String WithdrawCallBack(WithdrawCallBackEntity withdrawCallBackEntity) {
        log.debug("提现结果通知接收参数：" + withdrawCallBackEntity.toString());

        //获取原始记录
        TrusteeshipOperation trusteeshipOperation = trusteeshipOperationBO.get(withdrawCallBackEntity.getOuter_trade_no());
        //数据已经处理了
        if (trusteeshipOperation == null || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.PASSED) || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.REFUSED)) {
            return SUCCESS;
        }
        //记录请求数据
        TrusteeshipOperation saveOp = new TrusteeshipOperation();
        saveOp.setId(IdGenerator.randomUUID());
        saveOp.setType(TrusteeshipConstants.OperationType.CALLBACK);
        saveOp.setStatus(TrusteeshipConstants.OperationStatus.CALLBACK);
        saveOp.setMarkId(withdrawCallBackEntity.getOuter_trade_no());
        saveOp.setOperator(TrusteeshipConstants.Trusteeship.SINAPAY + withdrawCallBackEntity.getNotify_type().getType_name() + withdrawCallBackEntity.getWithdraw_status());

        log.error("提现结果通知：" + TrusteeshipConstants.Trusteeship.SINAPAY + withdrawCallBackEntity.getNotify_type().getType_name() + withdrawCallBackEntity.getWithdraw_status());
        saveOp.setRequestTime(new Date());
        try {
            saveOp.setResponseData(om.writeValueAsString(withdrawCallBackEntity));
        } catch (JsonProcessingException e) {
            saveOp.setResponseData(withdrawCallBackEntity.toString());
        }
        saveOp.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
        saveOp.setOriginalOperation(trusteeshipOperation);

        checkSign(withdrawCallBackEntity, saveOp);
        if (trusteeshipOperationBO.get(saveOp.getType(), saveOp.getMarkId(), saveOp.getOperator(), saveOp.getTrusteeship()) != null) {
            //记录已经存在
            return SUCCESS;
        }
        trusteeshipOperationBO.save(saveOp);

        //TODO 改定时任务
        sinaPayPublisher.WithdrawStatusSyncEventPublisher(saveOp.getId());


        return SUCCESS;
    }


    /**
     * 4.8 企业会员审核通知
     *
     * @param auditCallBackEntity
     * @return
     */
    @RequestMapping(value = "/callback", params = "notify_type=audit_status_sync")
    @ResponseBody
    public String auditCallback(AuditCallBackEntity auditCallBackEntity) {
        log.debug("企业会员审核通知接收参数：" + auditCallBackEntity.toString());

        //获取原始记录
        TrusteeshipOperation trusteeshipOperation = trusteeshipOperationBO.get(auditCallBackEntity.getAudit_order_no());
        //数据已经处理了
        if (trusteeshipOperation == null || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.PASSED) || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.REFUSED)) {
            return SUCCESS;
        }
        //记录请求数据
        TrusteeshipOperation saveOp = new TrusteeshipOperation();
        saveOp.setId(IdGenerator.randomUUID());
        saveOp.setType(TrusteeshipConstants.OperationType.CALLBACK);
        saveOp.setStatus(TrusteeshipConstants.OperationStatus.CALLBACK);
        saveOp.setMarkId(auditCallBackEntity.getAudit_order_no());
        saveOp.setOperator(TrusteeshipConstants.Trusteeship.SINAPAY + auditCallBackEntity.getNotify_type().getType_name() + auditCallBackEntity.getAudit_status());

        log.error("企业会员审核通知：" + TrusteeshipConstants.Trusteeship.SINAPAY + auditCallBackEntity.getNotify_type().getType_name() + auditCallBackEntity.getAudit_status());
        saveOp.setRequestTime(new Date());
        try {
            saveOp.setResponseData(om.writeValueAsString(auditCallBackEntity));
        } catch (JsonProcessingException e) {
            saveOp.setResponseData(auditCallBackEntity.toString());
        }
        saveOp.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
        saveOp.setOriginalOperation(trusteeshipOperation);

        checkSign(auditCallBackEntity, saveOp);
        if (trusteeshipOperationBO.get(saveOp.getType(), saveOp.getMarkId(), saveOp.getOperator(), saveOp.getTrusteeship()) != null) {
            //记录已经存在
            return SUCCESS;
        }
        trusteeshipOperationBO.save(saveOp);

        //TODO 改定时任务
        sinaPayPublisher.AuditStatusSyncEventPublisher(saveOp.getId());

        return SUCCESS;
    }

    /**
     * 4.9 标审核通知
     *
     * @param bidCallBackEntity
     * @return
     */
    @RequestMapping(value = "/callback", params = "notify_type=bid_status_sync")
    @ResponseBody
    public String auditCallback(BidCallBackEntity bidCallBackEntity) {
        log.debug("标审核通知接收参数：" + bidCallBackEntity.toString());
        //获取原始记录 这里需要用markId获取
        TrusteeshipOperation trusteeshipOperation = trusteeshipOperationBO.get(bidCallBackEntity.getOut_bid_no());
        //数据已经处理了
        if (trusteeshipOperation == null || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.PASSED) || trusteeshipOperation.getStatus().equals(TrusteeshipConstants.OperationStatus.REFUSED)) {
            return SUCCESS;
        }
        //记录请求数据
        TrusteeshipOperation saveOp = new TrusteeshipOperation();
        saveOp.setId(IdGenerator.randomUUID());
        saveOp.setType(TrusteeshipConstants.OperationType.CALLBACK);
        saveOp.setStatus(TrusteeshipConstants.OperationStatus.CALLBACK);
        saveOp.setMarkId(bidCallBackEntity.getOut_bid_no());
        saveOp.setOperator(TrusteeshipConstants.Trusteeship.SINAPAY + bidCallBackEntity.getNotify_type().getType_name() + bidCallBackEntity.getBid_status());

        log.error("标审核通知：" + TrusteeshipConstants.Trusteeship.SINAPAY + bidCallBackEntity.getNotify_type().getType_name() + bidCallBackEntity.getBid_status());
        saveOp.setRequestTime(new Date());
        try {
            saveOp.setResponseData(om.writeValueAsString(bidCallBackEntity));
        } catch (JsonProcessingException e) {
            saveOp.setResponseData(bidCallBackEntity.toString());
        }
        saveOp.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);

        saveOp.setOriginalOperation(trusteeshipOperation);
        checkSign(bidCallBackEntity, saveOp);
        if (trusteeshipOperationBO.get(saveOp.getType(), saveOp.getMarkId(), saveOp.getOperator(), saveOp.getTrusteeship()) != null) {
            //记录已经存在
            return SUCCESS;
        }
        trusteeshipOperationBO.save(saveOp);

        //TODO 改定时任务
        sinaPayPublisher.BidStatusSyncEventPublisher(saveOp.getId());

        return SUCCESS;
    }

    /**
     * 3.1.1 用户信息变更通知
     *
     * @param userInfoCallBackEntity
     * @return
     */
    @RequestMapping(value = "/callback", params = "notify_type=mig_set_pay_password")
    @ResponseBody
    public String auditCallback(UserInfoCallBackEntity userInfoCallBackEntity) {
        log.debug("用户信息变更通知" + userInfoCallBackEntity.toString());
        return SUCCESS;
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return FAIL;
    }


    private void checkSign(SinaInEntity entity, TrusteeshipOperation saveOp) {
        try {
            if (sinaUtils.checkSign(entity))
                return;

        } catch (UnsupportedEncodingException e) {
            log.error("checkSign Failed");
        }
        saveOp.setType(TrusteeshipConstants.OperationType.CHECK_SIGN_ERROR);
        saveOp.setStatus(TrusteeshipConstants.OperationStatus.CHECK_SIGN_ERROR);
    }

}
