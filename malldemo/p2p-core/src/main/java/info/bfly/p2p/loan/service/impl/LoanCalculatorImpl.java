package info.bfly.p2p.loan.service.impl;

import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.core.util.ArithUtil;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.loan.LoanConstants.LoanStatus;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.service.LoanCalculator;
import info.bfly.p2p.repay.RepayConstants.RepayType;
import info.bfly.p2p.repay.exception.IllegalLoanTypeException;
import info.bfly.p2p.repay.model.LoanRepay;
import info.bfly.p2p.repay.model.Repay;
import info.bfly.p2p.repay.service.RepayCalculator;
import info.bfly.p2p.repay.service.impl.NormalRepayCPMCalculator;
import info.bfly.p2p.repay.service.impl.NormalRepayRFCLCalculator;
import info.bfly.p2p.repay.service.impl.NormalRepayRLIOCalculator;
import info.bfly.p2p.risk.FeeConfigConstants.FeePoint;
import info.bfly.p2p.risk.FeeConfigConstants.FeeType;
import info.bfly.p2p.risk.model.RiskRank;
import info.bfly.p2p.risk.service.impl.FeeConfigBO;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("loanCalculator")
public class LoanCalculatorImpl implements LoanCalculator {
    @Resource
    HibernateTemplate ht;
    @Resource
    FeeConfigBO feeConfigBO;
    @Resource
    NormalRepayRFCLCalculator normalRepayRFCLCalculator;
    @Resource
    NormalRepayCPMCalculator normalRepayCPMCalculator;
    @Resource
    NormalRepayRLIOCalculator normalRepayRLIOCalculator;
    @Resource
    RepayCalculator repayCalculator;
    @Resource
    ConfigService configService;

    @Override
    public Double calculateAnticipatedInterest(Loan loan) {
        List<Repay> repays = calculateAnticipatedRepays(loan);
        if (repays == null) {
            return null;
        }
        double interest = 0D;
        for (Repay repay : repays) {
            interest = ArithUtil.add(interest, repay.getInterest());
        }
        return interest;
    }

    @Override
    public List<Repay> calculateAnticipatedRepays(Loan loan) {
        if (loan.getType() == null) {
            return null;
        }
        // 先付利息后还本金
        if (loan.getType().getRepayType().equals(RepayType.RFCL)) {
            // 如果是即投即生息，则为T+1计息，从计息日开始计息，所以investTime的值为（计息日-1天）
            return normalRepayRFCLCalculator.generateRepays(loan.getLoanMoney(), loan.getInterestBeginTime(), loan.getRate(), loan.getDeadline(), loan.getType().getRepayTimeUnit(), loan.getType()
                    .getRepayTimePeriod(), loan.getInterestBeginTime(), loan.getType().getInterestType(), loan.getType().getInterestPoint(), null);
        } else if (loan.getType().getRepayType().equals(RepayType.CPM)) {
            return normalRepayCPMCalculator.generateRepays(loan.getLoanMoney(), null, loan.getRate(), loan.getDeadline(), loan.getType().getRepayTimeUnit(), loan.getType().getRepayTimePeriod(), loan
                    .getInterestBeginTime(), loan.getType().getInterestType(), loan.getType().getInterestPoint(), null);
        } else if (loan.getType().getRepayType().equals(RepayType.RLIO)) {
            return normalRepayRLIOCalculator.generateRepays(loan.getLoanMoney(), null, loan.getRate(), loan.getDeadline(), loan.getType().getRepayTimeUnit(), loan.getType().getRepayTimePeriod(), loan
                    .getInterestBeginTime(), loan.getType().getInterestType(), loan.getType().getInterestPoint(), null);
        } else {
            throw new IllegalLoanTypeException("RepayType: " + loan.getType().getRepayType() + ". 不支持该还款类型。");
        }
    }

    @Override
    public Double calculateCashDeposit(double loanMoney) {
        return feeConfigBO.getFee(FeePoint.APPLY_LOAN, FeeType.CASH_DEPOSIT, null, null, loanMoney);
    }

    @Override
    public Double calculateLoanInterest(String loanId) {
        Loan loan = ht.get(Loan.class, loanId);
        // 如果借款已经有还款生成，则直接取还款数据
        List<LoanRepay> lrs = loan.getLoanRepays();
        Double sumInterest = 0D;
        if (lrs.size() > 0) {
            for (LoanRepay loanRepay : lrs) {
                sumInterest = ArithUtil.add(sumInterest, loanRepay.getInterest());
            }
        } else {
            // 计算借款的预计利息
            sumInterest = repayCalculator.calculateAnticipatedInterest(loan.getLoanMoney(), loanId);
        }
        return sumInterest;
    }

    @Override
    public Double calculateMoneyMaxInvested(String loanId) throws NoMatchingObjectsException {
        Loan loan = ht.get(Loan.class, loanId);
        if (loan == null) {
            throw new NoMatchingObjectsException(Loan.class, "cannot find loan by loanId:" + loanId);
        }
        Double needRaised = calculateMoneyNeedRaised(loanId);
        Double maxInvested = loan.getMaxInvestMoney();
        return needRaised < maxInvested ? needRaised : maxInvested;
    }

