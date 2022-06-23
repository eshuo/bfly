package info.bfly.p2p.borrower.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.picture.PictureConstants;
import info.bfly.archer.picture.model.AutcMtrPicture;
import info.bfly.archer.picture.model.AutcMtrType;
import info.bfly.archer.picture.model.AuthenticationMaterials;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.ImageUploadUtil;
import info.bfly.p2p.borrower.BorrowerConstant;
import info.bfly.p2p.borrower.model.BorrowerAuthentication;
import info.bfly.p2p.borrower.service.BorrowerService;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * 认证材料
 */
@Component
@Scope(ScopeType.VIEW)
public class BorrowerAuthenticationHome extends EntityHome<BorrowerAuthentication> implements Serializable {
    private static final long    serialVersionUID = 6552070747082409860L;
    private              Boolean ispass           = false;
    public static final  String  TYPE             = "/bAuth/";
    @Log
    private Logger log;

    private String userId;

    private String             verifyMessage;
    @Qualifier(value = "borrowService")
    @Autowired
    private BorrowerService    borrowerService;
    @Resource
    private LoginUserInfo      loginUser;
    /**
     * 企业营业执照
     */
    private AuthenticationBean businessLicense;
    /**
     * 组织机构代码证
     */
    private AuthenticationBean organizationLicense;
    /**
     * 税务登记证
     */
    private AuthenticationBean taxRegistrationLicense;
    /**
     * 单位银行结算账户开户许可证
     */
    private AuthenticationBean bankAccountOpenLicense;
    /**
     * 机构信用代码证
     */
    private AuthenticationBean creditAgenciesLicense;
    /**
     * ICP备案许可
     */
    private AuthenticationBean ICPLicense;
    /**
     * 行业许可证
     */
    private AuthenticationBean vocationalPermissionLicense;
    /**
     * 企业法人证件正面
     */
    private AuthenticationBean legalIdCardFront;
    /**
     * 企业法人证件反面
     */
    private AuthenticationBean legalIdCardBack;

    public BorrowerAuthenticationHome() {

    }


    public class AuthenticationBean {
        private String                  autcMtrTypeId;
        private AuthenticationMaterials mutcMtr;

        AuthenticationBean(String autcMtrTypeId) {
            this.autcMtrTypeId = autcMtrTypeId;
            mutcMtr = getImage();
        }


        public AuthenticationMaterials getMutcMtr() {
            return mutcMtr;
        }

        public void setMutcMtr(AuthenticationMaterials mutcMtr) {
            this.mutcMtr = mutcMtr;
        }

