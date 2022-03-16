package info.bfly.archer.message.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.message.model.InBox;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class InBoxList extends EntityQuery<InBox> implements Serializable {
    private String status;

    public InBoxList() {
        setHql("select ib from InBox ib");
        setCountHql("select count(ib) from InBox ib");
        final String[] RESTRICTIONS = {"ib.recevier.id = #{inBoxList.example.recevier.id}", "ib.status like #{inBoxList.example.status}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public String getStatus() {
        return status;
    }

    @Override
    protected void initExample() {
        InBox inBox = new InBox();
        inBox.setRecevier(new User());
        setExample(inBox);
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
