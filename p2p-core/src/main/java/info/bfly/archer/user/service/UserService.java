package info.bfly.archer.user.service;

import info.bfly.archer.common.exception.*;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.exception.ConfigNotFoundException;
import info.bfly.archer.user.exception.NotConformRuleException;
import info.bfly.archer.user.exception.RoleNotFoundException;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.User;

/**
 * Description: 用户service
 */
public interface UserService {
    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    User authenticate(String username, String password) throws UserNotFoundException;

    /**
     * 通过邮件激活用户
     *
     * @param activeCode 激活邮件里的激活码
     * @throws UserNotFoundException
     * @throws NoMatchingObjectsException
     * @throws AuthInfoOutOfDateException      认证码过期异常
     * @throws AuthInfoAlreadyActivedException
     */
    void activateUserByEmailActiveCode(String activeCode) throws UserNotFoundException, NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    /**
     * 为该用户添加一个角色，当用户不存在或者角色不存在的时候会抛出异常
     *
     * @param userId 用户编号
     * @param roleId 角色编号
     * @throws UserNotFoundException 用户不存在
     * @throws RoleNotFoundException 角色不存在
     */
    void addRole(String userId, String roleId) throws UserNotFoundException, RoleNotFoundException;

    /**
     * 绑定邮箱
     *
     * @param userId
     * @param email
     * @param authCode
     * @throws UserNotFoundException
     * @throws NoMatchingObjectsException
     * @throws AuthInfoOutOfDateException      认证码过期异常
     * @throws AuthInfoAlreadyActivedException
     */
    void bindingEmail(String userId, String email, String authCode) throws UserNotFoundException, NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    /**
     * 绑定手机号
     *
     * @param userId
     * @param mobileNumber
     * @param authCode
     * @throws UserNotFoundException
     * @throws NoMatchingObjectsException
     * @throws AuthInfoOutOfDateException      认证码过期异常
     * @throws AuthInfoAlreadyActivedException
     */
    void bindingMobileNumber(String userId, String mobileNumber, String authCode) throws UserNotFoundException, NoMatchingObjectsException, AuthInfoOutOfDateException,
            AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    /**
     * 改变当前用户状态，用户状态参照 {@link UserConstants.UserStatus}， 如果当前用户不存在则抛出异常
     *
     * @param userId
     * @param status 状态编号
     * @throws UserNotFoundException   系统中找不到该用户
     * @throws ConfigNotFoundException 找不到该状态
     */
    void changeUserStatus(String userId, String status) throws UserNotFoundException, ConfigNotFoundException;

    /**
     * 管理员创建借款者
     *
     * @param user
     */
    void createBorrowerByAdmin(User user);

    /**
     * 锁定当前用户，用户状态参照 {@link UserConstants.UserStatus}， 如果用户不存在抛出异常
     *
     * @param userId
     * @throws UserNotFoundException 系统中找不到该用户
     */
    void disableUser(String userId) throws UserNotFoundException;

    /**
     * 锁定当前用户，用户状态参照 {@link UserConstants.UserStatus}， 如果当前用户不存在则抛出异常
     *
     * @param userId
     * @throws UserNotFoundException 系统中找不到该用户
     */
    void enableUser(String userId) throws UserNotFoundException;

    /**
     * 根据邮箱查找用户
     *
     * @param email
     * @return
     * @throws UserNotFoundException
     */
    User getUserByEmail(String email) throws UserNotFoundException;

    /**
     * 根据用户编号查找用户
     *
     * @param userId 用户编号
     * @return
     * @throws UserNotFoundException
     */
    User getUserById(String userId) throws UserNotFoundException;

    /**
     * 根据用户编号查找用户,不用缓存
     *
     * @param userId 用户编号
     * @return
     * @throws UserNotFoundException
     */
    User getUserByIdWithOutCache(String userId) throws UserNotFoundException;

    /**
     * 根据手机号查找用户
     *
     * @param mobileNumber
     * @return
     * @throws UserNotFoundException
     */
    User getUserByMobileNumber(String mobileNumber) throws UserNotFoundException;