    @Override
    public Double calculateMoneyMinInvested(String loanId) throws NoMatchingObjectsException {
        Loan loan = ht.get(Loan.class, loanId);
        if (loan == null) {
            throw new NoMatchingObjectsException(Loan.class, "cannot find loan by loanId:" + loanId);
        }
        Double needRaised = calculateMoneyNeedRaised(loanId);
        Double maxInvested = loan.getMinInvestMoney();
        return needRaised < maxInvested ? needRaised : maxInvested;
    }

    @Override
    public Double calculateMoneyNeedRaised(String loanId) throws NoMatchingObjectsException {
        Loan loan = ht.get(Loan.class, loanId);
        if (loan == null) {
            throw new NoMatchingObjectsException(Loan.class, "cannot find loan by loanId:" + loanId);
        }
        // 统计所有的此借款的投资信息，求和做减法，得出尚未募集到的金额。
        // FIXME:记得，不用通过loan.invests取。为什么？
        List<Object> investMoney = (List<Object>) ht.find("select sum(invest.money) from Invest invest where invest.loan.id=? and invest.status !=? and invest.status !=?  ", loanId,
                InvestConstants.InvestStatus.CANCEL, InvestConstants.InvestStatus.REFUND);
        double sumMoney = investMoney.get(0) == null ? 0D : (Double) investMoney.get(0);
        double remain = ArithUtil.sub(loan.getLoanMoney(), sumMoney);
        return remain < 0 ? 0 : remain;
    }

    @Override
    public Double calculateRaiseCompletedRate(String loanId) throws NoMatchingObjectsException {
        double remainMoney = calculateMoneyNeedRaised(loanId);
        Loan loan = ht.get(Loan.class, loanId);


        Double initMoney = loan.getInitMoney();

        Double loanMoney = loan.getLoanMoney();
        if (initMoney ==null || initMoney <= 0) {
            initMoney = loanMoney;
        }
        return ArithUtil.round((loanMoney - remainMoney) / initMoney * 100, 2);
    }

    @Override
    public String calculateRemainTime(String loanId) throws NoMatchingObjectsException {
        Loan loan = ht.get(Loan.class, loanId);
        if (loan == null) {
            throw new NoMatchingObjectsException(Loan.class, "loanId:" + loanId);
        }
        if (loan.getExpectTime() == null) {
            return "未开始";
        }
        Long time = (loan.getExpectTime().getTime() - System.currentTimeMillis()) / 1000;
        if (time < 0 || !loan.getStatus().equals(LoanStatus.RAISING)) {
            return "已到期";
        }
        long days = time / 3600 / 24;
        long hours = (time / 3600) % 24;
        long minutes = (time / 60) % 60;
        if (minutes < 1) {
            minutes = 1L;
        }
        return days + "天" + hours + "时" + minutes + "分";
    }

    @Override
    public RiskRank calculateRiskRank(String loanId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long countSuccessInvest(String loanId) {
        Object o = ht.find("select count(im) from Invest im where im.loan.id = ? and  im.status in (?,?,?,?,?,?) and ( type != ? or type is null)  ", loanId, InvestConstants.InvestStatus.BID_SUCCESS,
                InvestConstants.InvestStatus.COMPLETE, InvestConstants.InvestStatus.OVERDUE, InvestConstants.InvestStatus.BAD_DEBT, InvestConstants.InvestStatus.REPAYING, InvestConstants.InvestStatus.BID_FROZEN,
                InvestConstants.InvestType.COMPANYINVEST).get(0);
        if (o == null) {
            return 0L;
        }
        return (Long) o;
    }

    @Override
    public Double calculateMoneyNeedMake(String loanId) throws NoMatchingObjectsException {
        return calculateMoney(loanId, InvestConstants.InvestStatus.BID_FROZEN);
    }

    @Override
    public Double calculateMoneyToBidSuccess(String loanId) throws NoMatchingObjectsException {

        Loan loan = ht.get(Loan.class, loanId);
        if (loan == null) {
            throw new NoMatchingObjectsException(Loan.class, "cannot find loan by loanId:" + loanId);
        }
        // FIXME:记得，不用通过loan.invests取。为什么？
        List<Object> investMoney = (List<Object>) ht.find("select sum(invest.money) from Invest invest where invest.loan.id=? and invest.status  in (?,?)", loanId,
                InvestConstants.InvestStatus.BID_SUCCESS,InvestConstants.InvestStatus.REPAYING);
        double sumMoney = investMoney.get(0) == null ? 0D : (Double) investMoney.get(0);
        double remain = ArithUtil.sub(loan.getLoanMoney(), sumMoney);
        return remain < 0 ? 0 : remain;


//        return calculateMoney(loanId, InvestConstants.InvestStatus.BID_SUCCESS);//REPAYING
    }


    private Double calculateMoney(String loanId, String status) throws NoMatchingObjectsException {
        Loan loan = ht.get(Loan.class, loanId);
        if (loan == null) {
            throw new NoMatchingObjectsException(Loan.class, "cannot find loan by loanId:" + loanId);
        }
        // FIXME:记得，不用通过loan.invests取。为什么？
        List<Object> investMoney = (List<Object>) ht.find("select sum(invest.money) from Invest invest where invest.loan.id=? and invest.status =? ", loanId,
                status);
        double sumMoney = investMoney.get(0) == null ? 0D : (Double) investMoney.get(0);
        double remain = ArithUtil.sub(loan.getLoanMoney(), sumMoney);
        return remain < 0 ? 0 : remain;
    }


}
