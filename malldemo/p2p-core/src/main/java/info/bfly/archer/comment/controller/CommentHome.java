package info.bfly.archer.comment.controller;

import info.bfly.archer.comment.CommentConstants;
import info.bfly.archer.comment.model.Comment;
import info.bfly.archer.comment.service.CommentService;
import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.StringManager;
import info.bfly.p2p.loan.model.Loan;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.REQUEST)
public class CommentHome extends EntityHome<Comment> implements Serializable {
    private static final long          serialVersionUID = -2828486824208504783L;
    private static       StringManager sm               = StringManager.getManager(CommentConstants.Package);
    @Log
    static  Logger    log;
    /**
     * 父节点id，用于回复评论
     */
    private String pid;
    private boolean savecommentLoan = false;
    private String            randCode;
    @Resource
    private CommentService    cService;
    @Resource
    private LoginUserInfo     loginUserInfo;
    @Resource
    private HibernateTemplate ht;
    private Comment           parentComment;

    public CommentHome() {
        setUpdateView(FacesUtil.redirect(CommentConstants.View.COMMENT_LIST));
        setDeleteView(FacesUtil.redirect(CommentConstants.View.COMMENT_LIST));
    }

    /**
     * 验证注册验证码
     */
    public boolean checkvalidateCode() {
        String systemValidatecode = (String) FacesUtil.getSessionAttribute("rand");
        if (StringUtils.isEmpty(getRandCode())) {
            return true;
        }
        return !systemValidatecode.equals(getRandCode().toUpperCase());
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    protected Comment createInstance() {
        Comment comment = new Comment();
        comment.setNode(new Node());
        // 为了回复评论的时候方便
        comment.setParentComment(new Comment());
        return comment;
    }

    /**
     * 删除后台评论
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String delete() {
        return super.delete();
    }

    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteNode() {
        Comment comm = getBaseService().get(getEntityClass(), getInstance().getId());
        comm.setStatus(CommentConstants.COMMENT_STATUS_DISABLE);
        getBaseService().save(comm);
        try {
            FacesUtil.getHttpServletResponse().sendRedirect(FacesUtil.getCurrentAppUrl() + "/node/" + getInstance().getNode().getId() + "#comment-" + getInstance().getId());
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return "";
    }

    public Comment getParentComment() {
        if (parentComment == null) {
            // FIXME:用户随便输入一个url，是不是要进行判断？
            if (pid != null && !pid.equals("")) {
                if (parentComment == null) {
                    parentComment = cService.findById(pid);
                }
            }
        }
        return parentComment;
    }

    public String getPid() {
        return pid;
    }

    public String getRandCode() {
        return randCode;
    }

    public boolean hasCustomRole(User user, String roleName) {
        for (Role role : user.getRoles()) {
            if (role.getId().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasInvestRole(User user) {
        for (Role role : user.getRoles()) {
            if (role.getId().equals("INVESTOR")) {
                return true;
            }
        }
        return false;
    }

    public boolean isSavecommentLoan() {
        return savecommentLoan;
    }

    /**
     * 后台保存/编辑评论
     */
    @Override
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('USER')")
    public String save() {
        boolean isEdit = false;
        if (getInstance().getId() != null && !getInstance().getId().equals("")) {
            // 编辑状态
            Comment comm = getBaseService().get(getEntityClass(), getInstance().getId());
            comm.setTitle(getInstance().getTitle());
            comm.setBody(getInstance().getBody());
            setInstance(comm);
            isEdit = true;
        } else {
            getInstance().setId(IdGenerator.randomUUID());
            if (getParentComment() == null) {
                Long index = cService.getCommentNumberFL(getInstance().getNode()) + 1;
                getInstance().setThread(index.toString());
                getInstance().setParentComment(null);
            } else {
                Long index = cService.getCommentNumberFC(parentComment) + 1;
                getInstance().setThread(parentComment.getThread() + "." + index.toString());
                getInstance().setParentComment(parentComment);
            }
            // setUpdateView(FacesUtil.redirect("pretty:/node/structure"));
            getInstance().setCreateTime(new Date());
            getInstance().setStatus(CommentConstants.COMMENT_STATUS_ENABLE);
            // 这个添加评论时候得到用户信息，从spring-security上下文对象中得出
            User user = getBaseService().get(User.class, loginUserInfo.getLoginUserId());
            // System.out.println(getInstance().getNode());
            getInstance().setUserByCreator(user);
            if (CommentHome.log.isInfoEnabled()) {
                CommentHome.log.info(CommentHome.sm.getString("log.addComment", getInstance().getCreateTime(), user.getUsername(), getInstance().getNode().getId(), getInstance().getId()));
            }
            getInstance().setUpdateTime(new Date());
            getInstance().setIp(FacesUtil.getHttpServletRequest().getRemoteHost());
        }
        return super.save();
    }

    /**
     * 保存前台用户评论
     *
     * @param loan
     */
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('USER')")
    public String saveComment(Loan loan) {
        getInstance().setId(IdGenerator.randomUUID());
        getInstance().setLoan(loan);
        getInstance().setCreateTime(new Date());
        getInstance().setNode(null);
        Comment com = ht.get(Comment.class, pid);
        if (com != null) {
            getInstance().setBody(getInstance().getBody().replace("@" + com.getUserByCreator().getUsername() + ":", ""));
        }
        getInstance().setParentComment(com);
        getInstance().setStatus(CommentConstants.COMMENT_STATUS_ENABLE);
        User user = getBaseService().get(User.class, loginUserInfo.getLoginUserId());
        getInstance().setUserByCreator(user);
        getInstance().setUpdateTime(new Date());
        getInstance().setIp(FacesUtil.getHttpServletRequest().getRemoteHost());
        super.save(false);
        savecommentLoan = true;
        return null;
    }

    /**
     * 保存前台用户评论
     *
     * @param loanId
     * @param commentUserId
     */
    @Transactional(readOnly = false)
    @PreAuthorize("hasRole('USER')")
    public void saveComment(String loanId, String commentUserId) {
        Loan loan = getBaseService().get(Loan.class, loanId);
        // FIXME:验证
        getInstance().setId(IdGenerator.randomUUID());
        getInstance().setLoan(loan);
        getInstance().setParentComment(null);
        getInstance().setCreateTime(new Date());
        getInstance().setNode(null);
        getInstance().setStatus(CommentConstants.COMMENT_STATUS_ENABLE);
        getInstance().setUserByCreator(new User(commentUserId));
        getInstance().setUpdateTime(new Date());
        getInstance().setIp(FacesUtil.getHttpServletRequest().getRemoteHost());
        getBaseService().save(getInstance());
        FacesUtil.addInfoMessage("留言成功！");
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setRandCode(String randCode) {
        this.randCode = randCode;
    }

    public void setSavecommentLoan(boolean savecommentLoan) {
        this.savecommentLoan = savecommentLoan;
    }
}
