package cgh.community.community.dto;

import cgh.community.community.model.User;
import lombok.Data;

/**
 * 通知DTO
 * @author Akuma
 * @date 2020/5/12 21:02
 */
@Data
public class NotificationDTO {
    private Long id;    //通知id
    private Long gmtCreate; //通知创建时间
    private Integer status; //通知状态，0表示未读，1表示已读
    private Long notifier;  //通知者id
    private String notifierName;    //通知者name
    private String outerTitle;  //通知的外部类title（问题的title）
    private Long outerId;   //通知的外部类id（评论id或问题id）
    private String typeName;    //通知的类型名称（评论或问题）
    private Integer type;   //通知的类型，1代表问题，2代表评论
}
