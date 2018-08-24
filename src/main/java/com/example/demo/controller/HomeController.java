package com.example.demo.controller;

import com.example.demo.dto.StateDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class HomeController {


    @Autowired
    private UserService userService;

    @RequestMapping(value = "/home")
    public String home(){
        return "home";
    }

    @ResponseBody
    @RequestMapping(value = "/index")
    public User index(){
        User user = new User();
        user.setId(1001);
        user.setName("kevin lin");
        return user;
    }

    @ResponseBody
    @RequestMapping(value = "/register")
    public StateDto register(HttpServletResponse httpResponse, User user){
        Map<String,Object> map = userService.register(user.getAccount(),user.getPassword(),user.getName());
        if (map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",(String)map.get("ticket"));
            cookie.setPath("/");
            httpResponse.addCookie(cookie);
            return new StateDto(200,"register succeed.",(User)map.get("user"));
        }else {
            return new StateDto(400,"register fail.",null);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/login")
    public StateDto login(HttpServletRequest httpServletRequest,HttpServletResponse httpResponse,User user){
        Map<String,Object> map = userService.checkLoginStatue(httpServletRequest.getCookies());
        if (map.containsKey("user")){
            return new StateDto(200,"login succeed.",(User)map.get("user"));
        }else {
            map = userService.login(user.getAccount(),user.getPassword());
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",(String)map.get("ticket"));
                cookie.setPath("/");
                httpResponse.addCookie(cookie);
                return new StateDto(200,"login succeed.",(User)map.get("user"));
            }else {
                return new StateDto(400,"login fail.",null);
            }
        }
    }
}
