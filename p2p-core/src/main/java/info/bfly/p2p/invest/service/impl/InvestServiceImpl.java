package info.bfly.p2p.invest.service.impl;

import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.user.UserBillConstants.OperatorInfo;
import info.bfly.archer.user.service.UserBillService;
import info.bfly.archer.user.service.impl.UserBillBO;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.coupon.CouponConstants;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.coupon.service.UserCouponService;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.exception.ExceedMaxAcceptableRate;
import info.bfly.p2p.invest.exception.ExceedMoneyNeedRaised;
import info.bfly.p2p.invest.exception.IllegalLoanStatusException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.LoanConstants.LoanStatus;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.service.LoanCalculator;
import info.bfly.p2p.loan.service.LoanService;
import info.bfly.p2p.repay.service.RepayService;
import info.bfly.p2p.risk.service.SystemBillService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Description:
 */
@Service("investService")
public class InvestServiceImpl implements InvestService {
    @Resource
    HibernateTemplate ht;
    @Resource
    UserBillService   ubs;
    @Resource
    ConfigService     cs;
    @Resource
    LoanService       loanService;
    @Resource
    LoanCalculator    loanCalculator;
    @Resource
    RepayService      repayService;
    @Resource
    UserBillBO        userBillBO;

    @Autowired
    SystemBillService systemBillService;
    @Resource
    UserCouponService userCouponService;
    @Autowired
    IdGenerator       idGenerator;

    @Log
    Logger log;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Invest create(Invest invest) throws InsufficientBalance, ExceedMoneyNeedRaised, ExceedMaxAcceptableRate, ExceedDeadlineException, UnreachedMoneyLimitException, IllegalLoanStatusException {
        String loanId = invest.getLoan().getId();
        invest.setInvestMoney(invest.getMoney());
        // ??????????????????
        Loan loan = ht.get(Loan.class, loanId);
        ht.evict(loan);
        loan = ht.get(Loan.class, loanId, LockMode.PESSIMISTIC_WRITE);
        if (!loan.getStatus().equals(LoanStatus.RAISING)) {
            throw new IllegalLoanStatusException(loan.getStatus());
        }

        // ????????????????????????????????????????????????????????????????????????????????????????????????
        double remainMoney;
        try {
            remainMoney = loanCalculator.calculateMoneyNeedRaised(loan.getId());
        } catch (NoMatchingObjectsException e) {
            throw new RuntimeException(e);
        }

        //TODO  ???????????????????????????
        if (invest.getMoney() > remainMoney) {
            throw new ExceedMoneyNeedRaised();
        }
        // ???????????????????????????????????????????????????
        if (invest.getUserCoupon() != null) {
            if (!invest.getUserCoupon().getStatus().equals(CouponConstants.UserCouponStatus.UNUSED)) {
                throw new ExceedDeadlineException();
            }
            // ???????????????????????????????????????
            if (invest.getMoney() < invest.getUserCoupon().getCoupon().getLowerLimitMoney()) {
                throw new UnreachedMoneyLimitException();
            }
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // investMoney>???????????????+??????
            if (invest.getMoney() > invest.getUserCoupon().getCoupon().getMoney() + ubs.getBalance(invest.getUser().getId())) {
                throw new InsufficientBalance();
            }
        } else if (invest.getMoney() > ubs.getBalance(invest.getUser().getId())) {
            throw new InsufficientBalance();
        }
        invest.setStatus(InvestConstants.InvestStatus.WAIT_AFFIRM);
        invest.setRate(loan.getRate());
        invest.setTime(new Date());
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ????????????invest??????????????????????????????????????????==??????????????????????????????loan?????????????????????
        invest.setId(generateId(invest.getLoan().getId()));
        if (invest.getTransferApply() == null || StringUtils.isEmpty(invest.getTransferApply().getId())) {
            invest.setTransferApply(null);
        }
        if (invest.getUserCoupon() != null) {
            // ???????????????????????????????????????
            userCouponService.userUserCoupon(invest.getUserCoupon().getId(), invest.getId());
            double realMoney = ArithUtil.sub(invest.getMoney(), invest.getUserCoupon().getCoupon().getMoney());
            if (realMoney < 0) {
                realMoney = 0;
            }
            invest.setInvestMoney(realMoney);
        }

        //??????????????????
        //ubs.freezeMoney(invest.getUser().getId(), invest.getInvestMoney(), OperatorInfo.INVEST_SUCCESS, "????????????????????????????????????ID:" + invest.getLoan().getId() + "  ??????id:" + invest.getId());
        ht.save(invest);
        return invest;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void delete(Invest invest) {
        ht.delete(invest);
    }

    @Override
    public Invest get(String investId) {
        return ht.get(Invest.class, investId);
    }

    @Override
    public Invest get(String investId, LockMode lockMode) {
        return ht.get(Invest.class, lockMode);
    }

    @Override
    @Transactional
    public void update(Invest invest) {
        ht.update(invest);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Invest frozen(Invest invest) {
        invest = ht.merge(invest);
        if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM)) {
            invest.setStatus(InvestConstants.InvestStatus.BID_FROZEN);
            ht.update(invest);
        }
        return invest;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Invest success(Invest invest) throws InsufficientBalance {
        invest = ht.merge(invest);
        if (InvestConstants.InvestStatus.WAIT_LOANING_VERIFY.equals(invest.getStatus()) || InvestConstants.InvestStatus.WAIT_AFFIRM.equals(invest.getStatus())) {
            double realMoney = invest.getMoney();
            if (invest.getUserCoupon() != null) {
                realMoney = ArithUtil.sub(invest.getMoney(), invest.getUserCoupon().getCoupon().getMoney());
                if (realMoney < 0) {
                    realMoney = 0;
                }
            }
            if (invest.getPayMethod().startsWith("balance")){
                invest.setAccountType(StringUtils.defaultIfEmpty(invest.getPayMethod().substring(invest.getPayMethod().lastIndexOf("^")+1),"BASIC"));
                ubs.transferOutFromBalance(invest.getUser().getId(), realMoney, OperatorInfo.INVEST_SUCCESS, "?????????????????????????????????, ??????ID???" + invest.getLoan().getId(), invest.getAccountType());
            }
            // ??????????????????
            systemBillService.transferInto(realMoney, OperatorInfo.INVEST_SUCCESS, "?????????????????????????????????, ??????ID???" + invest.getLoan().getId());

            invest.setStatus(InvestConstants.InvestStatus.BID_SUCCESS);
            ht.update(invest);
            if (invest.getInvestId() != null) {
                Invest no = ht.get(Invest.class, invest.getInvestId());
                no.setStatus(InvestConstants.InvestStatus.BID_SUCCESS);
                ht.saveOrUpdate(no);
            }
        }
        return invest;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Invest fail(Invest invest) {
        if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM)) {

            //???????????????

            if (invest.getUserCoupon() != null) {
                userCouponService.restoreUserCoupon(invest.getUserCoupon().getId());
            }

            //todo ????????????????????????

            //unfreezeMoney

            try {
                userBillBO.unfreezeMoney(invest.getUser().getId(), invest.getInvestMoney(), OperatorInfo.CANCEL_INVEST, "????????????");
            } catch (InsufficientBalance insufficientBalance) {
                //????????????
            }


            invest.setStatus(InvestConstants.InvestStatus.CANCEL);
            ht.update(invest);
            // ????????????????????????????????????????????????????????????????????????
            if (invest.getLoan().getStatus()
                    .equals(LoanConstants.LoanStatus.RECHECK)) {
                invest.getLoan().setStatus(LoanConstants.LoanStatus.RAISING);
                ht.update(invest.getLoan());
            }

        }
        return invest;
    }

