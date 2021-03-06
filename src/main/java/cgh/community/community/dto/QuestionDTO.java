package cgh.community.community.dto;

import cgh.community.community.model.User;
import lombok.Data;

/**
 * 问题DTO类
 */
@Data
public class QuestionDTO {
    private Long id; //问题id
    private String title;   //标题
    private String description; //描述
    private String tag; //标签
    private Long gmtCreate; //创建时间
    private Long gmtModified;   //修改时间
    private Long creator;    //创建问题用户id
    private Integer viewCount;  //浏览数
    private Integer commentCount;   //评论数
    private Integer likeCount;  //点赞数
    private String[] tags;

    private User user;  //用户

    /**
     * 以,分割标签
     */
    public void cutTag(){
        tags = tag.split(",");
    }
}
