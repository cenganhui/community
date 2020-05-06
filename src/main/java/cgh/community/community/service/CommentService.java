package cgh.community.community.service;

import cgh.community.community.enums.CommentTypeEnum;
import cgh.community.community.exception.CustomizeErrorCode;
import cgh.community.community.exception.CustomizeException;
import cgh.community.community.mapper.CommentMapper;
import cgh.community.community.mapper.QuestionExtMapper;
import cgh.community.community.mapper.QuestionMapper;
import cgh.community.community.model.Comment;
import cgh.community.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Akuma
 * @date 2020/5/5 15:22
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    /**
     * 添加一条评论
     * @param comment
     */
    @Transactional
    public void insert(Comment comment) {
        //判断评论的父类id是否存在
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //判断评论的类型是否存在
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //判断评论的类型是否是评论
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }
        //判断评论的类型是否是问题
        else{
            //回复问题
            Question dbQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(dbQuestion == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //问题回复数+1
            dbQuestion.setCommentCount(1);
            commentMapper.insert(comment);
            questionExtMapper.incCommentCount(dbQuestion);
        }
    }
}
