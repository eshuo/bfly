package info.bfly.p2p.borrower.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.borrower.model.InvestorPersonalInfo;
import info.bfly.p2p.borrower.service.BorrowerService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;


@Component
@Scope(ScopeType.VIEW)
public class InvestorPersonalInfoHome extends EntityHome<InvestorPersonalInfo> implements Serializable {
    private static final long serialVersionUID = 6478003822213218802L;
    private Boolean ispass = false;
    @Log
    private Logger log;
    private String verifyMessage;
    @Qualifier("borrowService")
    @Resource
    private BorrowerService borrowerService;
    @Resource
    private LoginUserInfo loginUser;


    public String getVerifyMessage() {
        return verifyMessage;
    }


    @Override
    protected InvestorPersonalInfo createInstance() {
        InvestorPersonalInfo lai = new InvestorPersonalInfo();
        try {
            lai = borrowerService.initInvestorPersonalInfo(loginUser.getLoginUserId());
        } catch (UserNotFoundException e) {
            log.debug(e.getMessage());
        }
        return lai;
    }

    @Override
    public String save() {
        borrowerService.saveOrUpdateInvestorPersonalInfo(getInstance());
        FacesUtil.addInfoMessage("保存成功，请等待管理员审核。");
        return "pretty:userCenter";
    }

    public Boolean isIspass() {
        return ispass;
    }


    public void setIspass(Boolean ispass) {
        this.ispass = ispass;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }


    public String verify(InvestorPersonalInfo investorPersonalInfo) {
        borrowerService.verifyRealNameCertificatione(investorPersonalInfo.getUserId(), ispass, investorPersonalInfo.getVerifiedMessage(), loginUser.getLoginUserId());
        FacesUtil.addInfoMessage("保存成功");
        return null;
    }
}
