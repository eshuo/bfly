package info.bfly.p2p.message.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.message.model.UserMessageWay;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Scope(ScopeType.VIEW)
public class UserMessageWayList extends EntityQuery<UserMessageWay> implements Serializable {
    @Log
    private static Logger log;
    @Resource
    private        HibernateTemplate              ht;
}
