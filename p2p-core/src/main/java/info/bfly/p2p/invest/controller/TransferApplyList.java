package info.bfly.p2p.invest.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.model.TransferApply;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class TransferApplyList extends EntityQuery<TransferApply> implements Serializable {
    private Double minCorpus;
    private Double maxCorpus;
    private Double maxRate;
    private Double minRate;
    private Double maxPremium;
    private Double minPremium;

    public TransferApplyList() {
        final String[] RESTRICTIONS = {"transferApply.status like #{transferApplyList.example.status}", "transferApply.corpus >= #{transferApplyList.minCorpus}",
                "transferApply.corpus <= #{transferApplyList.maxCorpus}", "transferApply.invest.rate <= #{transferApplyList.maxRate}", "transferApply.invest.rate >= #{transferApplyList.minRate}",
                "transferApply.premium <= #{transferApplyList.maxPremium}", "transferApply.premium >= #{transferApplyList.minPremium}",
                "transferApply.invest.user.username like #{transferApplyList.example.invest.user.username}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public Double getMaxCorpus() {
        return maxCorpus;
    }

    public Double getMaxPremium() {
        return maxPremium;
    }

    public Double getMaxRate() {
        return maxRate;
    }

    public Double getMinCorpus() {
        return minCorpus;
    }

    public Double getMinPremium() {
        return minPremium;
    }

    public Double getMinRate() {
        return minRate;
    }

    @Override
    protected void initExample() {
        TransferApply ta = new TransferApply();
        Invest i = new Invest();
        i.setUser(new User());
        ta.setInvest(i);
        setExample(ta);
    }

    public void setMaxCorpus(Double maxCorpus) {
        this.maxCorpus = maxCorpus;
    }

    public void setMaxPremium(Double maxPremium) {
        this.maxPremium = maxPremium;
    }

    public void setMaxRate(Double maxRate) {
        this.maxRate = maxRate;
    }

    public void setMinAndMaxCorpus(double minCorpus, double maxCorpus) {
        this.minCorpus = minCorpus;
        this.maxCorpus = maxCorpus;
    }

    public void setMinAndMaxPremium(double minPremium, double maxPremium) {
        this.minPremium = minPremium;
        this.maxPremium = maxPremium;
    }

    public void setMinAndMaxRate(double minRate, double maxRate) {
        this.minRate = minRate;
        this.maxRate = maxRate;
    }

    public void setMinCorpus(Double minCorpus) {
        this.minCorpus = minCorpus;
    }

    public void setMinPremium(Double minPremium) {
        this.minPremium = minPremium;
    }

    public void setMinRate(Double minRate) {
        this.minRate = minRate;
    }
}
