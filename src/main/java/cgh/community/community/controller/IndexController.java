package cgh.community.community.controller;

import cgh.community.community.dto.PaginationDTO;
import cgh.community.community.dto.QuestionDTO;
import cgh.community.community.mapper.QuestionMapper;
import cgh.community.community.mapper.UserMapper;
import cgh.community.community.model.Question;
import cgh.community.community.model.User;
import cgh.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 主页接口
 */
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    /**
     * 主页
     * @param request
     * @param model
     * @param page  当前页号
     * @param size  当前页的问题数量
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size){
        //从请求中拿出cookie
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                //找到cookie中的token
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    //根据token去数据库中找相应用户
                    User user = userMapper.findByToken(token);
                    //若用户存在，则将用户设置进session中
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        //根据页号和分页数，获取分页问题DTO并加入model
        PaginationDTO paginationDTO = questionService.list(page,size);
        model.addAttribute("paginationDTO",paginationDTO);
        return "index";
    }
}
