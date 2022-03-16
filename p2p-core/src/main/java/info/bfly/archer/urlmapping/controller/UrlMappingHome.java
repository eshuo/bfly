package info.bfly.archer.urlmapping.controller;

import com.ocpsoft.pretty.MyPrettyFilter;
import com.ocpsoft.pretty.PrettyContext;
import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.urlmapping.UrlMappingConstants;
import info.bfly.archer.urlmapping.model.UrlMapping;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class UrlMappingHome extends EntityHome<UrlMapping> implements Serializable {
    private static final long          serialVersionUID = 5604262025783258436L;
    @Log
    static Logger log;
    private final static StringManager sm               = StringManager.getManager(UrlMappingConstants.Package);

    public UrlMappingHome() {
        setUpdateView(FacesUtil.redirect(UrlMappingConstants.View.URL_MAPPING_LIST));
        setDeleteView(FacesUtil.redirect(UrlMappingConstants.View.URL_MAPPING_LIST));
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (UrlMappingHome.log.isInfoEnabled()) {
            UrlMappingHome.log.info(UrlMappingHome.sm.getString("log.info.deleteUrlMapping", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        // 清除当前用户session里的pretty-config
        FacesUtil.getHttpSession().removeAttribute(PrettyContext.CONFIG_KEY);
        return super.delete();
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        // 判重 判断pattern是否重复
        List<UrlMapping> mappings = (List<UrlMapping>) getBaseService().findByNamedQuery("UrlMapping.findByPattern", getInstance().getPattern());
        if (mappings.size() > 0) {
            // 编辑mapping的时候本身的的 pattern已经存在于系统中
            if (!(mappings.size() == 1 && StringUtils.equals(mappings.get(0).getId(), getInstance().getId()))) {
                FacesUtil.addErrorMessage(UrlMappingHome.sm.getString("patternExist"));
                return null;
            }
        }
        // 清除当前用户session里的pretty-config
        // FacesUtil.getHttpSession().removeAttribute(PrettyContext.CONFIG_KEY);
        // 清除application中的pretty-config
        FacesUtil.getHttpSession().getServletContext().removeAttribute(MyPrettyFilter.CONFIG_KEY);
        return super.save();
    }
}