    /**
     * 判断某个用户是否拥有某个权限
     *
     * @param userId 用户id
     * @param roleId 权限id
     * @return
     */
    boolean hasRole(String userId, String roleId);

    /**
     * 修改现金密码
     *
     * @param userId          用户id
     * @param newCashPassword 新密码
     * @throws UserNotFoundException
     */
    void modifyCashPassword(String userId, String newCashPassword) throws UserNotFoundException;

    /**
     * 是否设置现金密码
     *
     * @param userId 用户id
     * @throws UserNotFoundException
     */
    boolean isSetCashPassword(String userId) throws UserNotFoundException;

    /**
     * 修改登录密码
     *
     * @param userId      用户id
     * @param newPassword 新密码
     * @throws UserNotFoundException
     */
    void modifyPassword(String userId, String newPassword) throws UserNotFoundException;

    /**
     * 实名认证，即授予借款权限（不绑定手机，无需验证手机认证码）
     *
     * @param user 修改过属性值的持久态对象
     */
    void realNameCertification(User user);

    /**
     * 实名认证，即授予借款权限
     *
     * @param user 修改过属性值的持久态对象
     * @throws NoMatchingObjectsException
     * @throws AuthInfoOutOfDateException      认证码过期异常
     * @throws AuthInfoAlreadyActivedException
     */
    void realNameCertification(User user, String authCode) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    /**
     * 通过邮箱进行实名认证。
     *
     * @param user     需要被实名认证的用户
     * @param authCode 认证码
     * @throws NoMatchingObjectsException
     * @throws AuthInfoOutOfDateException
     * @throws AuthInfoAlreadyActivedException
     * @author liuchun
     */
    void realNameCertificationByEmail(User user, String authCode) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    void refreshAuthorities(String userId);// 刷新登录用户权限

    /**
     * 用户基本信息注册(通过邮箱激活)
     *
     * @param user 用户对象
     */
    void register(User user);

    /**
     * 用户基本信息注册（通过邮箱激活）
     *
     * @param user
     * @param referrer 推荐人
     */
    void register(User user, String referrer);

    /**
     * 通过手机号注册
     *
     * @param user
     * @param authCode
     * @throws NoMatchingObjectsException
     * @throws AuthInfoOutOfDateException      认证码过期异常
     * @throws AuthInfoAlreadyActivedException
     */
    void registerByMobileNumber(User user, String authCode) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    /**
     * 通过手机号注册
     *
     * @param user
     * @param authCode 认证码
     * @param referrer 推荐人
     * @throws NoMatchingObjectsException
     * @throws AuthInfoOutOfDateException      认证码过期异常
     * @throws AuthInfoAlreadyActivedException
     */
    void registerByMobileNumber(User user, String authCode, String referrer) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    /**
     * 重置登录密码
     *
     * @param userId 用户id
     */
    void resetPassword(String userId);

    /**
     * 发送激活邮件
     *
     * @param userId
     * @throws UserNotFoundException
     */
    void sendActiveEmail(String userId, String authCode) throws UserNotFoundException;

    /**
     * 再次发送激活邮件 wangxiao 5-6
     */
    void sendActiveEmailAgain(User user);

    /**
     * 绑定邮箱发送邮件
     *
     * @param userId 用户编号
     * @param email  邮箱（当前邮箱、新邮箱）
     * @throws UserNotFoundException
     */
    void sendBindingEmail(String userId, String email) throws UserNotFoundException;

    /**
     * 给绑定手机发送认证码
     *
     * @param userId 用户编号
     * @param mobile 绑定手机号
     * @throws UserNotFoundException
     */
    void sendBindingMobileNumberSMS(String userId, String mobile) throws UserNotFoundException;
    
    /**
     * 
    * (修改登陆密码手机校验提交)
    * @Title: updatePasswordByMobileNumberSMS 
    * @param userId
    * @param mobileNumber
    * @throws AuthInfoAlreadyInColdException    设定文件 
    * @return void    返回类型
     */
    void updatePasswordByMobileNumberSMS(String userId, String mobileNumber) throws AuthInfoAlreadyInColdException;
    
