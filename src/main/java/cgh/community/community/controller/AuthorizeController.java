package cgh.community.community.controller;

import cgh.community.community.dto.AccessTokenDTO;
import cgh.community.community.dto.GithubUser;
import cgh.community.community.mapper.UserMapper;
import cgh.community.community.model.User;
import cgh.community.community.provider.GithubProvider;
import cgh.community.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 * github授权登录接口
 */
@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    /**
     * github登录授权的回调
     * @param code
     * @param state
     * @param response
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //获取github用户信息
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null && githubUser.getId() != null){
            //设置用户信息并存入数据库
            User user = new User();
            String token = UUID.randomUUID().toString();    //创建一个token
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            //将token存入cookie中，并响应回去
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }
        else{
            //登录失败，重新登录
            log.error("callback get github error,{}",githubUser);
            return "redirect:/";
        }
    }

    /**
     * 用户登出
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public String Logout(HttpServletRequest request,
                         HttpServletResponse response){
        //删除用户session
        request.getSession().removeAttribute("user");
        //设置token为空
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
