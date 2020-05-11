package cgh.community.community.controller;

import cgh.community.community.cache.TagCache;
import cgh.community.community.dto.QuestionDTO;
import cgh.community.community.mapper.QuestionMapper;
import cgh.community.community.model.Question;
import cgh.community.community.model.User;
import cgh.community.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

/**
 * 发布接口
 */
@Controller
public class PublishController {


    @Autowired
    private QuestionService questionService;

    /**
     * 根据问题id进入问题编辑页面，对问题进行修改
     * @param id    问题id
     * @param model
     * @return
     */
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model){
        QuestionDTO question = questionService.getById(id);
        //向model中添加问题内容
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    /**
     * 发布页
     * @return
     */
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
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
            @RequestParam("id") Long id,
            HttpServletRequest request,
            Model model){
        //向model中添加问题内容
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
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
        //验证便签是否合法
        String invalid = TagCache.filterInvalid(tag);
        if(StringUtils.isNoneBlank(invalid)){
            model.addAttribute("error","输入非法标签：" + invalid);
            return "publish";
        }

        //验证登录
        User user = (User)request.getSession().getAttribute("user");
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
        question.setId(id);
        //根据问题创建或更新问题
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
