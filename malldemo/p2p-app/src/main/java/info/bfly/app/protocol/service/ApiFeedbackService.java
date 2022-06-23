package info.bfly.app.protocol.service;

import info.bfly.archer.feedBack.controller.FeedbackHome;
import info.bfly.archer.feedBack.model.Feedback;
import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@Service
@Scope(ScopeType.REQUEST)
public class ApiFeedbackService extends FeedbackHome<Feedback> implements Serializable {
    private static final long serialVersionUID = -1744889849552957945L;

}
