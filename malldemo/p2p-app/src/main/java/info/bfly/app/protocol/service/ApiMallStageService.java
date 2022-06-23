package info.bfly.app.protocol.service;

import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.mall.controller.MallStageList;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope(ScopeType.REQUEST)
@Service
public class ApiMallStageService extends MallStageList implements Serializable  {

    private static final long serialVersionUID = -8603362678614051347L;

    public ApiMallStageService() {
        final String[] RESTRICTIONS = {};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
