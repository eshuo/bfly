package info.bfly.p2p.bankcard.model;

// default package

import info.bfly.archer.user.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 银行卡信息
 */
@Entity
@Table(name = "bank_card")
@NamedQueries({@NamedQuery(name = "BankCard.getBankCardListByUserId", query = "from BankCard card where card.user.id = ?")})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BankCard implements Serializable {
    // Fields
    private static final long serialVersionUID = -4688314618074759547L;
    private String id;
    private User   user;
    // 开户行
    private String name;
    // 银行名称
    private String bank;
    /**
     * 银行账户服务类型，对公、对私
     */
    private String bankServiceType;
    // 银行编号
    private String bankNo;
    // 银行所在省份
    private String bankProvince;
    // 银行所在城市
    private String bankCity;
    // 账户类型
    private String bankCardType;
    // 开户行地址
    private String bankArea;
    // 银行卡账户名称
    private String accountName;
    // 银行卡号
    private String cardNo;
    // 绑定金额
    private Double bindingprice;
    //验证类型
    private String verifyMode;
    private Date   time;
    // 绑定状态
    private String status;
    // 绑定手机号
    private String bindPhone;
    //第三方卡号
    private String thirdNo;
    //是否是安全卡
    private String safeCard;

    // Constructors

    /**
     * default constructor
     */
    public BankCard() {
    }


    /**
     * full constructor
     */
    public BankCard(String id, User user, String name, String bank, String bankArea, String cardNo, Date time, String status) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.bank = bank;
        this.bankArea = bankArea;
        this.cardNo = cardNo;
        this.time = time;
        this.status = status;
    }

    public BankCard(String id, User user, String name, String bank, String bankServiceType, String bankNo, String bankProvince, String bankCity, String bankCardType, String bankArea, String accountName, String cardNo, Double bindingprice, String verifyMode, Date time, String status, String bindPhone, String thirdNo,String safeCard) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.bank = bank;
        this.bankServiceType = bankServiceType;
        this.bankNo = bankNo;
        this.bankProvince = bankProvince;
        this.bankCity = bankCity;
        this.bankCardType = bankCardType;
        this.bankArea = bankArea;
        this.accountName = accountName;
        this.cardNo = cardNo;
        this.bindingprice = bindingprice;
        this.verifyMode = verifyMode;
        this.time = time;
        this.status = status;
        this.bindPhone = bindPhone;
        this.thirdNo = thirdNo;
        this.safeCard = safeCard;
    }

    @Column(name = "account_name", length = 200)
    public String getAccountName() {
        return accountName;
    }

    @Column(name = "bank", length = 100)
    public String getBank() {
        return bank;
    }

    @Column(name = "bank_area", length = 512)
    public String getBankArea() {
        return bankArea;
    }

    @Column(name = "bank_card_type", length = 100)
    public String getBankCardType() {
        return bankCardType;
    }

    @Column(name = "bank_city", length = 100)
    public String getBankCity() {
        return bankCity;
    }

    @Column(name = "bank_no", length = 128)
    public String getBankNo() {
        return bankNo;
    }

    @Column(name = "bank_province", length = 100)
    public String getBankProvince() {
        return bankProvince;
    }

    @Column(name = "bank_service_type", length = 100)
    public String getBankServiceType() {
        return bankServiceType;
    }

    @Column(name = "binding_price")
    public Double getBindingprice() {
        return bindingprice;
    }

    @Column(name = "verify_mode")
    public String getVerifyMode() {
        return verifyMode;
    }

    @Column(name = "card_no", length = 100)
    public String getCardNo() {
        return cardNo;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return status;
    }

    @Column(name = "time", nullable = false, length = 19)
    public Date getTime() {
        return time;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }


    @Column(name = "bind_phone")
    public String getBindPhone() {
        return bindPhone;
    }

    @Column(name = "third_no")
    public String getThirdNo() {
        return thirdNo;
    }

    @Column(name= "safe_card")
    public String getSafeCard() {
        return safeCard;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setBankArea(String bankArea) {
        this.bankArea = bankArea;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public void setBankServiceType(String bankServiceType) {
        this.bankServiceType = bankServiceType;
    }

    public void setBindingprice(Double bindingprice) {
        this.bindingprice = bindingprice;
    }

    public void setVerifyMode(String verifyMode) {
        this.verifyMode = verifyMode;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setUser(User user) {
        this.user = user;
    }




    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public void setThirdNo(String thirdNo) {
        this.thirdNo = thirdNo;
    }

    public void setSafeCard(String safeCard) {
        this.safeCard = safeCard;
    }


}
