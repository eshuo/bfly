package info.bfly.app.protocol.service;

import info.bfly.app.protocol.model.request.AuthenticationValue;
import info.bfly.archer.picture.PictureConstants;
import info.bfly.archer.picture.model.AuthenticationMaterials;
import info.bfly.archer.user.model.Area;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.AreaService;
import info.bfly.core.util.DateUtil;
import info.bfly.p2p.borrower.model.BorrowerAuthentication;
import info.bfly.p2p.borrower.model.InvestorPersonalInfo;
import info.bfly.p2p.borrower.service.AuthenticationMaterialsService;
import info.bfly.p2p.borrower.service.BorrowerService;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.service.TrusteeshipAccountService;
import info.bfly.pay.controller.SinaUserController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/4/13 0013.
 */
@Service
public class ApiUserService {

    @Resource
    private HibernateTemplate ht;

    @Resource
    private SinaUserController sinaUserController;

    @Qualifier("borrowService")
    @Resource
    private BorrowerService borrowerService;

    @Autowired
    private AuthenticationMaterialsService authenticationMaterialsService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private TrusteeshipAccountService trusteeshipAccountService;


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean addUserParam(User user, AuthenticationValue value) {


        user.setRealname(value.getUserName());
        user.setIdCard(value.getIdCard());
        user.setSex(value.getSex());
        user.setBirthday(DateUtil.StringToDate(value.getBrithDate(), "yyyy-MM-dd"));


        if(StringUtils.isNotEmpty(value.getAddress())){
            Area areaById = areaService.getAreaById(value.getAddress());
            if (areaById != null) {
                user.setArea(areaById);
            }
        }

        user.setCurrentAddress(value.getDetailedAddress());

        BorrowerAuthentication borrowerAuthentication = borrowerService.initBorrowerAuthentication(user.getId());

        if (StringUtils.isNotEmpty(value.getIdPhotoUrl())) {

            AuthenticationMaterials authenticationMaterials = authenticationMaterialsService.initAutcMtr(PictureConstants.AutcMtrType.ID_CARD_PHOTO);

            borrowerAuthentication.setIdCardScan(authenticationMaterials);

            authenticationMaterialsService.handleAutcMtrUpload(authenticationMaterials, value.getIdPhotoUrl());

            borrowerService.saveOrUpdateBorrowerAuthentication(borrowerAuthentication);
        }

        if (StringUtils.isNotEmpty(value.getIdCopyPhotoUrl())) {

            AuthenticationMaterials authenticationMaterials = authenticationMaterialsService.initAutcMtr(PictureConstants.AutcMtrType.ID_CARD_SCAN);

            borrowerAuthentication.setIdCardScan(authenticationMaterials);

            authenticationMaterialsService.handleAutcMtrUpload(authenticationMaterials, value.getIdCopyPhotoUrl());

            borrowerService.saveOrUpdateBorrowerAuthentication(borrowerAuthentication);

        }


        ht.saveOrUpdate(user);
        borrowerService.saveOrUpdateInvestorPersonalInfo(borrowerService.initInvestorPersonalInfo(user.getId()));

        return true;
    }


    public String registerSinaPay(User user) throws TrusteeshipReturnException {

        borrowerService.verifyRealNameCertificatione(user.getId(), false, "新注册用户，默认审核失败", user.getId());

        if (trusteeshipAccountService.getTrusteeshipAccount(user.getId(), TrusteeshipConstants.Trusteeship.SINAPAY) == null) {
            //首先新浪开户
            sinaUserController.registerSinaPay(user);
        }
        InvestorPersonalInfo investorPersonalInfo = ht.get(InvestorPersonalInfo.class, user.getId());

        if (investorPersonalInfo != null)
            return sinaUserController.realSinaPay(investorPersonalInfo);
        else
            throw new TrusteeshipReturnException("InvestorPersonalInfo Is NULL ");
    }


    public String showMemberInfosSina(String userId) throws TrusteeshipReturnException {
        return sinaUserController.showMemberInfosSinaSinaPay(userId, "1", "DEFAULT", "", "", "", false);
    }
}
