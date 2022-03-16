package info.bfly.p2p.message.service;

import info.bfly.p2p.message.exception.SmsSendErrorException;
import info.bfly.p2p.message.service.impl.ApiResultBase;

/**
 * 发短信 返回信息详见文档。
 *
 * @author Administrator
 *
 */
public interface SmsService {
    /**
     * 发送短信
     *
     * @param content
     * @param mobileNumber
     * @throws SmsSendErrorException
     */
    void send(String content, String mobileNumber) throws SmsSendErrorException;


    <T extends ApiResultBase> T send(String content, String mobileNumber, Class<T> type) throws SmsSendErrorException;
}