        private void setImage(AuthenticationMaterials mutcMtr) {
            this.mutcMtr = mutcMtr;
            switch (autcMtrTypeId) {
                //身份证扫描件
                case PictureConstants.AutcMtrType.ID_CARD_SCAN:
                    getInstance().setIdCardScan(mutcMtr);
                    break;
                //手持身份证照片
                case PictureConstants.AutcMtrType.ID_CARD_PHOTO:
                    getInstance().setIdCardPhoto(mutcMtr);
                    break;
                //银行征信报告
                case PictureConstants.AutcMtrType.ID_BANK_CREDITREPORT:
                    getInstance().setBankCreditReport(mutcMtr);
                    break;
                //户口卡
                case PictureConstants.AutcMtrType.ID_HUKOU_SCAN:
                    getInstance().setHuKouScan(mutcMtr);
                    break;
                //手持户口卡照片
                case PictureConstants.AutcMtrType.ID_HUKOU_PHOTO:
                    getInstance().setHuKouPhoto(mutcMtr);
                    break;
                //学历证书扫描件
                case PictureConstants.AutcMtrType.ID_DIPLOMA_SCAN:
                    getInstance().setDiplomaScan(mutcMtr);
                    break;
                //收入证明
                case PictureConstants.AutcMtrType.ID_PROOF_EARNINGS:
                    getInstance().setProofEarnings(mutcMtr);
                    break;
                //账户流水扫描件
                case PictureConstants.AutcMtrType.ID_ACCOUNT_FLOW:
                    getInstance().setAccountFlow(mutcMtr);
                    break;
                //工作证件扫描件
                case PictureConstants.AutcMtrType.ID_WORK_CERTIFICATE:
                    getInstance().setWorkCertificate(mutcMtr);
                    break;
                //学生证
                case PictureConstants.AutcMtrType.ID_STUDENT_ID:
                    getInstance().setStudentId(mutcMtr);
                    break;
                //职称证书
                case PictureConstants.AutcMtrType.ID_POSITIONAL_TITLES:
                    getInstance().setPositionalTitles(mutcMtr);
                    break;
                //房产证明
                case PictureConstants.AutcMtrType.ID_HOUSE_INFO:
                    getInstance().setHouseInfo(mutcMtr);
                    break;
                //车辆证明
                case PictureConstants.AutcMtrType.ID_CAR_INFO:
                    getInstance().setCarInfo(mutcMtr);
                    break;
                //结婚证
                case PictureConstants.AutcMtrType.ID_MARRIAGE_CERTIFICATE:
                    getInstance().setMarriageCertificate(mutcMtr);
                    break;
                //其他财产证明
                case PictureConstants.AutcMtrType.ID_OTHER_ESTATE:
                    getInstance().setOtherEstate(mutcMtr);
                    break;
                //其他居住地证明
                case PictureConstants.AutcMtrType.ID_OTHER_DOMICILE:
                    getInstance().setOtherDomicile(mutcMtr);
                    break;
                //其他可确认身份的证件
                case PictureConstants.AutcMtrType.ID_OTHERID_CERTIFICATE:
                    getInstance().setOtherIdCertificate(mutcMtr);
                    break;
                //其他能证明稳定收入的材料
                case PictureConstants.AutcMtrType.ID_OTHER_INCOMEINFO:
                    getInstance().setOtherIncomeInfo(mutcMtr);
                    break;
                //微博认证
                case PictureConstants.AutcMtrType.ID_MICROBLOG_INFO:
                    getInstance().setMicroblogInfo(mutcMtr);
                    break;
                //企业营业执照
                case PictureConstants.AutcMtrType.ID_BUSINESS_LICENSE:
                    getInstance().setBusinessLicense(mutcMtr);
                    break;
                //组织机构代码证
                case PictureConstants.AutcMtrType.ID_ORGANIZATION_LICENSE:
                    getInstance().setOrganizationLicense(mutcMtr);
                    break;
                //税务登记证
                case PictureConstants.AutcMtrType.ID_TAX_REGISTRATION_LICENSE:
                    getInstance().setTaxRegistrationLicense(mutcMtr);
                    break;
                //单位银行结算账户开户许可证
                case PictureConstants.AutcMtrType.ID_BANK_ACCOUNT_OPEN_LICENSE:
                    getInstance().setBankAccountOpenLicense(mutcMtr);
                    break;
                //机构信用代码证
                case PictureConstants.AutcMtrType.ID_CREDIT_AGENCIES_LICENSE:
                    getInstance().setCreditAgenciesLicense(mutcMtr);
                    break;
                //CP备案许可
                case PictureConstants.AutcMtrType.ID_ICP_LICENSE:
                    getInstance().setICPLicense(mutcMtr);
                    break;
                //行业许可证
                case PictureConstants.AutcMtrType.ID_VOCATIONAL_PERMISSION_LICENSE:
                    getInstance().setVocationalPermissionLicense(mutcMtr);
                    break;
                //企业法人证件正面
                case PictureConstants.AutcMtrType.ID_LEGALID_CARD_FRONT:
                    getInstance().setLegalIdCardFront(mutcMtr);
                    break;
                //企业法人证件反面
                case PictureConstants.AutcMtrType.ID_LEGALID_CARD_BACK:
                    getInstance().setLegalIdCardBack(mutcMtr);
                    break;
                //企业流水账户信息
                case PictureConstants.AutcMtrType.ID_BUSINESS_ACCOUNTFLOW:
                    getInstance().setBusinessAccountFlow(mutcMtr);
                    break;
                default:
                    break;
            }
        }

