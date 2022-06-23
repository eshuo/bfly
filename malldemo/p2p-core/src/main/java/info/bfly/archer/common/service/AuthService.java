package info.bfly.archer.common.service;

import info.bfly.archer.common.exception.*;
import info.bfly.archer.common.model.AuthInfo;

import java.util.Date;

/**
 *
 *
 * Description: 信息认证service，生成和验证认证信息。例如：手机短信认证，邮箱认证等
 *
 * 
 */
public interface AuthService {
    /**
     * 生成认证码
     *
     * @param source 验证来源，userId之类
     * @param target 验证目标，记录手机号或者邮箱等
     * @param deadline 验证超时
     * @param authType 验证类型
     * @return
     * @throws AuthInfoAlreadyInColdException
     */
    AuthInfo createAuthInfo(String source, String target, Date deadline, String authType) throws AuthInfoAlreadyInColdException;
    /**
     * 自定义生成认证码
     *
     * @param source 验证来源，userId之类
     * @param target 验证目标，记录手机号或者邮箱等
     * @param deadline 验证有效时间
     * @param authType 验证类型
     * @param authCode 验证码
     * @return
     * @throws AuthInfoAlreadyInColdException
     */
    AuthInfo createAuthInfo(String source, String target, Date deadline, String authType,String authCode) throws AuthInfoAlreadyInColdException;


    /**
     * 验证认证码（source，target，authType 为联合主键）
     *
     * @param authCode
     * @throws NoMatchingObjectsException
     * @throws AuthInfoOutOfDateException      认证码过期异常
     * @throws AuthInfoAlreadyActivedException 认证信息已经被激活
     */
    void verifyAuthInfo(String source, String target, String authCode, String authType) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    /**
     * 获取认证信息
     * @param source
     * @param target
     * @param authType
     */
    String getAuthCode(String source, String target, String authType);

    /**
     * 强制激活
     * @param source
     * @param target
     * @param authType
     */
    void activate(String source, String target, String authType);
}
