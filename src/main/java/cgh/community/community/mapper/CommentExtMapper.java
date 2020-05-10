package cgh.community.community.mapper;

import cgh.community.community.model.Comment;

/**
 * 自定义扩展
 */
public interface CommentExtMapper {
    /**
     * 增加评论的评论数
     * @param comment
     * @return
     */
    int incCommentCount(Comment comment);
}