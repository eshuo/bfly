package info.bfly.p2p.message.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.message.MessageConstants;
import info.bfly.p2p.message.model.UserMessageNode;
import info.bfly.p2p.message.model.UserMessageTemplate;
import info.bfly.p2p.message.model.UserMessageWay;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class UserMessageTemplateList extends EntityQuery<UserMessageTemplate> implements Serializable {
    @Log
    private static Logger log;
    /**
     * 所有用户可设置的userMessageTempalte
     */
    private        List<UserMessageTemplate>      allSetableUmts;
    @Resource
    private        HibernateTemplate              ht;

    public UserMessageTemplateList() {
        final String[] RESTRICTIONS = { "id like #{userMessageTemplateList.example.id}", "userMessageWay.name like #{userMessageTemplateList.example.userMessageWay.name}",
                "userMessageNode.name like #{userMessageTemplateList.example.userMessageNode.name}", "name like #{userMessageTemplateList.example.name}",
                "description like #{userMessageTemplateList.example.description}" };
        ArrayList<String> a = new ArrayList(Arrays.asList(RESTRICTIONS));
        setRestrictionExpressionStrings(a);
    }

    public List<UserMessageTemplate> getAllSetableUmts() {
        if (allSetableUmts == null) {
            allSetableUmts = (List<UserMessageTemplate>) getHt().find("from UserMessageTemplate umt where umt.status='" + MessageConstants.UserMessageTemplateStatus.OPEN + "'");
        }
        return allSetableUmts;
    }

    @Override
    protected void initExample() {
        UserMessageTemplate umt = new UserMessageTemplate();
        umt.setUserMessageNode(new UserMessageNode());
        umt.setUserMessageWay(new UserMessageWay());
        setExample(umt);
    }

    public void setAllSetableUmts(List<UserMessageTemplate> allSetableUmts) {
        this.allSetableUmts = allSetableUmts;
    }
}
