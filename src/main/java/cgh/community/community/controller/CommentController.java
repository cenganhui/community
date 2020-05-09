package cgh.community.community.controller;

import cgh.community.community.dto.CommentCreateDTO;
import cgh.community.community.dto.CommentDTO;
import cgh.community.community.dto.ResultDTO;
import cgh.community.community.enums.CommentTypeEnum;
import cgh.community.community.exception.CustomizeErrorCode;
import cgh.community.community.model.Comment;
import cgh.community.community.model.User;
import cgh.community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 评论接口
 * @author Akuma
 * @date 2020/5/5 13:58
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 评论
     * @param commentCreateDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        //判断用户是否有登录
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //判断评论是否为空
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        //创建一个评论对象
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentator(user.getId());
        //添加进数据库
        commentService.insert(comment);
        //返回ok
        return ResultDTO.okOf();
    }

    /**
     * 二级评论
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List> comments(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOList = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOList);
    }
}
