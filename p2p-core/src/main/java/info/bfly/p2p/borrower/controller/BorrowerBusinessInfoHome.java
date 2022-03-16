package info.bfly.p2p.borrower.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.borrower.model.BorrowerBusinessInfo;
import info.bfly.p2p.borrower.service.BorrowerService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * 企业基本信息
 * Created by XXSun on 5/23/2017.
 */
@Component
@Scope(ScopeType.VIEW)
public class BorrowerBusinessInfoHome<T extends BorrowerBusinessInfo> extends EntityHome<BorrowerBusinessInfo> {
    private static final long serialVersionUID = -3681593098801525342L;

    @Log
    private Logger log;

    @Autowired
    private BorrowerService borrowerService;

    private String userId;

    @Autowired
    private LoginUserInfo loginUser;

    @Override
    protected BorrowerBusinessInfo createInstance() {
        BorrowerBusinessInfo borrowerBusinessInfo = new BorrowerBusinessInfo();
        try {
            if (StringUtils.isNotEmpty(userId))
                borrowerBusinessInfo = borrowerService.initBorrowerBusinessInfo(userId);
            else
                borrowerBusinessInfo = borrowerService.initBorrowerBusinessInfo(loginUser.getLoginUserId());
        } catch (UserNotFoundException e) {
            log.debug(e.getMessage());
        }
        return borrowerBusinessInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PreAuthorize("hasRole('USER_EDIT')")
    public String saveByAdmin() {
        borrowerService.saveOrUpdateBorrowerBusinessInfo(getInstance());
        // FacesUtil.addInfoMessage("保存成功，请等待审核。");
        return FacesUtil.redirect("/admin/user/userList");
    }

}