    @Override
    public String generateId(String loanId) {
        return idGenerator.nextId(Invest.class);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void cancelOrder(String investId) {
        Invest invest = ht.get(Invest.class, investId);
        Loan loan = ht.get(Loan.class, invest.getLoan().getId(), LockMode.PESSIMISTIC_WRITE);
        if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_CANCEL_AFFIRM)) {
            //?????????
            invest.setStatus(InvestConstants.InvestStatus.REFUND);
            ht.update(invest);
            //????????????
            try {
                userBillBO.unfreezeMoney(invest.getUser().getId(), invest.getInvestMoney(), OperatorInfo.REFUND, "??????",invest.getAccountType());
            } catch (InsufficientBalance insufficientBalance) {
                log.error(invest.getUser().getId() + "??????????????????" + invest.getInvestMoney() + "?????????????????????:" + insufficientBalance.getMessage());
            }
            if (loan.getStatus()
                    .equals(LoanConstants.LoanStatus.RECHECK)) {            // ????????????????????????????????????????????????????????????????????????
                loan.setStatus(LoanConstants.LoanStatus.RAISING);
                ht.update(loan);
            }
            log.info(investId + "??????????????????");
        }
    }

    @Override
    public void investRefunds(String investId) {

        Invest invest = ht.get(Invest.class, investId);
        Loan loan = ht.get(Loan.class, invest.getLoan().getId(), LockMode.PESSIMISTIC_WRITE);
        if (invest.getStatus().equals(InvestConstants.InvestStatus.APP_REFUND)) {
            invest.setStatus(InvestConstants.InvestStatus.REFUND);
            //????????????
            userBillBO.transferIntoBalance(invest.getUser().getId(), invest.getInvestMoney(), OperatorInfo.REFUND, loan.getName() + "??????????????????");
        }
        ht.update(invest);
        log.info(investId + "??????????????????");

    }


    @Override
    public void transferOutFromFrozen(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance {
        userBillBO.transferOutFromFrozen(userId, money, operatorInfo, operatorDetail);
    }


}
