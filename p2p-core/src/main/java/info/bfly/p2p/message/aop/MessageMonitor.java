package info.bfly.p2p.message.aop;

import info.bfly.core.annotations.Log;
import info.bfly.core.util.DateStyle;
import info.bfly.core.util.DateUtil;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.message.MessageConstants.UserMessageNodeId;
import info.bfly.p2p.message.service.impl.MessageBO;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 消息动作监听器，在需要发送消息的节点进行监听
 */
//@Component
//@Aspect
public class MessageMonitor {
    @Resource
    private MessageBO messageBO;
    @Log
    Logger log;

    /**
     * 借款审核通过
     *
     * @param user
     * @param role
     */
   //@AfterReturning(argNames = "loan", value = "execution(public void info.bfly.p2p.loan.service.LoanService.passApply(..)) " + "&& args(loan)")
    public void passApply(Loan loan) {
        if (log.isDebugEnabled()) {
            log.debug("MessageMonitor:passApply, 借款审核通过，借款编号：" + loan.getId());
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("loanName", loan.getName());
        params.put("time", DateUtil.DateToString(loan.getVerifyTime(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBO.sendMsg(loan.getUser(), UserMessageNodeId.LOAN_VERIFY_SUCCESS, params);
    }

    /**
     * 充值成功
     *
     * @param user
     * @param role
     */
    //@AfterReturning(argNames = "recharge", value = "execution(public void info.bfly.p2p.user.service.impl.RechargeBO.rechargeSuccess(..)) && args(recharge)")
    public void rechargeSuccess(Recharge recharge) {
        if (log.isDebugEnabled()) {
            log.debug("MessageMonitor:rechargeSuccess, rechargeId:" + recharge.getId());
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", recharge.getUser().getUsername());
        params.put("time", DateUtil.DateToString(recharge.getCallbackTime(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        params.put("money", recharge.getActualMoney().toString());
        params.put("rechargeId", recharge.getId());
        messageBO.sendMsg(recharge.getUser(), UserMessageNodeId.RECHARGE_SUCCESS, params);
    }
}
