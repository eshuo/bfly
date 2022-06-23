package info.bfly.archer.message.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.message.model.InBox;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.message.MessageConstants;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
@SuppressWarnings("unchecked")
public class InBoxHome extends EntityHome<InBox> implements java.io.Serializable {
    private static final long serialVersionUID = 499451981561651732L;
    @Log
    private static Logger log;
    private        String                         selectItems;
    @Resource
    private        HibernateTemplate              ht;
    @Resource
    private        LoginUserInfo                  loginUser;

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        String inBoxId = FacesUtil.getParameter("inBoxId");
        InBox inBox = ht.get(InBox.class, inBoxId);
        if (inBox != null) {
            ht.delete(inBox);
            FacesUtil.addInfoMessage("信息删除成功！");
        }
        return null;
    }

    /**
     * 根据inBox id删除inBox
     *
     * @param inBoxId
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public String delete(String inBoxId) {
        InBox inBox = ht.get(InBox.class, inBoxId);
        if (inBox != null) {
            ht.delete(inBox);
            FacesUtil.addInfoMessage("信息删除成功！");
        }
        return null;
    }

    /**
     * 批量删除
     */
    @Transactional(readOnly = false)
    public void deleteSelectInBox() {
        if (StringUtils.isNotBlank(selectItems)) {
            String[] inboxIds = selectItems.split(",");
            for (String inboxId : inboxIds) {
                InBox inbox = ht.get(InBox.class, inboxId);
                if (inbox != null) {
                    getBaseService().delete(inbox);
                }
            }
        }
    }

    public List<InBox> getAllInBoxByLoginUser() {
        if (StringUtils.isEmpty(loginUser.getLoginUserId())) {
            return (List<InBox>) ht.findByNamedQuery("InBox.finInBoxByLoginUser", loginUser.getLoginUserId());
        }
        return null;
    }

    /**
     * 通过用户的id获取所有的未读信息
     *
     * @return
     */
    public List<InBox> getAllInBoxNoReadByLoginUser() {
        if (StringUtils.isEmpty(loginUser.getLoginUserId())) {
            return (List<InBox>) ht.findByNamedQuery("InBox.finInBoxNoReadByLoginUser", loginUser.getLoginUserId());
        }
        return null;
    }

    /**
     * 获取某个用户的未读的信息的数量
     *
     * @param userId
     * @return
     * @author liuchun
     */
    public long getAllInBoxNoReadByUserId(String userId) {
        String hql = "select count(ib) from InBox ib where ib.status = ? and ib.recevier.id=?";
        Object o = ht.find(hql, MessageConstants.InBoxConstants.NOREAD, userId).get(0);
        if (o == null) {
            return 0;
        }
        return (Long) o;
    }

    public String getSelectItems() {
        return selectItems;
    }

    /**
     * 标记已读
     */
    public void setLetterReaded() {
        if (StringUtils.isNotBlank(selectItems)) {
            String[] inboxIds = selectItems.split(",");
            for (String inboxId : inboxIds) {
                ht.bulkUpdate("update InBox set status = ? where id = ?", MessageConstants.InBoxConstants.ISREAD, inboxId);
            }
        }
    }

    /**
     * 读信
     *
     * @param inBoxId
     */
    @Transactional
    public void setLetterReaded(String inBoxId) {
        InBox ib = getBaseService().get(InBox.class, inBoxId);
        ib.setStatus(MessageConstants.InBoxConstants.ISREAD);
        getBaseService().update(ib);
        RequestContext context = RequestContext.getCurrentInstance();
        // 即时更新前台未读信息数量
        context.addCallbackParam("noread", ht.findByNamedQuery("InBox.finInBoxNoReadByLoginUser", loginUser.getLoginUserId()).size());
    }

    public void setSelectItems(String selectItems) {
        this.selectItems = selectItems;
    }
}
