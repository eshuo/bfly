package info.bfly.app.protocol.service;

import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.orders.controller.OrdersList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by XXSun on 2016/12/26.
 */
@Scope(ScopeType.REQUEST)
@Service
public class ApiOrderService extends OrdersList implements Serializable {


    /** 
    * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
    */ 
    private static final long serialVersionUID = 9195942581256311346L;

    public ApiOrderService() {
        final String[] RESTRICTIONS = {" order1.user.id = #{apiOrderService.example.user.id}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
