package info.bfly.p2p.invest.controller;

import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserBillService;
import info.bfly.archer.user.service.UserService;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.exception.ExceedMaxAcceptableRate;
import info.bfly.p2p.invest.exception.ExceedMoneyNeedRaised;
import info.bfly.p2p.invest.exception.IllegalLoanStatusException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.service.CapitalPoolService;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.invest.util.CompanyUtil;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Loan;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 代投
 *
 */

public class CapitalPoolInvestHome extends InvestHome {
    @Resource
    CapitalPoolService capitalPoolService;
    @Resource
    LoginUserInfo      loginUserInfo;
    @Resource
    InvestService      investService;
    @Resource
    UserService        userService;
    @Resource
    UserBillService    userBillService;

    public String redirectInvest() {
        FacesUtil.setSessionAttribute("money", getInstance().getInvestMoney());
        FacesUtil.setRequestAttribute("loanId", getInstance().getLoan().getId());
        return "pretty:user_invest_bidding";
    }

    @Override
    public String save() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            // 帐号标志
            String accountFlag = request.getParameter("accountFlag");
            // 投资金额
            double money = (Double) request.getSession().getAttribute("money");
            Loan loan = getBaseService().get(Loan.class, getInstance().getLoan().getId());
            if (loan.getUser().getId().equals(loginUserInfo.getLoginUserId())) {
                FacesUtil.addInfoMessage("你不能投自己的项目！");
                return null;
            } else {
                this.getInstance().setUser(new User(loginUserInfo.getLoginUserId()));
                this.getInstance().setIsAutoInvest(false);
                Invest investGoodMoney = getInstance();
                investGoodMoney.setInvestMoney(money);
                Invest investIPSMoney = (Invest) investGoodMoney.copy();
                investIPSMoney.setMoney(money);
                Invest investCompanyMoney = (Invest) investGoodMoney.copy();
                investCompanyMoney.setMoney(money);
                if ("H".equals(accountFlag)) {
                    saveGoodMoney(investGoodMoney);
                    saveCompanyMoney(investCompanyMoney, investGoodMoney);
                }
                if ("P".equals(accountFlag)) {
                    saveIpsMoney(investIPSMoney);
                }
            }
        } catch (InsufficientBalance e) {
            FacesUtil.addErrorMessage("账户余额不足，请充值！");
            return null;
        } catch (ExceedMoneyNeedRaised e) {
            FacesUtil.addErrorMessage("投资金额不能大于尚未募集的金额！");
            return null;
        } catch (ExceedMaxAcceptableRate e) {
            FacesUtil.addErrorMessage("竞标利率不能大于借款者可接受的最高利率！");
            return null;
        } catch (ExceedDeadlineException e) {
            FacesUtil.addErrorMessage("优惠券已过期");
            return null;
        } catch (UnreachedMoneyLimitException e) {
            FacesUtil.addErrorMessage("投资金额未到达优惠券使用条件");
            return null;
        } catch (IllegalLoanStatusException e) {
            FacesUtil.addErrorMessage("当前借款不可投资");
            return null;
        } catch (IOException e) {
            // log.debug(e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            // log.debug(e.getMessage());
            return null;
        } catch (UserNotFoundException e) {
            // log.debug(e.getMessage());
            return null;
        }
        FacesUtil.addInfoMessage("投资成功！");
        if (FacesUtil.isMobileRequest()) {
            return "pretty:mobile_user_invests";
        }
        return "pretty:user_invest_bidding";
    }

    private void saveCompanyMoney(Invest investCompanyMoney, Invest investGoodMoney) throws UserNotFoundException, IOException {
        if (null != investCompanyMoney && investGoodMoney != null) {
            User user = userService.getUserById(CompanyUtil.getRandomStr());
            investCompanyMoney.setUser(user);
            investCompanyMoney.setType(InvestConstants.InvestType.COMPANYINVEST);
            investCompanyMoney.setAccountType(InvestConstants.InvestType.IPSACCOUNT);
            investCompanyMoney.setInvestId(investGoodMoney.getId());
            // TODO 自己托管平台操作
            /*
             * try{
             * investOperation.createOperation(investCompanyMoney,FacesUtil.
             * getCurrentInstance()); }catch (IpsPayOperationException e) {
             * FacesUtil.addErrorMessage(e.getMessage()); } catch (IOException
             * e) { log.debug(e.getMessage()); }
             */
        }
    }

    private void saveGoodMoney(Invest investGoodMoney) throws InsufficientBalance, ExceedMoneyNeedRaised, ExceedMaxAcceptableRate, ExceedDeadlineException, UnreachedMoneyLimitException,
            IllegalLoanStatusException {
        if (null != investGoodMoney) {
            String goodId = capitalPoolService.generateId(investGoodMoney.getLoan().getId());
            investGoodMoney.setAccountType(InvestConstants.InvestType.GOODACCOUNT);
            investGoodMoney.setType(InvestConstants.InvestType.PERSONINVEST);
            capitalPoolService.createGood(investGoodMoney, goodId, investGoodMoney.getInvestMoney());
        }
    }

    private void saveIpsMoney(Invest investIPSMoney) throws IOException {
        if (null != investIPSMoney) {
            investIPSMoney.setAccountType(InvestConstants.InvestType.IPSACCOUNT);
            investIPSMoney.setType(InvestConstants.InvestType.PERSONINVEST);
            // TODO 资金托管平台操作
            /*
             * try{ investOperation.createOperation(investIPSMoney,FacesUtil.
             * getCurrentInstance()); }catch (IpsPayOperationException e) {
             * FacesUtil.addErrorMessage(e.getMessage()); } catch (IOException
             * e) { log.debug(e.getMessage()); }
             */
        }
    }
}
