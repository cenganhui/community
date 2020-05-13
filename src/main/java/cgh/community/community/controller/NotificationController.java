package cgh.community.community.controller;

import cgh.community.community.dto.NotificationDTO;
import cgh.community.community.enums.NotificationTypeEnum;
import cgh.community.community.model.User;
import cgh.community.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * 通知接口
 * @author Akuma
 * @date 2020/5/13 10:04
 */
@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 设置通知已读
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/notification/{id}")
    public String setRead(HttpServletRequest request,
                          @PathVariable(name = "id") Long id){
        //获取当前用户
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }
        //根据通知id和user查询通知DTO
        NotificationDTO notificationDTO = notificationService.read(id,user);
        if(NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterId();
        }
        else{
            return "redirect:/";
        }
    }
}
