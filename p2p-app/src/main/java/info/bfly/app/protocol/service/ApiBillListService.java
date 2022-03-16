package info.bfly.app.protocol.service;

import info.bfly.archer.user.controller.UserBillList;
import info.bfly.archer.user.model.UserBill;
import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/1/10 0010.
 */
@Scope(ScopeType.REQUEST)
@Service
public class ApiBillListService extends UserBillList<UserBill> {

    public ApiBillListService() {
        final String[] RESTRICTIONS = {"id like #{apiBillListService.example.id}", "user.id like #{apiBillListService.example.user.id}", "type like #{apiBillListService.example.type}",
                "typeInfo like #{apiBillListService.example.typeInfo}", "time >= #{apiBillListService.startTime}", "time <= #{apiBillListService.endTime}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

}
