package com.example.demo.service;

import com.example.demo.common.MD5Util;
import com.example.demo.entity.LoginTicket;
import com.example.demo.entity.User;
import com.example.demo.mapper.LoginTicketMapper;
import com.example.demo.mapper.UserMappler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMappler userMappler;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    /**
     * 注册用户信息
     * @param account
     * @param password
     * @param name
     * @return
     */
    public Map<String,Object> register(String account, String password,String name){
        Map<String,Object> map = new HashMap<>();
        Random random = new Random();
        if (StringUtils.isBlank(account)){
            map.put("msg","用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User u = userMappler.seletByAccount(account);
        if (u!=null){
            map.put("msg","用户名已经被占用");
            return map;
        }

        User user = new User();
        user.setAccount(account);
        user.setName(name);
        user.setSalt(MD5Util.getSalt());
        user.setHeadUrl(String.format("https://images.nowcoder.com/head/%dm.png",random.nextInt(1000)));
        user.setPassword(MD5Util.getSaltMD5(password,user.getSalt()));
        userMappler.insertUser(user);
        User userMessage = userMappler.seletByAccount(account);
        userMessage.setPassword("");

        String ticket = addLoginTicket(userMessage.getId());
        map.put("ticket",ticket);
        map.put("user",userMessage);

        return map;
    }

    /**
     * 登录
     * @param account
     * @param password
     * @return
     */
    public Map<String,Object> login(String account, String password){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(account)){
            map.put("msg","用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User u = userMappler.seletByAccount(account);
        if (u==null){
            map.put("msg","用户名不存在");
            return map;
        }

        if (!MD5Util.getSaltMD5(password,u.getSalt()).equals(u.getPassword())){
            map.put("msg","密码错误");
            return map;
        }


        String ticket = addLoginTicket(u.getId());
        map.put("ticket",ticket);
        map.put("user",u);

        return map;
    }

    /**
     * 保存登录状态
     * @param userId
     * @return
     */
    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*30);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));

        loginTicketMapper.insertLoginTicket(loginTicket);

        return loginTicket.getTicket();
    }

    public Map<String,Object> checkLoginStatue(Cookie[] cookies){
        Map<String,Object> map = new HashMap<>();
        int mark = 0;
        String ticket = null;
        for (Cookie cookie : cookies){
            if (cookie.getName().equals("ticket")){
                ticket = cookie.getValue();
                mark = 1;
            }
        }
        if ( mark == 0 ){
            map.put("msg","登录过期");
            return map;
        }
        LoginTicket loginTicket = loginTicketMapper.seletByTicket(ticket);
        Date now = new Date();
        if (loginTicket.getExpired().before(now)){
            map.put("msg","登录过期");
        }else {
            User user = userMappler.seletById(loginTicket.getId());
            user.setPassword(null);
            map.put("user",user);
        }
        return map;
    }
}
