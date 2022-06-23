package info.bfly.app.protocol.service;

import info.bfly.archer.comment.controller.CommentList;
import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/1/3 0003.
 */
@Scope(ScopeType.REQUEST)
@Service
public class ApiCommentService extends CommentList implements Serializable {


    public ApiCommentService() {
        final String[] RESTRICTIONS = {"loan.id = #{apiCommentService.loanId}", "loan.id = #{apiCommentService.example.loan.id}", "userByCreator.id = #{apiCommentService.example.userByCreator.id}",
                "loan.name like #{apiCommentService.example.loan.name}", "createTime >= #{apiCommentService.searchcommitMinTime}", "createTime <= #{apiCommentService.searchcommitMaxTime}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
