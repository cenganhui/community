package cgh.community.community.controller;

import cgh.community.community.mapper.QuestionMapper;
import cgh.community.community.mapper.UserMapper;
import cgh.community.community.model.Question;
import cgh.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 发布接口
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 发布页
     * @return
     */
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    /**
     * 发布问题
     * @param title
     * @param description
     * @param tag
     * @param request
     * @param model
     * @return
     */
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model){
        //向model中添加问题内容
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        //判断问题内容是否为空，若为空，则跳会publish页面，并提示，将已填写的内容回显
        if(title == null || title == ""){
            model.addAttribute("error","标题不能为空！");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error","问题补充不能为空！");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error","标签不能为空！");
            return "publish";
        }

        //验证登录
        User user = null;
        //从请求中拿出cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            //找到cookie中的token
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                //根据token去数据库中找相应用户
                user = userMapper.findByToken(token);
                //若用户存在，则将用户设置进session中
                if(user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        if(user == null){   //用户没登录
            model.addAttribute("error","用户未登陆");
            return "publish";
        }
        //登录成功，创建question，插入数据库，重定向到主页
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";
    }
}
