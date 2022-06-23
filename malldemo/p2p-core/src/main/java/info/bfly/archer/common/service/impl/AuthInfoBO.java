package info.bfly.archer.common.service.impl;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.model.AuthInfo;
import info.bfly.archer.common.model.AuthInfoPK;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("authInfoBO")
public class AuthInfoBO {
    @Resource
    private HibernateTemplate ht;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void activate(AuthInfo authInfo) {
        authInfo.setStatus(CommonConstants.AuthInfoStatus.ACTIVATED);
        ht.update(authInfo);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void activate(String source, String target, String authType) {
        AuthInfo authInfo = get(source, target, authType);
        authInfo.setStatus(CommonConstants.AuthInfoStatus.ACTIVATED);
        ht.update(authInfo);
    }

    // 存储一个来源，查询时候，来源、目标、类型，三个作为联合主键。
    public AuthInfo get(String source, String target, String authType) {
       return ht.get(AuthInfo.class,new AuthInfoPK(source,target,authType));
    }

    public AuthInfo get(String source, String target, String authType, String authCode) {
        AuthInfo ai = get(source, target, authType);
        if (ai.getAuthCode().equals(authCode)) {
            return ai;
        }
        return null;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void save(AuthInfo authInfo) {
        ht.saveOrUpdate(authInfo);
    }
}
