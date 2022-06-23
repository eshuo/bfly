package info.bfly.app.protocol.service;

import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.bankcard.controller.BankCardList;
import info.bfly.p2p.bankcard.model.BankCard;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11 0011.
 */
@Service
@Scope(ScopeType.REQUEST)
public class ApiBankCardListService extends BankCardList {

    public ApiBankCardListService() {
        final String[] RESTRICTIONS = {"bankCard.user.id like #{apiBankCardListService.example.user.id}", "bankCard.status like #{apiBankCardListService.example.status}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }


    public List<BankCard> getBankCardByUser(User user) {
        if (user == null)
            return null;
        return (List<BankCard>) getHt().find("from BankCard where user.id = ?", user.getId());
    }

}
