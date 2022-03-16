package info.bfly.p2p.borrower.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.borrower.model.BorrowerInfo;
import info.bfly.p2p.borrower.model.CreditRatingLog;
import info.bfly.p2p.risk.service.RiskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Component
@Scope(ScopeType.VIEW)
public class BorrowerInfoHome extends EntityHome<BorrowerInfo> implements Serializable {
    private static final long serialVersionUID = 6610669212953385288L;
    @Log
    private static Logger log;
    private final static String UPDATE_VIEW = FacesUtil.redirect("/admin/user/verifyLoanerList");
    @Resource
    private LoginUserInfo loginUserInfo;
    @Resource
    HibernateTemplate ht;
    @Resource
    private RiskService riskService;
    private String      reason;

    public BorrowerInfoHome() {
        // FIXME：保存角色的时候会执行一条更新User的语句
        setUpdateView(BorrowerInfoHome.UPDATE_VIEW);
    }

    public String getReason() {
        return reason;
    }

    // 计算风险准备金利率
    public Double getReserveRate() {
        BorrowerInfo loanerInfo = ht.get(BorrowerInfo.class, loginUserInfo.getLoginUserId());
        String riskLevel = loanerInfo.getRiskLevel();
        return riskService.getRPRateByRank(riskLevel);
    }

    /**
     * 获取用户信用等级
     *
     * @param user
     * @return
     */
    public String getUserRiskLevel(User user) {
        BorrowerInfo loanerInfo = ht.get(BorrowerInfo.class, user.getId());
        String riskLevel = loanerInfo.getRiskLevel();
        return riskLevel;
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        if (StringUtils.isNotEmpty(getInstance().getRiskLevel())) {
            CreditRatingLog creditRatingLog = new CreditRatingLog();
            creditRatingLog.setId(IdGenerator.randomUUID());
            creditRatingLog.setOperator(loginUserInfo.getLoginUserId());
            creditRatingLog.setTime(new Date());
            creditRatingLog.setReason(reason);
            creditRatingLog.setUser(getInstance().getUser());
            creditRatingLog.setDetails("信用评级改为" + getInstance().getRiskLevel());
            getBaseService().save(creditRatingLog);
            if (BorrowerInfoHome.log.isDebugEnabled())
                BorrowerInfoHome.log.debug("用户[" + loginUserInfo.getLoginUserId() + "]更改了[" + getInstance().getUser().getId() + "]的信用评级记录，评级改为" + getInstance().getRiskLevel());
        }
        return super.save();
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
