package cgh.community.community.controller;

import cgh.community.community.dto.PaginationDTO;
import cgh.community.community.mapper.UserMapper;
import cgh.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
     * @param model
     * @param page  当前页号
     * @param size  当前页的问题数量
     * @return
     */
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size){
        //根据页号和分页数，获取分页问题DTO并加入model
        PaginationDTO paginationDTO = questionService.list(page,size);
        model.addAttribute("paginationDTO",paginationDTO);
        return "index";
    }
}
