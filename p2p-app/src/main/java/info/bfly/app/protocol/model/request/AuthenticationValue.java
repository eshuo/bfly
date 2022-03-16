package info.bfly.app.protocol.model.request;

/**
 * Created by Administrator on 2017/1/20 0020.
 */
public class AuthenticationValue {

    //真实姓名
    private String userName;
    //身份证
    private String idCard;
    //性别
    private String sex;
    //出生日期
    private String brithDate;
    //居住地址
    private String address;
    //详细地址
    private String detailedAddress;
    //身份证照片地址
    private String idPhotoUrl;
    //身份证复印件照片地址
    private String idCopyPhotoUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBrithDate() {
        return brithDate;
    }

    public void setBrithDate(String brithDate) {
        this.brithDate = brithDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getIdPhotoUrl() {
        return idPhotoUrl;
    }

    public void setIdPhotoUrl(String idPhotoUrl) {
        this.idPhotoUrl = idPhotoUrl;
    }

    public String getIdCopyPhotoUrl() {
        return idCopyPhotoUrl;
    }

    public void setIdCopyPhotoUrl(String idCopyPhotoUrl) {
        this.idCopyPhotoUrl = idCopyPhotoUrl;
    }
}
