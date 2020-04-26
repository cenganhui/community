package cgh.community.community.controller;

import cgh.community.community.mapper.UserMapper;
import cgh.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 主页接口
 */
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 主页
     * @param request
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request){
        //从请求中拿出cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            //找到cookie中的token
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                //根据token去数据库中找相应用户
                User user = userMapper.findByToken(token);
                //若用户存在，则将用户设置进session中
                if(user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }


        return "index";
    }
}
