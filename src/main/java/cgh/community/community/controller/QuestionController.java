package cgh.community.community.controller;

import cgh.community.community.dto.CommentDTO;
import cgh.community.community.dto.QuestionDTO;
import cgh.community.community.enums.CommentTypeEnum;
import cgh.community.community.service.CommentService;
import cgh.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Akuma
 * @date 2020/5/2 13:27
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    /**
     * 根据问题id进入问题页面
     * @param id    问题id
     * @param model
     * @return
     */
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model){
        //获得问题DTO
        QuestionDTO questionDTO = questionService.getById(id);
        //获得关联问题DTO列表
        List<QuestionDTO> relatedQuestionDTOList = questionService.selectRelated(questionDTO);
        //获得评论DTO列表
        List<CommentDTO> commentDTOList = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        //累加阅读数
        questionService.incView(id);
        model.addAttribute("questionDTO",questionDTO);
        model.addAttribute("commentDTOList",commentDTOList);
        model.addAttribute("relatedQuestionDTOList",relatedQuestionDTOList);

        return "question";
    }
}