    /**
     * 
    * (修改登陆密码手机校验提交)
    * @Title: updatePasswordByMobileNumberSubmit 
    * @param user
    * @param authCode
    * @throws NoMatchingObjectsException
    * @throws AuthInfoOutOfDateException
    * @throws AuthInfoAlreadyActivedException
    * @throws AuthInfoOutOfTimesException
    * @throws AuthCodeNotMatchException    设定文件 
    * @return void    返回类型
     */
    void updatePasswordByMobileNumberSubmit(User user, String authCode) throws NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    /**
     * 发送更换绑定邮箱的邮件
     *
     * @param userId
     * @param oriEmail 原来的邮箱
     * @throws UserNotFoundException
     * @throws AuthInfoAlreadyInColdException
     */
    void sendChangeBindingEmail(String userId, String oriEmail) throws UserNotFoundException, AuthInfoAlreadyInColdException;

    /**
     * 发送更换绑定手机号的短信
     *
     * @param userId
     * @param oriMobileNumber 原来的手机号
     * @throws UserNotFoundException
     * @throws AuthInfoAlreadyInColdException
     */
    void sendChangeBindingMobileNumberSMS(String userId, String oriMobileNumber) throws UserNotFoundException, AuthInfoAlreadyInColdException;

    /**
     * 找回密码发送邮件，发送激活码
     *
     * @param email 注册邮箱
     * @throws UserNotFoundException
     * @throws AuthInfoAlreadyInColdException
     */
    @Deprecated
    void sendFindLoginPasswordEmail(String email) throws UserNotFoundException, AuthInfoAlreadyInColdException;

    /**
     * 找回密码发送邮件，通过发送的连接重置密码
     *
     * @param email 注册邮箱
     * @throws UserNotFoundException
     * @throws AuthInfoAlreadyInColdException
     */
    void sendFindLoginPasswordLinkEmail(String email) throws UserNotFoundException, AuthInfoAlreadyInColdException;


    /**
     * 通过手机验证码找回密码发送短信
     * @param userId
     * @param mobileNumber
     */
    void sendFindLoginPasswordByMobileNumberSMS(String userId,String mobileNumber) throws AuthInfoAlreadyInColdException;



    /**
     * 发送“通过手机号注册”的认证短信
     *
     * @param mobileNumber
     * @throws AuthInfoAlreadyInColdException
     */
    void sendRegisterByMobileNumberSMS(String mobileNumber) throws AuthInfoAlreadyInColdException;

    /**
     * 验证找回密码链接中的activeCode
     *
     * @param activeCode
     * @return 需要修改密码的用户
     * @throws UserNotFoundException
     * @throws AuthInfoAlreadyActivedException
     */
    User verifyFindLoginPasswordActiveCode(String activeCode) throws UserNotFoundException, NoMatchingObjectsException, AuthInfoOutOfDateException, AuthInfoAlreadyActivedException, AuthInfoOutOfTimesException, AuthCodeNotMatchException;

    /**
     * 验证旧交易密码 正确返回true，错误返回false
     *
     * @param userId          用户id
     * @param oldcashPassword 旧交易密码
     * @return boolean
     * @throws UserNotFoundException
     */
    boolean verifyOldCashPassword(String userId, String oldcashPassword) throws UserNotFoundException;

    /**
     * 验证旧登录密码 正确返回true，错误返回false
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @return boolean
     * @throws UserNotFoundException
     */
    boolean verifyOldPassword(String userId, String oldPassword) throws UserNotFoundException;

    /**
     * 验证密码规则，如果不符合条件则抛出异常，如果符合条件则返回true
     *
     * @param password
     * @return
     * @throws NotConformRuleException 密码不符合格式则抛出此异常
     */
    boolean verifyPasswordRule(String password) throws NotConformRuleException;

    /**
     * 用户名规则验证，例如：不允许有中文、特殊符号，长度5-16
     *
     * @param username 用户名
     * @return boolean
     * @throws NotConformRuleException 用户名不符合规则
     */
    boolean verifyUsernameRule(String username) throws NotConformRuleException;

    /**
     * 保存用户
     *
     * @param user
     */
    void save(User user);

    User getUserByLoginId(String loginId);

    /**
     * 是否是系统账号
     * @param user
     * @return
     */
    boolean isSystem(User user);
}
