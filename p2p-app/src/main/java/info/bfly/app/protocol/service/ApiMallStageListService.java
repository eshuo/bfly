package info.bfly.app.protocol.service;

import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.mall.controller.MallStageList;
import info.bfly.p2p.loan.controller.LoanList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by XXSun on 2016/12/26.
 */
@Scope(ScopeType.REQUEST)
@Service
public class ApiMallStageListService extends MallStageList implements Serializable {
    private static final long serialVersionUID = -974404397035429685L;
    public ApiMallStageListService() {
        final String[] RESTRICTIONS = {" mallstage.id = #{apiMallStageListService.example.id}",
                " mallstage.mallStageCache.id = #{apiMallStageListService.example.mallStageCache.id}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
