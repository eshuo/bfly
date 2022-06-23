package info.bfly.pay.p2p.user.service.impl;

import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserBill;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.WithdrawCash;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.user.service.WithdrawCashService;
import info.bfly.pay.bean.enums.ACCOUNT_TYPE;
import info.bfly.pay.bean.enums.WITHDRAW_STATUS;
import info.bfly.pay.bean.order.param.WithdrawCashParam;
import info.bfly.pay.controller.SinaOrderController;
import info.bfly.pay.controller.SinaUserController;
import info.bfly.pay.p2p.user.service.SinaWithdrawCashService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Created by XXSun on 5/11/2017.
 */
@Service
public class SinaWithdrawCashServiceimpl implements SinaWithdrawCashService {

    @Autowired
    private WithdrawCashService withdrawCashService;

    @Autowired
    private SinaUserController sinaUserController;

    @Autowired
    private SinaOrderController sinaOrderController;

    @Log
    private Logger log;

    @Override
    public WithdrawCash getWithdrawById(String withdrawId) {
        return withdrawCashService.getWithdrawById(withdrawId);
    }


    @Override
    public void applyWithdrawCash(WithdrawCash withdrawCash) throws InsufficientBalance, TrusteeshipReturnException {

        if (UserConstants.WithdrawStatus.INIT.equals(withdrawCash.getStatus())) {
            updateStatus(withdrawCash, UserConstants.WithdrawStatus.WAIT_FREEZE);
            doLog(withdrawCash);
            String sinaPay = sinaUserController.balanceFreezeSinaPay(withdrawCash, ACCOUNT_TYPE.BASIC, "提现冻结金额");
            if (sinaPay.equals("SUCCESS")) {
                updateStatus(withdrawCash, UserConstants.WithdrawStatus.FREEZE_SUCCESS);
                try {
                    withdrawCashService.applyWithdrawCash(withdrawCash);
                    doLog(withdrawCash);
                    FacesUtil.addInfoMessage("您的提现申请已经提交成功，请等待审核！");
                } catch (InsufficientBalance e) {
                    updateStatus(withdrawCash, UserConstants.WithdrawStatus.WAIT_UNFREEZE);
                    log.info("{} 提现处理出现异常，申请解冻冻结金额", withdrawCash.getId());
                    String result = sinaUserController.BalanceUnfreezeSinaPay(withdrawCash, "提现处理异常，用户账户的资金会自动解冻");
                    if (result.equals("SUCCESS")) {
                        updateStatus(withdrawCash, UserConstants.WithdrawStatus.UNFREEZE_SUCCESS);
                        doLog(withdrawCash);
                        log.info("{} 提现处理出现异常，解冻冻结金额成功", withdrawCash.getId());
                    } else {
                        updateStatus(withdrawCash, UserConstants.WithdrawStatus.UNFREEZE_FAIL);
                        doLog(withdrawCash);
                        log.error("{} 提现处理异常，解冻冻结金额失败", withdrawCash.getId());
                        throw new TrusteeshipReturnException("{} 提现处理异常，解冻冻结金额失败", withdrawCash.getId());
                    }
                    //ASSERT 这种情况不应该发生
                    throw new TrusteeshipReturnException("余额不足！提现处理出现异常");
                }
            } else {
                updateStatus(withdrawCash, UserConstants.WithdrawStatus.FREEZE_FAIL);
                doLog(withdrawCash);
                log.info("{} 提现处理异常，申请失败，冻结金额失败，请查询 {}", withdrawCash.getId(), sinaPay);
                throw new TrusteeshipReturnException("申请失败 {}", sinaPay);
            }
        }

    }

    @Override
    public WithdrawCash generateWithdraw(Double money, User user) {
        return withdrawCashService.generateWithdraw(money, user);
    }

    @Override
    public void update(WithdrawCash withdrawCash) {
        withdrawCashService.update(withdrawCash);
    }

    @Override
    public void updateStatus(WithdrawCash withdrawCash, String status) {
        withdrawCash.setStatus(status);
        WithdrawCash withdrawById = withdrawCashService.getWithdrawById(withdrawCash.getId());
        withdrawById.setStatus(status);
        update(withdrawById);
    }

    @Override
    public double calculateFee(double amount) {
        return withdrawCashService.calculateFee(amount);
    }

