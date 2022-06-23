package info.bfly.app.protocol.model.request;



/**
 * Created by Administrator on 2017/1/6 0006.
 */
public class UserAddressValue {

    private String detailAddress;
    
    private String consigneeName;
    
    private String consigneePhone;
    
    
    private String area;
    /**
     * 默认收货地址
     */
    private String isDefault;
    
    public String getDetailAddress() {
        return detailAddress;
    }
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
    public String getConsigneeName() {
        return consigneeName;
    }
    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }
    public String getConsigneePhone() {
        return consigneePhone;
    }
    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
    
    
    
}
