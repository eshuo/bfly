package info.bfly.p2p.bankcard.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.bankcard.model.BankCard;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 银行卡查询
 */
@Component
@Scope(ScopeType.VIEW)
public class BankCardList extends EntityQuery<BankCard> implements Serializable {
    private static final long serialVersionUID = -1350682013319140386L;
    private List<BankCard> bankCardListbyLoginUser;
    @Resource
    private LoginUserInfo  loginUserInfo;

    public BankCardList() {
        final String[] RESTRICTIONS = { "bankCard.user.id like #{bankCardList.example.user.id}", "bankCard.status like #{bankCardList.example.status}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public List<BankCard> getBankCardListbyLoginUser() {
        if (bankCardListbyLoginUser == null) {
            User loginUser = getHt().get(User.class, loginUserInfo.getLoginUserId());
            if (loginUser == null) {
                FacesUtil.addErrorMessage("用户未登录");
                return null;
            }
            bankCardListbyLoginUser = (List<BankCard>) getHt().find("from BankCard where user.id = ?", loginUser.getId());
        }
        return bankCardListbyLoginUser;
    }

    @Override
    protected void initExample() {
        BankCard bc = new BankCard();
        bc.setUser(new User());
        setExample(bc);
    }
}
