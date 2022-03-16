package info.bfly.pay.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.exception.AuthCodeNotMatchException;
import info.bfly.archer.common.exception.AuthInfoAlreadyInColdException;
import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.common.service.AuthService;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.picture.model.AuthenticationMaterials;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserBill;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.DateUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.ImageUploadUtil;
import info.bfly.p2p.bankcard.BankCardConstants;
import info.bfly.p2p.bankcard.model.BankCard;
import info.bfly.p2p.bankcard.service.BankCardService;
import info.bfly.p2p.borrower.controller.BorrowerAuthenticationHome;
import info.bfly.p2p.borrower.model.BorrowerAuthentication;
import info.bfly.p2p.borrower.model.BorrowerBusinessInfo;
import info.bfly.p2p.borrower.model.InvestorPersonalInfo;
import info.bfly.p2p.borrower.service.BorrowerService;
import info.bfly.p2p.loan.model.WithdrawCash;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.model.TrusteeshipAccount;
import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;
import info.bfly.p2p.trusteeship.service.TrusteeshipAccountService;
import info.bfly.p2p.user.service.WithdrawCashService;
import info.bfly.pay.bean.SinaAPI;
import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.callback.*;
import info.bfly.pay.bean.enums.*;
import info.bfly.pay.bean.user.*;
import info.bfly.pay.service.impl.UserPayOperationService;
import info.bfly.pay.util.SFTPUtil;
import info.bfly.pay.util.SinaUtils;
import info.bfly.pay.util.ZipUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by XXSun on 2017/1/12.
 */
@Controller
public class SinaUserController implements Serializable {
    private static final long   serialVersionUID = -8063320850063560517L;
    private static final String SUCCESS          = "success";

    @Value("#{refProperties['redirectMainUrl']}")
    private String mainUrl;

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    @Log
    Logger log;
    @Resource
    private HibernateTemplate ht;
    @Autowired
    private LoginUserInfo     loginUserInfo;

    @Autowired
    private SinaUtils sinaUtils;

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;


    /**
     * 返回RUL验证信息
     *
     * @param source
     * @param target
     * @param deadline
     * @param authType
     * @param code
     * @return
     */
    private String returnAuthCode(String source, String target, Date deadline, String authType, String code) {

        try {
            authService.createAuthInfo(source, target, deadline, authType, code);
        } catch (AuthInfoAlreadyInColdException e) {
            e.printStackTrace();
        }
        return target + "/" + authType;
    }


    // -----------注册用户接口Begin
    @Autowired
    private UserPayOperationService<SinaInEntity, UserSinaEntity> service;
    @Autowired
    private TrusteeshipAccountService                             trusteeshipAccountService;

    //涉及数据保存的一定要开启事务！！！
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String registerSinaPay(User user) throws TrusteeshipReturnException {
        UserSinaEntity userSinaEntity = service.getRequestEntity(UserSinaEntity.class);
        userSinaEntity.setService(SinaAPI.CREATE_ACTIVATE_MEMBER.getService_name());
        userSinaEntity.setIdentity_id(user.getId());
        userSinaEntity.setMember_type(user.getUserType());
        TrusteeshipOperation operation = service.createOperation(userSinaEntity);
        SinaInEntity sinaIn = service.sendHttpClientOperation(userSinaEntity, operation, SinaInEntity.class);
        log.info(user.getId() + "do" + "[registerSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            trusteeshipAccountService.bindTrusteeshipAccount(user.getId(), TrusteeshipConstants.Trusteeship.SINAPAY, user.getId());
        } else if (sinaIn.getResponse_code().equals(RESPONSE_CODE.DUPLICATE_IDENTITY_ID)) {
            if (trusteeshipAccountService.getTrusteeshipAccount(user.getId(), TrusteeshipConstants.Trusteeship.SINAPAY) == null)
                trusteeshipAccountService.bindTrusteeshipAccount(user.getId(), TrusteeshipConstants.Trusteeship.SINAPAY, user.getId());
        } else {
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
        }
        //TODO 其余异常处理 已经注册？等等等。。
        return SUCCESS;
    }

    // -----------注册用户接口End
    // -----------实名用户接口Begin
    @Autowired
    private UserPayOperationService<SinaInEntity, RealUserSinaEntity> service2;


    @Autowired
    BorrowerService borrowerService;

