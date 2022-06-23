package info.bfly.p2p.borrower.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.borrower.BorrowerConstant;
import info.bfly.p2p.borrower.model.BorrowerPersonalInfo;
import info.bfly.p2p.borrower.service.BorrowerService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 用户一般信息
 */
@Component
@Scope(ScopeType.VIEW)
public class BorrowerPersonalInfoHome extends EntityHome<BorrowerPersonalInfo> implements Serializable {
    private static final long serialVersionUID = 2183425077050259095L;
    @Log
    private Logger log;
    private Boolean ispass = false;
    private String          verifyMessage;
    @Resource
    private BorrowerService borrowService;
    @Resource
    private LoginUserInfo   loginUser;

    @Override
    protected BorrowerPersonalInfo createInstance() {
        BorrowerPersonalInfo personInfo = new BorrowerPersonalInfo();
        try {
            personInfo = borrowService.initBorrowerPersonalInfo(loginUser.getLoginUserId());
        } catch (UserNotFoundException e) {
            log.debug(e.getMessage());
        }
        return personInfo;
    }

    public String getVerifyMessage() {
        return verifyMessage;
    }

    public void initVerify(BorrowerPersonalInfo borrowerPersonalInfo) {
        setInstance(borrowerPersonalInfo);
        if ((BorrowerConstant.Verify.passed).equals(this.getInstance().getVerified())) {
            ispass = true;
        }
    }

    public Boolean isIspass() {
        return ispass;
    }

    @Override
    public String save() {
        // getInstance().setUserId(loginUser.getLoginUserId());
        borrowService.saveOrUpdateBorrowerPersonalInfo(getInstance());
        // FacesUtil.addInfoMessage("保存成功，请填写“工作及财务信息”。");
        return "pretty:loanerAdditionInfo";
    }

    public void setIspass(Boolean ispass) {
        this.ispass = ispass;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    /**
     * 审核借款用户个人基本信息
     *
     * @return
     */
    public String verify(BorrowerPersonalInfo borrowerPersonalInfo) {
        borrowService.verifyBorrowerPersonalInfo(borrowerPersonalInfo.getUserId(), ispass, borrowerPersonalInfo.getVerifiedMessage(), loginUser.getLoginUserId());
        FacesUtil.addInfoMessage("保存成功");
        return null;
    }
}
