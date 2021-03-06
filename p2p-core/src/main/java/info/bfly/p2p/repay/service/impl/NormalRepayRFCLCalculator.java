package info.bfly.p2p.repay.service.impl;

import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.DateUtil;
import info.bfly.p2p.repay.RepayConstants.InterestPoint;
import info.bfly.p2p.repay.RepayConstants.InterestType;
import info.bfly.p2p.repay.RepayConstants.RepayUnit;
import info.bfly.p2p.repay.exception.IllegalLoanTypeException;
import info.bfly.p2p.repay.model.Repay;
import info.bfly.p2p.repay.model.RepayCustomPeriod;
import info.bfly.p2p.repay.service.NormalRepayCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 先付利息后还本金，正常还款计算器
 *
 */
@Service("normalRepayRFCLCalculator")
public class NormalRepayRFCLCalculator implements NormalRepayCalculator {
    /**
     * 生成按天计息、按天s还款的投资还款数据
     *
     * @param investMoney        投资金额
     * @param investTime         投资时间
     * @param rate               年利率
     * @param deadline           总期数
     * @param repayTimePeriod    还款周期 （两天，三天之类）
     * @param interestBeginTime  开始计息时间
     * @param interestBeginPoint 计息节点（即投即生息，放款后生息 之类）
     */
    private List<Repay> generateDayDayRepays(double investMoney, Date investTime, double rate, Integer deadline, Integer repayTimePeriod, Date interestBeginTime, String interestBeginPoint) {
        List<Repay> repays = new ArrayList<Repay>();
        for (int i = 1; i <= deadline; i++) {
            Repay ir = new Repay();
            ir.setDefaultInterest(0D);
            // 只要不是最后一期，无需还本金
            if (i == deadline) {
                ir.setCorpus(investMoney);
            } else {
                ir.setCorpus(0D);
            }
            // interestBeginTime的第二天开始计息
            int interestDays = repayTimePeriod;
            if (i == 1 && interestBeginPoint.equals(InterestPoint.INTEREST_BEGIN_ON_INVEST)) {
                // 第一期，需处理 即投即生息 所产生的利息
                // 计息的天数，投资后第二天开始计息
                // FIXME:此处有bug,不能往前选超过一个还款周期,不然就会在第一个还款日以后,还有投资出现,就没法计算了.
                interestDays = interestDays - DateUtil.calculateIntervalDays(interestBeginTime, investTime) - 1;
            }
            // 金额*还款周期*还款周期单位*还款周期单位利率 = 利息
            ir.setInterest(ArithUtil.round(rate / 365 * interestDays * investMoney, 2));
            ir.setLength(repayTimePeriod);
            ir.setPeriod(i);
            // 计息日+第几期*还款周期单位=还款日
            ir.setRepayDay(DateUtil.addDay(interestBeginTime, i * repayTimePeriod));
            repays.add(ir);
        }
        return repays;
    }

    /**
     * 生成按天计息、按月s还款的投资还款数据
     *
     * @param investMoney        投资金额
     * @param investTime         投资时间
     * @param rate               年利率
     * @param deadline           总期数
     * @param repayTimePeriod    还款周期 （两月，三月之类）
     * @param interestBeginTime  开始计息时间
     * @param interestBeginPoint 计息节点（即投即生息，放款后生息 之类）
     */
    private List<Repay> generateDayMonthRepays(double investMoney, Date investTime, double rate, Integer deadline, Integer repayTimePeriod, Date interestBeginTime, String interestBeginPoint) {
        List<Repay> repays = new ArrayList<Repay>();
        for (int i = 1; i <= deadline; i++) {
            Repay ir = new Repay();
            ir.setDefaultInterest(0D);
            // 只要不是最后一期，无需还本金
            if (i == deadline) {
                ir.setCorpus(investMoney);
            } else {
                ir.setCorpus(0D);
            }
            // interestBeginTime的第二天开始计息
            int interestDays = DateUtil.getIntervalDays(DateUtil.addMonth(interestBeginTime, (i - 1) * repayTimePeriod), DateUtil.addMonth(interestBeginTime, i * repayTimePeriod));
            if (i == 1 && interestBeginPoint.equals(InterestPoint.INTEREST_BEGIN_ON_INVEST)) {
                // 第一期，需处理 即投即生息 所产生的利息
                // 计息的天数，投资后第二天开始计息
                // FIXME:此处有bug,不能往前选超过一个还款周期,不然就会在第一个还款日以后,还有投资出现,就没法计算了.
                interestDays = interestDays - DateUtil.calculateIntervalDays(interestBeginTime, investTime);
            }
            // 金额*还款周期*还款周期单位*还款周期单位利率 = 利息
            ir.setInterest(ArithUtil.round(rate / 365 * interestDays * investMoney, 2));
            ir.setLength(repayTimePeriod);
            ir.setPeriod(i);
            // 计息日+第几期*还款周期=还款日
            ir.setRepayDay(DateUtil.addMonth(interestBeginTime, i * repayTimePeriod));
            repays.add(ir);
        }
        return repays;
    }

    @Override
    public List<Repay> generateRepays(double investMoney, Date investTime, double rate, Integer deadline, String repayTimeUnit, Integer repayTimePeriod, Date interestBeginTime, String interestType,
                                      String interestBeginPoint, List<RepayCustomPeriod> customPeriods) {
        // TODO:自定义（不等额分期还款）尚未实现
        if (interestType.equals(InterestType.DAY)) {
            // 按天计息
            if (repayTimeUnit.equals(RepayUnit.DAY)) {
                // 按天s还款
                return generateDayDayRepays(investMoney, investTime, rate, deadline, repayTimePeriod, interestBeginTime, interestBeginPoint);
            } else if (repayTimeUnit.equals(RepayUnit.MONTH)) {
                // 按月s还款
                return generateDayMonthRepays(investMoney, investTime, rate, deadline, repayTimePeriod, interestBeginTime, interestBeginPoint);
            }
        } else if (interestType.equals(InterestType.MONTH)) {
            // 按月计息
            if (interestBeginPoint.equals(InterestPoint.INTEREST_BEGIN_ON_INVEST)) {
                // 不支持即投即生息
                throw new IllegalLoanTypeException("interestType: " + interestType + ", interestPoint:" + interestBeginPoint + ". 按月计息不支持即投即生息。");
            }
            if (repayTimeUnit.equals(RepayUnit.MONTH)) {
                // 按月s还款
                throw new IllegalLoanTypeException("interestType: " + interestType + ", repayTimeUnit:" + repayTimeUnit + ". 先付利息后还本金，不支持按月计息。");
            } else if (repayTimeUnit.equals(RepayUnit.DAY)) {
                // 按天s还款
                // 按月计息，按天还款，抛异常
                throw new IllegalLoanTypeException("interestType: " + interestType + ", repayTimeUnit:" + repayTimeUnit + ". 按月计息不支持按天还款。");
            }
        }
        throw new IllegalLoanTypeException("interestType: " + interestType + ", repayTimeUnit:" + repayTimeUnit + ". 不支持该借款类型。");
    }
}