        private AuthenticationMaterials getImage() {
            if (mutcMtr == null)
                switch (autcMtrTypeId) {
                    //身份证扫描件
                    case PictureConstants.AutcMtrType.ID_CARD_SCAN:
                        return getInstance().getIdCardScan();
                    //手持身份证照片
                    case PictureConstants.AutcMtrType.ID_CARD_PHOTO:
                        return getInstance().getIdCardPhoto();
                    //银行征信报告
                    case PictureConstants.AutcMtrType.ID_BANK_CREDITREPORT:
                        return getInstance().getBankCreditReport();
                    //户口卡
                    case PictureConstants.AutcMtrType.ID_HUKOU_SCAN:
                        return getInstance().getHuKouScan();
                    //手持户口卡照片
                    case PictureConstants.AutcMtrType.ID_HUKOU_PHOTO:
                        return getInstance().getHuKouPhoto();
                    //学历证书扫描件
                    case PictureConstants.AutcMtrType.ID_DIPLOMA_SCAN:
                        return getInstance().getDiplomaScan();
                    //收入证明
                    case PictureConstants.AutcMtrType.ID_PROOF_EARNINGS:
                        return getInstance().getProofEarnings();
                    //账户流水扫描件
                    case PictureConstants.AutcMtrType.ID_ACCOUNT_FLOW:
                        return getInstance().getAccountFlow();
                    //工作证件扫描件
                    case PictureConstants.AutcMtrType.ID_WORK_CERTIFICATE:
                        return getInstance().getWorkCertificate();
                    //学生证
                    case PictureConstants.AutcMtrType.ID_STUDENT_ID:
                        return getInstance().getStudentId();
                    //职称证书
                    case PictureConstants.AutcMtrType.ID_POSITIONAL_TITLES:
                        return getInstance().getPositionalTitles();
                    //房产证明
                    case PictureConstants.AutcMtrType.ID_HOUSE_INFO:
                        return getInstance().getHouseInfo();
                    //车辆证明
                    case PictureConstants.AutcMtrType.ID_CAR_INFO:
                        return getInstance().getCarInfo();
                    //结婚证
                    case PictureConstants.AutcMtrType.ID_MARRIAGE_CERTIFICATE:
                        return getInstance().getMarriageCertificate();
                    //其他财产证明
                    case PictureConstants.AutcMtrType.ID_OTHER_ESTATE:
                        return getInstance().getOtherEstate();
                    //其他居住地证明
                    case PictureConstants.AutcMtrType.ID_OTHER_DOMICILE:
                        return getInstance().getOtherDomicile();
                    //其他可确认身份的证件
                    case PictureConstants.AutcMtrType.ID_OTHERID_CERTIFICATE:
                        return getInstance().getOtherIdCertificate();
                    //其他能证明稳定收入的材料
                    case PictureConstants.AutcMtrType.ID_OTHER_INCOMEINFO:
                        return getInstance().getOtherIncomeInfo();
                    //微博认证
                    case PictureConstants.AutcMtrType.ID_MICROBLOG_INFO:
                        return getInstance().getMicroblogInfo();
                    //企业营业执照
                    case PictureConstants.AutcMtrType.ID_BUSINESS_LICENSE:
                        return getInstance().getBusinessLicense();
                    //组织机构代码证
                    case PictureConstants.AutcMtrType.ID_ORGANIZATION_LICENSE:
                        return getInstance().getOrganizationLicense();
                    //税务登记证
                    case PictureConstants.AutcMtrType.ID_TAX_REGISTRATION_LICENSE:
                        return getInstance().getTaxRegistrationLicense();
                    //单位银行结算账户开户许可证
                    case PictureConstants.AutcMtrType.ID_BANK_ACCOUNT_OPEN_LICENSE:
                        return getInstance().getBankAccountOpenLicense();
                    //机构信用代码证
                    case PictureConstants.AutcMtrType.ID_CREDIT_AGENCIES_LICENSE:
                        return getInstance().getCreditAgenciesLicense();
                    //CP备案许可
                    case PictureConstants.AutcMtrType.ID_ICP_LICENSE:
                        return getInstance().getICPLicense();
                    //行业许可证
                    case PictureConstants.AutcMtrType.ID_VOCATIONAL_PERMISSION_LICENSE:
                        return getInstance().getVocationalPermissionLicense();
                    //企业法人证件正面
                    case PictureConstants.AutcMtrType.ID_LEGALID_CARD_FRONT:
                        return getInstance().getLegalIdCardFront();
                    //企业法人证件反面
                    case PictureConstants.AutcMtrType.ID_LEGALID_CARD_BACK:
                        return getInstance().getLegalIdCardBack();
                    //企业流水账户信息
                    case PictureConstants.AutcMtrType.ID_BUSINESS_ACCOUNTFLOW:
                        return getInstance().getBusinessAccountFlow();
                    default:
                        break;
                }

            return mutcMtr;
        }

