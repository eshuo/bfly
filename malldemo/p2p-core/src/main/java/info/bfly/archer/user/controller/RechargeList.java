package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.RechargeBankCard;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.user.service.RechargeService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 充值查询
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class RechargeList extends EntityQuery<Recharge> implements java.io.Serializable {
    private static final long serialVersionUID = 9057256750216810237L;
    @Log
    static  Logger                 log;
    @Resource
    private RechargeService        rechargeService;
    private List<RechargeBankCard> rechargeBankCards;
    private Date                   startTime;
    private Date                   endTime;

    public RechargeList() {
        final String[] RESTRICTIONS = {"id like #{rechargeList.example.id}", "time >= #{rechargeList.startTime}", "time <= #{rechargeList.endTime}", "status = #{rechargeList.example.status}",
                "rechargeWay like #{rechargeList.example.rechargeWay}", "user.username like #{rechargeList.example.user.username}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        // addOrder("time", super.DIR_DESC);
    }

    public Date getEndTime() {
        return endTime;
    }

    public List<RechargeBankCard> getRechargeBankCards() {
        if (rechargeBankCards == null) {
            rechargeBankCards = rechargeService.getBankCardsList();
        }
        return rechargeBankCards;
    }

    // ~~~~~~~~~~~~~~~~
    public Date getStartTime() {
        return startTime;
    }

    @Override
    protected void initExample() {
        super.initExample();
        getExample().setUser(new User());
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
