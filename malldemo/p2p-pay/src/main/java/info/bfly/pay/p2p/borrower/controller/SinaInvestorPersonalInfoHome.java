package info.bfly.pay.p2p.borrower.controller;

import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.service.UserService;
import info.bfly.archer.user.service.VerifyHistoryService;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.borrower.controller.InvestorPersonalInfoHome;
import info.bfly.p2p.borrower.model.InvestorPersonalInfo;
import info.bfly.p2p.borrower.service.BorrowerService;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.service.TrusteeshipAccountService;
import info.bfly.pay.controller.SinaUserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by XXSun on 3/9/2017.
 */
@Component
@Scope(ScopeType.VIEW)
public class SinaInvestorPersonalInfoHome extends InvestorPersonalInfoHome {

    private static final long serialVersionUID = 503922219775893032L;
    @Autowired
    SinaUserController sinaUserController;
    @Autowired
    BorrowerService borrowerService;
    @Autowired
    TrusteeshipAccountService trusteeshipAccountService;
    @Autowired
    UserService userService;

    @Autowired
    LoginUserInfo loginUserInfo;
    @Autowired
    VerifyHistoryService verifyHistoryService;


    @Override
    public Class<InvestorPersonalInfo> getEntityClass() {
        return InvestorPersonalInfo.class;
    }


    public String save() {

        borrowerService.saveOrUpdateInvestorPersonalInfo(getInstance());

        return verify(getInstance());
    }

//    private void saveInvestor(InvestorPersonalInfo investorPersonalInfo) {
//        borrowerService.saveOrUpdateInvestorPersonalInfo(investorPersonalInfo);
//    }

    @Override
    @Transactional
    public String verify(InvestorPersonalInfo investorPersonalInfo) {
        //默认拒绝 提交新浪审核后通过
        borrowerService.verifyRealNameCertificatione(investorPersonalInfo.getUserId(), false, investorPersonalInfo.getVerifiedMessage() + (isIspass() ? ",等待新浪支付系统确认" : ""), loginUserInfo.getLoginUserId());

        FacesUtil.addInfoMessage("保存成功");
        if (isIspass()) {
            if (trusteeshipAccountService.getTrusteeshipAccount(investorPersonalInfo.getUserId(), TrusteeshipConstants.Trusteeship.SINAPAY) == null) {
                //首先新浪开户
                try {
                    sinaUserController.registerSinaPay(userService.getUserById(investorPersonalInfo.getUserId()));
                    FacesUtil.addInfoMessage("新浪开户成功");

                } catch (TrusteeshipReturnException e) {
                    FacesUtil.addInfoMessage(e.getMessage());

                }
            }
            try {
                sinaUserController.realSinaPay(investorPersonalInfo);
                FacesUtil.addInfoMessage("新浪实名申请成功");
            } catch (TrusteeshipReturnException e) {
                FacesUtil.addInfoMessage(e.getMessage());
            }

        }
        return "";
    }
}
