package info.bfly.p2p.risk.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.risk.model.RiskRank;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class RiskRankList extends EntityQuery<RiskRank> implements Serializable {
    private static final long serialVersionUID = -4630618354626224515L;

    public RiskRankList() {
        final String[] RESTRICTIONS = {"id like #{riskRankList.example.id}", "rank like #{riskRankList.example.rank}",};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @Override
    public List<RiskRank> getAllResultList() {
        if (allResultList == null) {
            allResultList = super.getAllResultList();
            Collections.sort(allResultList, new Comparator<RiskRank>() {
                @Override
                public int compare(RiskRank o1, RiskRank o2) {
                    return o1.getScore() - o2.getScore();
                }
            });
        }
        return allResultList;
    }
}
