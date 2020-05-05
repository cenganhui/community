package cgh.community.community.advice;

import cgh.community.community.dto.ResultDTO;
import cgh.community.community.exception.CustomizeErrorCode;
import cgh.community.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常拦截，但不能拦截4xx、5xx等的异常
 * @author Akuma
 * @date 2020/5/4 15:27
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    /**
     * 根据请求的不同做不同的异常返回
     * @param e
     * @param model
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    Object handle(Throwable e, Model model, HttpServletRequest request){

        String contentType = request.getContentType();
        //如果是ajax异步请求，返回JSON
        if("application/json".equals(contentType)){
            //返回JSON
            if(e instanceof CustomizeException){
                return ResultDTO.errorOf((CustomizeException)e);
            }
            else{
                return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
        }
        //否则返回错误页面
        else{
            //错误页面跳转
            if(e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }
            else{
                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }

}
