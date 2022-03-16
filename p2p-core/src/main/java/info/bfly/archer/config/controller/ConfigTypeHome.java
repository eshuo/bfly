package info.bfly.archer.config.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.model.Config;
import info.bfly.archer.config.model.ConfigType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
@Scope(ScopeType.VIEW)
public class ConfigTypeHome extends EntityHome<ConfigType> implements Serializable {
    private static final long serialVersionUID = -425680301716876873L;
    @Log
    static Logger log;
    private static final StringManager sm      = StringManager.getManager(ConfigConstants.Package);
    private              List<Config>  configs = new ArrayList<Config>();

    public ConfigTypeHome() {
        setUpdateView(FacesUtil.redirect(ConfigConstants.View.CONFIG_TYPE_LIST));
        setDeleteView(FacesUtil.redirect(ConfigConstants.View.CONFIG_TYPE_LIST));
    }

    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String configDetail() {
        String id = FacesUtil.getParameter("typeId");
        super.setId(id);
        getInstance().setConfigs(configs);
        super.save();
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String delete() {
        if (ConfigTypeHome.log.isInfoEnabled()) {
            ConfigTypeHome.log.info(ConfigTypeHome.sm.getString("log.info.deleteConfigType", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        return super.delete();
    }

    public List<Config> getConfigs() {
        return configs;
    }

    @PostConstruct
    public void init() {
        if (FacesUtil.getParameter("id") != null) {
            String id = FacesUtil.getParameter("id");
            super.setId(id);
            configs = new ArrayList<Config>();
            configs = getInstance().getConfigs();
        }
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String save() {
        return super.save();
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }
}
