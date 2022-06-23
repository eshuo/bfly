package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.user.UserBillConstants.OperatorInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserBill;
import info.bfly.archer.user.service.UserBillService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.user.service.RechargeService;
import info.bfly.p2p.user.service.WithdrawCashService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Scope(ScopeType.VIEW)
public class UserBillHome extends EntityHome<UserBill> implements Serializable {
    private static final long    serialVersionUID = 8275737062032497384L;
    @Log
    private static Logger log;
    private static StringManager sm = StringManager.getManager(UserConstants.Package);
    @Resource
    private UserBillService      ubs;
    @Resource
    private RechargeService      rechargeService;
    @Resource
    private WithdrawCashService  wcService;
    @Resource
    private ConfigService        cs;

    @Override
    protected UserBill createInstance() {
        UserBill bill = new UserBill();
        bill.setUser(new User());
        return bill;
    }

    /**
     * 获取用户账户余额
     *
     * @return
     */
    public double getBalanceByUserId(String userId) {
        return ubs.getBalance(userId);
    }
    /**
     * 获取用户账户余额
     *
     * @return
     */
    public double getBalanceByUserId(String userId,String accountType) {
        return ubs.getBalance(userId,accountType);
    }
    /**
     * 获取用户账户冻结金额
     *
     * @param userId
     *            用户id
     * @return 余额
     */
    public double getFrozenMoneyByUserId(String userId) {
        return ubs.getFrozenMoney(userId);
    }

    /**
     * 获取用户账户冻结金额
     *
     * @param userId
     *            用户id
     * @return 余额
     */
    public double getFrozenMoneyByUserId(String userId,String accountType) {
        return ubs.getFrozenMoney(userId,accountType);
    }

    /**
     * 管理员操作借款账户
     */
    public String managerUserBill() {
        if (UserBillHome.log.isInfoEnabled()) UserBillHome.log.info("管理员后台手工干预账户余额，干预类型：" + this.getInstance().getType() + "，干预金额：" + this.getInstance().getMoney());
        try {
            if (this.getInstance().getType().equals("管理员-充值")) {
                rechargeService.rechargeByAdmin(getInstance());
            }
            else if (this.getInstance().getType().equals("管理员-提现")) {
                wcService.withdrawByAdmin(getInstance());
            }
            else if (this.getInstance().getType().equals("转入到余额")) {
                ubs.transferIntoBalance(getInstance().getUser().getId(), getInstance().getMoney(), OperatorInfo.ADMIN_OPERATION, getInstance().getDetail(),getInstance().getAccountType());
            }
            else if (this.getInstance().getType().equals("从余额转出")) {
                ubs.transferOutFromBalance(getInstance().getUser().getId(), getInstance().getMoney(), OperatorInfo.ADMIN_OPERATION, getInstance().getDetail(),getInstance().getAccountType());
            }
            else if (this.getInstance().getType().equals("管理员-冻结金额")) {
                ubs.freezeMoney(getInstance().getUser().getId(), getInstance().getMoney(), OperatorInfo.ADMIN_OPERATION, getInstance().getDetail(),getInstance().getAccountType());
            }
            else if (this.getInstance().getType().equals("管理员-解冻金额")) {
                ubs.unfreezeMoney(getInstance().getUser().getId(), getInstance().getMoney(), OperatorInfo.ADMIN_OPERATION, getInstance().getDetail(),getInstance().getAccountType());
            }
            else {
                UserBillHome.log.warn("未知的转账类型：" + this.getInstance().getType());
                FacesUtil.addErrorMessage("未知的转账类型：" + this.getInstance().getType());
                return null;
            }
        }
        catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage("余额不足");
            return null;
        }
        FacesUtil.addInfoMessage("操作成功！");
        return FacesUtil.redirect("/admin/fund/userBillList");
    }
}
