package info.bfly.p2p.invest.service.impl;

import info.bfly.core.annotations.Log;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.model.InvestRefunds;
import info.bfly.p2p.invest.service.InvestRefundsService;
import info.bfly.p2p.invest.service.InvestService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5 0005.
 */
@Service("investRefundsService")
public class InvestRefundsServiceImpl implements InvestRefundsService {


    @Log
    private Logger log;


    @Resource
    HibernateTemplate ht;

    @Autowired
    IdGenerator idGenerator;

    @Autowired
    InvestService investService;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public InvestRefunds init(Invest invest) {
        if (invest == null) {
            return null;
        }
        InvestRefunds investRefunds = new InvestRefunds();

        investRefunds.setId(idGenerator.nextId(InvestRefunds.class));

        investRefunds.setStatus(InvestConstants.InvestRefundsStatus.REFUND_APPLY);

        investRefunds.setOperationOrderNo(invest.getOperationOrderNo());

        investRefunds.setInvest(invest);

        investRefunds.setRefundMoney(new BigDecimal(invest.getInvestMoney()));

        investRefunds.setUser(invest.getUser());

        invest.setStatus(InvestConstants.InvestStatus.APP_REFUND);

        ht.save(investRefunds);
        ht.update(invest);
        return investRefunds;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateOrSaveInvestRefunds(InvestRefunds investRefunds) {
        ht.saveOrUpdate(investRefunds);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void refundSuccess(String investRefundId) {

        InvestRefunds investRefunds = ht.get(InvestRefunds.class, investRefundId);

        if (investRefunds == null) {
            throw new RuntimeException("investRefundId ：" + investRefundId + " Is Null!");
        }

        try {
            //退款
            investService.investRefunds(investRefunds.getInvest().getId());
        } catch (Exception e) {
            log.error(investRefundId + "退款失败！" + e.getMessage());
            investRefunds.setStatus(InvestConstants.InvestRefundsStatus.REFUND_FAIL);
            updateOrSaveInvestRefunds(investRefunds);
            return;
        }


        investRefunds.setStatus(InvestConstants.InvestRefundsStatus.REFUND_SUCCESS);
        updateOrSaveInvestRefunds(investRefunds);
        log.info(investRefundId + "退款成功!");
    }

    @Override
    public InvestRefunds getInvestRefundsByOperationOrderNo(String operationOrderNo) {

        List<InvestRefunds> investRefundss = (List<InvestRefunds>) ht.find("from InvestRefunds ir where ir.operationOrderNo = ?", operationOrderNo);

        if (investRefundss.size() == 1)
            return investRefundss.get(0);

        return null;
    }
}