    //涉及数据保存的一定要开启事务！！！
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String realSinaPay(InvestorPersonalInfo investorPersonalInfo) throws TrusteeshipReturnException {
        RealUserSinaEntity realUserSinaEntity = service2.getRequestEntity(RealUserSinaEntity.class);
        TrusteeshipAccount trusteeshipAccount = trusteeshipAccountService.getTrusteeshipAccount(investorPersonalInfo.getUserId(), TrusteeshipConstants.Trusteeship.SINAPAY);
        realUserSinaEntity.setService(SinaAPI.SET_REAL_NAME.getService_name());
        realUserSinaEntity.setReal_name(trusteeshipAccount.getUser().getRealname());
        realUserSinaEntity.setIdentity_id(trusteeshipAccount.getUser().getId());
        realUserSinaEntity.setCert_no(trusteeshipAccount.getUser().getIdCard());
        TrusteeshipOperation operation = service2.createOperation(realUserSinaEntity);

        SinaInEntity sinaIn = service2.sendHttpClientOperation(realUserSinaEntity, operation, SinaInEntity.class);
        log.info(investorPersonalInfo.getUserId() + "do" + "[realSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
        if ((sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) || sinaIn.getResponse_code().equals(RESPONSE_CODE.DUPLICATE_VERIFY)) {
            //TODO 认证成功后保存用户信息
            borrowerService.verifyRealNameCertificatione(investorPersonalInfo.getUserId(), true, "新浪支付，实名认证通过", TrusteeshipConstants.Trusteeship.SINAPAY);
            return SUCCESS;
        }
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.ILLEGAL_ARGUMENT)) {
            //TODO 认证信息出错处理
            borrowerService.verifyRealNameCertificatione(investorPersonalInfo.getUserId(), false, "新浪支付，实名认证失败" + sinaIn.getResponse_message(), TrusteeshipConstants.Trusteeship.SINAPAY);
        }
        throw new TrusteeshipReturnException(sinaIn.getResponse_message());
    }

    // -----------实名用户接口End

    //------------设置支付密码重定向Begin
    @Autowired
    private UserPayOperationService<RedirectSinaInEntity, SetPayPasswordSinaEntity> service4;

    public String setPayPasswordSinaPay(String userId) throws TrusteeshipReturnException {
        SetPayPasswordSinaEntity setPayPasswordSinaEntity = service4.getRequestEntity(SetPayPasswordSinaEntity.class);
        setPayPasswordSinaEntity.setService(SinaAPI.SET_PAY_PASSWORD.getService_name());
        setPayPasswordSinaEntity.setIdentity_id(userId);
//        setPayPasswordSinaEntity.setReturn_url(url);
        TrusteeshipOperation operation = service4.createOperation(setPayPasswordSinaEntity);
        RedirectSinaInEntity sinaIn = service4.sendHttpClientOperation(setPayPasswordSinaEntity, operation, RedirectSinaInEntity.class);
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {

            //returnAuthCode
            return returnAuthCode(setPayPasswordSinaEntity.getIdentity_id(), operation.getId(), getDeadTime("10m"), setPayPasswordSinaEntity.getService(), sinaIn.getRedirect_url());

//            return sinaIn.getRedirect_url();
        } else {

            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
        }
    }

    //------------设置支付密码重定向End
    //------------修改支付密码重定向Begin
    @Autowired
    private UserPayOperationService<RedirectSinaInEntity, SetPayPasswordSinaEntity> service5;

    public String modifyPayPasswordSinaPay(String userId) throws TrusteeshipReturnException {
        SetPayPasswordSinaEntity modifyPayPasswordSinaEntity = service5.getRequestEntity(SetPayPasswordSinaEntity.class);
        modifyPayPasswordSinaEntity.setService(SinaAPI.MODIFY_PAY_PASSWORD.getService_name());
        modifyPayPasswordSinaEntity.setIdentity_id(userId);
//        modifyPayPasswordSinaEntity.setReturn_url(url);
        TrusteeshipOperation operation = service5.createOperation(modifyPayPasswordSinaEntity);
        RedirectSinaInEntity sinaIn = service5.sendHttpClientOperation(modifyPayPasswordSinaEntity, operation, RedirectSinaInEntity.class);
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))
            return returnAuthCode(modifyPayPasswordSinaEntity.getIdentity_id(), operation.getId(), getDeadTime("10m"), modifyPayPasswordSinaEntity.getService(), sinaIn.getRedirect_url());
        else
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
    }

    //------------修改支付密码重定向End
    //------------找回支付密码重定向Begin
    @Autowired
    private UserPayOperationService<RedirectSinaInEntity, SetPayPasswordSinaEntity> service6;


    public String findPayPasswordSinaPay(String userId) throws TrusteeshipReturnException {
        SetPayPasswordSinaEntity findPayPasswordSinaEntity = service6.getRequestEntity(SetPayPasswordSinaEntity.class);
        findPayPasswordSinaEntity.setService(SinaAPI.FIND_PAY_PASSWORD.getService_name());
        findPayPasswordSinaEntity.setIdentity_id(userId);
//        findPayPasswordSinaEntity.setReturn_url(url);
        TrusteeshipOperation operation = service6.createOperation(findPayPasswordSinaEntity);
        RedirectSinaInEntity sinaIn = service6.sendHttpClientOperation(findPayPasswordSinaEntity, operation, RedirectSinaInEntity.class);
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))
            return returnAuthCode(findPayPasswordSinaEntity.getIdentity_id(), operation.getId(), getDeadTime("10m"), findPayPasswordSinaEntity.getService(), sinaIn.getRedirect_url());
        else
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
    }

    //------------找回支付密码重定向End
    //------------查询是否设置支付密码Begin
    @Autowired
    private UserPayOperationService<QueryIsSetPayPasswordEntity, SetPayPasswordSinaEntity> service7;

    public boolean queryIsSetPayPasswordSinaPay(String userId) {
        SetPayPasswordSinaEntity queryIsSetPayPasswordEntity = service7.getRequestEntity(SetPayPasswordSinaEntity.class);
        queryIsSetPayPasswordEntity.setService(SinaAPI.QUERY_IS_SET_PAY_PASSWORD.getService_name());
        queryIsSetPayPasswordEntity.setIdentity_id(userId);
        TrusteeshipOperation operation = service7.createOperation(queryIsSetPayPasswordEntity);
        QueryIsSetPayPasswordEntity sinaIn = service7.sendHttpClientOperation(queryIsSetPayPasswordEntity, operation, QueryIsSetPayPasswordEntity.class);
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            if ("Y".equals(sinaIn.getIs_set_paypass())) {
                userService.modifyCashPassword(userId, ".a12asxnomatter");
                service7.success(operation.getId());
                return true;
            }
        }
        return false;
    }

    //------------查询是否设置支付密码End
    //------------绑定银行卡Begin
    @Autowired
    private UserPayOperationService<BindBankCardResponseEntity, BindBankCardEntity> service8;


    @Autowired
    private ConfigService configService;

    private String bankCardId;
    private String smsNo;

    public void setBankCardId(String bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getBankCardId() {
        return bankCardId;
    }

    public String getSmsNo() {
        return smsNo;
    }

    public void setSmsNo(String smsNo) {
        this.smsNo = smsNo;
    }

    //TODO 完成bankcard service!!!
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String bindBankCardSinaPay(BankCard card) throws TrusteeshipReturnException {
        BindBankCardEntity bindBankCardEntity = service8.getRequestEntity(BindBankCardEntity.class);
        User currentUser = userService.getUserById(loginUserInfo.getLoginUserId());
        bindBankCardEntity.setService(SinaAPI.BINDING_BANK_CARD.getService_name());
        bindBankCardEntity.setRequest_no(IdGenerator.randomUUID());
        bindBankCardEntity.setIdentity_id(card.getUser() == null ? currentUser.getId() : card.getUser().getId());
        bindBankCardEntity.setVerify_mode(VERIFY_MODE.SIGN);

        bindBankCardEntity.setBank_code(card.getBankNo());
        bindBankCardEntity.setBank_account_no(card.getCardNo());
        card.setBankCardType(CARD_TYPE.DEBIT.getType_name());
        card.setBankServiceType(CARD_ATTRIBUTE.C.getAttribute_name());
        bindBankCardEntity.setCard_type(CARD_TYPE.DEBIT);
        bindBankCardEntity.setCard_attribute(CARD_ATTRIBUTE.C);

        bindBankCardEntity.setPhone_no(card.getBindPhone());

        bindBankCardEntity.setProvince(card.getBankProvince());
        bindBankCardEntity.setCity(card.getBankCity());

        //TODO 检查卡号是否被绑定,下面的处理方式是错误的。需要查询卡号是否绑定，是否是当前用户绑定，已经绑定状态？待绑定状态？

        BankCard example = new BankCard();
        example.setCardNo(card.getCardNo());
        List<BankCard> byExample = ht.findByExample(example);
        if (!byExample.isEmpty()) {
            BankCard bankCard = byExample.get(0);
            bankCard.setBankNo(card.getBankNo());
            bankCard.setBank(card.getBank());
            bankCard.setCardNo(card.getCardNo());
            bankCard.setBindPhone(card.getBindPhone());
            bankCard.setBankProvince(card.getBankProvince());
            bankCard.setBankCity(card.getBankCity());
            bankCard.setBankCardType(CARD_TYPE.DEBIT.getType_name());
            bankCard.setBankServiceType(CARD_ATTRIBUTE.C.getAttribute_name());
            card = bankCard;
        }

        TrusteeshipOperation operation = service8.createOperation(bindBankCardEntity);
        BindBankCardResponseEntity sinaIn = service8.sendHttpClientOperation(bindBankCardEntity, operation, BindBankCardResponseEntity.class);
        if (sinaIn == null)
            throw new TrusteeshipReturnException("请求超时，返回为空");
        log.info("{} do {} return {} with {}", loginUserInfo.getLoginUserId(), SinaAPI.BINDING_BANK_CARD, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            //需要推进
//            if (StringUtils.isEmpty(card.getId()))
            card.setId(IdGenerator.randomUUID());
            if (StringUtils.isEmpty(sinaIn.getCard_id()) && StringUtils.isNoneEmpty(sinaIn.getTicket())) {
                card.setStatus(BankCardConstants.BankCardStatus.NEWADD);
                card.setUser(currentUser);
                String deadtime = StringUtils.defaultString(configService.getConfigValue("card_ticket_deadtime"), "14m");
                try {
                    authService.createAuthInfo(currentUser.getId(), card.getId(), getDeadTime(deadtime), CommonConstants.AuthInfoType.BINDING_BANK_CARD, sinaIn.getTicket());
                } catch (AuthInfoAlreadyInColdException e) {
                    log.error(e.getMessage());
                }
                ht.save(card);
                bankCardId = card.getId();
                return bankCardId;
            }
            //只能提现使用
            if ("Y".equals(sinaIn.getIs_verified()) && StringUtils.isNotEmpty(sinaIn.getCard_id())) {
                card.setStatus(BankCardConstants.BankCardStatus.ONLYWITHDRAW);
                card.setUser(currentUser);
                ht.save(card);
                return null;
            }
        } else {
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
        }
        return "";
    }

    private Date getDeadTime(String minute) {
        return DateUtil.add(new Date(), minute);

    }

    //------------绑定银行卡End
    //------------绑定银行卡推进Begin
    @Autowired
    private UserPayOperationService<BindingBankCardAdvanceResponseEntity, BindingBankCardAdvanceEntity> service9;


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String bindingBankCardAdvanceSinaPay(String cardid, String code) throws TrusteeshipReturnException {
        BindingBankCardAdvanceEntity bindingBankCardAdvanceEntity = service9.getRequestEntity(BindingBankCardAdvanceEntity.class);
        User currentUser = userService.getUserById(loginUserInfo.getLoginUserId());
        try {
            authService.verifyAuthInfo(currentUser.getId(), cardid, null, CommonConstants.AuthInfoType.BINDING_BANK_CARD);
        } catch (AuthCodeNotMatchException e) {
            //do nothing
        } catch (NoMatchingObjectsException e) {
            return null;
        } catch (Exception e) {
            return "ticket can't used";
        }
        String ticket = authService.getAuthCode(currentUser.getId(), cardid, CommonConstants.AuthInfoType.BINDING_BANK_CARD);
        bindingBankCardAdvanceEntity.setService(SinaAPI.BINDING_BANK_CARD_ADVANCE.getService_name());

        bindingBankCardAdvanceEntity.setTicket(ticket);
        bindingBankCardAdvanceEntity.setValid_code(code);
        TrusteeshipOperation operation = service9.createOperation(bindingBankCardAdvanceEntity);
        BindingBankCardAdvanceResponseEntity sinaIn = service9.sendHttpClientOperation(bindingBankCardAdvanceEntity, operation, BindingBankCardAdvanceResponseEntity.class);
        log.info("{} do {} return {} with {}", loginUserInfo.getLoginUserId(), SinaAPI.BINDING_BANK_CARD_ADVANCE, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            if ("Y".equals(sinaIn.getIs_verified()) && StringUtils.isNotEmpty(sinaIn.getCard_id())) {
                BankCard bankCard = ht.get(BankCard.class, cardid);
                bankCard.setThirdNo(sinaIn.getCard_id());
                bankCard.setStatus(BankCardConstants.BankCardStatus.BINDING);
                bankCard.setVerifyMode("Y");
                ht.saveOrUpdate(bankCard);
                authService.activate(currentUser.getId(), cardid, CommonConstants.AuthInfoType.BINDING_BANK_CARD);
                return "success";
            }
        } else {
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
        }
        return "fail";
    }

    //------------绑定银行卡推进End
    //------------解除绑定银行卡Begin
    @Autowired
    private UserPayOperationService<UnbindingBankCardResponseEntity, UnbindingBankCardEntity> service10;


    public String unbindingBankCardSinaPay(String cardId) throws TrusteeshipReturnException {
        UnbindingBankCardEntity unbindingBankCardEntity = service10.getRequestEntity(UnbindingBankCardEntity.class);
        BankCard bankCard = ht.get(BankCard.class, cardId);
        if (bankCard == null)
            throw new TrusteeshipReturnException("bankCard Is Null");


        User currentUser = userService.getUserById(loginUserInfo.getLoginUserId());
        unbindingBankCardEntity.setService(SinaAPI.UNBINDING_BANK_CARD.getService_name());
        unbindingBankCardEntity.setIdentity_id(loginUserInfo.getLoginUserId());
        unbindingBankCardEntity.setCard_id(bankCard.getThirdNo());
        unbindingBankCardEntity.setAdvance_flag("Y");
        unbindingBankCardEntity.setClient_ip(loginUserInfo.getRemoteAddr());

        TrusteeshipOperation operation = service10.createOperation(unbindingBankCardEntity);
        UnbindingBankCardResponseEntity sinaIn = service10.sendHttpClientOperation(unbindingBankCardEntity, operation, UnbindingBankCardResponseEntity.class);

        if (sinaIn == null)
            throw new TrusteeshipReturnException("请求超时，返回为空");

        log.info("{} do {} return {} with {}", loginUserInfo.getLoginUserId(), SinaAPI.UNBINDING_BANK_CARD, sinaIn.getResponse_code(), sinaIn.getResponse_message());

        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            if (StringUtils.isNotEmpty(sinaIn.getTicket())) {
                String deadtime = StringUtils.defaultString(configService.getConfigValue("unbind_card_ticket_deadtime"), "14m");
                try {
                    authService.createAuthInfo(currentUser.getId(), bankCard.getId(), getDeadTime(deadtime), CommonConstants.AuthInfoType.UNBINDING_BANK_CARD, sinaIn.getTicket());
                } catch (AuthInfoAlreadyInColdException e) {
                    throw new TrusteeshipReturnException(e.getMessage());
                }
                return "success";
            }
        } else {
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
        }

        return "";
    }

    //------------解除绑定银行卡End
    //------------解除绑定银行卡推进Begin
    @Autowired
    private UserPayOperationService<SinaInEntity, UnbindingBankCardAdvanceEntity> service11;


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String unbindingBankCardAdvanceSinaPay(String cardId, String code) throws TrusteeshipReturnException {
        UnbindingBankCardAdvanceEntity unbindingBankCardAdvanceEntity = service11.getRequestEntity(UnbindingBankCardAdvanceEntity.class);
        BankCard bankCard = ht.get(BankCard.class, cardId);
        if (bankCard == null)
            throw new TrusteeshipReturnException("bankCard Is Null");

        User currentUser = userService.getUserById(loginUserInfo.getLoginUserId());
        unbindingBankCardAdvanceEntity.setService(SinaAPI.UNBINDING_BANK_CARD_ADVANCE.getService_name());


        unbindingBankCardAdvanceEntity.setTicket(authService.getAuthCode(currentUser.getId(), bankCard.getId(), CommonConstants.AuthInfoType.UNBINDING_BANK_CARD));
        unbindingBankCardAdvanceEntity.setValid_code(code);
        unbindingBankCardAdvanceEntity.setIdentity_id(loginUserInfo.getLoginUserId());
        unbindingBankCardAdvanceEntity.setClient_ip(loginUserInfo.getRemoteAddr());


        TrusteeshipOperation operation = service11.createOperation(unbindingBankCardAdvanceEntity);
        SinaInEntity sinaIn = service11.sendHttpClientOperation(unbindingBankCardAdvanceEntity, operation, SinaInEntity.class);
        if (sinaIn == null)
            throw new TrusteeshipReturnException("请求超时，返回为空");
        log.info("{} do {} return {} with {}", loginUserInfo.getLoginUserId(), SinaAPI.UNBINDING_BANK_CARD_ADVANCE, sinaIn.getResponse_code(), sinaIn.getResponse_message());

        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {

            bankCard.setThirdNo("");
            bankCard.setStatus(BankCardConstants.BankCardStatus.DELETED);
            ht.saveOrUpdate(bankCard);
            authService.activate(currentUser.getId(), bankCard.getId(), CommonConstants.AuthInfoType.UNBINDING_BANK_CARD);
            return "success";
        } else {
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
        }
    }

    //------------解除绑定银行卡推进End
    //------------查询银行卡Begin
    @Autowired
    private UserPayOperationService<QueryBankCardListEntity, QueryBankCardEntity> service12;


    @Autowired
    private BankCardService bankCardService;


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String querybindingBankCardSinaPay() {
        return querybindingBankCardSinaPay(null);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String querybindingBankCardSinaPay(String cardId) {
        QueryBankCardEntity querybindingBankCardEntity = service12.getRequestEntity(QueryBankCardEntity.class);
        User currentUser = userService.getUserById(loginUserInfo.getLoginUserId());
        querybindingBankCardEntity.setIdentity_id(currentUser.getId());
        querybindingBankCardEntity.setService(SinaAPI.QUERY_BANK_CARD.getService_name());
        querybindingBankCardEntity.setCard_id(cardId);

        TrusteeshipOperation operation = service12.createOperation(querybindingBankCardEntity);
        QueryBankCardListEntity sinaIn = service12.sendHttpClientOperation(querybindingBankCardEntity, operation, QueryBankCardListEntity.class);
        log.info("{} do {} return {} with {}", loginUserInfo.getLoginUserId(), SinaAPI.QUERY_BANK_CARD, sinaIn.getResponse_code(), sinaIn.getResponse_message());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            List<BankCard> sinaBankCards = sinaUtils.castSinaStringtoList(sinaIn.getCard_list(), "|", "^", BankCard.class, "thirdNo", "bankNo", "cardNo", "accountName", "bankCardType", "bankServiceType", "verifyMode", "a_time", "safeCard");
            //同步绑定银行卡数据 下面的同步方式有问题
            List<BankCard> bankCards = bankCardService.getBankCardsByUserId(currentUser.getId());
            final List<BankCard> finalBankCards = new ArrayList<BankCard>();
            for (BankCard sinaBankCard : sinaBankCards) {
                boolean finded = false;
                for (BankCard bankCard : bankCards) {
                    if (sinaBankCard.getThirdNo().equals(bankCard.getThirdNo())) {
                        bankCard.setStatus(sinaBankCard.getStatus());
                        bankCard.setSafeCard(sinaBankCard.getSafeCard());
                        finalBankCards.add(bankCard);
                        finded = true;
                        break;
                    }

                }
                if (!finded) {
                    sinaBankCard.setId(IdGenerator.randomUUID());
                    sinaBankCard.setUser(currentUser);
                    sinaBankCard.setStatus(BankCardConstants.BankCardStatus.BINDING);
                    finalBankCards.add(sinaBankCard);
                }

            }
            for (BankCard bankCard : finalBankCards) {
                ht.saveOrUpdate(bankCard);
            }
        }
        return "";
    }

    //------------查询银行卡End
    //------------查询余额/基金份额Begin
    @Autowired
    private UserPayOperationService<QueryBalanceResponseEntity, QueryBalanceEntity> service13;


    //涉及数据保存的一定要开启事务！！！
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String queryBalanceSinaPay(ACCOUNT_TYPE accountType) {
        QueryBalanceEntity queryBalanceEntity = service13.getRequestEntity(QueryBalanceEntity.class);
        User currentUser = userService.getUserById(loginUserInfo.getLoginUserId());
        queryBalanceEntity.setIdentity_id(currentUser.getId());
        queryBalanceEntity.setAccount_type(accountType);
        queryBalanceEntity.setService(SinaAPI.QUERY_BALANCE.getService_name());
        TrusteeshipOperation operation = service13.createOperation(queryBalanceEntity);
        QueryBalanceResponseEntity sinaIn = service13.sendHttpClientOperation(queryBalanceEntity, operation, QueryBalanceResponseEntity.class);
        log.info(currentUser.getId() + "do" + "[queryBalanceSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code()
        );
        if ((sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))) {
            UserBill userBill = ht.get(UserBill.class, currentUser.getId());
            userBill.setBalance(sinaIn.balance);
            userBill.setUser(currentUser);
            ht.saveOrUpdate(userBill);
        }
