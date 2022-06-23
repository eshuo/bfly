package info.bfly.p2p.borrower.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.borrower.BorrowerConstant;
import info.bfly.p2p.borrower.model.BorrowerAdditionalInfo;
import info.bfly.p2p.borrower.service.BorrowerService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 认证个人详细信息
 */
@Component
@Scope(ScopeType.VIEW)
public class BorrowerAdditionalInfoHome extends EntityHome<BorrowerAdditionalInfo> implements Serializable {
    private static final long    serialVersionUID = 952092753176159653L;
    private              Boolean ispass           = false;
    @Log
    private Logger          log;
    private String          verifyMessage;
    @Resource
    private LoginUserInfo   loginUser;
    @Resource
    private BorrowerService borrowService;


    public String getVerifyMessage() {
        return verifyMessage;
    }

    // 初始化登录用户信息
    @Override
    protected void initInstance() {
        BorrowerAdditionalInfo lai = new BorrowerAdditionalInfo();
        try {
            lai = borrowService.initBorrowerAdditionalInfo(loginUser.getLoginUserId());
        } catch (UserNotFoundException e) {
            log.debug(e.getMessage());
        }
        setInstance(lai);
    }

    public void initVerify(BorrowerAdditionalInfo borrowerAdditionalInfo) {
        setInstance(borrowerAdditionalInfo);
        if ((BorrowerConstant.Verify.passed).equals(this.getInstance().getVerified())) {
            ispass = true;
        }
    }

    public Boolean isIspass() {
        return ispass;
    }

    @Override
    public String save() {
        borrowService.saveOrUpdateBorrowerAdditionalInfo(getInstance());
        // FacesUtil.addInfoMessage("保存成功，请等待审核。");
        return "pretty:loanerAuthentication";
    }

    public void setIspass(Boolean ispass) {
        this.ispass = ispass;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    /**
     * 审核借款用户补充信息
     *
     * @return
     */
    public String verify(BorrowerAdditionalInfo borrowerAdditionalInfo) {
        borrowService.verifyBorrowerAdditionalInfo(borrowerAdditionalInfo.getUserId(), ispass, getInstance().getVerifiedMessage(), loginUser.getLoginUserId());
        FacesUtil.addInfoMessage("保存成功");
        return null;
    }
}
