package cgh.community.community.controller;

import cgh.community.community.cache.HotTagCache;
import cgh.community.community.dto.PaginationDTO;
import cgh.community.community.mapper.UserMapper;
import cgh.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 主页接口
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

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
                        @RequestParam(name = "size",defaultValue = "5") Integer size,
                        @RequestParam(name = "search",required = false) String search,
                        @RequestParam(name = "tag",required = false) String tag){
        //根据页号和分页数，获取分页问题DTO并加入model
        PaginationDTO paginationDTO = questionService.list(search,tag,page,size);
        //获取热门标签列表
        List<String> tags = hotTagCache.getHots();

        model.addAttribute("paginationDTO",paginationDTO);
        model.addAttribute("search",search);
        model.addAttribute("tags",tags);
        model.addAttribute("tag",tag);

        return "index";
    }
}
