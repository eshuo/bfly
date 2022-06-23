package info.bfly.p2p.invest.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.invest.exception.ExceedInvestTransferMoney;
import info.bfly.p2p.invest.exception.InvestTransferException;
import info.bfly.p2p.invest.model.TransferApply;
import info.bfly.p2p.invest.service.TransferService;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 */
@Component
@Scope(ScopeType.VIEW)
public class TransferApplyHome extends EntityHome<TransferApply> implements Serializable {
    @Resource
    private TransferService transferService;
    @Resource
    private LoginUserInfo   loginUserInfo;
    @Resource
    private ConfigService   configService;

    /**
     * 申请债权转让
     *
     * @param investId      被转让的还款Id
     * @param money         转让的本金
     * @param transferMoney 债权转让价格
     * @param transferRate  债权转让的利率
     * @param deadline      债权转让到期时间
     */
    public String applyInvestTransfer() {
        TransferApply ta = this.getInstance();
        try {
            transferService.canTransfer(ta.getInvest().getId());
            transferService.applyInvestTransfer(ta);
            FacesUtil.addInfoMessage("债权转让申请成功！");
            return "pretty:user-transfer-transfering";
        } catch (ExceedInvestTransferMoney e) {
            FacesUtil.addErrorMessage(e.getMessage());
        } catch (InvestTransferException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        }
        return null;
    }

    /**
     * 取消债权
     *
     * @param investTransferId 债权Id
     */
    public void cancel(String investTransferId) {
        transferService.cancel(investTransferId);
        FacesUtil.addInfoMessage("取消债权成功");
    }

    public boolean canTransfer(String investId) {
        try {
            return transferService.canTransfer(investId);
        } catch (InvestTransferException e) {
            // TODO:此处可以log.debug返回false的原因，便于查找为嘛无法进行债权转让
            return false;
        }
    }
}