//        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.ILLEGAL_ARGUMENT)) {
//            log.info(user.getId() + "do" + "[queryBalanceSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code()
//            );
//        }
        return "";
    }

    //------------查询余额/基金份额End
    //------------查询收支明细Begin
    @Autowired
    private UserPayOperationService<QueryAccountDetailsResponseEntity, QueryAccountDetailsEntity> service14;


    public String queryAccountDetailsSinaPay(ACCOUNT_TYPE accountType, Number pageNo, Number pageSize) throws ParseException {
        QueryAccountDetailsEntity queryAccountDetailsEntity = service14.getRequestEntity(QueryAccountDetailsEntity.class);
        User currentUser = userService.getUserById(loginUserInfo.getLoginUserId());
        Date now = new Date();
        Date before = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        before = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String startTime = sdf.format(before);
        String endTime = sdf.format(now);
        queryAccountDetailsEntity.setIdentity_id(currentUser.getId());
        queryAccountDetailsEntity.setAccount_type(accountType);
        queryAccountDetailsEntity.setPage_no(pageNo);
        queryAccountDetailsEntity.setPage_size(pageSize);
        queryAccountDetailsEntity.setService(SinaAPI.QUERY_ACCOUNT_DETAILS.getService_name());
        queryAccountDetailsEntity.setStart_time(startTime);
        queryAccountDetailsEntity.setEnd_time(endTime);
        TrusteeshipOperation operation = service14.createOperation(queryAccountDetailsEntity);
        QueryAccountDetailsResponseEntity sinaIn = service14.sendHttpClientOperation(queryAccountDetailsEntity, operation, QueryAccountDetailsResponseEntity.class);
        log.info(currentUser.getId() + "do" + "[queryAccountDetailsSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            List<UserBill> userBills = sinaUtils.castSinaStringtoList(sinaIn.detail_list, "|", "^", UserBill.class, "String1", "String2", "String3", "String4", "String5", "String6");
            UserBill userBill = ht.get(UserBill.class, currentUser.getId());
            userBill.setDetail(sinaIn.getDetail_list());
            ht.save(userBill);
        }
        return "";
    }

    //------------查询收支明细End


    //------------冻结余额Begin
    @Autowired
    private UserPayOperationService<SinaInEntity, BalanceFreezeEntity> service15;


    @Autowired
    private WithdrawCashService withdrawCashService;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String balanceFreezeSinaPay(WithdrawCash withdrawCash, ACCOUNT_TYPE accountType, String summary) {
        BalanceFreezeEntity balanceFreezeEntity = service15.getRequestEntity(BalanceFreezeEntity.class);
        withdrawCash = withdrawCashService.getWithdrawById(withdrawCash.getId());
        if (UserConstants.WithdrawStatus.WAIT_FREEZE.equals(withdrawCash.getStatus())) {
            balanceFreezeEntity.setOut_freeze_no(IdGenerator.randomUUID());
            balanceFreezeEntity.setService(SinaAPI.BALANCE_FREEZE.getService_name());
            balanceFreezeEntity.setAmount(withdrawCash.getMoney() + withdrawCash.getFee());
            balanceFreezeEntity.setSummary(summary);
            balanceFreezeEntity.setAccount_type(accountType);
            balanceFreezeEntity.setIdentity_id(withdrawCash.getUser().getId());
            balanceFreezeEntity.setExtend_param("notify_type^sync");
            TrusteeshipOperation operation = service15.createOperation(balanceFreezeEntity, TrusteeshipConstants.OperationType.BALANCE_FREEZE, withdrawCash.getId());
            balanceFreezeEntity.setOut_freeze_no(operation.getId());
            service15.updateOperation(balanceFreezeEntity, operation);
            SinaInEntity sinaIn = service15.sendHttpClientOperation(balanceFreezeEntity, operation, SinaInEntity.class);
            log.info(withdrawCash.getUser().getId() + "do" + "[balanceFreezeSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
            if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
                withdrawCash.setFreezeOperationOrderNo(operation.getId());
                ht.update(withdrawCash);
                service15.success(operation.getId());
                log.info(withdrawCash.getId() + "冻结成功");
                return "SUCCESS";
            } else {
                service15.refuse(operation.getId());
                log.info(withdrawCash.getId() + "冻结失败");
                return sinaIn.getResponse_message();
            }
        } else {
            return "只有WAIT_FREEZE状态的提现才能冻结金额";
        }
    }


    //其他金额冻结


    //其他金额解冻


    //------------冻结余额End
    //------------解冻余额Begin
    @Autowired
    private UserPayOperationService<SinaInEntity, BalanceUnfreezeEntity> service16;


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String BalanceUnfreezeSinaPay(WithdrawCash withdrawCash, String summary) {
        BalanceUnfreezeEntity balanceUnfreezeEntity = service16.getRequestEntity(BalanceUnfreezeEntity.class);
        withdrawCash = withdrawCashService.getWithdrawById(withdrawCash.getId());
        balanceUnfreezeEntity.setService(SinaAPI.BALANCE_UNFREEZE.getService_name());
        balanceUnfreezeEntity.setOut_unfreeze_no(IdGenerator.randomUUID());
        balanceUnfreezeEntity.setIdentity_id(withdrawCash.getUser().getId());
        balanceUnfreezeEntity.setOut_freeze_no(IdGenerator.randomUUID());
//        balanceUnfreezeEntity.setAmount(withdrawCash.getMoney() + withdrawCash.getFee()); //不传参时解冻单号全部金额
        balanceUnfreezeEntity.setSummary(summary);
        balanceUnfreezeEntity.setOut_freeze_no(withdrawCash.getFreezeOperationOrderNo());

        TrusteeshipOperation operation = service16.createOperation(balanceUnfreezeEntity, TrusteeshipConstants.OperationType.BALANCE_UNFREEZE, withdrawCash.getId());

        balanceUnfreezeEntity.setOut_unfreeze_no(operation.getId());//解冻请求单号
        service16.updateOperation(balanceUnfreezeEntity, operation);
        SinaInEntity sinaIn = service16.sendHttpClientOperation(balanceUnfreezeEntity, operation, SinaInEntity.class);
        log.info(withdrawCash.getUser().getId() + "do" + "[BalanceUnfreezeSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            service16.success(operation.getId());
            log.info("{} 解冻成功", withdrawCash.getId());
            return "SUCCESS";
        } else {
            service16.refuse(operation.getId());
            log.info("{} 解冻失败 {}", withdrawCash.getId(), sinaIn.getResponse_message());
            return sinaIn.getResponse_message();
        }

    }

    //------------解冻余额End
    //------------查询冻结解冻结果Begin
    @Autowired
    private UserPayOperationService<QueryCtrlResultResponseEntity, QueryCtrlResultEntity> service17;


    public String queryCtrlResultSinaPay(WithdrawCash withdrawCash) {
        QueryCtrlResultEntity queryCtrlResultEntity = service17.getRequestEntity(QueryCtrlResultEntity.class);
        queryCtrlResultEntity.setService(SinaAPI.QUERY_CTRL_RESULT.getService_name());
        queryCtrlResultEntity.setIdentity_id(withdrawCash.getUser().getId());
        queryCtrlResultEntity.setOut_ctrl_no(IdGenerator.randomUUID());
        TrusteeshipOperation operation = service17.createOperation(queryCtrlResultEntity, TrusteeshipConstants.OperationType.BALANCE_FREEZE, withdrawCash.getId());
        queryCtrlResultEntity.setOut_ctrl_no(operation.getId());
        service17.updateOperation(queryCtrlResultEntity, operation);
        QueryCtrlResultResponseEntity sinaIn = service17.sendHttpClientOperation(queryCtrlResultEntity, operation, QueryCtrlResultResponseEntity.class);
        log.info(withdrawCash.getUser().getId() + "do" + "[queryCtrlResultSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            log.info("冻结成功");
        } else {
            log.info("冻结失败");
        }
        return sinaIn.getResponse_message();
    }

    //------------查询冻结解冻结果End


    //------------查询企业会员信息Begin
    @Autowired
    private UserPayOperationService<QueryMemberInfosEntity, UserSinaEntity> service18;

    public String qeryMemberInfosSinaPay() {
        queryMemberInfosEntity.setService(SinaAPI.QUERY_MEMBER_INFOS.getService_name());
        TrusteeshipOperation operation = service18.createOperation(queryMemberInfosEntity);
        QueryMemberInfosEntity sinaIn = service18.sendHttpClientOperation(queryMemberInfosEntity, operation, QueryMemberInfosEntity.class);
        return "";
    }


    private UserSinaEntity                                                  queryMemberInfosEntity;
    //------------查询企业会员信息End
    //------------查询企业会员审核结果Begin
    @Autowired
    private UserPayOperationService<QueryAuditResultEntity, UserSinaEntity> service19;


    public String queryAuditResultSinaPay() {
        UserSinaEntity queryAuditResultEntity = service19.getRequestEntity(UserSinaEntity.class);
        queryAuditResultEntity.setService(SinaAPI.SET_REAL_NAME.getService_name());
        TrusteeshipOperation operation = service19.createOperation(queryAuditResultEntity);
        QueryAuditResultEntity sinaIn = service19.sendHttpClientOperation(queryAuditResultEntity, operation, QueryAuditResultEntity.class);
        return "";
    }

    //------------查询企业会员审核结果End
    //------------请求审核企业会员资质Begin
    @Autowired
    private UserPayOperationService<SinaInEntity, AuditMemberInfosEntity> service20;


    @Autowired
    private ZipUtil zipUtil;

    @Autowired
    private SFTPUtil sftpUtil;

    @Value("#{refProperties['sinapay_sftp_upload_directory']}")
    private String uploadPath;

    public String auditMemberInfosSinaPay(String userId) throws TrusteeshipReturnException {

        AuditMemberInfosEntity auditMemberInfosEntity = service20.getRequestEntity(AuditMemberInfosEntity.class);

        BorrowerBusinessInfo bbi = borrowerService.initBorrowerBusinessInfo(userId);
        auditMemberInfosEntity.setService(SinaAPI.AUDIT_MEMBER_INFOS.getService_name());
        auditMemberInfosEntity.setIdentity_id(bbi.getBorrowerInfo().getUser().getId());
        auditMemberInfosEntity.setMember_type(bbi.getBorrowerInfo().getUser().getUserType());
        try {
            BeanUtils.copyProperties(auditMemberInfosEntity, bbi);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TrusteeshipReturnException("数据转换错误 {}", e.getMessage());
        }
        //打包文件
        /**
         企业营业执照	yyzz
         组织机构代码证	zzjgz
         税务登记证	swdjz
         单位银行结算账户开户许可证	jsxkz
         机构信用代码证	jgxyz
         ICP备案许可	icp
         行业许可证	hyxkz
         企业法人证件正面	frzjz
         企业法人证件反面	frzjf
         */
        BorrowerAuthentication borrowerAuthentication = bbi.getBorrowerInfo().getBorrowerAuthentication();

        AuthenticationMaterials yyzz = borrowerAuthentication.getBusinessLicense();
        AuthenticationMaterials zzjgz = borrowerAuthentication.getOrganizationLicense();
        AuthenticationMaterials swdjz = borrowerAuthentication.getTaxRegistrationLicense();
        AuthenticationMaterials jsxkz = borrowerAuthentication.getBankAccountOpenLicense();
        AuthenticationMaterials jgxyz = borrowerAuthentication.getCreditAgenciesLicense();
        AuthenticationMaterials icp = borrowerAuthentication.getICPLicense();
        AuthenticationMaterials hyzkz = borrowerAuthentication.getVocationalPermissionLicense();
        AuthenticationMaterials frzjz = borrowerAuthentication.getLegalIdCardFront();
        AuthenticationMaterials frzjf = borrowerAuthentication.getLegalIdCardBack();
        String realPath = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("");
        File zipFolder = new File(realPath, BorrowerAuthenticationHome.TYPE + userId + ImageUploadUtil.ZIP_PATH + "/" + userId);
        if (zipFolder.exists()) {
            try {
                FileUtils.forceDelete(zipFolder);
            } catch (IOException e) {
                throw new TrusteeshipReturnException("删除压缩文件出错");
            }
        }

        if (!zipFolder.mkdirs())
            throw new TrusteeshipReturnException("创建压缩文件错误");

        copyZipFiles(realPath, zipFolder, yyzz, "yyzz");
        copyZipFiles(realPath, zipFolder, zzjgz, "zzjgz");
        copyZipFiles(realPath, zipFolder, swdjz, "swdjz");
        copyZipFiles(realPath, zipFolder, jsxkz, "jsxkz");
        copyZipFiles(realPath, zipFolder, jgxyz, "jgxyz");
        copyZipFiles(realPath, zipFolder, icp, "icp");
        copyZipFiles(realPath, zipFolder, hyzkz, "hyzkz");
        copyZipFiles(realPath, zipFolder, frzjz, "frzjz");
        copyZipFiles(realPath, zipFolder, frzjf, "frzjf");

        File zipFile = new File(zipFolder.getPath() + ".zip");
        zipUtil.zip(zipFile, zipFolder);


        auditMemberInfosEntity.setFileName(zipFile.getName());
        auditMemberInfosEntity.setDigestType("MD5");
        auditMemberInfosEntity.setExtend_param("notify_type^sync");

        if (sftpUtil.upload(uploadPath, zipFile.getPath())) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(zipFile);
                String md5DigestAsHex = DigestUtils.md5Hex(fileInputStream);
                auditMemberInfosEntity.setDigest(md5DigestAsHex);
            } catch (IOException e) {
                throw new TrusteeshipReturnException("签名文件错误");
            } finally {
                IOUtils.closeQuietly(fileInputStream);
            }
            if (zipFolder.exists()) {
                try {
                    FileUtils.forceDelete(zipFolder);
                } catch (IOException e) {
                    log.error("delete zipFolder {} fail {}", zipFolder.getPath(), e);
                }
            }
            if (zipFile.exists()) {
                try {
                    FileUtils.forceDelete(zipFile);
                } catch (IOException e) {
                    log.error("delete zipFile {} fail {}", zipFile.getPath(), e);
                }
            }
            TrusteeshipOperation operation = service20.createOperation(auditMemberInfosEntity, TrusteeshipConstants.OperationType.AUDIT_MEMBER_INFOS, userId);
            auditMemberInfosEntity.setAudit_order_no(operation.getId());
            service20.updateOperation(auditMemberInfosEntity, operation);
            SinaInEntity sinaIn = service20.sendHttpClientOperation(auditMemberInfosEntity, operation, SinaInEntity.class);
            log.info(userId + " do " + "[queryCtrlResultSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
            if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
                log.info("{} 申请企业账户审核成功", userId);
                return SUCCESS;
            } else {
                log.info("{} 申请企业账户审核失败", userId);
                throw new TrusteeshipReturnException(sinaIn.getResponse_message());
            }
        } else
            throw new TrusteeshipReturnException("上传文件出错！");
    }

    private void copyZipFiles(String realPath, File zipFolder, AuthenticationMaterials materials, String fileName) throws TrusteeshipReturnException {
        if (materials != null && !materials.getPictures().isEmpty()) {
            File yyzzFile = new File(realPath, materials.getPictures().get(0).getPicture());
            if (yyzzFile.exists()) {
                try {
                    FileUtils.copyFile(yyzzFile, new File(zipFolder, fileName + "." + FilenameUtils.getExtension(yyzzFile.getName())));
                } catch (IOException e) {
                    throw new TrusteeshipReturnException("拷贝文件 {} 错误 {}", yyzzFile.getPath(), e.getMessage());
                }
            }
        }
    }

    //------------请求审核企业会员资质End
    //------------经办人信息Begin
    @Autowired
    private UserPayOperationService<SinaInEntity, SmtFundAgentBuyEntity> service21;


    public String smtFundAgentBuySinaPay(String userId) throws TrusteeshipReturnException {
        SmtFundAgentBuyEntity smtFundAgentBuyEntity = service21.getRequestEntity(SmtFundAgentBuyEntity.class);
        BorrowerBusinessInfo bbi = borrowerService.initBorrowerBusinessInfo(userId);
        smtFundAgentBuyEntity.setIdentity_id(bbi.getBorrowerInfo().getUser().getId());
        smtFundAgentBuyEntity.setAgent_name(bbi.getAgent_name());
        smtFundAgentBuyEntity.setLicense_no(bbi.getAgent_license_no());
        smtFundAgentBuyEntity.setLicense_type_code(bbi.getLicense_type_code());
        smtFundAgentBuyEntity.setAgent_mobile(bbi.getAgent_mobile());
        smtFundAgentBuyEntity.setEmail(bbi.getAgent_email());
        smtFundAgentBuyEntity.setService(SinaAPI.SMT_FUND_AGENT_BUY.getService_name());

        TrusteeshipOperation operation = service21.createOperation(smtFundAgentBuyEntity);
        SinaInEntity sinaIn = service21.sendHttpClientOperation(smtFundAgentBuyEntity, operation, SinaInEntity.class);
        log.info(userId + "do" + "[smtFundAgentBuySinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            return SUCCESS;
        } else
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());

    }

    //------------经办人信息End
    //------------查询经办人信息Begin
    @Autowired
    private UserPayOperationService<QueryFundAgentBuyEntity, UserSinaEntity> service22;


    public boolean queryFundAgentBuySinaPay(String userId) {
        UserSinaEntity queryFundAgentBuyEntity = service22.getRequestEntity(UserSinaEntity.class);
        queryFundAgentBuyEntity.setIdentity_id(userId);
        queryFundAgentBuyEntity.setService(SinaAPI.QUERY_FUND_AGENT_BUY.getService_name());
        TrusteeshipOperation operation = service22.createOperation(queryFundAgentBuyEntity);
        QueryFundAgentBuyEntity sinaIn = service22.sendHttpClientOperation(queryFundAgentBuyEntity, operation, QueryFundAgentBuyEntity.class);
        log.info(loginUserInfo.getLoginUserId() + "do" + "[queryFundAgentBuySinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
        if ((sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))) {
            BorrowerBusinessInfo borrowerBusinessInfo = borrowerService.initBorrowerBusinessInfo(userId);
            borrowerBusinessInfo.setAgent_id(sinaIn.getAgent_id());
            borrowerService.saveOrUpdateBorrowerBusinessInfo(borrowerBusinessInfo);
            return true;
        } else
            return false;
    }

    //------------查询经办人信息End
    //------------sina页面展示用户信息Begin
    @Autowired
    private UserPayOperationService<RedirectSinaInEntity, ShowMemberInfosSinaEntity> service23;


    public String showMemberInfosSinaSinaPay(String userId, String respMethod, String defaultPage, String hidePages, String templetCustom, String singleCustom, boolean isAdmin) throws TrusteeshipReturnException {
        ShowMemberInfosSinaEntity showMemberInfosSinaEntity = service23.getRequestEntity(ShowMemberInfosSinaEntity.class);
        showMemberInfosSinaEntity.setIdentity_id(userId);
        showMemberInfosSinaEntity.setService(SinaAPI.SHOW_MEMBER_INFOS_SINA.getService_name());
        showMemberInfosSinaEntity.setResp_method(respMethod);
        showMemberInfosSinaEntity.setDefault_page(defaultPage);
        showMemberInfosSinaEntity.setHide_pages(hidePages);
        showMemberInfosSinaEntity.setTemplet_custom(templetCustom);
        showMemberInfosSinaEntity.setSingle_custom(singleCustom);
        TrusteeshipOperation operation = service23.createOperation(showMemberInfosSinaEntity);
        RedirectSinaInEntity sinaIn = service23.sendHttpClientOperation(showMemberInfosSinaEntity, operation, RedirectSinaInEntity.class);
        if (sinaIn == null)
            throw new TrusteeshipReturnException("请求超时，返回为空");

        if ((sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))) {
            //TODO 返回的重定向路径放哪？？       	sinaIn.getRedirect_url();
            log.info(userId + "do" + "[showMemberInfosSinaSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code()
            );
            return returnAuthCode(isAdmin ? loginUserInfo.getLoginUserId() : showMemberInfosSinaEntity.getIdentity_id(), operation.getId(), getDeadTime("10"), showMemberInfosSinaEntity.getService(), sinaIn.getRedirect_url());
            //returnAuthCode(setPayPasswordSinaEntity.getIdentity_id(), operation.getId(), getDeadTime("10"), setPayPasswordSinaEntity.getService(), sinaIn.getRedirect_url());
        }
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.ILLEGAL_ARGUMENT)) {
            log.info(userId + "do" + "[showMemberInfosSinaSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code()
            );
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
        }
        return "";
    }


    //------------sina页面展示用户信息End
    //------------查询中间账户Begin
    @Autowired
    private UserPayOperationService<QueryMiddleAccountResponseEntity, QueryMiddleAccountEntity> service24;


    public String queryMiddleAccountSinaPay() {
        QueryMiddleAccountEntity queryMiddleAccountEntity = service24.getRequestEntity(QueryMiddleAccountEntity.class);
        queryMiddleAccountEntity.setService(SinaAPI.QUERY_MIDDLE_ACCOUNT.getService_name());
        queryMiddleAccountEntity.setOut_trade_code(OUT_TRADE_CODE.OUT_LOAN_1);
        TrusteeshipOperation operation = service24.createOperation(queryMiddleAccountEntity);

        QueryMiddleAccountResponseEntity sinaIn = service24.sendHttpClientOperation(queryMiddleAccountEntity, operation, QueryMiddleAccountResponseEntity.class);
        //log.info(user.getId() + "do" + "[findVerifyMobileSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code()
        if ((sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))) {

        }

        return "";
    }

    //------------查询中间账户End
    //------------修改认证手机Begin
    @Autowired
    private UserPayOperationService<SinaInEntity, UserSinaEntity> service25;


    public String modifyVerifyMobileSinaPay(User user) {
        UserSinaEntity modifyVerifyMobile = service25.getRequestEntity(UserSinaEntity.class);
        modifyVerifyMobile.setService(SinaAPI.MODIFY_VERIFY_MOBILE.getService_name());
        modifyVerifyMobile.setIdentity_id(user.getId());
        TrusteeshipOperation operation = service25.createOperation(modifyVerifyMobile);
        SinaInEntity sinaIn = service25.sendHttpClientOperation(modifyVerifyMobile, operation, SinaInEntity.class);
        if ((sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))) {
            log.info(user.getId() + "do" + "[modifyVerifyMobileSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code()
            );
        }
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.ILLEGAL_ARGUMENT)) {
            log.info(user.getId() + "do" + "[modifyVerifyMobileSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code()
            );
        }
        return "";
    }

    //------------修改认证手机End
    //------------找回认证手机Begin
    @Autowired
    private UserPayOperationService<SinaInEntity, UserSinaEntity> service26;


    public String findVerifyMobileSinaPay(User user) {
        UserSinaEntity findVerifyMobile = service26.getRequestEntity(UserSinaEntity.class);
        findVerifyMobile.setService(SinaAPI.FIND_VERIFY_MOBILE.getService_name());
        findVerifyMobile.setIdentity_id(user.getId());
        TrusteeshipOperation operation = service25.createOperation(findVerifyMobile);
        SinaInEntity sinaIn = service25.sendHttpClientOperation(findVerifyMobile, operation, SinaInEntity.class);
        if ((sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS))) {
            log.info(user.getId() + "do" + "[findVerifyMobileSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
        }
        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.ILLEGAL_ARGUMENT)) {
            log.info(user.getId() + "do" + "[findVerifyMobileSinaPay]" + "return" + sinaIn.getResponse_message() + "with" + sinaIn.getResponse_code());
        }
        return "";
    }


    //------------找回认证手机End


    //-----------绑定认证信息start

    @Autowired
    private UserPayOperationService<RedirectSinaInEntity, VerifySinaBaseEntity> service27;


    public String bindingVerify(User user) {
        VerifySinaBaseEntity verifySinaBaseEntity = service27.getRequestEntity(VerifySinaBaseEntity.class);

        verifySinaBaseEntity.setService(SinaAPI.BINDING_VERIFY.getService_name());

        verifySinaBaseEntity.setIdentity_id(user.getId());

        //TODO  密文，使用RSA 加密。明文长度：30
        verifySinaBaseEntity.setVerify_type("");

        verifySinaBaseEntity.setClient_ip(loginUserInfo.getRemoteAddr());

        TrusteeshipOperation operation = service27.createOperation(verifySinaBaseEntity);
        RedirectSinaInEntity sinaIn = service27.sendHttpClientOperation(verifySinaBaseEntity, operation, RedirectSinaInEntity.class);

        return "";
    }

    //-----------绑定认证信息end

    //-----------解绑认证信息Start

    public String unbindingVerify(User user) {

        VerifySinaBaseEntity verifySinaBaseEntity = service27.getRequestEntity(VerifySinaBaseEntity.class);
        verifySinaBaseEntity.setService(SinaAPI.UNBINDING_VERIFY.getService_name());

        verifySinaBaseEntity.setIdentity_id(user.getId());

        verifySinaBaseEntity.setClient_ip(loginUserInfo.getRemoteAddr());

        TrusteeshipOperation operation = service27.createOperation(verifySinaBaseEntity);
        RedirectSinaInEntity sinaIn = service27.sendHttpClientOperation(verifySinaBaseEntity, operation, RedirectSinaInEntity.class);

        return "";
    }


    //-----------解绑认证信息end


    //查询认证信息

