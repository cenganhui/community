package cgh.community.community.controller;

import cgh.community.community.dto.CommentDTO;
import cgh.community.community.dto.ResultDTO;
import cgh.community.community.exception.CustomizeErrorCode;
import cgh.community.community.mapper.CommentMapper;
import cgh.community.community.model.Comment;
import cgh.community.community.model.User;
import cgh.community.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
     * @param commentDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        //判断用户是否有登录
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //创建一个评论对象
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentator(user.getId());
        //添加进数据库
        commentService.insert(comment);
        //返回ok
        return ResultDTO.okOf();
    }
}
