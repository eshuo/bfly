package info.bfly.pay.p2p.borrower.controller;

import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.VerifyHistory;
import info.bfly.archer.user.service.UserService;
import info.bfly.archer.user.service.VerifyHistoryService;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.borrower.controller.BorrowerBusinessInfoHome;
import info.bfly.p2p.borrower.model.BorrowerBusinessInfo;
import info.bfly.p2p.borrower.service.BorrowerService;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.service.TrusteeshipAccountService;
import info.bfly.pay.controller.SinaUserController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.bean.ViewScoped;

/**
 * Created by XXSun on /25/2017.
 */
@Component
@ViewScoped
public class SinaBorrowerBusinessInfoHome<T extends BorrowerBusinessInfo> extends BorrowerBusinessInfoHome<BorrowerBusinessInfo> {
    @Autowired
    SinaUserController        sinaUserController;
    @Autowired
    BorrowerService           borrowerService;
    @Autowired
    TrusteeshipAccountService trusteeshipAccountService;
    @Autowired
    UserService               userService;

    @Autowired
    LoginUserInfo        loginUserInfo;
    @Autowired
    VerifyHistoryService verifyHistoryService;


    public String verifySuccess(BorrowerBusinessInfo bbi) {
        return verify(bbi, true);
    }

    public String verifyFail(BorrowerBusinessInfo bbi) {
        return verify(bbi, false);
    }

    public String agentVerifySuccess(BorrowerBusinessInfo bbi) {
        return agentVerify(bbi, true);
    }

    public String agentVerifyFail(BorrowerBusinessInfo bbi) {
        return agentVerify(bbi, false);
    }

    @Transactional
    private String verify(BorrowerBusinessInfo bbi, boolean ispass) {
        //???????????? ???????????????????????????
        VerifyHistory verifyHistory = new VerifyHistory();
        verifyHistory.setVerifyUser(new User(loginUserInfo.getLoginUserId()));
        verifyHistory.setStatus(ispass ? UserConstants.VerifyStatus.SUCCESS : UserConstants.VerifyStatus.FAIL);
        verifyHistory.setVerifyMessage(bbi.getVerifiedMessage());
        verifyHistory.setVerifyTarget(bbi.getUserId());
        verifyHistory.setVerifyType(UserConstants.VerifyType.BorrowerBusinessInfo);
        verifyHistoryService.addVerifyHistory(verifyHistory);
        FacesUtil.addInfoMessage("????????????");
        borrowerService.verifyBorrowerAuthentication(bbi.getUserId(), ispass, bbi.getVerifiedMessage(), loginUserInfo.getLoginUserId());
        borrowerService.verifyBorrowerBusinessInfo(bbi.getUserId(), false, bbi.getVerifiedMessage() + (ispass ? ",??????????????????????????????" : ""), loginUserInfo.getLoginUserId());
        if (ispass) {
            if (trusteeshipAccountService.getTrusteeshipAccount(bbi.getUserId(), TrusteeshipConstants.Trusteeship.SINAPAY) == null) {
                try {
                    sinaUserController.registerSinaPay(userService.getUserById(bbi.getUserId()));
                    FacesUtil.addInfoMessage("??????????????????");
                } catch (TrusteeshipReturnException e) {
                    FacesUtil.addInfoMessage(e.getMessage());
                }
            }
            try {
                sinaUserController.auditMemberInfosSinaPay(bbi.getUserId());
            } catch (TrusteeshipReturnException e) {
                FacesUtil.addErrorMessage(e.getMessage());
            }
            FacesUtil.addInfoMessage("????????????????????????");
        }
        return "";
    }

    @Transactional
    private String agentVerify(BorrowerBusinessInfo bbi, boolean ispass) {
        //???????????? ???????????????????????????
        VerifyHistory verifyHistory = new VerifyHistory();
        verifyHistory.setVerifyUser(new User(loginUserInfo.getLoginUserId()));
        verifyHistory.setStatus(ispass ? UserConstants.VerifyStatus.SUCCESS : UserConstants.VerifyStatus.FAIL);
        verifyHistory.setVerifyMessage(bbi.getVerifiedMessage());
        verifyHistory.setVerifyTarget(bbi.getUserId());
        verifyHistory.setVerifyType(UserConstants.VerifyType.BorrowerBusinessAgentInfo);
        verifyHistoryService.addVerifyHistory(verifyHistory);
        FacesUtil.addInfoMessage("????????????");
        borrowerService.verifyBorrowerBusinessAgentInfo(bbi.getUserId(), false, bbi.getAgentVerifiedMessage() + (ispass ? ",??????????????????????????????" : ""), loginUserInfo.getLoginUserId());
        if (bbi.getIsPassedVerify()) {
            if (ispass)
                try {
                    if (StringUtils.isNotEmpty(bbi.getAgent_id()) || sinaUserController.queryFundAgentBuySinaPay(bbi.getUserId())) {

                    } else {
                        sinaUserController.smtFundAgentBuySinaPay(bbi.getUserId());
                    }
                    borrowerService.verifyBorrowerBusinessAgentInfo(bbi.getUserId(), true, bbi.getAgentVerifiedMessage() + ",?????????????????????????????????", loginUserInfo.getLoginUserId());
                    FacesUtil.addInfoMessage("????????????????????????");
                } catch (TrusteeshipReturnException e) {
                    FacesUtil.addErrorMessage(e.getMessage());
                }
        } else
            FacesUtil.addErrorMessage("???????????????????????????????????????????????????????????????");
        return "pretty:";
    }


}
