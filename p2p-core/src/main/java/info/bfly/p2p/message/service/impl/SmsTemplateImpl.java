package info.bfly.p2p.message.service.impl;

import info.bfly.core.util.DateStyle;
import info.bfly.core.util.DateUtil;
import info.bfly.p2p.message.MessageConstants;
import info.bfly.p2p.message.model.UserMessageTemplate;
import info.bfly.p2p.message.service.SmsTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service
public class SmsTemplateImpl implements SmsTemplate {
    @Resource
    private HibernateTemplate ht;
    @Resource
    private MessageBO         messageBo;

    @Override
    public void sendBorrowerReceiveRemind(String name, String mobileNumber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBo.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.BORROWER_RECEIVE_REMIND + "_sms"), params, mobileNumber);
    }

    @Override
    public void sendBorrowerReimburseRemind(String name, String mobileNumber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBo.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.BORROWER_REPAY_REMIND + "_sms"), params, mobileNumber);
    }

    @Override
    public void sendInvestorFlowRemind(String name, String mobileNumber, Double money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("money", String.valueOf(money));
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBo.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.INVEST_FLOW_REMIND + "_sms"), params, mobileNumber);
    }

    @Override
    public void sendInvestorFullRemind(String name, String mobileNumber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBo.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.INVEST_FULL_REMIND + "_sms"), params, mobileNumber);
        // System.out.println("???????????????#{name}?????????????????????????????????????????????????????????????????????????????????????????????XXXX.XX???????????????????????????????????????????????????");
    }

    @Override
    public void sendInvestorReceiveRemind(String name, String mobileNumber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBo.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.INVEST_REPAY_REMIND + "_sms"), params, mobileNumber);
    }

    @Override
    public void sendInvestorUnderlyDelyRemind(String name, String mobileNumber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBo.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.INVEST_DELAY_REMIND + "_sms"), params, mobileNumber);
    }

    @Override
    public void sendInvestSuccess(String mobileNumber, Double investMoney, Double ratePercent, Double income) {
        Map<String, String> params = new HashMap<String, String>();
        // ??????????????????
        params.put("investMoney", String.valueOf(investMoney));
        // ????????????
        params.put("ratePercent", String.valueOf(ratePercent));
        // ????????????
        params.put("income", String.valueOf(income));
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBo.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.INVEST_SUCCESS + "_sms"), params, mobileNumber);
    }

    @Override
    public void sendRechargeSuccess(String mobileNumber, Double money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", String.valueOf(money));
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBo.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.HAOYOUBI_RECHARGE_SUCCESS + "_sms"), params, mobileNumber);
    }

    @Override
    public void sendWithdrawSuccess(String mobileNumber, Double money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", String.valueOf(money));
        params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
        messageBo.sendSMS(ht.get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.WITHDRAW_SUCCESS + "_sms"), params, mobileNumber);
    }
}
