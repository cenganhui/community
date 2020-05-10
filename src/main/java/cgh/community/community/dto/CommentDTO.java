package cgh.community.community.dto;

import cgh.community.community.model.User;
import lombok.Data;

/**
 * 评论DTO类
 * @author Akuma
 * @date 2020/5/7 15:06
 */
@Data
public class CommentDTO {

    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;

    private User user;  //评论所属用户
}
