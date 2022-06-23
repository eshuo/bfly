package info.bfly.pay.p2p.user.service.impl;

import info.bfly.archer.common.service.BaseService;
import info.bfly.archer.user.UserBillConstants;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.VerifyHistory;
import info.bfly.archer.user.service.UserBillService;
import info.bfly.archer.user.service.UserService;
import info.bfly.archer.user.service.VerifyHistoryService;
import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.risk.FeeConfigConstants;
import info.bfly.p2p.risk.service.SystemBillService;
import info.bfly.p2p.risk.service.impl.FeeConfigBO;
import info.bfly.p2p.trusteeship.exception.TrusteeshipFormException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipTicketException;
import info.bfly.pay.controller.SinaOrderController;
import info.bfly.pay.p2p.user.model.MoneyTransfer;
import info.bfly.pay.p2p.user.service.SinaMoneyTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Created by XXSun on 6/5/2017.
 */
@Service
public class SinaMoneyTransferServiceImpl implements SinaMoneyTransferService {

    private final String DEFAULT_ACCOUNT_TYPE = "BASIC";

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private SystemBillService    systemBillService;
    @Autowired
    private VerifyHistoryService verifyHistoryService;

    @Autowired
    private FeeConfigBO feeConfigBO;

    @Autowired
    private HibernateTemplate ht;


    @Autowired
    private BaseService<MoneyTransfer> moneyTransferService;

    @Autowired
    private SinaOrderController sinaOrderController;

    @Override
    public MoneyTransfer initTransfer(String fromUserId, String fromAccountType, double money, String toUserId, String toAccountType) throws InsufficientBalance {
        Assert.hasText(fromUserId, "转出用户不能为空");
        Assert.hasText(toUserId, "转入用户不能为空");
        Assert.hasText(fromAccountType, "转出账户类型不能为空");
        Assert.hasText(toAccountType, "转入账户类型不能为空");
        Assert.isTrue(money > 0, "转账金额不能小于0");
        User fromU = userService.getUserById(fromUserId);
        User toU = userService.getUserById(toUserId);
        MoneyTransfer transfer = new MoneyTransfer();
        transfer.setId(idGenerator.nextId(MoneyTransfer.class));
        transfer.setFromUser(fromU);
        transfer.setFormAccountType(fromAccountType);
        transfer.setMoney(money);
        transfer.setFee(calculateFee(fromU, money));
        transfer.setToUser(toU);
        transfer.setToAccountType(toAccountType);
        transfer.setDate(new Date());
        transfer.setStatus(UserConstants.MoneyTransferStatus.INIT);
        checkMoney(transfer);
        ht.save(transfer);
        return transfer;
    }

    private double calculateFee(User user, double amount) {
        if (userService.isSystem(user)) {
            return 0d;
        }
        return feeConfigBO.getFee(FeeConfigConstants.FeePoint.MONEY_TRANSFER, FeeConfigConstants.FeeType.FACTORAGE, null, null, amount);
    }

    private boolean checkMoney(MoneyTransfer transfer) throws InsufficientBalance {
        double balance = 0d;
        if (userService.isSystem(transfer.getFromUser())) {
            balance = systemBillService.getBalance();
        } else balance = userBillService.getBalance(transfer.getFromUser().getId(), transfer.getFormAccountType());
        if (ArithUtil.sub(balance, transfer.getMoney()) < 0)
            throw new InsufficientBalance("账户" + transfer.getFormAccountType() + "余额不足");
        return true;
    }

    @Override
    public MoneyTransfer initTransfer(String fromUserId, double money, String toUserId) throws InsufficientBalance {
        return initTransfer(fromUserId, DEFAULT_ACCOUNT_TYPE, money, toUserId, DEFAULT_ACCOUNT_TYPE);
    }

    @Override
    public MoneyTransfer initSystemTransfer(double money, String toUserId) throws InsufficientBalance {
        return initSystemTransfer(money, toUserId, DEFAULT_ACCOUNT_TYPE);
    }

    @Override
    public MoneyTransfer initSystemTransfer(double money, String toUserId, String toAccountType) throws InsufficientBalance {
        return initTransfer("system", DEFAULT_ACCOUNT_TYPE, money, toUserId, toAccountType);
    }

