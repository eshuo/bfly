package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.message.controller.MessageUtil;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class UserList extends EntityQuery<User> implements Serializable {
    private static final long          serialVersionUID = 3478250097373767022L;
    private static final String        COUNT_HQL        = "select count(distinct user) from User user left join user.roles role";
    private static final String        HQL              = "select distinct user from User user left join user.roles role";
    private static       StringManager sm               = StringManager.getManager(UserConstants.Package);
    @Log
    private  Logger log;
    private        List<User>                     hasLoanRoleUsers;
    private        Date                           registerTimeStart;
    private        Date                           registerTimeEnd;
    @Resource
    private        MessageUtil                    messageUtil;
    private        String                         fromWhere;                                                                                // 推荐人
    private        List<User>                     selectedUsers;
    private        String                         title;
    private        String                         message;

    public UserList() {
        setCountHql(UserList.COUNT_HQL);
        setHql(UserList.HQL);
        final String[] RESTRICTIONS = { "user.id like #{userList.example.id}", "user.username like #{userList.example.username}", "user.mobileNumber like #{userList.example.mobileNumber}",
                "user.status like #{userList.example.status}", "user.email like #{userList.example.email}", "user.registerTime >= #{userList.registerTimeStart}",
                "user.registerTime <= #{userList.registerTimeEnd}", "user in elements(role.users) and role.id = #{userList.example.roles[0].id}", "user.referrer=#{userList.example.referrer}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    /**
     * 获取有借款权限的用户
     */
    @SuppressWarnings({"unchecked"})
    public List<User> allHasLoanRoleUser() {
        List<User> users = (List<User>) getHt().find("from User");
        List<User> hasLoanRoleUser = new ArrayList<User>();
        for (User user : users) {
            for (Role role : user.getRoles()) {
                if (role.getId().equals("LOANER") || role.getId().equals("ADMINISTRATOR")) {
                    hasLoanRoleUser.add(user);
                }
            }
        }
        return hasLoanRoleUser;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public List<User> getHasLoanRoleUsers() {
        if (hasLoanRoleUsers == null) {
            hasLoanRoleUsers = allHasLoanRoleUser();
        }
        return hasLoanRoleUsers;
    }

    @Override
    public LazyDataModel<User> getLazyModel() {
        if (fromWhere != null && fromWhere.length() > 0) {
            super.addRestriction("mt.who=user.id");
        }
        return super.getLazyModel();
    }

    @Override
    public User getLazyModelRowData(String rowKey) {
        List<User> users = (List<User>) getLazyModel().getWrappedData();
        for (User user : users) {
            if (user.getId().equals(rowKey)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Object getLazyModelRowKey(User user) {
        return user.getId();
    }

    public String getMessage() {
        return message;
    }

    /**
     * 获取新注册的用户，按照注册时间倒序排列
     *
     * @param count
     *            新注册用户个数
     * @return
     */
    public List<User> getNewUsers(int count) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.addOrder(Order.desc("registerTime"));
        getHt().setCacheQueries(true);
        return (List<User>) getHt().findByCriteria(criteria, 0, count);
    }

    public Date getRegisterTimeEnd() {
        return registerTimeEnd;
    }

    public Date getRegisterTimeStart() {
        return registerTimeStart;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public String getTitle() {
        return title;
    }

    /**
     * 获取网站有效注册人数
     *
     * @return
     */
    // FIXME :放到用户统计类中
    public long getValidUserNumber() {
        return (Long) getHt().find("select count(user) from User user where user.status='1'").get(0);
    }

    @Override
    protected void initExample() {
        User user = new User();
        List<Role> roles = new ArrayList<Role>();
        roles.add(new Role());
        user.setRoles(roles);
        setExample(user);
    }

    /**
     * 根据用户名模糊查询符合条件的用户，最多返回50条记录
     */
    @SuppressWarnings("unchecked")
    public List<User> queryUsersByUserName(String username) {
        log.debug("模糊查询用户名，传入的值为：" + username);
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.like("username", "%" + username + "%"));
        criteria.add(Restrictions.eq("status","1"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<User>) getHt().findByCriteria(criteria, 0, 50);
    }

    /**
     * 给被选中的用户发站内信。
     */
    @Transactional(readOnly = false)
    public void sendMessageToSelectedUsers() {
        if (getSelectedUsers().size() == 0) {
            FacesUtil.addErrorMessage("请选择用户！");
        } else {
            for (User user : getSelectedUsers()) {
                messageUtil.savePrivateMsg(user.getId(), title, message);
            }
            RequestContext.getCurrentInstance().addCallbackParam("sendSuccess", true);
            FacesUtil.addInfoMessage("发送成功！");
            getSelectedUsers().clear();
            title = null;
            message = null;
        }
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public void setHasLoanRoleUsers(List<User> hasLoanRoleUsers) {
        this.hasLoanRoleUsers = hasLoanRoleUsers;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRegisterTimeEnd(Date registerTimeEnd) {
        this.registerTimeEnd = registerTimeEnd;
    }

    public void setRegisterTimeStart(Date registerTimeStart) {
        this.registerTimeStart = registerTimeStart;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
