package info.bfly.p2p.user.service.impl;

import info.bfly.archer.user.UserBillConstants.OperatorInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.UserConstants.WithdrawStatus;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserBill;
import info.bfly.archer.user.service.impl.UserBillBO;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.WithdrawCash;
import info.bfly.p2p.risk.FeeConfigConstants.FeePoint;
import info.bfly.p2p.risk.FeeConfigConstants.FeeType;
import info.bfly.p2p.risk.service.SystemBillService;
import info.bfly.p2p.risk.service.impl.FeeConfigBO;
import info.bfly.p2p.user.service.WithdrawCashService;
import org.apache.commons.lang.StringUtils;
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
@Service(value = "withdrawCashService")
public class WithdrawCashServiceImpl implements WithdrawCashService {
    @Log
    private Logger log;
    @Resource
    private
    HibernateTemplate ht;
    @Resource
    private FeeConfigBO feeConfigBO;
    @Resource
    UserBillBO        userBillBO;
    @Resource
    SystemBillService sbs;

    @Autowired
    IdGenerator idGenerator;


    @Override
    public WithdrawCash getWithdrawById(String withdrawId) {
        return ht.get(WithdrawCash.class, withdrawId);
    }


    @Override
    public WithdrawCash generateWithdraw(Double money, User user) {
        return generateWithdraw(money, "BASIC", user);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public WithdrawCash generateWithdraw(Double money, String accountType, User user) {

        WithdrawCash withdrawCash = new WithdrawCash();
        withdrawCash.setAccountType(accountType);
        withdrawCash.setId(generateId());
        withdrawCash.setMoney(money);
        withdrawCash.setUser(user);
        withdrawCash.setFee(calculateFee(money));
        withdrawCash.setTime(new Date());
        withdrawCash.setCashFine(0.0);
        withdrawCash.setStatus(UserConstants.WithdrawStatus.INIT);//初始化
        ht.save(withdrawCash);

        return withdrawCash;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void update(WithdrawCash withdrawCash) {
        ht.update(withdrawCash);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public WithdrawCash merge(WithdrawCash withdrawCash) {
        ht.evict(withdrawCash);
        return ht.merge(withdrawCash);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void applyWithdrawCash(WithdrawCash withdraw) throws InsufficientBalance {

        // FIXME:缺验证
        if (StringUtils.isEmpty(withdraw.getId()))
            withdraw.setId(generateId());
        withdraw.setFee(calculateFee(withdraw.getMoney()));
        withdraw.setCashFine(0D);
        withdraw.setTime(new Date());
        //TODO 提交新浪冻结申请
        userBillBO.freezeMoney(withdraw.getUser().getId(), withdraw.getMoney(), OperatorInfo.APPLY_WITHDRAW, "申请提现，冻结提现金额, 提现编号:" + withdraw.getId(), withdraw.getAccountType());
        userBillBO.freezeMoney(withdraw.getUser().getId(), withdraw.getFee(), OperatorInfo.APPLY_WITHDRAW, "申请提现，冻结提现手续费, 提现编号:" + withdraw.getId(),withdraw.getAccountType());
        // 等待审核
        withdraw.setStatus(UserConstants.WithdrawStatus.WAIT_VERIFY);
        ht.save(withdraw);
    }

    @Override
    public double calculateFee(double amount) {
        return feeConfigBO.getFee(FeePoint.WITHDRAW, FeeType.FACTORAGE, null, null, amount);
    }

    /**
     * 生成id
     *
     * @return
     */
    private String generateId() {

        return idGenerator.nextId(WithdrawCash.class);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void passWithdrawCashApply(WithdrawCash withdrawCash) {
        // 更新提现审核状态，到等待复核状态
        WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId(), LockMode.PESSIMISTIC_WRITE);
        wdc.setVerifyTime(new Date());
        wdc.setStatus(UserConstants.WithdrawStatus.RECHECK);
        wdc.setType(withdrawCash.getType());
        wdc.setVerifyMessage(withdrawCash.getVerifyMessage());
        wdc.setVerifyUser(withdrawCash.getVerifyUser());
        ht.update(wdc);
        log.info("提现审核初审通过，提现编号：" + wdc.getId() + "，审核人：" + withdrawCash.getVerifyUser().getId() + "，审核理由：" + withdrawCash.getVerifyMessage() + "，审核时间:" + wdc.getVerifyTime());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void passWithdrawCashRecheck(WithdrawCash withdrawCash) {
        // 更改状态为确认提现
        WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId(), LockMode.PESSIMISTIC_WRITE);
        wdc.setRecheckTime(new Date());
        wdc.setStatus(UserConstants.WithdrawStatus.CONFIRM);//等待确认
        wdc.setType(withdrawCash.getType());
        wdc.setRecheckMessage(withdrawCash.getRecheckMessage());
        wdc.setRecheckUser(withdrawCash.getRecheckUser());
        ht.update(wdc);
        log.info("提现审核复核通过，提现编号：" + wdc.getId() + "，审核人：" + withdrawCash.getRecheckUser().getId() + "，审核理由：" + withdrawCash.getRecheckMessage() + "，审核时间:" + wdc.getRecheckTime());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void refuseWithdrawCashApply(WithdrawCash withdrawCash) {
        // 解冻申请时候冻结的金额
        WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId(), LockMode.PESSIMISTIC_WRITE);
        wdc.setStatus(UserConstants.WithdrawStatus.VERIFY_FAIL);
        wdc.setVerifyTime(new Date());
        wdc.setVerifyMessage(withdrawCash.getVerifyMessage());
        wdc.setType(withdrawCash.getType());
        wdc.setVerifyUser(withdrawCash.getVerifyUser());
        ht.update(wdc);
        try {
            userBillBO.unfreezeMoney(wdc.getUser().getId(), wdc.getMoney(), OperatorInfo.REFUSE_APPLY_WITHDRAW, "提现申请被拒绝，解冻提现金额, 提现ID:" + withdrawCash.getId(), withdrawCash.getAccountType());
            userBillBO.unfreezeMoney(wdc.getUser().getId(), wdc.getFee(), OperatorInfo.REFUSE_APPLY_WITHDRAW, "提现申请被拒绝，解冻手续费, 提现ID:" + withdrawCash.getId(), withdrawCash.getAccountType());
        } catch (InsufficientBalance e) {
            throw new RuntimeException(e);
        }
        log.info("提现审核初审通过，提现编号：" + wdc.getId() + "，审核人：" + withdrawCash.getVerifyUser().getId() + "，审核理由：" + withdrawCash.getVerifyMessage() + "，审核时间:" + wdc.getVerifyTime());

    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void refuseWithdrawCashRecheck(WithdrawCash withdrawCash) {
        // 解冻申请时候冻结的金额
        WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId(), LockMode.PESSIMISTIC_WRITE);
        wdc.setStatus(UserConstants.WithdrawStatus.VERIFY_FAIL);
        wdc.setRecheckTime(new Date());
        wdc.setRecheckMessage(withdrawCash.getRecheckMessage());
        wdc.setType(withdrawCash.getType());
        wdc.setRecheckUser(withdrawCash.getRecheckUser());
        ht.update(wdc);
        try {
            userBillBO.unfreezeMoney(wdc.getUser().getId(), wdc.getMoney(), OperatorInfo.REFUSE_APPLY_WITHDRAW, "提现申请被拒绝，解冻提现金额, 提现ID:" + withdrawCash.getId(),withdrawCash.getAccountType());
            userBillBO.unfreezeMoney(wdc.getUser().getId(), wdc.getFee(), OperatorInfo.REFUSE_APPLY_WITHDRAW, "提现申请被拒绝，解冻手续费, 提现ID:" + withdrawCash.getId(),withdrawCash.getAccountType());
        } catch (InsufficientBalance e) {
            throw new RuntimeException(e);
        }
        log.info("提现审核复核未通过，提现编号：" + wdc.getId() + "，审核人：" + withdrawCash.getRecheckUser().getId() + "，审核理由：" + withdrawCash.getRecheckMessage() + "，审核时间:" + wdc.getRecheckTime());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawByAdmin(UserBill ub) throws InsufficientBalance {
        WithdrawCash wc = new WithdrawCash();
        wc.setCashFine(0D);
        wc.setFee(0D);
        wc.setAccountType(ub.getAccountType());
        wc.setId(generateId());
        wc.setIsWithdrawByAdmin(true);
        wc.setMoney(ub.getMoney());
        wc.setStatus(WithdrawStatus.SUCCESS);
        wc.setTime(new Date());
        wc.setUser(ub.getUser());
        ht.save(wc);
        userBillBO.transferOutFromBalance(ub.getUser().getId(), ub.getMoney(), OperatorInfo.ADMIN_OPERATION, ub.getDetail(),ub.getAccountType());
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void processWithdraw(WithdrawCash withdrawCash) {

        // 从冻结金额中取，系统账户也要记录
        WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
        ht.evict(wdc);
        wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.PESSIMISTIC_WRITE);
        wdc.setCallbackTime(new Date());
        wdc.setStatus(WithdrawStatus.PROCESS);
        ht.merge(wdc);
        log.info("提现处理中，提现编号：" + wdc.getId());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void confirmWithdraw(WithdrawCash withdrawCash) {
        // 从冻结金额中取，系统账户也要记录
        WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
        ht.evict(wdc);
        wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.PESSIMISTIC_WRITE);
        wdc.setCallbackTime(new Date());
        wdc.setStatus(UserConstants.WithdrawStatus.SUCCESS);
        ht.merge(wdc);
        try {
            userBillBO.transferOutFromFrozen(wdc.getUser().getId(), wdc.getMoney(), OperatorInfo.WITHDRAW_SUCCESS, "提现完成，取出冻结金额, 提现ID:" + withdrawCash.getId(),withdrawCash.getAccountType());
            userBillBO.transferOutFromFrozen(wdc.getUser().getId(), wdc.getFee(), OperatorInfo.WITHDRAW_SUCCESS, "提现完成，取出冻结手续费, 提现ID:" + withdrawCash.getId(),withdrawCash.getAccountType());
            //sbs.transferInto(wdc.getFee(), OperatorInfo.WITHDRAW_SUCCESS, "提现申请通过, 扣除手续费。提现ID:" + withdrawCash.getId());
        } catch (InsufficientBalance e) {
            //正常操作不会出现;
            log.error(e.getMessage());
        }
        log.info("确认通过，提现编号：" + wdc.getId());
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void failWithdraw(WithdrawCash withdrawCash) {
        WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
        ht.evict(wdc);
        wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.PESSIMISTIC_WRITE);
        wdc.setCallbackTime(new Date());
        wdc.setStatus(UserConstants.WithdrawStatus.FAIL);
        ht.merge(wdc);
        //解冻金额
        try {
            userBillBO.unfreezeMoney(wdc.getUser().getId(), wdc.getMoney(), OperatorInfo.REFUSE_APPLY_WITHDRAW, "提现申请被拒绝，解冻提现金额, 提现ID:" + withdrawCash.getId(),withdrawCash.getAccountType());
            userBillBO.unfreezeMoney(wdc.getUser().getId(), wdc.getFee(), OperatorInfo.REFUSE_APPLY_WITHDRAW, "提现申请被拒绝，解冻手续费, 提现ID:" + withdrawCash.getId(),withdrawCash.getAccountType());
        } catch (InsufficientBalance insufficientBalance) {
            log.error(insufficientBalance.getMessage());
        }
        log.info("提现失败");
    }
}
