package com.test.controller;

import com.nannan.beans.factory.annotation.Autowired;
import com.test.entity.User;
import com.test.service.BaseService;
import com.nannan.web.bind.annotation.RequestMapping;
import com.nannan.web.bind.annotation.ResponseBody;
import com.nannan.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloWorldBean {

    @Autowired
    BaseService web_baseservice;

    @RequestMapping("/test2")
    public void doTest2(HttpServletRequest request, HttpServletResponse response) {
        String str = "test 2, hello world!";
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @RequestMapping("/test5")
    public ModelAndView doTest5(User user) {
        ModelAndView mav = new ModelAndView("test","msg",user.getName());
        return mav;
    }
    @RequestMapping("/test6")
    public String doTest6(User user) {
        return "error";
    }

    @RequestMapping("/test7")
    @ResponseBody
    public User doTest7(User user) {
        System.out.println(user.getBirthday());
        user.setName(user.getName() + "---");
        //user.setBirthday(new Date());
        return user;
    }

}
