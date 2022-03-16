package info.bfly.app.protocol.service;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.p2p.loan.model.Recharge;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
@Service
public class ApiRechargeListService  extends EntityQuery<Recharge> {


    public ApiRechargeListService(){
        final String[] RESTRICTIONS = {"id like #{apiRechargeListService.example.id}",  "status = #{apiRechargeListService.example.status}",
                "rechargeWay like #{apiRechargeListService.example.rechargeWay}", "user.username like #{apiRechargeListService.example.user.username}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }





}