//    public String queryVerify(User user) {
//
//        verifySinaBaseEntity.setService(SinaAPI.QUERY_VERIFY.getService_name());
//
//        verifySinaBaseEntity.setIdentity_id(user.getId());
//
//        TrusteeshipOperation operation = service27.createOperation(verifySinaBaseEntity);
//        RedirectSinaInEntity sinaIn = service27.sendHttpClientOperation(verifySinaBaseEntity, operation, RedirectSinaInEntity.class);
//
//
//        return "";
//    }


    //我的银行卡


    @Autowired
    private UserPayOperationService<RedirectSinaInEntity, WebBankEntity> service28;


    public String WebBank(User user) {
        WebBankEntity webBankEntity = service28.getRequestEntity(WebBankEntity.class);
        webBankEntity.setService(SinaAPI.WEB_BINDING_BANK_CARD.getService_name());

        webBankEntity.setIdentity_id(user.getId());

        TrusteeshipOperation operation = service28.createOperation(webBankEntity);
        RedirectSinaInEntity sinaIn = service28.sendHttpClientOperation(webBankEntity, operation, RedirectSinaInEntity.class);

        return "";
    }


    //委托扣款
    @Autowired
    private UserPayOperationService<RedirectSinaInEntity, WithholdAuthorityEntity> service29;


    public String WithholdAuthority(User user) {
        WithholdAuthorityEntity withholdAuthorityEntity = service29.getRequestEntity(WithholdAuthorityEntity.class);

        withholdAuthorityEntity.setService(SinaAPI.HANDLE_WITHHOLD_AUTHORITY.getService_name());

        withholdAuthorityEntity.setIdentity_id(user.getId());

        TrusteeshipOperation operation = service29.createOperation(withholdAuthorityEntity);
        RedirectSinaInEntity sinaIn = service29.sendHttpClientOperation(withholdAuthorityEntity, operation, RedirectSinaInEntity.class);

        return "";
    }

    //修改委托扣款


    public String modifyAuthority(User user) {

        WithholdAuthorityEntity withholdAuthorityEntity = service29.getRequestEntity(WithholdAuthorityEntity.class);
        withholdAuthorityEntity.setService(SinaAPI.MODIFY_WITHHOLD_AUTHORITY.getService_name());

        withholdAuthorityEntity.setIdentity_id(user.getId());

        TrusteeshipOperation operation = service29.createOperation(withholdAuthorityEntity);
        RedirectSinaInEntity sinaIn = service29.sendHttpClientOperation(withholdAuthorityEntity, operation, RedirectSinaInEntity.class);

        return "";
    }

    //解除委托

    public String RelieveAuthority(User user) {

        WithholdAuthorityEntity withholdAuthorityEntity = service29.getRequestEntity(WithholdAuthorityEntity.class);
        withholdAuthorityEntity.setService(SinaAPI.RELIEVE_WITHHOLD_AUTHORITY.getService_name());

        withholdAuthorityEntity.setIdentity_id(user.getId());

        TrusteeshipOperation operation = service29.createOperation(withholdAuthorityEntity);
        RedirectSinaInEntity sinaIn = service29.sendHttpClientOperation(withholdAuthorityEntity, operation, RedirectSinaInEntity.class);

        return "";
    }


    //查询委托

    @Autowired
    private UserPayOperationService<RedirectSinaInEntity, SinaUserBaseEntity> service30;


    public String QueryAuthority(User user) {

        SinaUserBaseEntity sinaUserBaseEntity = service30.getRequestEntity(SinaUserBaseEntity.class);
        sinaUserBaseEntity.setService(SinaAPI.QUERY_WITHHOLD_AUTHORITY.getService_name());

        sinaUserBaseEntity.setIdentity_id(user.getId());

        TrusteeshipOperation operation = service30.createOperation(sinaUserBaseEntity);
        RedirectSinaInEntity sinaIn = service30.sendHttpClientOperation(sinaUserBaseEntity, operation, RedirectSinaInEntity.class);

        return "";
    }


    //实名图像认证重定向

    public String webRealNamePicAuth(User user) throws TrusteeshipReturnException {

        SinaUserBaseEntity sinaUserBaseEntity = service30.getRequestEntity(SinaUserBaseEntity.class);
        sinaUserBaseEntity.setService(SinaAPI.WEB_REAL_NAME_PIC_AUTH.getService_name());

        sinaUserBaseEntity.setIdentity_id(user.getId());

        TrusteeshipOperation operation = service30.createOperation(sinaUserBaseEntity);
        RedirectSinaInEntity sinaIn = service30.sendHttpClientOperation(sinaUserBaseEntity, operation, RedirectSinaInEntity.class);

        if (sinaIn.getResponse_code().equals(RESPONSE_CODE.APPLY_SUCCESS)) {
            return sinaIn.getRedirect_url();
        } else {
            throw new TrusteeshipReturnException(sinaIn.getResponse_message());
        }
//        return "";
    }


}
