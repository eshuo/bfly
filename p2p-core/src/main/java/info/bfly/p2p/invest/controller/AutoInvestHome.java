package info.bfly.p2p.invest.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.invest.InvestConstants;
import info.bfly.p2p.invest.model.AutoInvest;
import info.bfly.p2p.invest.service.AutoInvestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class AutoInvestHome extends EntityHome<AutoInvest> implements Serializable {
    private static final long serialVersionUID = -3216519392507627133L;
    @Resource
    private AutoInvestService autoInvestService;
    @Resource
    private LoginUserInfo     loginUserInfo;

    /**
     * 取消自动投标
     *
     * @return 跳转
     */
    @SuppressWarnings("unchecked")
    public String cancleAutoInvest() {
        String uname = loginUserInfo.getLoginUserId();
        List<AutoInvest> alist = (List<AutoInvest>) getBaseService().find("from AutoInvest ai where ai.user.id =?", uname);
        AutoInvest ai = alist.get(0);
        // set autoinvest status
        ai.setStatus(InvestConstants.AutoInvest.Status.OFF);
        // close autoinvest
        autoInvestService.settingAutoInvest(ai);
        setInstance(ai);
        // return savesuccess info to client
        FacesUtil.addInfoMessage("关闭自动投标成功！");
        return "pretty:autoInvest";
    }

    @Override
    protected AutoInvest createInstance() {
        AutoInvest ai = new AutoInvest();
        ai.setUser(new User());
        // ai.setMinRiskRank(new RiskRank());
        // ai.setMaxRiskRank(new RiskRank());
        return ai;
    }

    public long getOrderByUserId(String userId) {
        /*
         * String hql = "select count(ai) from AutoInvest ai where ai.status =?"
         * +
         * " and ai.lastAutoInvestTime<=(select ai2.lastAutoInvestTime from AutoInvest ai2 where ai2.user.id=?)"
         * +
         * " and ai.seqNum<(select ai2.seqNum from AutoInvest ai2 where ai2.user.id=?)"
         * ;
         */
        // Object o = getBaseService().find(hql, new
        // String[]{InvestConstants.AutoInvest.OperationStatus.ON,
        // userId,userId}).get(0);
        // if (o == null) {
        // return 0L;
        // }
        // FIXME: 需要解决性能问题
        long index = 0;
        // 遍历所有自动投标用户
        List<AutoInvest> ais = (List<AutoInvest>) getBaseService().find("from AutoInvest ai where ai.status = '" + InvestConstants.AutoInvest.Status.ON
                + "' order by ai.lastAutoInvestTime asc, ai.seqNum asc");
        for (AutoInvest ai : ais) {
            if (StringUtils.equals(ai.getUserId(), userId)) {
                break;
            }
            index++;
        }
        return index + 1;
    }

    @Override
    protected void initInstance() {
        AutoInvest ai = getBaseService().get(AutoInvest.class, loginUserInfo.getLoginUserId());
        if (ai != null) {
            setInstance(ai);
        } else {
            setInstance(createInstance());
        }
    }

    /**
     * 保存自动投标
     */
    public String saveAutoInvest() {
        // 先从数据库中查找当前用户最早自动投标的最大序号
        String uname = loginUserInfo.getLoginUserId();
        // current user name
        getInstance().setUser(new User(uname));
        // auto invest time
        getInstance().setStatus(InvestConstants.AutoInvest.Status.ON);
        // autoInvest start
        autoInvestService.settingAutoInvest(getInstance());
        // return savesuccess info to client
        FacesUtil.addInfoMessage("开启自动投标成功！");
        clearInstance();
        return "pretty:autoInvest";
    }
}
