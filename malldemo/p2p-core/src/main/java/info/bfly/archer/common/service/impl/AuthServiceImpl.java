package info.bfly.archer.common.service.impl;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.exception.*;
import info.bfly.archer.common.model.AuthInfo;
import info.bfly.archer.common.service.AuthService;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.DateStyle;
import info.bfly.core.util.DateUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;
    @Resource
    AuthInfoBO        authInfoBO;
    @Resource
    ConfigService     configService;


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public AuthInfo createAuthInfo(String source, String target, Date deadline, String authType) throws AuthInfoAlreadyInColdException {
        return createAuthInfo(source, target, deadline, authType, RandomStringUtils.random(6, false, true));
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public AuthInfo createAuthInfo(String source, String target, Date deadline, String authType, String authCode) throws AuthInfoAlreadyInColdException {
        AuthInfo ai = authInfoBO.get(source, target, authType);
        //timeout 用于配置创建验证码的冷却时间
        String timeout = configService.getConfigValue(authType + "timeout", CommonConstants.DEFAULT_TIMEOUT);
        String verify_times = configService.getConfigValue(authType + "verifytimes", CommonConstants.DEFAULT_VERIFY_TIMES);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, Integer.parseInt(timeout));
        Date time = c.getTime();
        //检查是否还在冷却中
        if (ai != null && ai.getGenerationTime().after(time)) throw new AuthInfoAlreadyInColdException("save can be actived at ["
                + DateUtil.DateToString(ai.getGenerationTime(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN) + "]");
        if (ai == null) {
            ai = new AuthInfo();
        }
        ai.setAuthTarget(target);
        ai.setAuthSource(source);
        ai.setAuthType(authType);
        ai.setAuthCode(authCode);
        ai.setStatus(CommonConstants.AuthInfoStatus.INACTIVE);
        ai.setDeadline(deadline);
        ai.setDeadtime(Integer.parseInt(verify_times));
        ai.setGenerationTime(new Date());
        authInfoBO.save(ai);
        return ai;
    }

    @Override
    public void verifyAuthInfo(String source, String target, String authCode, String authType) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException {
        if (AuthServiceImpl.log.isDebugEnabled()) {
            AuthServiceImpl.log.debug("source:" + source + " target:" + target + " authCode:" + authCode + " authType:" + authType);
        }
        AuthInfo ai = authInfoBO.get(source, target, authType);
        if (ai == null) {
            //TODO 没找到，抛异常，目前已有异常不合适
            throw new NoMatchingObjectsException(AuthInfo.class, "source:" + source + " target:" + target + " authCode:" + authCode + " authType:" + authType);
        }
        ai.setDeadtime(ai.getDeadtime() - 1);
        authInfoBO.save(ai);
        if (!StringUtils.equals(ai.getStatus(), CommonConstants.AuthInfoStatus.INACTIVE)) {
            // FIXME:不是未激活状态，抛异常
            throw new AuthInfoAlreadyActivedException("source:" + source + " target:" + target + " authCode:" + authCode + " authType:" + authType);
        }
        if (ai.getDeadline() != null && ai.getDeadline().before(new Date())) {
            // FIXME:已经过期，抛异常
            throw new AuthInfoOutOfDateException("source:" + source + " target:" + target + " authCode:" + authCode + " authType:" + authType);
        }
        if (ai.getDeadtime() < 0) {
            throw new AuthInfoOutOfTimesException("source:" + source + " target:" + target + " authCode:" + authCode + " authType:" + authType);
        }

        if (!StringUtils.equals(ai.getAuthCode(), authCode)) {
            throw new AuthCodeNotMatchException("source:" + source + " target:" + target + " authCode:" + authCode + " authType:" + authType);
        }
        authInfoBO.activate(ai);

    }

    @Override
    public String getAuthCode(String source, String target, String authType) {
        //TODO 是否部分验证信息不能读取？
        return authInfoBO.get(source, target, authType).getAuthCode();
    }

    @Override
    public void activate(String source, String target, String authType) {
        authInfoBO.activate(source, target, authType);
    }
}
