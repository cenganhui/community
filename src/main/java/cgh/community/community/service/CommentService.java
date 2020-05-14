package cgh.community.community.service;

import cgh.community.community.dto.CommentDTO;
import cgh.community.community.enums.CommentTypeEnum;
import cgh.community.community.enums.NotificationStatusEnum;
import cgh.community.community.enums.NotificationTypeEnum;
import cgh.community.community.exception.CustomizeErrorCode;
import cgh.community.community.exception.CustomizeException;
import cgh.community.community.mapper.*;
import cgh.community.community.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Akuma
 * @date 2020/5/5 15:22
 */
@Service
@Slf4j
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 添加一条评论
     * @param comment
     * @param commentator
     */
    @Transactional
    public void insert(Comment comment, User commentator) {
        //判断评论的父类id是否存在
        if(comment.getParentId() == null || comment.getParentId() == 0){
            log.error("target param not found,{}",comment.getParentId());
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //判断评论的类型是否存在
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            log.error("type param wrong,{}",comment.getType());
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //判断评论的类型是否是评论
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                log.error("comment not found,{}",dbComment);
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            //根据评论获取这个问题
            Question dbQuestion = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(dbQuestion == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //插入新评论
            commentMapper.insert(comment);
            //回复评论时给评论的评论数+1
            dbComment.setCommentCount(1);
            commentExtMapper.incCommentCount(dbComment);

            //创建通知
            createNotify(comment,dbComment.getCommentator(),commentator.getName(),dbQuestion.getTitle(),NotificationTypeEnum.REPLY_COMMENT,dbQuestion.getId());
        }
        //判断评论的类型是否是问题
        else{
            //回复问题
            Question dbQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(dbQuestion == null){
                log.error("question not found,{}",dbQuestion);
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //问题回复数+1
            dbQuestion.setCommentCount(1);
            commentMapper.insert(comment);
            questionExtMapper.incCommentCount(dbQuestion);

            //创建通知
            createNotify(comment,dbQuestion.getCreator(),commentator.getName(),dbQuestion.getTitle(),NotificationTypeEnum.REPLY_QUESTION,dbQuestion.getId());

        }
    }

    /**
     * 创建一个新通知
     * @param comment   从comment获取通知者
     * @param receiver  接收者
     * @param notifierName  通知者name
     * @param outerTitle    外部类title（问题的title）
     * @param notificationTypeEnum  通知类型
     * @param outerId   外部类id（评论id或者问题id）
     */
    private void createNotify(Comment comment,Long receiver,String notifierName,String outerTitle,NotificationTypeEnum notificationTypeEnum,Long outerId){
        if(receiver == comment.getCommentator()){
            return ;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());

        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setType(notificationTypeEnum.getType());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);

        notificationMapper.insert(notification);
    }



    /**
     * 根据问题id和评论类型获取评论DTO列表
     * @param id
     * @param type
     * @return
     */
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        //获取到评论列表
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        //判断评论列表是否为空
        if(comments.size() == 0){
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转成userMap
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long,User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(),user -> user));
        //将comment转成commentDTO
        List<CommentDTO> commentDTOList = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        //返回评论DTO列表
        return commentDTOList;
    }
}
