package info.bfly.p2p.invest.service.impl;

import info.bfly.p2p.invest.service.InvestCalculator;
import info.bfly.p2p.repay.service.RepayCalculator;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("investCalculator")
public class InvestCalculatorImpl implements InvestCalculator {
    @Resource
    RepayCalculator repayCalculator;

    @Override
    public Double calculateAnticipatedInterest(double investMoney, String loanId) {
        return repayCalculator.calculateAnticipatedInterest(investMoney, loanId);
    }
}
