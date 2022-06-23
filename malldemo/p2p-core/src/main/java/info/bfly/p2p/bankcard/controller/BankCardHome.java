package info.bfly.p2p.bankcard.controller;

import info.bfly.archer.comment.controller.CommentHome;
import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.controller.UserHome;
import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserService;
import info.bfly.archer.user.service.impl.UserBO;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.bankcard.BankCardConstants.BankCardStatus;
import info.bfly.p2p.bankcard.model.BankCard;
import info.bfly.p2p.user.service.RechargeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

@Component
@Scope(ScopeType.VIEW)
public class BankCardHome extends EntityHome<BankCard> implements Serializable {
    private static final long serialVersionUID = 6763528001126109285L;
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;
    @Resource
    private UserBO          userBO;
    @Resource
    private UserService     userService;
    @Resource
    private UserHome        userHome;
    @Resource
    private LoginUserInfo   loginUserInfo;
    @Resource
    private RechargeService rechargeService;
    @Qualifier(value = "commentHome")
    @Resource
    private CommentHome     commenHome;
    private String          realNameString;
    private String          idCardString;
    private String          sexString;
    private Date            birthdate;
    private final String WEALTHROLE = "user_wealth_role";

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        // 银行卡标记为删除状态
        this.getInstance().setStatus(BankCardStatus.DELETED);
        getBaseService().update(this.getInstance());
        return "pretty:bankCardList";
    }

    @Override
    @Transactional(readOnly = false)
    public String delete(String bankCardId) {
        BankCard bc = getBaseService().get(BankCard.class, bankCardId);
        if (bc == null) {
            FacesUtil.addErrorMessage("未找到编号为" + bankCardId + "的银行卡！");
        } else {
            // 银行卡标记为删除状态
            setInstance(bc);
            this.getInstance().setStatus(BankCardStatus.DELETED);
            getBaseService().update(this.getInstance());
            setInstance(null);
        }
        return "pretty:bankCardList";
    }

    /**
     * 删除银行卡，资金托管
     *
     * @return
     */
    public String deleteTrusteeship() {
        throw new RuntimeException("you must override this method!");
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getIdCardString() {
        return idCardString;
    }

    public String getRealNameString() {
        return realNameString;
    }

    public String getSexString() {
        return sexString;
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        User loginUser = getBaseService().get(User.class, loginUserInfo.getLoginUserId());
        if (loginUser == null) {
            FacesUtil.addErrorMessage("用户未登录");
            return null;
        }
        if (StringUtils.isEmpty(this.getInstance().getId())) {
            getInstance().setId(IdGenerator.randomUUID());
            getInstance().setUser(loginUser);
            getInstance().setStatus(BankCardStatus.BINDING);
            getInstance().setBank(rechargeService.getBankNameByNo(getInstance().getBankNo()));
        }
        getInstance().setBankCardType(this.getInstance().getBankCardType());
        getInstance().setBankServiceType(this.getInstance().getBankServiceType());
        getInstance().setTime(new Date());
        super.save(false);
        setInstance(null);
        FacesUtil.addInfoMessage("保存银行卡成功！");
        return "pretty:bankCardList";
    }

    @Transactional(readOnly = false)
    public String saveByWealth() {
        User loginUser = getBaseService().get(User.class, loginUserInfo.getLoginUserId());
        if (loginUser == null) {
            FacesUtil.addErrorMessage("用户未登录");
            return null;
        }
        // if (!commenHome.hasCustomRole(loginUser, WEALTHROLE)) {
        loginUser.setRealname(userHome.getInstance().getRealname());
        loginUser.setSex(userHome.getInstance().getSex());
        loginUser.setIdCard(userHome.getInstance().getIdCard());
        loginUser.setBirthday(userHome.getInstance().getBirthday());
        // }
        userBO.addRole(loginUser, new Role(WEALTHROLE));
        this.save();
        FacesUtil.addInfoMessage("保存银行卡成功！");
        // 刷新登录用户权限
        userService.refreshAuthorities(loginUser.getId());
        return "pretty:bankCardList";
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setIdCardString(String idCardString) {
        this.idCardString = idCardString;
    }

    public void setRealNameString(String realNameString) {
        this.realNameString = realNameString;
    }

    public void setSexString(String sexString) {
        this.sexString = sexString;
    }
}
