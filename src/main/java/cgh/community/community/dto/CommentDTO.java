package cgh.community.community.dto;

import lombok.Data;

/**
 * 评论DTO类
 * @author Akuma
 * @date 2020/5/5 13:57
 */
@Data
public class CommentDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
