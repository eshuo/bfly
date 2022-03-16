package info.bfly.archer.user.model;

import info.bfly.crowd.user.model.UserAddress;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User entity. 用户，包括系统管理员；暂定username和id为相同值
 */
@Entity
@Table(name = "user")
@NamedQueries({@NamedQuery(name = "User.findUserByUsername", query = "from User u where u.username = ?"), @NamedQuery(name = "User.findUserByEmail", query = "from User u where u.email = ?"),
        @NamedQuery(name = "User.findUserbymobileNumber", query = "from User u where u.mobileNumber = ?")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class User implements Serializable {
    // Fields
    private static final long serialVersionUID = 1561246673236774897L;
    private String id;
    /*** 居住地 */
    private Area   area;
    private String username;
    /**
     * 照片
     */
    private String photo;
    private String email;
    private String password;
    private String status;
    private String realname;
    private String sex;
    private String cashPassword;
    private Date   birthday;
    private String homeAddress;
    private String qq;
    private String msn;
    private String mobileNumber;
    private String idCard;
    private String currentAddress;
    private String nickname;
    private String securityQuestion1;
    private String securityQuestion2;
    private String securityAnswer1;
    private String securityAnswer2;
    private Date   lastLoginTime;
    private Date   registerTime;
    private String comment;
    private String userType = "2";
    private List<Role> roles = new ArrayList<Role>(0);
    private List<UserAddress> userAddresses = new ArrayList<>(0);
    
    
    private Date         disableTime;
    /**
     * 用户等级
     */
    private LevelForUser level;
    /**
     * 推荐人
     */
    private String       referrer;

    // Constructors

    /**
     * default constructor
     */
    public User() {
    }

    public User(String userId) {
        id = userId;
    }

    /**
     * minimal constructor
     */
    public User(String id, String username, String password, Date registerTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.registerTime = registerTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area")
    public Area getArea() {
        return area;
    }

    @Column(name = "birthday", length = 10)
    public Date getBirthday() {
        return birthday;
    }

    @Column(name = "cash_password", length = 50)
    public String getCashPassword() {
        return cashPassword;
    }

    @Lob
    @Column(name = "comment", columnDefinition = "CLOB")
    public String getComment() {
        return comment;
    }

    @Column(name = "current_address", length = 100)
    public String getCurrentAddress() {
        return currentAddress;
    }

    @Column(name = "disable_time")
    public Date getDisableTime() {
        return disableTime;
    }

    @Column(name = "email", length = 100)
    public String getEmail() {
        return email;
    }

    @Column(name = "home_address", length = 100)
    public String getHomeAddress() {
        return homeAddress;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "id_card", length = 20)
    public String getIdCard() {
        return idCard;
    }

    @Column(name = "last_login_time", length = 19)
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level")
    public LevelForUser getLevel() {
        return level;
    }

    @Column(name = "mobile_number", length = 18)
    public String getMobileNumber() {
        return mobileNumber;
    }

    @Column(name = "MSN", length = 100)
    public String getMsn() {
        return msn;
    }

    @Column(name = "nickname", length = 50)
    public String getNickname() {
        return nickname;
    }

    @Column(name = "password", nullable = false, length = 100)
    public String getPassword() {
        return password;
    }

    @Column(name = "photo", length = 500)
    public String getPhoto() {
        return photo;
    }

    @Column(name = "QQ", length = 12)
    public String getQq() {
        return qq;
    }

    @Column(name = "realname", length = 10)
    public String getRealname() {
        return realname;
    }

    @Column(name = "referrer", length = 1000)
    public String getReferrer() {
        return referrer;
    }

    @Column(name = "register_time", nullable = false, length = 19)
    public Date getRegisterTime() {
        return registerTime;
    }

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false, updatable = false)})
    public List<Role> getRoles() {
        return roles;
    }
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	public List<UserAddress> getUserAddresses() {
		return userAddresses;
	}
    
    @Column(name = "security_answer1", length = 100)
    public String getSecurityAnswer1() {
        return securityAnswer1;
    }

    @Column(name = "security_answer2", length = 100)
    public String getSecurityAnswer2() {
        return securityAnswer2;
    }

    @Column(name = "security_question1", length = 100)
    public String getSecurityQuestion1() {
        return securityQuestion1;
    }

    @Column(name = "security_question2", length = 100)
    public String getSecurityQuestion2() {
        return securityQuestion2;
    }

    @Column(name = "sex", length = 2)
    public String getSex() {
        return sex;
    }

    @Column(name = "user_type",length = 1)
    public String getUserType() {
        return userType;
    }

    @Column(name = "status", nullable = false)
    public String getStatus() {
        return status;
    }

    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setCashPassword(String cashPassword) {
        this.cashPassword = cashPassword;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setLevel(LevelForUser level) {
        this.level = level;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setSecurityAnswer1(String securityAnswer1) {
        this.securityAnswer1 = securityAnswer1;
    }

    public void setSecurityAnswer2(String securityAnswer2) {
        this.securityAnswer2 = securityAnswer2;
    }

    public void setSecurityQuestion1(String securityQuestion1) {
        this.securityQuestion1 = securityQuestion1;
    }

    public void setSecurityQuestion2(String securityQuestion2) {
        this.securityQuestion2 = securityQuestion2;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

	public void setUserAddresses(List<UserAddress> userAddresses) {
		this.userAddresses = userAddresses;
	}
    

}
