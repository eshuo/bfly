package info.bfly.pay.p2p.user.controller;

import info.bfly.archer.user.controller.RechargeList;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.pay.controller.SinaOrderController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/3 0003.
 */
@Component
@Scope(ScopeType.VIEW)
public class SinaRechargeList extends RechargeList {

    @Resource
    SinaOrderController sinaOrderController;


    @Override
    public Class<Recharge> getEntityClass() {
        return Recharge.class;
    }
    public SinaRechargeList() {
        final String[] RESTRICTIONS = {"id like #{sinaRechargeList.example.id}", "time >= #{sinaRechargeList.startTime}", "time <= #{sinaRechargeList.endTime}", "status = #{sinaRechargeList.example.status}",
                "rechargeWay like #{sinaRechargeList.example.rechargeWay}", "user.username like #{sinaRechargeList.example.user.username}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
    /**
     * 查询充值
     *
     * @return
     */

    public void queryRecharge() {


    }


}
