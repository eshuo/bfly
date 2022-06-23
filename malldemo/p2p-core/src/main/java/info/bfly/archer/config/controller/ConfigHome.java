package info.bfly.archer.config.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.model.Config;
import info.bfly.archer.config.model.ConfigType;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Scope(ScopeType.VIEW)
public class ConfigHome extends EntityHome<Config> implements java.io.Serializable {
    private static final long serialVersionUID = -3402722937438296548L;
    @Log
    static Logger log;
    private final static StringManager sm = StringManager.getManager(ConfigConstants.Package);
    @Resource
    private LoginUserInfo loginUserInfo;

    public ConfigHome() {
        setUpdateView(FacesUtil.redirect(ConfigConstants.View.CONFIG_LIST));
        setDeleteView(FacesUtil.redirect(ConfigConstants.View.CONFIG_LIST));
    }

    @Override
    public Config createInstance() {
        Config config = new Config();
        config.setConfigType(new ConfigType());
        return config;
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String delete() {
        if (ConfigHome.log.isInfoEnabled()) {
            ConfigHome.log.info(ConfigHome.sm.getString("log.info.deleteConfig", loginUserInfo.getLoginUserId(), new Date(), getId()));
        }
        return super.delete();
    }

    /**
     * 通过配置编号得到配置的值
     *
     * @param configId
     * @return
     */
    public String getConfigValue(String configId) {
        Config config = getBaseService().get(Config.class, configId);
        if (config != null) {
            return config.getValue();
        }
        return "";
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String save() {
        if (StringUtils.isEmpty(getInstance().getConfigType().getId())) {
            getInstance().setConfigType(null);
        }
        return super.save();
    }
}
