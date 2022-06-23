package info.bfly.p2p.borrower.service;

import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.p2p.borrower.model.*;

/**
 */
public interface BorrowerService {

    /**
     * 初始化实名认证信息
     *
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    InvestorPersonalInfo initInvestorPersonalInfo(String userId) throws UserNotFoundException;

    /**
     * 初始化BorrowerBusinessInfo，企业借款人信息
     *
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    BorrowerBusinessInfo initBorrowerBusinessInfo(String userId) throws UserNotFoundException;

    /**
     * 初始化BorrowerAdditionalInfo，借款人工作财务信息
     *
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    BorrowerAdditionalInfo initBorrowerAdditionalInfo(String userId) throws UserNotFoundException;

    /**
     * 初始化BorrowerAuthentication，借款人材料信息
     *
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    BorrowerAuthentication initBorrowerAuthentication(String userId) throws UserNotFoundException;

    /**
     * 初始化BorrowerPersonalInfo，借款人普通信息
     *
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    BorrowerPersonalInfo initBorrowerPersonalInfo(String userId) throws UserNotFoundException;

    /**
     * InvestorPersonalInfo，实名认证信息
     *
     * @param ipi
     */
    void saveOrUpdateInvestorPersonalInfo(InvestorPersonalInfo ipi);

    /**
     * BorrowerBusinessInfo，企业认证信息
     *
     * @param bbi
     */
    void saveOrUpdateBorrowerBusinessInfo(BorrowerBusinessInfo bbi);

    /**
     * 保存BorrowerAdditionalInfo，借款人工作财务信息
     *
     * @param bai
     */
    void saveOrUpdateBorrowerAdditionalInfo(BorrowerAdditionalInfo bai);

    /**
     * 保存BorrowerAuthentication，借款人材料信息
     *
     * @param ba
     */
    void saveOrUpdateBorrowerAuthentication(BorrowerAuthentication ba);

    /**
     * 保存BorrowerPersonalInfo，借款人普通信息
     *
     * @param bpi
     */
    void saveOrUpdateBorrowerPersonalInfo(BorrowerPersonalInfo bpi);

    /**
     * 审核实名信息
     *
     * @param userId         普通信息编号
     * @param isPassed       是否通过
     * @param msg            审核信息
     * @param verifiedUserId 审核用户编号
     */
    void verifyRealNameCertificatione(String userId, boolean isPassed, String msg, String verifiedUserId);

    /**
     * 审核 BorrowerAdditionalInfo，借款人工作财务信息
     *
     * @param baiId          工作财务信息编号
     * @param isPassed       是否通过
     * @param msg            审核信息
     * @param verifiedUserId 审核用户编号
     */
    void verifyBorrowerAdditionalInfo(String baiId, boolean isPassed, String msg, String verifiedUserId);

    /**
     * 审核 BorrowerAuthentication，借款人材料信息
     *
     * @param baId           材料信息编号
     * @param isPassed       是否通过
     * @param msg            审核信息
     * @param verifiedUserId 审核用户编号
     */
    void verifyBorrowerAuthentication(String baId, boolean isPassed, String msg, String verifiedUserId);

    /**
     * 审核 BorrowerPersonalInfo，借款人普通信息
     *
     * @param bpiId          普通信息编号
     * @param isPassed       是否通过
     * @param msg            审核信息
     * @param verifiedUserId 审核用户编号
     */
    void verifyBorrowerPersonalInfo(String bpiId, boolean isPassed, String msg, String verifiedUserId);
    /**
     * 审核 BorrowerBusinessInfo，企业借款人普通信息
     *
     * @param bbiId          普通信息编号
     * @param isPassed       是否通过
     * @param msg            审核信息
     * @param verifiedUserId 审核用户编号
     */
    void verifyBorrowerBusinessInfo(String bbiId, boolean isPassed, String msg, String verifiedUserId);    /**
     * 审核 BorrowerBusinessInfo，企业借款人经办人普通信息
     *
     * @param bbiId          普通信息编号
     * @param isPassed       是否通过
     * @param msg            审核信息
     * @param verifiedUserId 审核用户编号
     */
    void verifyBorrowerBusinessAgentInfo(String bbiId, boolean isPassed, String msg, String verifiedUserId);


}
