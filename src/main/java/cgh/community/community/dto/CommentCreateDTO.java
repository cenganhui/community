package cgh.community.community.dto;

import lombok.Data;

/**
 * 创建评论DTO类
 * @author Akuma
 * @date 2020/5/5 13:57
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