    @Override
    public void passWithdrawCashApply(WithdrawCash withdrawCash) throws TrusteeshipReturnException {
        withdrawCash.setVerifyUser(new User(getLoginUserName()));
        withdrawCash.setVerifyTime(new Date());

        if (UserConstants.WithdrawStatus.WAIT_VERIFY.equals(getWithdrawById(withdrawCash.getId()).getStatus())) {
            withdrawCashService.passWithdrawCashApply(withdrawCash);
            log.info("提现审核初审通过，请等待财务确认，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getVerifyUser().getId() + "，审核理由：" + withdrawCash.getVerifyMessage() + "，审核时间:" + withdrawCash.getVerifyTime());

            FacesUtil.addInfoMessage("审核通过，请等待财务确认");
        } else {
            log.info("提现审核初审失败，只有等待审核的提现才能审核，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getVerifyUser().getId() + "，审核理由：" + withdrawCash.getVerifyMessage() + "，审核时间:" + withdrawCash.getVerifyTime());
            throw new TrusteeshipReturnException("审核失败，只有等待审核的提现才能审核");
        }
    }

    private String getLoginUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public void passWithdrawCashRecheck(WithdrawCash withdrawCash) throws TrusteeshipReturnException {
        withdrawCash.setRecheckUser(new User(getLoginUserName()));
        withdrawCash.setRecheckTime(new Date());
        if (UserConstants.WithdrawStatus.RECHECK.equals(getWithdrawById(withdrawCash.getId()).getStatus())) {
            withdrawCashService.passWithdrawCashRecheck(withdrawCash);
            log.info("提现审核复核成功，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getRecheckUser().getId() + "，审核理由：" + withdrawCash.getRecheckMessage() + "，审核时间:" + withdrawCash.getRecheckTime());
            FacesUtil.addInfoMessage("审核通过，请等待用户确认");
        } else {
            log.info("提现审核复核失败，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getRecheckUser().getId() + "，审核理由：" + withdrawCash.getRecheckMessage() + "，审核时间:" + withdrawCash.getRecheckTime());
            FacesUtil.addErrorMessage("审核失败，只有通过初审的提现才能审核");
            throw new TrusteeshipReturnException("审核失败，只有通过初审的提现才能审核");
        }
    }

    @Override
    public void refuseWithdrawCashApply(WithdrawCash withdrawCash) throws TrusteeshipReturnException {
        withdrawCash.setVerifyUser(new User(getLoginUserName()));
        withdrawCash.setVerifyTime(new Date());
        if (UserConstants.WithdrawStatus.WAIT_VERIFY.equals(getWithdrawById(withdrawCash.getId()).getStatus())) {

            //不通过的时候解冻
            updateStatus(withdrawCash, UserConstants.WithdrawStatus.WAIT_UNFREEZE);
            doLog(withdrawCash);
            log.info("提现审核初审拒绝，申请解冻冻结金额，提现编号：{}", withdrawCash.getId());
            String sinaPay = sinaUserController.BalanceUnfreezeSinaPay(withdrawCash, "初审未通过，用户账户的资金会自动解冻");
            if (sinaPay.equals("SUCCESS")) {
                updateStatus(withdrawCash, UserConstants.WithdrawStatus.UNFREEZE_SUCCESS);
                doLog(withdrawCash);
                log.info("提现审核初审拒绝，解冻成功：" + sinaPay + "，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getVerifyUser().getId() + "，审核理由：" + withdrawCash.getVerifyMessage() + "，审核时间:" + withdrawCash.getVerifyTime());
                withdrawCashService.refuseWithdrawCashApply(withdrawCash);
                FacesUtil.addInfoMessage("初审未通过，用户账户的资金自动解冻");
            } else {
                updateStatus(withdrawCash, UserConstants.WithdrawStatus.UNFREEZE_FAIL);
                doLog(withdrawCash);
                log.info("提现审核初审拒绝，解冻失败：" + sinaPay + "，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getVerifyUser().getId() + "，审核理由：" + withdrawCash.getVerifyMessage() + "，审核时间:" + withdrawCash.getVerifyTime());
                throw new TrusteeshipReturnException("{} 提现审核初审拒绝，解冻资金失败 {}", withdrawCash.getId(), sinaPay);
            }
        } else {
            log.info("提现审核初审失败，只有新浪冻结成功的提现才能审核，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getVerifyUser().getId() + "，审核理由：" + withdrawCash.getVerifyMessage() + "，审核时间:" + withdrawCash.getVerifyTime());
            throw new TrusteeshipReturnException("{} 审核失败，只有新浪冻结成功的提现才能审核", withdrawCash.getId());
        }
    }

    @Override
    public void refuseWithdrawCashRecheck(WithdrawCash withdrawCash) throws TrusteeshipReturnException {
        withdrawCash.setRecheckUser(new User(getLoginUserName()));
        withdrawCash.setRecheckTime(new Date());
        if (UserConstants.WithdrawStatus.RECHECK.equals(getWithdrawById(withdrawCash.getId()).getStatus())) {
            //不通过的时候解冻
            updateStatus(withdrawCash, UserConstants.WithdrawStatus.WAIT_UNFREEZE);
            doLog(withdrawCash);
            log.info("提现审核复核拒绝，申请解冻冻结金额，提现编号：{}", withdrawCash.getId());
            String sinaPay = sinaUserController.BalanceUnfreezeSinaPay(withdrawCash, "复审未通过，用户账户的资金会自动解冻");
            if (sinaPay.equals("SUCCESS")) {
                updateStatus(withdrawCash, UserConstants.WithdrawStatus.UNFREEZE_SUCCESS);
                doLog(withdrawCash);
                withdrawCashService.refuseWithdrawCashRecheck(withdrawCash);
                log.info("提现审核复核拒绝，解冻成功：" + sinaPay + "，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getRecheckUser().getId() + "，审核理由：" + withdrawCash.getRecheckMessage() + "，审核时间:" + withdrawCash.getRecheckTime());
                FacesUtil.addInfoMessage("复核未通过，用户账户资金自动解冻成功");
            } else {
                updateStatus(withdrawCash, UserConstants.WithdrawStatus.UNFREEZE_FAIL);
                doLog(withdrawCash);
                log.info("提现审核复核拒绝，解冻失败：" + sinaPay + "，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getRecheckUser().getId() + "，审核理由：" + withdrawCash.getRecheckMessage() + "，审核时间:" + withdrawCash.getRecheckTime());
                throw new TrusteeshipReturnException("{} 提现审核复核失败，解冻失败： {} ", withdrawCash.getId(), sinaPay);
            }
        } else {
            log.info("提现审核复核失败，提现编号：" + withdrawCash.getId() + "，审核人：" + withdrawCash.getRecheckUser().getId() + "，审核理由：" + withdrawCash.getRecheckMessage() + "，审核时间:" + withdrawCash.getRecheckTime());
            throw new TrusteeshipReturnException("{} 提现审核复核失败，只有通过初审的提现才能审核 ", withdrawCash.getId());
        }

    }

    @Override
    public void withdrawByAdmin(UserBill ub) throws TrusteeshipReturnException {
        throw new TrusteeshipReturnException("暂未开放");
    }


    @Override
    public String getWithdrawLink(String withdrawId) throws TrusteeshipReturnException {
        WithdrawCash withdrawCash = getWithdrawById(withdrawId);
        if (withdrawCash == null)
            throw new TrusteeshipReturnException("提现编号 {} 不存在", withdrawId);
        if (UserConstants.WithdrawStatus.CONFIRM.equals(withdrawCash.getStatus())||UserConstants.WithdrawStatus.WAIT_UNFREEZE.equals(withdrawCash.getStatus())) {
            updateStatus(withdrawCash, UserConstants.WithdrawStatus.WAIT_UNFREEZE);
            doLog(withdrawCash);
            //申请解冻金额
            String sinaPay = sinaUserController.BalanceUnfreezeSinaPay(withdrawCash, "确认提现，用户账户的资金解冻");
            if (sinaPay.equals("SUCCESS")) {
                log.debug("{} 提现链接申请处理成功 ", withdrawCash.getId());
                updateStatus(withdrawCash, UserConstants.WithdrawStatus.UNFREEZE_SUCCESS);
                doLog(withdrawCash);
            } else {
                updateStatus(withdrawCash, UserConstants.WithdrawStatus.UNFREEZE_FAIL);
                doLog(withdrawCash);
                log.error("{} 提现处理出现异常 {}", withdrawCash.getId(), sinaPay);

                throw new TrusteeshipReturnException("提现处理失败，系统已经记录");
            }
        }
        if (UserConstants.WithdrawStatus.UNFREEZE_SUCCESS.equals(withdrawCash.getStatus())) {
            log.debug("{} 解冻成功，开始申请提现链接", withdrawCash.getId());
            List<WithdrawCashParam> withdrawCashParamList = sinaOrderController.queryHostingWithdrawSinaPay(withdrawCash);
            if (withdrawCashParamList != null && !withdrawCashParamList.isEmpty()) {
                WithdrawCashParam withdrawCashParam = withdrawCashParamList.get(0);
                log.debug("{} 获取到处理状态{}", withdrawCash.getId(), withdrawCashParam.getStatus());
                if (WITHDRAW_STATUS.INIT.equals(withdrawCashParam.getStatus()))
                    return sinaOrderController.createHostingWithdrawSinaPay(withdrawCash);
                else
                    throw new TrusteeshipReturnException("系统正在处理订单，请稍后");
            } else {
                return sinaOrderController.createHostingWithdrawSinaPay(withdrawCash);
            }
        } else if (UserConstants.WithdrawStatus.UNFREEZE_FAIL.equals(withdrawCash.getStatus())) {
            log.error("{} 解冻失败，请查询", withdrawCash.getId());

            throw new TrusteeshipReturnException("提现处理失败，系统已经记录，请稍后再试");
        };
        throw new TrusteeshipReturnException("获取链接失败");
    }

    @Override
    public void confirmWithdraw(WithdrawCash withdrawCash) {
        withdrawCashService.confirmWithdraw(withdrawCash);
    }

    @Override
    public void failWithdraw(WithdrawCash withdrawCash) {
        withdrawCashService.failWithdraw(withdrawCash);
    }

    public void doLog(WithdrawCash withdrawCash) {
        if (log.isDebugEnabled())
            log.debug("{} 申请提现:id{} 金额 {} 手续费 {} 状态 {}", withdrawCash.getUser().getId(), withdrawCash.getId(), withdrawCash.getMoney(), withdrawCash.getFee(), getWithdrawById(withdrawCash.getId()).getStatus());
    }
}
