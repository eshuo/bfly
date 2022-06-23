/**   
 * @Title: UserAddressServiceImpl.java 
 * @Package info.bfly.crowd.user.service.impl 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月20日 上午11:16:16 
 * @version V1.0   
 */
package info.bfly.crowd.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.bfly.archer.user.model.Area;
import info.bfly.crowd.user.model.UserAddress;
import info.bfly.crowd.user.service.UserAddressService;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.statistics.controller.LoanStatistics;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月20日 上午11:16:16 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: UserAddressServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月20日 上午11:16:16
 * 
 */
@Service("userAddressService")
public class UserAddressServiceImpl implements UserAddressService {

    @Resource
    HibernateTemplate ht;
    @Resource
    UserAddressBO userAddressBo;
    @Resource
    private LoanStatistics loanStatistics;
    

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createUserAddress(UserAddress userAddress) {
        
        boolean isDefault = userAddress.getIsDefault();
        
        //如果是默认收货地址，则其他地址改成false
        if(isDefault){
            List<UserAddress> userAddressList = (List<UserAddress>) ht
                    .find("Select userAddress from UserAddress userAddress where userAddress.user.id = ?",
                            userAddress.getUser().getId());
            for(UserAddress address : userAddressList){
                address.setIsDefault(false);
                ht.update(address);
            }
        }
        
        userAddress.setId(userAddressBo.generateId());
        userAddress.setStatus(1);
        ht.save(userAddress);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void setDefaultAddress(String id, String userId) {
        List<UserAddress> addressList = (List<UserAddress>) ht.find(
                "Select u1 from UserAddress u1 where u1.user.id = ?", userId);
        for (UserAddress userAddress : addressList) {
            if (id.equals(userAddress.getId())) {
                userAddress.setIsDefault(true);
            } else {
                userAddress.setIsDefault(false);
            }
            ht.update(userAddress);
        }

    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateUserAddress(UserAddress userAddress) {
        ht.update(userAddress);
    }

    @Override
    public String changeToMobileLoanAddress(List<Area> loanAreaList) {
        String loanAddress = "";
        if (null == loanAreaList || loanAreaList.size() == 0)
            loanAddress = "\"/*/*/*\"";
        else {
            StringBuffer sb = new StringBuffer();
            String mobileStr = "";
            for (Area area : loanAreaList) {
                mobileStr = loanStatistics.getMobileAreaId(area.getId());
                String[] split = mobileStr.split("/");

                if (split.length == 3) {
                    mobileStr = mobileStr + "/*";

                } else if (split.length == 2) {
                    mobileStr = mobileStr + "/*/*";
                } else {

                }

                sb.append(",\"").append(mobileStr).append("\"");

            }
            loanAddress = sb.toString().substring(1);
        }

        return "\"loanAddresses\":[" + loanAddress + "]";
    }
    
    
    
    private  String returnAllAreaId(Area area){
        if(null ==area)
            return "";
        if (null == area.getParent()) {
            return area.getId();
        } else{
            return returnAllAreaId(area.getParent()) + area.getId();
        }
        
    }
    
    @Override
    public Boolean judgeIsReach(String userAddressId, String loanId) {
        UserAddress userAddress = ht.get(UserAddress.class, userAddressId);

        if (null == userAddress
                || StringUtils.isEmpty(userAddress.getAreas()[0]))
            return false;
        Loan loan = ht.get(Loan.class, loanId);
        if (null == loan)
            return false;

        List<Area> areaList = loan.getAreas();
        // 空值或者无数据则为全国范围
        if (null == areaList || areaList.size() == 0)
            return true;

        String userAreaId = userAddress.getAreas()[0];
        Area userArea = ht.get(Area.class, userAreaId);
        String userAreaStr = this.returnAllAreaId(userArea);

        String loanAreaStr = "";
        for (Area area : areaList) {
            loanAreaStr = this.returnAllAreaId(area);
            if (userAreaStr.startsWith(loanAreaStr))
                return true;
            else
                continue;
        }

        return false;
    }

    @Override
    public UserAddress searchUserDefaultAddress(String userId) {
         List<UserAddress> list = (List<UserAddress>) ht.find("Select u1 from UserAddress u1 where u1.user.id=? and u1.isDefault = true", userId);
         if(null != list && list.size() > 0)
             return list.get(0);
        return null;
    }


  
}
