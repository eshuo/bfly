package info.bfly.archer.comment.service.impl;

import info.bfly.archer.comment.model.Comment;
import info.bfly.archer.comment.service.CommentService;
import info.bfly.archer.common.service.impl.BaseServiceImpl;
import info.bfly.archer.node.model.Node;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "commentService")
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements CommentService {
    @Resource
    private HibernateTemplate ht;

    @Override
    public Comment findById(String commentId) {
        //TODO 根据主键获取用get方法
        Comment comment = ht.get(Comment.class, commentId);
        return comment;
    }

    @Override
    @Transactional(readOnly = false)
    public void addComment(Comment comment) {
        ht.save(comment);
    }

    // TODO 修改回复方法？

    @Override
    public Long getCommentNumberFC(Comment comment) {
        String hql = "select count(comment) from Comment comment where comment.parentComment.id='" + comment.getId() + "'";
        return (Long) ht.find(hql).get(0);
    }

    @Override
    public Long getCommentNumberFL(Node node) {
        String hql = "select count(comment) from Comment comment where comment.node.id='" + node.getId() + "' AND comment.parentComment = null";
        return (Long) ht.find(hql).get(0);
    }
}
