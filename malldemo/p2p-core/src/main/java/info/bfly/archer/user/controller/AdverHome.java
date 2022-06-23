package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.user.model.AdverLeague;
import info.bfly.archer.user.model.AdverModel;
import info.bfly.archer.user.service.AdverService;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.DateUtil;
import info.bfly.core.annotations.Log; import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class AdverHome extends EntityHome<AdverLeague> implements Serializable {
    private static final long serialVersionUID = -4280065514707348738L;
    // 广告联盟
    @Resource
    private        AdverService adverService;
    private        Date         regStartDate;
    private        Date         regEndDate;
    @Log
    private static Logger log;

    public AdverService getAdverService() {
        return adverService;
    }

    public List<AdverModel> getCountGroupByMid() {
        // log.info(regStartDate);
        return adverService.getCoungGroupMid(DateUtil.DateToString(regStartDate, "yyyy-MM-dd hh:mm:ss"), DateUtil.DateToString(regEndDate, "yyyy-MM-dd hh:mm:ss"));
    }

    public Date getRegEndDate() {
        return regEndDate;
    }

    public Date getRegStartDate() {
        return regStartDate;
    }

    public String getStr() {
        String mid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("mid");
        log.info(mid);
        return mid;
    }

    public List<AdverLeague> queryList() {
        return adverService.queryList();
    }

    public void setAdverService(AdverService adverService) {
        this.adverService = adverService;
    }

    public void setRegEndDate(Date regEndDate) {
        this.regEndDate = regEndDate;
    }

    public void setRegStartDate(Date regStartDate) {
        this.regStartDate = regStartDate;
    }
}
