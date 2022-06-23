/**   
* @Title: UserAddressService.java 
* @Package info.bfly.crowd.user.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月20日 上午11:14:57 
* @version V1.0   
*/package info.bfly.crowd.user.service;

import java.util.List;

import info.bfly.archer.user.model.Area;
import info.bfly.crowd.user.model.UserAddress;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月20日 上午11:14:57 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: UserAddressService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月20日 上午11:14:57 
 *  
 */
public interface UserAddressService {
    
    void createUserAddress(UserAddress userAddress);
    
    void setDefaultAddress(String id,String userId);
    
    void updateUserAddress(UserAddress userAddress);
    
    /**
     * 
    * @Title: changeToMobileLoanAddress 
    * @Description:  转换项目的配送地址为手机端
    * @param @param loanAreaList
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    String changeToMobileLoanAddress(List<Area> loanAreaList);
    /**
     * 
    * @Title: judgeIsReach 
    * @Description:  判断用户的收货地址是否可以配送
    * @param @param userId
    * @param @param loanId
    * @param @return    设定文件 
    * @return Boolean    返回类型 
    * @throws
     */
    Boolean judgeIsReach(String userAddressId,String loanId);
    /**
     * 查询用户的默认收货地址
     * @param userId
     * @return
     */
    UserAddress searchUserDefaultAddress(String userId);
    
}
