package cgh.community.community.dto;

import cgh.community.community.model.User;
import lombok.Data;

/**
 * 问题DTO类
 */
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;

    private User user;
}
