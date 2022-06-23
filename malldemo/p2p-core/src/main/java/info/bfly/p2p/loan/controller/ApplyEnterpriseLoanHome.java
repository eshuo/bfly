package info.bfly.p2p.loan.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.loan.model.ApplyEnterpriseLoan;
import info.bfly.p2p.loan.service.LoanService;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Description:企业借款申请Home
 */
@Component
@Scope(ScopeType.VIEW)
public class ApplyEnterpriseLoanHome extends EntityHome<ApplyEnterpriseLoan> implements java.io.Serializable {
    private static final long serialVersionUID = 2087191467026632897L;
    @Resource
    LoginUserInfo loginUserInfo;
    @Resource
    LoanService   loanService;

    @Override
    public Class<ApplyEnterpriseLoan> getEntityClass() {
        return ApplyEnterpriseLoan.class;
    }

    @Override
    public String save() {
        User loginUser = getBaseService().get(User.class, loginUserInfo.getLoginUserId());
        if (loginUser == null) {
            FacesUtil.addErrorMessage("请先登陆");
        }
        this.getInstance().setUser(loginUser);
        loanService.applyEnterpriseLoan(this.getInstance());
        FacesUtil.addInfoMessage("您的融资申请已经提交，请等待管理员的审核！");
        return "pretty:user_loan_applying-p2c";
    }

    /**
     * 审核企业借款
     */
    public void verify(ApplyEnterpriseLoan ael) {
        loanService.verifyEnterpriseLoan(ael);
    }
}
