/**   
* @Title: UserAddressHome.java 
* @Package info.bfly.crowd.user.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月20日 上午10:45:21 
* @version V1.0   
*/package info.bfly.crowd.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.Area;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.AreaService;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.crowd.user.UserAddressConstants;
import info.bfly.crowd.user.model.UserAddress;
import info.bfly.crowd.user.service.UserAddressService;

import java.io.Serializable;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月20日 上午10:45:21 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: UserAddressHome 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月20日 上午10:45:21 
 *  
 */
@Component
@Scope(ScopeType.VIEW)
public class UserAddressHome extends EntityHome<UserAddress> implements
        Serializable {

    /** 
    * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
    */ 
    private static final long serialVersionUID = 8595840576805321725L;
    
    
    @Resource
    private LoginUserInfo   loginUserInfo;
    @Resource
    private UserAddressService userAddressService;
    @Resource
    private UserService userService;
    @Resource
    private AreaService areaService;
    
    private Area area;
    
    
    
    public UserAddressHome(){
        this.setDeleteView(UserAddressConstants.View.USER_ADDRESS_LIST);
        this.setUpdateView(UserAddressConstants.View.USER_ADDRESS_LIST);
    }
    
    
    public String createUserAddress(){
        User user =  userService.getUserById(loginUserInfo.getLoginUserId());
        this.getInstance().setUser(user);
        this.getInstance().setAreas(new String[]{area.getId()});
        userAddressService.createUserAddress(this.getInstance());
        return "pretty:userAddressList";
    }
    
    public String updateUserAddress(){
        this.getInstance().setAreas(new String[]{area.getId()});
        userAddressService.updateUserAddress(this.getInstance());
        if(this.getInstance().getIsDefault())
            userAddressService.setDefaultAddress(this.getId(), loginUserInfo.getLoginUserId());
        return "pretty:userAddressList";
    }
    
    public String setDefaultAddress(String id){
        userAddressService.setDefaultAddress(id, loginUserInfo.getLoginUserId());
        return "pretty:userAddressList";
    }
    
    public void deleteUserAddress(String userAddressId) {
        UserAddress address = getBaseService().get(UserAddress.class,
                userAddressId);
        //设置为不可用
        address.setStatus(0);
        userAddressService.updateUserAddress(address);
        FacesUtil.addInfoMessage("删除成功");
    }
    public Area getArea() {
        if(null != this.getInstance().getAreas() && null ==area){
            return  areaService.getAreaById(this.getInstance().getAreas()[0]);
        }
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
    
   
    
}
