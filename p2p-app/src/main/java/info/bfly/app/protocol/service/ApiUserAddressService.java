package info.bfly.app.protocol.service;

import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.user.controller.UserAddressList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by XXSun on 2016/12/26.
 */
@Scope(ScopeType.REQUEST)
@Service
public class ApiUserAddressService extends UserAddressList implements Serializable {
    /** 
    * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
    */ 
    private static final long serialVersionUID = 3910725977822129583L;

    public ApiUserAddressService() {
        
        final String[] RESTRICTIONS = {" userAddress.user.id = #{apiUserAddressService.example.user.id}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
