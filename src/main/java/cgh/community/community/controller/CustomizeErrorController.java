package cgh.community.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截4xx、5xx
 * @author Akuma
 * @date 2020/5/4 16:05
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
@Slf4j
public class CustomizeErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error";
    }

    /**
     * 返回对应的错误页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, Model model){
        HttpStatus status = getStatus(request);
        if(status.is4xxClientError()){
            model.addAttribute("message","请求出错");
            log.error("request error");
        }
        if(status.is5xxServerError()){
            model.addAttribute("message","稍等下吧");
            log.error("server error");
        }
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try{
            return HttpStatus.valueOf(statusCode);
        }
        catch (Exception e){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
