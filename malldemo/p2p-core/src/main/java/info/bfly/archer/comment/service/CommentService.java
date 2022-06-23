package info.bfly.archer.comment.service;

import info.bfly.archer.comment.model.Comment;
import info.bfly.archer.node.model.Node;

public interface CommentService {
    Comment findById(String commentId);
    
    /**
     * 
    * @Title: addComment 
    * @Description: 添加一条留言
    * @param @param comment    设定文件 
    * @return void    返回类型 
    * @throws
     */
    void addComment(Comment comment);
    

    /**
     * 获取一个comment的一级孩子数量。
     *
     * @param node
     * @return
     */
    Long getCommentNumberFC(Comment comment);

    /**
     * 获取一个node的一级评论数量。
     *
     * @param node
     * @return
     */
    Long getCommentNumberFL(Node node);
}
