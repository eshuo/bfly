package info.bfly.archer.system.controller;

import info.bfly.archer.system.SystemConstants;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope(ScopeType.REQUEST)
public class CacheManager {
    static StringManager sm = StringManager.getManager(SystemConstants.Package);
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;

    public void clearCache() {
        SessionFactory sf = ht.getSessionFactory();
		sf.getCache().evictAllRegions();
        final String message = CacheManager.sm.getString("log.clearCacheSuccess");
        if (CacheManager.log.isDebugEnabled()) {
            CacheManager.log.debug(message);
        }
        FacesUtil.addInfoMessage(message);
    }
}
