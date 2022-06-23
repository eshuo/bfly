package info.bfly.archer.user.service.impl;

import info.bfly.archer.common.service.ValidationService;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserInfoService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * Description:
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    HibernateTemplate ht;
    @Resource
    ValidationService validationSrv;

    @Override
    public List getUserMessage(String uid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEmailExist(String email) {
        List<User> users = (List<User>) ht.find("from User user where user.email=?", email);
        return users.size() != 0;
    }

    @Override
    public boolean isMobileNumberExist(String mobileNumber) {
        List<User> users = (List<User>) ht.find("from User user where user.mobileNumber=?", mobileNumber);
        return users.size() != 0;
    }

    @Override
    public boolean isSendMessageToUser(String uid, String nodeId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isUsernameExist(String username) {
        List<User> users = (List<User>) ht.find("from User user where user.username=?", username);
        return users.size() != 0;
    }

    @Override
    public boolean isVip(String userId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void modifyCashPassWord(String userId, String newPassword) {
        // TODO Auto-generated method stub
    }

    @Override
    public void modifyEmail(String userId, String newEmail) {
        // TODO Auto-generated method stub
    }

    @Override
    public void resetCashPassword(String userId) {
        // TODO Auto-generated method stub
    }

    @Override
    public void saveMessageSet(String uid, List list) {
        // TODO Auto-generated method stub
    }

    @Override
    public String userRiskRankCalculation(String uid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean verifyOldPassword(String userId, String oldPassword) {
        // TODO Auto-generated method stub
        return false;
    }
}
