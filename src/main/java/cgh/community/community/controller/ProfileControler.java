package cgh.community.community.controller;

import cgh.community.community.dto.PaginationDTO;
import cgh.community.community.model.User;
import cgh.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 个人页面接口
 * @author Akuma
 * @date 2020/5/1 14:16
 */
@Controller
public class ProfileControler {

    @Autowired
    private QuestionService questionService;

    /**
     * 获取我的问题列表
     * @param request
     * @param action
     * @param model
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){
        //判断登录
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){   //没有登录，则重定向到主页
            return "redirect:/";
        }

        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }
        else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        PaginationDTO paginationDTO = questionService.listByUserId(user.getId(),page,size);
        model.addAttribute("paginationDTO",paginationDTO);
        return "profile";
    }
}
