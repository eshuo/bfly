package info.bfly.archer.user.service.impl;

import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

@Service("userBO")
public class UserBO {
    @Resource
    HibernateTemplate ht;

    public void addRole(User user, Role role) {
        for (Role roleT : user.getRoles()) {
            if (roleT.getId().equals(role.getId())) {
                return;
            }
        }
        user.getRoles().add(role);
        ht.update(user);
    }

    public void enableUser(User user) {
        user.setStatus(UserConstants.UserStatus.ENABLE);
        ht.update(user);
    }


    public User getUserById(String userId){
        return ht.get(User.class,userId);
    }

    public User getUserByEmail(String email) {
        List<User> users = (List<User>) ht.find("from User user where user.email=?", email);
        if (users.size() == 0) {
            return null;
        }
        else if (users.size() > 1) {
            throw new DuplicateKeyException("duplicate user.email:" + email);
        }
        return users.get(0);
    }

    public User getUserByMobileNumber(String mobileNumber) {
        List<User> users = (List<User>) ht.find("from User user where user.mobileNumber=?", mobileNumber);
        if (users.size() == 0) {
            return null;
        }
        else if (users.size() > 1) {
            throw new DuplicateKeyException("duplicate user.mobileNumber:" + mobileNumber);
        }
        return users.get(0);
    }

    public User getUserByUsername(String username) {
        List<User> users = (List<User>) ht.find("from User user where user.username=?", username);
        if (users.size() == 0) {
            return null;
        } else if (users.size() > 1) {
            throw new DuplicateKeyException("duplicate user.username:" + username);
        }
        return users.get(0);
    }

    public void removeRole(User user, Role role) {
        List<Role> roles = user.getRoles();
        for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
            Role role2 = (Role) iterator.next();
            if (role2.getId().equals(role.getId())) {
                iterator.remove();
            }
        }
        ht.update(user);
    }

    public void save(User user) {
        validateField(user);
        user.setId(user.getUsername());
        ht.save(user);
    }

    public void update(User user) {
        validateField(user);
        ht.merge(user);
    }

    /**
     * 检查某些域值输入格式和是否已存在，例如：username、email、mobile
     *
     * @param user
     */
    private void validateField(User user) {
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new IllegalArgumentException("user.username can not be empty!");
        }
        if (!user.getUsername().matches("^([a-zA-Z0-9_]{5,10}|[a-zA-Z]{11}|(?=.*\\d)(?=.*[a-z])[a-zA-Z0-9_]{11}|[0|2-9][0-9]{10}|1[0][0-9]{9}|[a-zA-Z0-9_]{12,16})$")) {
            throw new IllegalArgumentException("user.username cannot be a mobile number and must contains by number or word!");
        }
        User userExist = getUserByUsername(user.getUsername());
        if (userExist != null && !userExist.getId().equals(user.getId())) {
            ht.evict(userExist);
            throw new DuplicateKeyException("user.username '" + user.getUsername() + "' already exists!");
        }
        if (StringUtils.isNotEmpty(user.getEmail())) {
            User emailUser = getUserByEmail(user.getEmail());
            if (emailUser != null && !emailUser.getId().equals(user.getId())) {
                ht.evict(emailUser);
                throw new DuplicateKeyException("user.email '" + user.getEmail() + "' already exists!");
            }
        }
        if (StringUtils.isNotEmpty(user.getMobileNumber())) {
            User mobileUser = getUserByMobileNumber(user.getMobileNumber());
            if (mobileUser != null && !mobileUser.getId().equals(user.getId())) {
                ht.evict(mobileUser);
                throw new DuplicateKeyException("user.mobileNumber '" + user.getMobileNumber() + "' already exists!");
            }
        }
    }
}
