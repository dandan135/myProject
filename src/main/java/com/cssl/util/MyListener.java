package com.cssl.util;

import com.cssl.pojo.Users;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@WebListener
public class MyListener implements ServletContextListener, HttpSessionListener {
    private ServletContext application;
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("application创建");
        //创建new一个list(保存登录的username)存进去application
        List<String> list=new ArrayList<String>();
        this.application=servletContextEvent.getServletContext();
        application.setAttribute("list",list);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session=httpSessionEvent.getSession();
        session.setMaxInactiveInterval(120);//设置session在2分钟后失效
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session=httpSessionEvent.getSession();
        //session销毁的时候获取当前登录的用户名
        //session.setMaxInactiveInterval(120);
        List<String> list=(List<String>)application.getAttribute("list");
        Users user=(Users)session.getAttribute("users");
        if(list.contains(user.getUsername())){//找到并移除
            list.remove(user.getUsername());
        }
    }
}
