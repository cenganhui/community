package cgh.community.community.service;

import cgh.community.community.dto.NotificationDTO;
import cgh.community.community.dto.PaginationDTO;
import cgh.community.community.dto.QuestionDTO;
import cgh.community.community.enums.NotificationStatusEnum;
import cgh.community.community.enums.NotificationTypeEnum;
import cgh.community.community.exception.CustomizeErrorCode;
import cgh.community.community.exception.CustomizeException;
import cgh.community.community.mapper.NotificationMapper;
import cgh.community.community.mapper.UserMapper;
import cgh.community.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akuma
 * @date 2020/5/12 21:07
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户id获取分页内容DTO
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();  //创建分页内容DTO
        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample); //获得分页内容总数

        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }
        else{
            totalPage = totalCount / size + 1;
        }
        //如果当前页号<1 或者>总页数，则当前页号=1或者最后一页
        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);  //计算出分页内容DTO内的属性

        //计算页面偏移，根据偏移和分页数量查询出当前分页的问题list
        Integer offset = size * (page -1);

        //根据用户id查询通知list
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        if(notificationList.size() == 0){
            return paginationDTO;
        }
        //创建通知DTO列表，并将内容设置好
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for(Notification notification : notificationList){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOList);
        return paginationDTO;
    }

    /**
     * 根据用户id查询未读通知数
     * @param userId
     * @return
     */
    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());

        return notificationMapper.countByExample(notificationExample);
    }

    /**
     * 根据通知id和用户id设置通知已读
     * @param id
     * @param user
     * @return  通知DTO
     */
    public NotificationDTO read(Long id, User user) {
        //查询到通知
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){   //判断通知是否为空
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        //System.out.println(notification.getReceiver()+"  "+user.getId());
        //判断接收者是不是用户
        if(notification.getReceiver() > user.getId() || notification.getReceiver() < user.getId()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        //设置通知已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        //创建通知DTO，并将该通知封装进通知DTO中，返回
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return  notificationDTO;
    }
}