        @Transactional(readOnly = false)
        public void clean() {
            mutcMtr = getImage();
            handleAutcMtrClean(mutcMtr);
            setImage(null);
            getBaseService().merge(getInstance());
            FacesUtil.addInfoMessage("清除成功！");
        }

        @Transactional(readOnly = false)
        public void upload(FileUploadEvent event) {
            if (getImage() == null) {
                setImage(initAutcMtr(autcMtrTypeId));
            } else {
                // 延迟加载，session关闭了，所以。。。
                setImage(getBaseService().get(AuthenticationMaterials.class, mutcMtr.getId()));
            }
            mutcMtr = getImage();
            handleAutcMtrUpload(mutcMtr, event);
        }
    }

    @Transactional(readOnly = false)
    public void bankCreditReportClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getBankCreditReport();
        this.getInstance().setBankCreditReport(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 银行征信报告
    @Transactional(readOnly = false)
    public void bankCreditReportUpload(FileUploadEvent event) {
        if (this.getInstance().getBankCreditReport() == null) {
            this.getInstance().setBankCreditReport(initAutcMtr(PictureConstants.AutcMtrType.ID_BANK_CREDITREPORT));
        } else {
            // 延迟加载，session关闭了，所以。。。
            this.getInstance().setBankCreditReport(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getBankCreditReport().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getBankCreditReport();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void businessAccountFlowClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getBusinessAccountFlow();
        this.getInstance().setBusinessAccountFlow(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 企业流水账户信息
    @Transactional(readOnly = false)
    public void businessAccountFlowUpload(FileUploadEvent event) {
        if (this.getInstance().getBusinessAccountFlow() == null) {
            this.getInstance().setBusinessAccountFlow(initAutcMtr(PictureConstants.AutcMtrType.ID_BUSINESS_ACCOUNTFLOW));
        } else {
            // 延迟加载，session关闭了，所以。。。
            this.getInstance().setBusinessAccountFlow(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getBusinessAccountFlow().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getBusinessAccountFlow();
        handleAutcMtrUpload(mutcMtr, event);
    }


    @Transactional(readOnly = false)
    public void carInfoClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getCarInfo();
        this.getInstance().setCarInfo(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传车辆证明
    @Transactional(readOnly = false)
    public void carInfoUpload(FileUploadEvent event) {
        if (this.getInstance().getCarInfo() == null) {
            this.getInstance().setCarInfo(initAutcMtr(PictureConstants.AutcMtrType.ID_CAR_INFO));
        } else {
            // 延迟加载，session关闭了，所以。。。
            this.getInstance().setCarInfo(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getCarInfo().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getCarInfo();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void diplomaScanClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getDiplomaScan();
        this.getInstance().setDiplomaScan(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传学历证书扫描件
    @Transactional(readOnly = false)
    public void diplomaScanUpload(FileUploadEvent event) {
        if (this.getInstance().getDiplomaScan() == null) {
            this.getInstance().setDiplomaScan(initAutcMtr(PictureConstants.AutcMtrType.ID_DIPLOMA_SCAN));
        } else {
            // 延迟加载，session关闭了，所以。。。
            this.getInstance().setDiplomaScan(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getDiplomaScan().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getDiplomaScan();
        handleAutcMtrUpload(mutcMtr, event);
    }

    private String fileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        InputStream is = null;
        try {
            is = file.getInputstream();
            FacesUtil.addInfoMessage("上传成功！");
            return ImageUploadUtil.upload(is, file.getFileName(), TYPE + getInstance().getUserId());
        } catch (IOException e) {
            log.debug(e.getMessage());
            FacesUtil.addErrorMessage("上传失败！");
            return null;
        }
    }

    public Boolean getIspass() {
        return ispass;
    }

    public String getVerifyMessage() {
        return verifyMessage;
    }

    @Transactional(readOnly = false)
    public void houseInfoClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getHouseInfo();
        this.getInstance().setHouseInfo(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传房产证明
    @Transactional(readOnly = false)
    public void houseInfoUpload(FileUploadEvent event) {
        if (this.getInstance().getHouseInfo() == null) {
            this.getInstance().setHouseInfo(initAutcMtr(PictureConstants.AutcMtrType.ID_HOUSE_INFO));
        } else {
            // 延迟加载，session关闭了，所以。。。
            this.getInstance().setHouseInfo(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getHouseInfo().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getHouseInfo();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void huKouPhotoClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getHuKouPhoto();
        this.getInstance().setHuKouPhoto(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 手持户口卡照片
    @Transactional(readOnly = false)
    public void huKouPhotoUpload(FileUploadEvent event) {
        if (this.getInstance().getHuKouPhoto() == null) {
            this.getInstance().setHuKouPhoto(initAutcMtr(PictureConstants.AutcMtrType.ID_HUKOU_PHOTO));
        } else {
            // 延迟加载，session关闭了，所以。。。
            this.getInstance().setHuKouPhoto(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getHuKouPhoto().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getHuKouPhoto();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void huKouScanClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getHuKouScan();
        this.getInstance().setHuKouScan(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 户口卡
    @Transactional(readOnly = false)
    public void huKouScanUpload(FileUploadEvent event) {
        if (this.getInstance().getHuKouScan() == null) {
            this.getInstance().setHuKouScan(initAutcMtr(PictureConstants.AutcMtrType.ID_HUKOU_SCAN));
        } else {
            this.getInstance().setHuKouScan(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getHuKouScan().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getHuKouScan();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void idCardPhotoClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getIdCardPhoto();
        this.getInstance().setIdCardPhoto(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传手持身份证照片
    @Transactional(readOnly = false)
    public void idCardPhotoUpload(FileUploadEvent event) {
        if (this.getInstance().getIdCardPhoto() == null) {
            this.getInstance().setIdCardPhoto(initAutcMtr(PictureConstants.AutcMtrType.ID_CARD_PHOTO));
        } else {
            this.getInstance().setIdCardPhoto(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getIdCardPhoto().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getIdCardPhoto();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void idCardScanClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getIdCardScan();
        this.getInstance().setIdCardScan(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传身份证扫描件
    @Transactional(readOnly = false)
    public void idCardScanUpload(FileUploadEvent event) {
        if (this.getInstance().getIdCardScan() == null) {
            this.getInstance().setIdCardScan(initAutcMtr(PictureConstants.AutcMtrType.ID_CARD_SCAN));
        } else {
            this.getInstance().setIdCardScan(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getIdCardScan().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getIdCardScan();
        handleAutcMtrUpload(mutcMtr, event);
    }


    @Transactional(readOnly = false)
    public void accountFlowClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getAccountFlow();
        this.getInstance().setAccountFlow(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 账户流水扫描件
    @Transactional(readOnly = false)
    public void accountFlowUpload(FileUploadEvent event) {
        if (this.getInstance().getAccountFlow() == null) {
            this.getInstance().setAccountFlow(initAutcMtr(PictureConstants.AutcMtrType.ID_ACCOUNT_FLOW));
        } else {
            // 延迟加载，session关闭了，所以。。。
            this.getInstance().setAccountFlow(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getAccountFlow().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getAccountFlow();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void marriageCertificateClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getMarriageCertificate();
        this.getInstance().setMarriageCertificate(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传结婚证
    @Transactional(readOnly = false)
    public void marriageCertificateUpload(FileUploadEvent event) {
        if (this.getInstance().getMarriageCertificate() == null) {
            this.getInstance().setMarriageCertificate(initAutcMtr(PictureConstants.AutcMtrType.ID_MARRIAGE_CERTIFICATE));
        } else {
            this.getInstance().setMarriageCertificate(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getMarriageCertificate().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getMarriageCertificate();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void microblogInfoClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getMicroblogInfo();
        this.getInstance().setMicroblogInfo(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 微博认证
    @Transactional(readOnly = false)
    public void microblogInfoUpload(FileUploadEvent event) {
        if (this.getInstance().getMicroblogInfo() == null) {
            this.getInstance().setMicroblogInfo(initAutcMtr(PictureConstants.AutcMtrType.ID_MICROBLOG_INFO));
        } else {
            this.getInstance().setMicroblogInfo(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getMicroblogInfo().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getMicroblogInfo();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void otherDomicileClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getOtherDomicile();
        this.getInstance().setOtherDomicile(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传其他居住地证明
    @Transactional(readOnly = false)
    public void otherDomicileUpload(FileUploadEvent event) {
        if (this.getInstance().getOtherDomicile() == null) {
            this.getInstance().setOtherDomicile(initAutcMtr(PictureConstants.AutcMtrType.ID_OTHER_DOMICILE));
        } else {
            this.getInstance().setOtherDomicile(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getOtherDomicile().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getOtherDomicile();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void otherEstateClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getOtherEstate();
        this.getInstance().setOtherEstate(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 其他财产证明
    @Transactional(readOnly = false)
    public void otherEstateUpload(FileUploadEvent event) {
        if (this.getInstance().getOtherEstate() == null) {
            this.getInstance().setOtherEstate(initAutcMtr(PictureConstants.AutcMtrType.ID_OTHER_ESTATE));
        } else {
            this.getInstance().setOtherEstate(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getOtherEstate().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getOtherEstate();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void workCertificateClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getWorkCertificate();
        this.getInstance().setWorkCertificate(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 工作证件扫描件
    @Transactional(readOnly = false)
    public void workCertificateUpload(FileUploadEvent event) {
        if (this.getInstance().getWorkCertificate() == null) {
            this.getInstance().setWorkCertificate(initAutcMtr(PictureConstants.AutcMtrType.ID_WORK_CERTIFICATE));
        } else {
            this.getInstance().setWorkCertificate(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getWorkCertificate().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getWorkCertificate();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void otherIdCertificateClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getOtherIdCertificate();
        this.getInstance().setOtherIdCertificate(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传其他可确认身份的证件
    @Transactional(readOnly = false)
    public void otherIdCertificateUpload(FileUploadEvent event) {
        if (this.getInstance().getOtherIdCertificate() == null) {
            this.getInstance().setOtherIdCertificate(initAutcMtr(PictureConstants.AutcMtrType.ID_OTHERID_CERTIFICATE));
        } else {
            this.getInstance().setOtherIdCertificate(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getOtherIdCertificate().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getOtherIdCertificate();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void otherIncomeInfoClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getOtherIncomeInfo();
        this.getInstance().setOtherIncomeInfo(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 其他能证明稳定收入的材料
    @Transactional(readOnly = false)
    public void otherIncomeInfoUpload(FileUploadEvent event) {
        if (this.getInstance().getOtherIncomeInfo() == null) {
            this.getInstance().setOtherIncomeInfo(initAutcMtr(PictureConstants.AutcMtrType.ID_OTHER_INCOMEINFO));
        } else {
            // 延迟加载，session关闭了，所以。。。
            this.getInstance().setOtherIncomeInfo(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getOtherIncomeInfo().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getOtherIncomeInfo();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void positionalTitlesClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getPositionalTitles();
        this.getInstance().setPositionalTitles(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传职称证书
    @Transactional(readOnly = false)
    public void positionalTitlesUpload(FileUploadEvent event) {
        if (this.getInstance().getPositionalTitles() == null) {
            this.getInstance().setPositionalTitles(initAutcMtr(PictureConstants.AutcMtrType.ID_POSITIONAL_TITLES));
        } else {
            // 延迟加载，session关闭了，所以。。。
            this.getInstance().setPositionalTitles(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getPositionalTitles().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getPositionalTitles();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void studentIdClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getStudentId();
        this.getInstance().setStudentId(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 上传学生证
    @Transactional(readOnly = false)
    public void studentIdUpload(FileUploadEvent event) {
        if (this.getInstance().getStudentId() == null) {
            this.getInstance().setStudentId(initAutcMtr(PictureConstants.AutcMtrType.ID_STUDENT_ID));
        } else {
            this.getInstance().setStudentId(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getStudentId().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getStudentId();
        handleAutcMtrUpload(mutcMtr, event);
    }

    @Transactional(readOnly = false)
    public void proofEarningsClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getProofEarnings();
        this.getInstance().setProofEarnings(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }

    // 收入证明
    @Transactional(readOnly = false)
    public void proofEarningsUpload(FileUploadEvent event) {
        if (this.getInstance().getProofEarnings() == null) {
            this.getInstance().setProofEarnings(initAutcMtr(PictureConstants.AutcMtrType.ID_PROOF_EARNINGS));
        } else {
            this.getInstance().setProofEarnings(getBaseService().get(AuthenticationMaterials.class, this.getInstance().getProofEarnings().getId()));
        }
        AuthenticationMaterials mutcMtr = this.getInstance().getProofEarnings();
        handleAutcMtrUpload(mutcMtr, event);
    }


    @Transactional(readOnly = false)
    public void businessLicenseClean() {
        AuthenticationMaterials mutcMtr = this.getInstance().getBusinessLicense();
        this.getInstance().setBusinessLicense(null);
        handleAutcMtrClean(mutcMtr);
        getBaseService().merge(getInstance());
        FacesUtil.addInfoMessage("清除成功！");
    }
    
    /*common*/

    /**
     * 清除某一项的所有图片
     *
     * @param mutcMtr
     */
    @Transactional(readOnly = false)
    private void handleAutcMtrClean(AuthenticationMaterials mutcMtr) {
        if (mutcMtr != null) {
            for (AutcMtrPicture pic : mutcMtr.getPictures()) {
                ImageUploadUtil.delete(pic.getPicture());
            }
            getBaseService().delete(mutcMtr);
        }
    }

    private void handleAutcMtrUpload(AuthenticationMaterials mutcMtr, FileUploadEvent event) {
        // 判断最大数量
        if (mutcMtr.getPictures().size() < mutcMtr.getType().getMaxNumber()) {
            // 保存上传图片
            String picturePath = fileUpload(event);
            AutcMtrPicture amPic = new AutcMtrPicture();
            amPic.setId(IdGenerator.randomUUID());
            amPic.setAutcMtr(mutcMtr);
            amPic.setPicture(picturePath);
            amPic.setSeqNum(mutcMtr.getPictures().size() + 1);
            mutcMtr.getPictures().add(amPic);
            getBaseService().update(mutcMtr);
            getBaseService().merge(getInstance());
        } else {
            // 超出允许图片数量的上限。
            FacesUtil.addErrorMessage("上传失败，最多允许上传" + mutcMtr.getType().getMaxNumber() + "张图片。");
        }
    }


    @Transactional(readOnly = false)
    private AuthenticationMaterials initAutcMtr(String autcMtrTypeId) {
        AuthenticationMaterials autcMtr = new AuthenticationMaterials();
        autcMtr.setId(IdGenerator.randomUUID());
        autcMtr.setType(new AutcMtrType(autcMtrTypeId));
        return getBaseService().merge(autcMtr);
    }

    @Override
    protected void initInstance() {
        try {
            if (StringUtils.isNotEmpty(userId))
                setInstance(borrowerService.initBorrowerAuthentication(userId));
            else
                setInstance(borrowerService.initBorrowerAuthentication(loginUser.getLoginUserId()));

            businessLicense = new AuthenticationBean(PictureConstants.AutcMtrType.ID_BUSINESS_LICENSE);
            organizationLicense = new AuthenticationBean(PictureConstants.AutcMtrType.ID_ORGANIZATION_LICENSE);
            taxRegistrationLicense = new AuthenticationBean(PictureConstants.AutcMtrType.ID_TAX_REGISTRATION_LICENSE);
            bankAccountOpenLicense = new AuthenticationBean(PictureConstants.AutcMtrType.ID_BANK_ACCOUNT_OPEN_LICENSE);
            creditAgenciesLicense = new AuthenticationBean(PictureConstants.AutcMtrType.ID_CREDIT_AGENCIES_LICENSE);
            ICPLicense = new AuthenticationBean(PictureConstants.AutcMtrType.ID_ICP_LICENSE);
            vocationalPermissionLicense = new AuthenticationBean(PictureConstants.AutcMtrType.ID_VOCATIONAL_PERMISSION_LICENSE);
            legalIdCardFront = new AuthenticationBean(PictureConstants.AutcMtrType.ID_LEGALID_CARD_FRONT);
            legalIdCardBack = new AuthenticationBean(PictureConstants.AutcMtrType.ID_LEGALID_CARD_BACK);
        } catch (UserNotFoundException e) {
            // FIXME:处理
            log.debug(e.getMessage());
        }
    }


    /**
     * 根据userId初始化实例
     *
     * @param userId
     */
    @PreAuthorize("hasRole('USER')")
    public void initBorrower(String userId) {

        try {
            setInstance(borrowerService.initBorrowerAuthentication(userId));
        } catch (UserNotFoundException e) {
            // FIXME:处理
            e.printStackTrace();
        }

    }


    public void initVerify(BorrowerAuthentication borrowerAuthentication) {
        setInstance(borrowerAuthentication);
        if ((BorrowerConstant.Verify.passed).equals(this.getInstance().getVerified())) {
            ispass = true;
        }
    }

    @PreAuthorize("hasRole('USER_BORROW_VERIFY')")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PreAuthorize("hasRole('USER_BORROW_VERIFY')")
    public String getUserId() {
        return userId;
    }

    @Override
    public String save() {
        borrowerService.saveOrUpdateBorrowerAuthentication(getInstance());
        FacesUtil.addInfoMessage("保存成功，请等待管理员审核。");
        return "pretty:user_loan_applying-p2p";
    }

    public String saveByAdmin() {
        save();
        return "pretty:";

    }

    public void setIspass(Boolean ispass) {
        this.ispass = ispass;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }


    /**
     * 审核借款用户认证资料
     *
     * @return
     */
    @Transactional(readOnly = false)
    public String verify(BorrowerAuthentication ba) {
        borrowerService.verifyBorrowerAuthentication(ba.getUserId(), ispass, ba.getVerifiedMessage(), loginUser.getLoginUserId());
        FacesUtil.addInfoMessage("保存成功");
        return FacesUtil.redirect("/admin/verify/verifyLoanerList");
    }


    public AuthenticationBean getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(AuthenticationBean businessLicense) {
        this.businessLicense = businessLicense;
    }

    public AuthenticationBean getOrganizationLicense() {
        return organizationLicense;
    }

    public void setOrganizationLicense(AuthenticationBean organizationLicense) {
        this.organizationLicense = organizationLicense;
    }

    public AuthenticationBean getTaxRegistrationLicense() {
        return taxRegistrationLicense;
    }

    public void setTaxRegistrationLicense(AuthenticationBean taxRegistrationLicense) {
        this.taxRegistrationLicense = taxRegistrationLicense;
    }

    public AuthenticationBean getBankAccountOpenLicense() {
        return bankAccountOpenLicense;
    }

    public void setBankAccountOpenLicense(AuthenticationBean bankAccountOpenLicense) {
        this.bankAccountOpenLicense = bankAccountOpenLicense;
    }

    public AuthenticationBean getCreditAgenciesLicense() {
        return creditAgenciesLicense;
    }

    public void setCreditAgenciesLicense(AuthenticationBean creditAgenciesLicense) {
        this.creditAgenciesLicense = creditAgenciesLicense;
    }

    public AuthenticationBean getICPLicense() {
        return ICPLicense;
    }

    public void setICPLicense(AuthenticationBean ICPLicense) {
        this.ICPLicense = ICPLicense;
    }

    public AuthenticationBean getVocationalPermissionLicense() {
        return vocationalPermissionLicense;
    }

    public void setVocationalPermissionLicense(AuthenticationBean vocationalPermissionLicense) {
        this.vocationalPermissionLicense = vocationalPermissionLicense;
    }

    public AuthenticationBean getLegalIdCardFront() {
        return legalIdCardFront;
    }

    public void setLegalIdCardFront(AuthenticationBean legalIdCardFront) {
        this.legalIdCardFront = legalIdCardFront;
    }

    public AuthenticationBean getLegalIdCardBack() {
        return legalIdCardBack;
    }

    public void setLegalIdCardBack(AuthenticationBean legalIdCardBack) {
        this.legalIdCardBack = legalIdCardBack;
    }
}
