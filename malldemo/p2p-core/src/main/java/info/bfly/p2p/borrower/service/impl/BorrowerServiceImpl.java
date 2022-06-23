package info.bfly.p2p.borrower.service.impl;

import info.bfly.archer.system.service.SpringSecurityService;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.VerifyHistory;
import info.bfly.archer.user.service.VerifyHistoryService;
import info.bfly.archer.user.service.impl.UserBO;
import info.bfly.core.annotations.Log;
import info.bfly.p2p.borrower.BorrowerConstant;
import info.bfly.p2p.borrower.model.*;
import info.bfly.p2p.borrower.service.BorrowerService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 */
@Service("borrowService")
public class BorrowerServiceImpl implements BorrowerService {
    @Resource
    private HibernateTemplate     ht;
    @Resource
    private UserBO                userBO;
    @Resource
    private SpringSecurityService springSecurityService;


    @Log
    private Logger log;

    @Qualifier("borrowService")
    @Resource
    private BorrowerService borrowerService;


    @Resource
    private VerifyHistoryService verifyHistoryService;


    /**
     * 查询borrowerInfo，如果没有，则创建
     *
     * @param user
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private BorrowerInfo getBorrowerInfo(User user) {
        BorrowerInfo bi = ht.get(BorrowerInfo.class, user.getId());
        if (bi == null) {
            bi = new BorrowerInfo(user);
            ht.save(bi);
        }
        return bi;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private void grantBorrowerRole(String biId) {
        BorrowerInfo bi = ht.get(BorrowerInfo.class, biId);
        User user = bi.getUser();
        if (UserConstants.UserType.PERSONAL.equals(user.getUserType())) {
            //实名认证
            InvestorPersonalInfo investorPersonal = bi.getInvestorPersonalInfo();
            if (investorPersonal != null && investorPersonal.getIsPassedVerify()) {
                userBO.addRole(user, new Role("INVESTOR"));
            } else {
                userBO.removeRole(user, new Role("INVESTOR"));
            }
            BorrowerAdditionalInfo addition = bi.getBorrowerAdditionalInfo();
            BorrowerAuthentication authentication = bi.getBorrowerAuthentication();
            BorrowerPersonalInfo personal = bi.getBorrowerPersonalInfo();
            if (addition != null && authentication != null && personal != null && addition.getIsPassedVerify() && authentication.getIsPassedVerify() && personal.getIsPassedVerify()) {
                // 添加借款权限
                userBO.addRole(user, new Role("LOANER"));
            } else {
                // 如果有一个未通过的,删除用户权限
                userBO.removeRole(user, new Role("LOANER"));
            }
        } else if (UserConstants.UserType.BUSINESS.equals(user.getUserType())) {
            //基本信息
            BorrowerBusinessInfo borrowerBusinessInfo = bi.getBorrowerBusinessInfo();
            //材料
            BorrowerAuthentication authentication = bi.getBorrowerAuthentication();

            //企业用户只能同时获取
            if (borrowerBusinessInfo != null && borrowerBusinessInfo.getIsPassedVerify() && borrowerBusinessInfo.getAgentIsPassedVerify() && authentication != null && authentication.getIsPassedVerify()) {
                userBO.addRole(user, new Role("INVESTOR"));
                userBO.addRole(user, new Role("LOANER"));
                userBO.addRole(user, new Role("MEMBER"));
            } else {
                userBO.removeRole(user, new Role("INVESTOR"));
                userBO.removeRole(user, new Role("LOANER"));
            }
        }
        user = ht.merge(user);
        springSecurityService.refreshLoginUserAuthorities(user.getId());
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public InvestorPersonalInfo initInvestorPersonalInfo(String userId) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("userId:" + userId);
        }
        InvestorPersonalInfo investorPersonalInfo = ht.get(InvestorPersonalInfo.class, userId);
        if (investorPersonalInfo == null) {
            investorPersonalInfo = new InvestorPersonalInfo();
            investorPersonalInfo.setUserId(userId);
            investorPersonalInfo.setVerified(BorrowerConstant.Verify.unverified);

        }

        if (investorPersonalInfo.getBorrowerInfo() == null) {
            investorPersonalInfo.setBorrowerInfo(getBorrowerInfo(user));
        }
        return investorPersonalInfo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public BorrowerAdditionalInfo initBorrowerAdditionalInfo(String userId) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("userId:" + userId);
        }
        BorrowerAdditionalInfo bai = ht.get(BorrowerAdditionalInfo.class, userId);
        if (bai == null) {
            bai = new BorrowerAdditionalInfo();
            // 初始化为工薪阶层，以便前台验证用。
            bai.setOccupation("工薪阶层");
            bai.setVerified(BorrowerConstant.Verify.unverified);
        }
        if (bai.getBorrowerInfo() == null) {
            bai.setBorrowerInfo(getBorrowerInfo(user));
        }
        return bai;
    }


    @Override
    public BorrowerBusinessInfo initBorrowerBusinessInfo(String userId) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("userId:" + userId);
        }
        BorrowerBusinessInfo bai = ht.get(BorrowerBusinessInfo.class, userId);
        if (bai == null) {
            bai = new BorrowerBusinessInfo();
            bai.setUserId(userId);
            bai.setVerified(BorrowerConstant.Verify.unverified);
        }
        if (bai.getBorrowerInfo() == null) {
            bai.setBorrowerInfo(getBorrowerInfo(user));
        }
        return bai;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public BorrowerAuthentication initBorrowerAuthentication(String userId) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("userId:" + userId);
        }
        BorrowerAuthentication ba = ht.get(BorrowerAuthentication.class, userId);
        if (ba == null) {
            ba = new BorrowerAuthentication();
            ba.setVerified(BorrowerConstant.Verify.unverified);
        }
        if (ba.getBorrowerInfo() == null) {
            ba.setBorrowerInfo(getBorrowerInfo(user));
        }
        return ba;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public BorrowerPersonalInfo initBorrowerPersonalInfo(String userId) throws UserNotFoundException {
        User user = ht.get(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("userId:" + userId);
        }
        BorrowerPersonalInfo bpi = ht.get(BorrowerPersonalInfo.class, userId);
        if (bpi == null) {
            bpi = new BorrowerPersonalInfo();
            bpi.setVerified(BorrowerConstant.Verify.unverified);
        }
        if (bpi.getBorrowerInfo() == null) {
            bpi.setBorrowerInfo(getBorrowerInfo(user));
        }
        return bpi;
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveOrUpdateInvestorPersonalInfo(InvestorPersonalInfo bai) {
        // TODO：验证，抛异常
        ht.saveOrUpdate(bai);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveOrUpdateBorrowerAdditionalInfo(BorrowerAdditionalInfo bai) {
        // TODO：验证，抛异常
        ht.saveOrUpdate(bai);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveOrUpdateBorrowerAuthentication(BorrowerAuthentication ba) {
        // TODO：验证，抛异常
        ht.saveOrUpdate(ba);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveOrUpdateBorrowerPersonalInfo(BorrowerPersonalInfo bpi) {
        // TODO：验证，抛异常
        ht.saveOrUpdate(bpi);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveOrUpdateBorrowerBusinessInfo(BorrowerBusinessInfo bbi) {
        // TODO：验证，抛异常
        ht.saveOrUpdate(bbi);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void verifyBorrowerAdditionalInfo(String baiId, boolean isPassed, String msg, String verifiedUserId) {
        // TODO:验证

        BorrowerAdditionalInfo bai = ht.get(BorrowerAdditionalInfo.class, baiId);
        if (bai != null) {
            bai.setIsPassedVerify(isPassed);
            bai.setVerifiedTime(new Date());
            bai.setVerifiedUser(new User(verifiedUserId));
            bai.setVerifiedMessage(msg);
            ht.update(bai);
            grantBorrowerRole(bai.getUserId());
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void verifyBorrowerAuthentication(String baId, boolean isPassed, String msg, String verifiedUserId) {
        // TODO:验证
        BorrowerAuthentication ba = ht.get(BorrowerAuthentication.class, baId);
        if (ba != null) {
            // 抛异常
            ba.setIsPassedVerify(isPassed);
            ba.setVerifiedTime(new Date());
            ba.setVerifiedUser(new User(verifiedUserId));
            ba.setVerifiedMessage(msg);
            ht.update(ba);
            grantBorrowerRole(ba.getUserId());
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void verifyBorrowerPersonalInfo(String bpiId, boolean isPassed, String msg, String verifiedUserId) {
        // TODO:验证
        BorrowerPersonalInfo bpi = ht.get(BorrowerPersonalInfo.class, bpiId);
        if (bpi != null) {
            // 抛异常
            bpi.setIsPassedVerify(isPassed);
            bpi.setVerifiedTime(new Date());
            bpi.setVerifiedUser(new User(verifiedUserId));
            bpi.setVerifiedMessage(msg);
            ht.update(bpi);
            grantBorrowerRole(bpi.getUserId());
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void verifyRealNameCertificatione(String userId, boolean isPassed, String msg, String verifiedUserId) {
        // TODO:验证
        VerifyHistory verifyHistory = new VerifyHistory();
        verifyHistory.setVerifyUser(userBO.getUserById(verifiedUserId));
        verifyHistory.setStatus(UserConstants.VerifyStatus.SUCCESS);
        verifyHistory.setVerifyMessage(msg);
        verifyHistory.setVerifyTarget(userId);
        verifyHistory.setVerifyType(UserConstants.VerifyType.InvestorPersonalInfo);
        verifyHistoryService.addVerifyHistory(verifyHistory);
        InvestorPersonalInfo bpi = ht.get(InvestorPersonalInfo.class, userId);
        if (bpi != null) {
            bpi.setIsPassedVerify(isPassed);
            bpi.setVerifiedTime(new Date());
            bpi.setVerifiedUser(new User(verifiedUserId));
            bpi.setVerifiedMessage(msg);
            ht.update(bpi);
            grantBorrowerRole(bpi.getUserId());
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void verifyBorrowerBusinessInfo(String bbiId, boolean isPassed, String msg, String verifiedUserId) {
        // TODO:验证
        BorrowerBusinessInfo bpi = ht.get(BorrowerBusinessInfo.class, bbiId);
        if (bpi != null) {
            bpi.setIsPassedVerify(isPassed);
            bpi.setVerifiedTime(new Date());
            bpi.setVerifiedUser(new User(verifiedUserId));
            bpi.setVerifiedMessage(msg);
            ht.update(bpi);
            grantBorrowerRole(bpi.getUserId());

        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void verifyBorrowerBusinessAgentInfo(String bbiId, boolean isPassed, String msg, String verifiedUserId) {
        // TODO:验证
        BorrowerBusinessInfo bpi = ht.get(BorrowerBusinessInfo.class, bbiId);
        if (bpi != null) {
            bpi.setAgentIsPassedVerify(isPassed);
            bpi.setAgentVerifiedTime(new Date());
            bpi.setAgentVerifiedUser(new User(verifiedUserId));
            bpi.setAgentVerifiedMessage(msg);
            ht.update(bpi);
            grantBorrowerRole(bpi.getUserId());
        }
        }
}
