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
        //???????????? ???????????????????????????
        borrowerService.verifyRealNameCertificatione(investorPersonalInfo.getUserId(), false, investorPersonalInfo.getVerifiedMessage() + (isIspass() ? ",??????????????????????????????" : ""), loginUserInfo.getLoginUserId());

        FacesUtil.addInfoMessage("????????????");
        if (isIspass()) {
            if (trusteeshipAccountService.getTrusteeshipAccount(investorPersonalInfo.getUserId(), TrusteeshipConstants.Trusteeship.SINAPAY) == null) {
                //??????????????????
                try {
                    sinaUserController.registerSinaPay(userService.getUserById(investorPersonalInfo.getUserId()));
                    FacesUtil.addInfoMessage("??????????????????");

                } catch (TrusteeshipReturnException e) {
                    FacesUtil.addInfoMessage(e.getMessage());

                }
            }
            try {
                sinaUserController.realSinaPay(investorPersonalInfo);
                FacesUtil.addInfoMessage("????????????????????????");
            } catch (TrusteeshipReturnException e) {
                FacesUtil.addInfoMessage(e.getMessage());
            }

        }
        return "";
    }
}
