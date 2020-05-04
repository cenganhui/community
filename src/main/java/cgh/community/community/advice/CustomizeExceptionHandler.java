package cgh.community.community.advice;

import cgh.community.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常拦截，但不能拦截4xx、5xx等的异常
 * @author Akuma
 * @date 2020/5/4 15:27
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model){

        if(e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());
        }
        else{
            model.addAttribute("message","稍等下吧");
        }
        return new ModelAndView("error");
    }

}
