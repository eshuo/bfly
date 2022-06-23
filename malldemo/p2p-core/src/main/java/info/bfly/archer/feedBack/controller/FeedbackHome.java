package info.bfly.archer.feedBack.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.feedBack.model.Feedback;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@Component
@Scope(ScopeType.VIEW)
public class FeedbackHome<E extends Feedback> extends EntityHome<Feedback> implements Serializable {
    private static final long serialVersionUID = -2287277265976930902L;


    @Override
    @Transactional(readOnly = false)
    public String save() {
        if (StringUtils.isEmpty(getInstance().getId())) {
            getInstance().setId(IdGenerator.randomUUID());
            getInstance().setStatus(getInstance().getStatus());
            getInstance().setContent(getInstance().getContent());
            getInstance().setEmail(getInstance().getEmail());
            getInstance().setMobileNumber(getInstance().getMobileNumber());
            getInstance().setRealname(getInstance().getRealname());
            getInstance().setTime(new Date());
        }
        return super.save();
    }
}
