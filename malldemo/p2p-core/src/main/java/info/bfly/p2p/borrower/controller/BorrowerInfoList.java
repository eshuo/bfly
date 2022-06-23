package info.bfly.p2p.borrower.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.borrower.model.BorrowerInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

@Component
@Scope(ScopeType.VIEW)
public class BorrowerInfoList extends EntityQuery<BorrowerInfo> implements Serializable {
    private static final long serialVersionUID = -4393546807304900224L;

    public BorrowerInfoList() {
        final String[] RESTRICTIONS = {"borrowerInfo.userId like #{borrowerInfoList.example.userId}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @Override
    protected void initExample() {
        BorrowerInfo li = new BorrowerInfo();
        li.setUser(new User());
        setExample(li);
    }
}