    @Override
    public String applyMoneyTransfer(MoneyTransfer transfer) throws InsufficientBalance, TrusteeshipFormException, TrusteeshipTicketException, TrusteeshipReturnException {
        Assert.isTrue(UserConstants.MoneyTransferStatus.INIT.equals(transfer.getStatus()), "转账初始异常，");
        checkMoney(transfer);
        if (userService.isSystem(transfer.getFromUser())) {
            systemBillService.transferOut(transfer.getMoney(), UserBillConstants.OperatorInfo.MONEY_TRANSFER, "转账" + transfer.getId());
        } else
            userBillService.freezeMoney(transfer.getFromUser().getId(), transfer.getMoney(), UserBillConstants.OperatorInfo.MONEY_TRANSFER, "转账", transfer.getFormAccountType());
        return sinaOrderController.createHostingCollectTradeSinaPay(transfer);
    }

    @Override
    public void passMoneyTransfer(MoneyTransfer transfer) throws TrusteeshipReturnException {
        Assert.isTrue(UserConstants.MoneyTransferStatus.WAIT_RECHECK.equals(transfer.getStatus()), "转账初始异常，");
        VerifyHistory verifyHistory = new VerifyHistory();
        verifyHistory.setVerifyUser(new User(SecurityContextHolder.getContext().getAuthentication().getName()));
        verifyHistory.setStatus(UserConstants.VerifyStatus.SUCCESS);
        verifyHistory.setVerifyMessage(transfer.getVerifyMessage());
        verifyHistory.setVerifyTarget(transfer.getId());
        verifyHistory.setVerifyType(UserConstants.VerifyType.MoneyTransfer);
        verifyHistoryService.addVerifyHistory(verifyHistory);
        moneyTransferService.update(transfer);
        sinaOrderController.createSingleHostingPayTradeSinaPay(transfer);
    }

    @Override
    public void refuseMoneyTransfer(MoneyTransfer transfer) throws InsufficientBalance {
        sinaOrderController.createHostingRefundSinaPay(transfer);
        transfer.setStatus(UserConstants.MoneyTransferStatus.REFUSE);
        if (userService.isSystem(transfer.getFromUser())) {
            systemBillService.transferInto(transfer.getMoney(), UserBillConstants.OperatorInfo.MONEY_TRANSFER, "转账拒绝" + transfer.getId());
        } else
            userBillService.unfreezeMoney(transfer.getFromUser().getId(), transfer.getMoney(), UserBillConstants.OperatorInfo.MONEY_TRANSFER, "转账拒绝", transfer.getFormAccountType());

        moneyTransferService.save(transfer);
    }

    @Override
    public void finishMoneyTransfer(MoneyTransfer transfer) throws InsufficientBalance {
        if (UserConstants.MoneyTransferStatus.TO_PROCESS.equals(transfer.getStatus())) {
            userBillService.transferIntoBalance(transfer.getToUser().getId(), transfer.getMoney(), UserBillConstants.OperatorInfo.MONEY_TRANSFER, "转账入账" + transfer.getId(), transfer.getToAccountType());
            if (!userService.isSystem(transfer.getFromUser())) {
                userBillService.transferOutFromFrozen(transfer.getFromUser().getId(), transfer.getMoney(), UserBillConstants.OperatorInfo.MONEY_TRANSFER, "转账" + transfer.getId(), transfer.getFormAccountType());
            }
            if (transfer.getFee() > 0) {
                userBillService.transferOutFromBalance(transfer.getToUser().getId(), transfer.getFee(), UserBillConstants.OperatorInfo.MONEY_TRANSFER, "转账手续费" + transfer.getId(), transfer.getToAccountType());
                systemBillService.transferInto(transfer.getFee(), UserBillConstants.OperatorInfo.MONEY_TRANSFER, "转账" + transfer.getId() + "手续费");
            }
            transfer.setStatus(UserConstants.MoneyTransferStatus.SUCCESS);
            moneyTransferService.save(transfer);
        }
    }
    @Override
    public MoneyTransfer bindTarget(MoneyTransfer transfer, String type, String target) {
        transfer.setType(type);
        transfer.setTarget(target);
        moneyTransferService.update(transfer);
        return transfer;
    }

}
