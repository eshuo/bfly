/**   
 * @Title: UserAddressList.java 
 * @Package info.bfly.crowd.user.controller 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月20日 下午2:51:43 
 * @version V1.0   
 */
package info.bfly.crowd.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.AreaService;
import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.user.model.UserAddress;
import info.bfly.crowd.user.service.UserAddressService;
import info.bfly.p2p.statistics.controller.LoanStatistics;

import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月20日 下午2:51:43 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: UserAddressList
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月20日 下午2:51:43
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class UserAddressList extends EntityQuery<UserAddress> implements
        Serializable {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = -6132282020800729975L;

    private static final String countHql = "Select count(userAddress) from UserAddress userAddress";

    private static final String hql = "Select  userAddress from UserAddress userAddress";
    
    private String userId;
    
    @Resource
    private LoanStatistics loanStatistics;
    
    @Resource
    private AreaService areaService;
    @Resource
    private LoginUserInfo loginUserInfo;
    @Resource
    private UserAddressService userAddressService;

    public UserAddressList() {
        this.setCountHql(countHql);
        this.setHql(hql);
        final String[] RESTRICTIONS = {"userAddress.user.id = #{userAddressList.userId}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
    
    @Override
    protected void initExample() {
        UserAddress example = new UserAddress();
        example.setUser(new User());
        setExample(example);
    }
    
    
    public String setDefaultAddress(String id){
        userAddressService.setDefaultAddress(id,loginUserInfo.getLoginUserId());
        this.setLazyModel(null);
        return "pretty:userCenter";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
