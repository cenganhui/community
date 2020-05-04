package cgh.community.community.controller;

import cgh.community.community.dto.QuestionDTO;
import cgh.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Akuma
 * @date 2020/5/2 13:27
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 根据问题id进入问题页面
     * @param id    问题id
     * @param model
     * @return
     */
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("questionDTO",questionDTO);
        return "question";
    }
}
