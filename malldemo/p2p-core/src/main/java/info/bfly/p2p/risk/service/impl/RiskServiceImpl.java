package info.bfly.p2p.risk.service.impl;

import info.bfly.p2p.risk.model.RiskIndemnitication;
import info.bfly.p2p.risk.model.RiskPreparedness;
import info.bfly.p2p.risk.service.RiskService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service(value = "riskService")
@SuppressWarnings("unchecked")
public class RiskServiceImpl implements RiskService {
    @Resource
    private HibernateTemplate ht;

    @Override
    public double getELRPRateByRank(String riskRankId) {
        String hql = "select rp from ELRiskPreparedness rp where rp.riskRank.id=?";
        List<RiskPreparedness> ris = (List<RiskPreparedness>) ht.find(hql, riskRankId);
        if (ris.size() > 0) {
            return Double.parseDouble(ris.get(0).getRate());
        }
        return 0;
    }

    @Override
    public double getRIRateByRank(String riskRankId) {
        String hql = "select ri from RiskIndemnitication ri where ri.riskRank.id=?";
        List<RiskIndemnitication> ris = (List<RiskIndemnitication>) ht.find(hql, riskRankId);
        if (ris.size() > 0) {
            return Double.parseDouble(ris.get(0).getRate());
        }
        return 0;
    }

    @Override
    public double getRPRateByRank(String riskRankId) {
        String hql = "select rp from RiskPreparedness rp where rp.riskRank.id=?";
        List<RiskPreparedness> ris = (List<RiskPreparedness>) ht.find(hql, riskRankId);
        if (ris.size() > 0) {
            return Double.parseDouble(ris.get(0).getRate());
        }
        return 0;
    }
}
